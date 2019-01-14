package com.rdpk.mars.rest;

import com.rdpk.mars.domain.Coordinates;
import com.rdpk.mars.domain.Direction;
import com.rdpk.mars.domain.MoveRoverAction;
import com.rdpk.mars.domain.Plateau;
import com.rdpk.mars.rest.command.*;
import com.rdpk.mars.rest.read.PlateauReadModel;
import com.rdpk.mars.rest.read.PlateauReadRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerHandler;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.rdpk.mars.domain.Direction.*;
import static com.rdpk.mars.domain.MoveRoverAction.*;

public class MarsRestServer extends AbstractVerticle {

  static Integer httpPort = 8080;
  static HttpServer server;

  static Logger logger = LoggerFactory.getLogger("MarsRestServer");

  final ConcurrentHashMap<String, Plateau> storage;
  final PlateauCommandHandler commandHandler;
  final PlateauReadRepository repository;

  MarsRestServer(ConcurrentHashMap<String, Plateau> storage, PlateauCommandHandler commandHandler,
                 PlateauReadRepository repository) {
    this.storage = storage;
    this.commandHandler = commandHandler;
    this.repository = repository;
  }

  public MarsRestServer() {
    storage = new ConcurrentHashMap<>();
    commandHandler = new PlateauCommandHandler(new PlateauRepository(storage));
    repository = new PlateauReadRepository(storage);
  }

  public void start(Future future) {

    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());
    router.route().handler(LoggerHandler.create());

    router.route()
            .consumes("application/json")
            .produces("application/json");

    router.get("/plateaus").handler(ctx -> {
      List<PlateauReadModel> plateaus = repository.getPlateaus();
      ctx.response()
              .putHeader("content-type", "application/json")
              .end(Json.encodePrettily(plateaus));
    });

    router.get("/plateaus/:plateauId").handler(ctx -> {
      Optional<PlateauReadModel> plateau = repository.find(ctx.pathParam("plateauId"));
      if (plateau.isPresent()) {
        ctx.response()
                .putHeader("content-type", "application/json")
                .end(Json.encodePrettily(plateau.get()));
      } else {
        ctx.response()
                .setStatusCode(404)
                .putHeader("content-type", "application/json")
                .end();
      }
    });

    router.post("/plateaus/:plateauId/commands/create").consumes("application/json").handler(ctx -> {
      JsonObject json = ctx.getBody().toJsonObject();
      String idParam = ctx.pathParam("plateauId");
      CreateCommand createCommand = new CreateCommand(idParam);
      commandHandler.create(createCommand);
      Coordinates coordinates = new Coordinates(json.getInteger("x"), json.getInteger("y"));
      ResizeAreaCommand resizeAreaCommand = new ResizeAreaCommand(idParam, coordinates);
      commandHandler.resize(resizeAreaCommand);
      ctx.response()
              .putHeader("Location", String.format("/plateaus/%s", idParam))
              .setStatusCode(303)
              .end();
    });

    router.post("/plateaus/:plateauId/commands/activate").consumes("application/json").handler(ctx -> {
      JsonObject json = ctx.getBody().toJsonObject();
      System.out.println(json.encodePrettily());
      String idParam = ctx.pathParam("plateauId");
      Coordinates target = new Coordinates(json.getInteger("x"), json.getInteger("y"));
      Direction direction = translateDirection(json.getString("direction"));
      ActivateRoverCommand command = new ActivateRoverCommand(idParam, target, direction);
      commandHandler.activate(command);
      ctx.response()
              .putHeader("Location", String.format("/plateaus/%s", idParam))
              .setStatusCode(303)
              .end();
    });

    router.post("/plateaus/:plateauId/commands/move").consumes("application/json").handler(ctx -> {
      JsonObject json = ctx.getBody().toJsonObject();
      String idParam = ctx.pathParam("plateauId");
      String result = json.getString("actions");
      List<Character> actions = result.chars().mapToObj(e->(char)e).collect(Collectors.toList());
      List<MoveRoverAction> domainActions = actions.stream()
              .map(this::translateMoveAction).collect(Collectors.toList());
      MoveRoverCommand command = new MoveRoverCommand(idParam, domainActions);
      commandHandler.move(command);
      ctx.response()
              .putHeader("Location", String.format("/plateaus/%s", idParam))
              .setStatusCode(303)
              .end();
    });

    server = vertx.createHttpServer(new HttpServerOptions().setPort(httpPort).setHost("0.0.0.0"));

    server.requestHandler(router).listen((ar) -> {
      if (ar.succeeded()) {
        logger.info("Server started on port " + ar.result().actualPort());
        future.complete();
      } else {
        logger.error("oops, something went wrong during server initialization", ar.cause());
        future.fail(ar.cause());
      }
    });

  }

  public void stop() {
    server.close();
  }

  Direction translateDirection(String character) {
    switch (character.toUpperCase()) {
      case "N":
        return NORTH;
      case "S":
        return SOUTH;
      case "E":
        return EAST;
      case "W":
        return WEST;
      default:
        return null;
    }
  }

  MoveRoverAction translateMoveAction(Character character) {
    switch (character) {
      case 'L':
        return TURN_LEFT;
      case 'R':
        return TURN_RIGHT;
      case 'M':
        return WALK;
      default:
        return null;
    }
  }

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Launcher.executeCommand("run", MarsRestServer.class.getName());
  }

}