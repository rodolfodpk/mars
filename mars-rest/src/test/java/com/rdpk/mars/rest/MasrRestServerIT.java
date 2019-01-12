package com.rdpk.mars.rest;


import com.rdpk.mars.command.PlateauCommandHandler;
import com.rdpk.mars.command.PlateauRepository;
import com.rdpk.mars.domain.Coordinates;
import com.rdpk.mars.domain.Plateau;
import com.rdpk.mars.read.PlateauReadRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;

import static com.rdpk.mars.domain.Direction.EAST;
import static com.rdpk.mars.domain.Direction.NORTH;
import static com.rdpk.mars.domain.MoveRoverAction.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MasrRestServerIT {

  static MarsRestServer1 verticle;
  static WebClient client;
  static String HOST =  "0.0.0.0";
  static ConcurrentHashMap<String, Plateau> storage;

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

    verticle = new MarsRestServer1(storage, commandHandler, repository);

    System.out.println("will try to get an avaliable port");
    MarsRestServer1.httpPort = httpPort();
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
    String expected1 = "[{\"id\":\"teste1\",\"x\":5,\"y\":5,\"rovers\":[{\"id\":1,\"x\":1,\"y\":3,\"direction\":\"N\"},{\"id\":2,\"x\":5,\"y\":1,\"direction\":\"E\"}],\"activeRover\":{\"id\":2,\"x\":5,\"y\":1,\"direction\":\"E\"}}]";
    client
      .get(MarsRestServer1.httpPort, HOST, "/plateaus")
      .expect(ResponsePredicate.SC_SUCCESS)
      .expect(ResponsePredicate.JSON)
      .send(tc.succeeding(response -> tc.verify(() -> {
            JsonArray array = response.bodyAsJsonArray();
            assertThat(1).isEqualTo(array.size());
            String received = array.toString();
            assertThat(200).isEqualTo(response.statusCode());
            assertThat(expected1).isEqualTo(received);
            tc.completeNow();
          })
        )
      );
  }

  @DisplayName("POST /plateaus/p1/commands/create")
  @Test
  void a2(VertxTestContext tc) {
    storage.clear();
    JsonObject request = new JsonObject().put("y", 5).put("x", 5);
    client
      .post(MarsRestServer1.httpPort, HOST, "/plateaus/p1/commands/create")
      .as(BodyCodec.jsonObject())
      .sendJson(request, tc.succeeding(response -> tc.verify(() -> {
          assertThat(201).isEqualTo(response.statusCode());
          assertThat(response.bodyAsString()).isNull();
          assertThat(response.getHeader("Location")).isEqualTo("/plateaus/p1");
          tc.completeNow();
        })
      )
    );
  }

}
