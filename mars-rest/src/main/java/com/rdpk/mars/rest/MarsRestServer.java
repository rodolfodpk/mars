package com.rdpk.mars.rest;

import com.rdpk.mars.command.PlateauCommandHandler;
import com.rdpk.mars.command.PlateauRepository;
import com.rdpk.mars.domain.Coordinates;
import com.rdpk.mars.domain.Plateau;
import com.rdpk.mars.read.PlateauReadModel;
import com.rdpk.mars.read.PlateauReadRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.api.RequestParameter;
import io.vertx.ext.web.api.RequestParameters;
import io.vertx.ext.web.api.contract.RouterFactoryOptions;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.ext.web.api.validation.ValidationException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.rdpk.mars.domain.Direction.EAST;
import static com.rdpk.mars.domain.Direction.NORTH;
import static com.rdpk.mars.domain.MoveRoverAction.TURN_LEFT;
import static com.rdpk.mars.domain.MoveRoverAction.TURN_RIGHT;
import static com.rdpk.mars.domain.MoveRoverAction.WALK;
import static java.util.Arrays.asList;

class MarsRestServer extends AbstractVerticle {

  static Integer httpPort = 8080;
  static HttpServer server;

  static Logger logger = LoggerFactory.getLogger("MarsRestServer");

  final Map<String, Plateau> storage ;
  final PlateauCommandHandler commandHandler;
  final PlateauReadRepository repository;

  MarsRestServer(Map<String, Plateau> storage, PlateauCommandHandler commandHandler, PlateauReadRepository repository) {
    this.storage = storage;
    this.commandHandler = commandHandler;
    this.repository = repository;
  }

  public void start(Future future) {

    runEvidenceUseCase();

    // Load the api spec. This operation is asynchronous
    OpenAPI3RouterFactory.create(this.vertx, "mars-api.yaml", openAPI3RouterFactoryAsyncResult -> {
      if (openAPI3RouterFactoryAsyncResult.failed()) {
        // Something went wrong during router factory initialization
        Throwable exception = openAPI3RouterFactoryAsyncResult.cause();
        logger.error("oops, something went wrong during factory initialization", exception);
        future.fail(exception);
      }

      // Spec loaded with success
      OpenAPI3RouterFactory routerFactory = openAPI3RouterFactoryAsyncResult.result();

      // Add an handler with operationId
      routerFactory.addHandlerByOperationId("listPlateaus", routingContext -> {
        System.out.println("*** GET plateaus");
        // Load the parsed parameters
        RequestParameters params = routingContext.get("parsedParameters");
        // Handle listPlateaus operation
        RequestParameter limitParameter = params.queryParameter(/* Parameter name */ "limit");
        if (limitParameter != null) {
          // limit parameter exists, use it!
          Integer limit = limitParameter.getInteger();
        } else {
          // limit parameter doesn't exist (it's not required).
          // If it's required you don't have to check if it's null!
        }
        List<PlateauReadModel> plateaus = repository.getPlateaus();
        routingContext.response()
                .putHeader("content-type", "application/json")
                .setStatusMessage("OK")
                .end(Json.encodePrettily(plateaus));
      });


      // Add a failure handler
      routerFactory.addFailureHandlerByOperationId("listPlateaus", routingContext -> {
        // This is the failure handler
        Throwable failure = routingContext.failure();
        if (failure instanceof ValidationException)
          // Handle Validation Exception
          routingContext.response()
                  .setStatusCode(400)
                  .setStatusMessage("ValidationException thrown! " + ((ValidationException) failure).type().name())
                  .end();
      });

      // Add a security handler
      routerFactory.addSecurityHandler("api_key", routingContext -> {
        // Handle security here
        routingContext.next();
      });

      // Before router creation you can enable/disable various router factory behaviours
      RouterFactoryOptions factoryOptions = new RouterFactoryOptions()
              .setMountValidationFailureHandler(false) // Disable mounting of dedicated validation failure handler
              .setMountResponseContentTypeHandler(true); // Mount ResponseContentTypeHandler automatically

      // Now you have to generate the router
      Router router = routerFactory.setOptions(factoryOptions).getRouter();

      // Now you can use your Router instance
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
    });

  }

  /**
   * Just for testing purposes
   */
  private void runEvidenceUseCase() {

    Plateau plateau =  new Plateau("teste1");
    plateau.resize(new Coordinates(5, 5));

    // rover 1
    plateau.activate(new Coordinates(1, 2), NORTH);
    plateau.move(asList(TURN_LEFT, WALK, TURN_LEFT, WALK, TURN_LEFT, WALK, TURN_LEFT, WALK, WALK));
    // rover 2
    plateau.activate(new Coordinates(3, 3), EAST);
    plateau.move(asList(WALK, WALK, TURN_RIGHT, WALK, WALK, TURN_RIGHT, WALK, TURN_RIGHT, TURN_RIGHT, WALK));
    storage.put(plateau.name, plateau);

  }

  public void stop() {
    server.close();
  }

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {

    Map<String, Plateau> storage = new ConcurrentHashMap<>();
    PlateauCommandHandler commandHandler = new PlateauCommandHandler(new PlateauRepository(storage));
    PlateauReadRepository repository = new PlateauReadRepository(storage);

    MarsRestServer verticle = new MarsRestServer(storage, commandHandler, repository);

    Vertx.vertx().deployVerticle(verticle);

    System.out.println("welcome to mars REST server!");

  }

}