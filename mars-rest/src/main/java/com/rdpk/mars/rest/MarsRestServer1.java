package com.rdpk.mars.rest;

import com.rdpk.mars.command.*;
import com.rdpk.mars.domain.Coordinates;
import com.rdpk.mars.domain.Direction;
import com.rdpk.mars.domain.Plateau;
import com.rdpk.mars.read.PlateauReadModel;
import com.rdpk.mars.read.PlateauReadRepository;
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

public class MarsRestServer1 extends AbstractVerticle {

  static Integer httpPort = 8080;
  static HttpServer server;

  static Logger logger = LoggerFactory.getLogger("MarsRestServer");

  final ConcurrentHashMap<String, Plateau> storage ;
  final PlateauCommandHandler commandHandler;
  final PlateauReadRepository repository;

  MarsRestServer1(ConcurrentHashMap<String, Plateau> storage, PlateauCommandHandler commandHandler,
                  PlateauReadRepository repository) {
    this.storage = storage;
    this.commandHandler = commandHandler;
    this.repository = repository;
  }

  public MarsRestServer1() {
    storage = new ConcurrentHashMap<>();
    commandHandler = new PlateauCommandHandler(new PlateauRepository(storage));
    repository = new PlateauReadRepository(storage);
  }

  public void start(Future future) {

    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());
    router.route().handler(LoggerHandler.create());

    router.get("/plateaus").handler(ctx -> {
      List<PlateauReadModel> plateaus = repository.getPlateaus();
      ctx.response()
          .putHeader("content-type", "application/json")
          .setStatusMessage("OK")
          .end(Json.encodePrettily(plateaus));
    });

    router.get("/plateaus/:plateauId").handler(ctx -> {
      Optional<PlateauReadModel> plateau = repository.find(ctx.pathParam("plateauId"));
      ctx.response()
        .putHeader("content-type", "application/json")
        .setStatusMessage("OK")
        .end(Json.encodePrettily(plateau));
    });

    router.post("/plateaus/:plateauId/commands/create").handler(ctx -> {
      JsonObject json = ctx.getBody().toJsonObject();
      String idParam = ctx.pathParam("plateauId");
      CreateCommand createCommand = new CreateCommand(idParam);
      commandHandler.create(createCommand);
      Coordinates coordinates = new Coordinates(json.getInteger("x"), json.getInteger("y"));
      ResizeAreaCommand resizeAreaCommand = new ResizeAreaCommand(idParam, coordinates);
      commandHandler.resize(resizeAreaCommand);
      ctx.response()
              .putHeader("Location", String.format("/plateaus/%s", idParam))
              .setStatusCode(201)
              .setStatusMessage("Created - Look to Location header")
              .end();
    });

    router.post("/plateaus/:plateauId/commands/activate").handler(ctx -> {
      JsonObject json = ctx.getBody().toJsonObject();
      String idParam = ctx.pathParam("plateauId");
      Coordinates target = new Coordinates(json.getInteger("x"), json.getInteger("y"));
      Direction direction = Direction.find(json.getString("direction"));
      ActivateRoverCommand command = new ActivateRoverCommand(idParam, target, direction);
      commandHandler.activate(command);
      ctx.response()
              .putHeader("Location", String.format("/plateaus/%s", idParam))
              .setStatusCode(201)
              .setStatusMessage("Created - Look to Location header")
              .end();
    });

    server = vertx.createHttpServer(new HttpServerOptions().setPort(httpPort).setHost("0.0.0.0"));

    server.requestHandler(router).listen((ar) ->  {
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

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {

    Launcher.executeCommand("run", MarsRestServer1.class.getName());

  }

}