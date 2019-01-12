package com.rdpk.mars.rest;


import com.rdpk.mars.command.PlateauCommandHandler;
import com.rdpk.mars.command.PlateauRepository;
import com.rdpk.mars.domain.Coordinates;
import com.rdpk.mars.domain.Plateau;
import com.rdpk.mars.read.PlateauReadRepository;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.rdpk.mars.domain.Direction.EAST;
import static com.rdpk.mars.domain.Direction.NORTH;
import static com.rdpk.mars.domain.MoveRoverAction.*;
import static java.util.Arrays.asList;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MasrRestServerIT {

  static MarsRestServer verticle;
  static WebClient client;
  static String HOST =  "0.0.0.0";
  static Map<String, Plateau> storage;

  private static int httpPort() {
    int httpPort = 0;
    try {
      ServerSocket socket = new ServerSocket(0);
      httpPort = socket.getLocalPort();
      socket.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return httpPort;
  }

  @BeforeAll
  static void setup(VertxTestContext tc, Vertx vertx) {

    storage = new ConcurrentHashMap<>();
    PlateauCommandHandler commandHandler = new PlateauCommandHandler(new PlateauRepository(storage));
    PlateauReadRepository repository = new PlateauReadRepository(storage);

    verticle = new MarsRestServer(storage, commandHandler, repository);

    System.out.println("will try to get an avaliable port");
    verticle.httpPort = httpPort();
    System.out.println("will try to deploy MainVerticle using HTTP_PORT = " + verticle.httpPort);
    client = WebClient.create(vertx);
    vertx.executeBlocking(event -> vertx.deployVerticle(verticle, deploy -> {
        event.complete();
    }), (Handler<AsyncResult<Void>>) event -> {
      if (event.succeeded()) {
        tc.completeNow();
      } else {
        tc.failNow(event.cause());
      }
    });
  }

  @DisplayName("GET /plateaus")
  @Test
  void a1(VertxTestContext tc) {
    storage.clear();
    Plateau plateau = new Plateau("teste1");
    plateau.resize(new Coordinates(5, 5));
    // rover 1
    plateau.activate(new Coordinates(1, 2), NORTH);
    plateau.move(asList(TURN_LEFT, WALK, TURN_LEFT, WALK, TURN_LEFT, WALK, TURN_LEFT, WALK, WALK));
    // rover 2
    plateau.activate(new Coordinates(3, 3), EAST);
    plateau.move(asList(WALK, WALK, TURN_RIGHT, WALK, WALK, TURN_RIGHT, WALK, TURN_RIGHT, TURN_RIGHT, WALK));
    storage.put(plateau.name, plateau);
    // TODO rovers in order, please
    String expected1 = "[{\"id\":\"teste1\",\"x\":5,\"y\":5,\"rovers\":[{\"id\":1,\"x\":1,\"y\":3,\"direction\":\"N\"},{\"id\":2,\"x\":5,\"y\":1,\"direction\":\"E\"}],\"activeRover\":{\"id\":2,\"x\":5,\"y\":1,\"direction\":\"E\"}}]";
    String expected2 = "[{\"id\":\"teste1\",\"x\":5,\"y\":5,\"rovers\":[{\"id\":2,\"x\":5,\"y\":1,\"direction\":\"E\"},{\"id\":1,\"x\":1,\"y\":3,\"direction\":\"N\"}],\"activeRover\":{\"id\":2,\"x\":5,\"y\":1,\"direction\":\"E\"}}]";
    client
      .get(MarsRestServer.httpPort, HOST, "/plateaus")
      .expect(ResponsePredicate.SC_SUCCESS)
      .expect(ResponsePredicate.JSON)
      .send(tc.succeeding(response2 -> tc.verify(() -> {
            @Nullable JsonArray json = response2.bodyAsJsonArray();
            System.out.println("Received response with status code " + response2.statusCode() + " with body \n" +
                    json.encodePrettily());
            String received = json.toString();
            if (received.equals(expected1) || received.equals(expected2)) {
              tc.completeNow();
              return;
            }
            tc.failNow(new RuntimeException("unexpected response : " +  received));
          })
        )
      );
  }

}
