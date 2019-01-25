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

public class MarsRestServer extends AbstractVerticle {

  static final String JSON_CONTENT_TYPE = "application/json";
  static final String TEXT_CONTENT_TYPE = "plain/text";

  static Integer httpPort = 8080;
  static HttpServer server;

  static Logger logger = LoggerFactory.getLogger("MarsRestServer");

  final ConcurrentHashMap<String, Plateau> storage;
  final PlateauCommandHandler commandHandler;
  final PlateauReadRepository repository;
  final ModelTranslator modelTranslator;

  MarsRestServer(ConcurrentHashMap<String, Plateau> storage, PlateauCommandHandler commandHandler,
                 PlateauReadRepository repository, ModelTranslator modelTranslator) {
    this.storage = storage;
    this.commandHandler = commandHandler;
    this.repository = repository;
    this.modelTranslator = modelTranslator;
  }

  public MarsRestServer() {
    storage = new ConcurrentHashMap<>();
    commandHandler = new PlateauCommandHandler(new PlateauRepository(storage));
    repository = new PlateauReadRepository(storage);
    modelTranslator = new ModelTranslator();
  }

  public void start(Future future) {

    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());
    router.route().handler(LoggerHandler.create());

    router.get("/plateaus").produces(JSON_CONTENT_TYPE).handler(ctx -> {
      List<PlateauReadModel> plateaus = repository.getPlateaus();
      ctx.response()
              .putHeader("content-type", JSON_CONTENT_TYPE)
              .end(Json.encodePrettily(plateaus));
    });

    router.get("/plateaus/:plateauId").handler(ctx -> {
      Optional<PlateauReadModel> plateau = repository.find(ctx.pathParam("plateauId"));
      if (plateau.isPresent()) {
        String type = ctx.request().getHeader("accept");
        switch (type) {
          case JSON_CONTENT_TYPE:
            ctx.response().putHeader("content-type", JSON_CONTENT_TYPE)
                    .end(Json.encodePrettily(plateau.get()));
            break;
          case TEXT_CONTENT_TYPE:
            ctx.response().putHeader("content-type", TEXT_CONTENT_TYPE)
                    .end(modelTranslator.convert(plateau.get()));
            break;
          default:
            ctx.response().putHeader("content-type", JSON_CONTENT_TYPE).end();
        }
      } else {
        ctx.response()
                .setStatusCode(404)
                .putHeader("content-type", JSON_CONTENT_TYPE)
                .end();
      }
    });

    router.post("/plateaus/:plateauId/commands/create").consumes(JSON_CONTENT_TYPE)
            .handler(ctx -> {
              JsonObject json = ctx.getBody().toJsonObject();
              String idParam = ctx.pathParam("plateauId");
              CreateCommand createCommand = new CreateCommand(idParam);
              commandHandler.create(createCommand);
              Coordinates coordinates = new Coordinates(json.getInteger("x"), json.getInteger("y"));
              ResizeAreaCommand resizeAreaCommand = new ResizeAreaCommand(idParam, coordinates);
              commandHandler.resize(resizeAreaCommand);
              String type = ctx.request().getHeader("accept");
              ctx.response()
                      .putHeader("accept", type)
                      .putHeader("Location", String.format("/plateaus/%s", idParam))
                      .setStatusCode(303)
                      .end();
            });

    router.post("/plateaus/:plateauId/commands/activate").consumes(JSON_CONTENT_TYPE)
            .handler(ctx -> {
              JsonObject json = ctx.getBody().toJsonObject();
              String idParam = ctx.pathParam("plateauId");
              Coordinates target = new Coordinates(json.getInteger("x"), json.getInteger("y"));
              Direction direction = modelTranslator.convert(json.getString("direction"));
              ActivateRoverCommand command = new ActivateRoverCommand(idParam, target, direction);
              commandHandler.activate(command);
              String type = ctx.request().getHeader("accept");
              ctx.response()
                      .putHeader("accept", type)
                      .putHeader("Location", String.format("/plateaus/%s", idParam))
                      .setStatusCode(303)
                      .end();
            });

    router.post("/plateaus/:plateauId/commands/move").consumes(JSON_CONTENT_TYPE)
            .handler(ctx -> {
              JsonObject json = ctx.getBody().toJsonObject();
              String idParam = ctx.pathParam("plateauId");
              String result = json.getString("actions");
              List<Character> actions = result.chars().mapToObj(e -> (char) e).collect(Collectors.toList());
              List<MoveRoverAction> domainActions = actions.stream()
                      .map(modelTranslator::convert).collect(Collectors.toList());
              MoveRoverCommand command = new MoveRoverCommand(idParam, domainActions);
              commandHandler.move(command);
              String type = ctx.request().getHeader("accept");
              ctx.response()
                      .putHeader("accept", type)
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


  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Launcher.executeCommand("run", MarsRestServer.class.getName());
  }

}