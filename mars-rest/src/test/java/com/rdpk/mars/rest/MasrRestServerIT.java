package com.rdpk.mars.rest;


import com.rdpk.mars.domain.Coordinates;
import com.rdpk.mars.domain.Plateau;
import com.rdpk.mars.rest.command.PlateauCommandHandler;
import com.rdpk.mars.rest.command.PlateauRepository;
import com.rdpk.mars.rest.read.PlateauReadRepository;
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
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MasrRestServerIT {

  static MarsRestServer verticle;
  static WebClient client;
  static String HOST = "0.0.0.0";
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

    verticle = new MarsRestServer(storage, commandHandler, repository);

    System.out.println("will try to get an avaliable port");
    MarsRestServer.httpPort = httpPort();
    System.out.println("will try to deploy verticle using HTTP_PORT = " + verticle.httpPort);
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

  @DisplayName("when calling GET /plateaus then it will return all the the plateaus on storage")
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
    String expected1 = "[{\"id\":\"teste1\",\"x\":5,\"y\":5,\"rovers\":[{\"id\":1,\"x\":1,\"y\":3," +
            "\"direction\":\"N\"},{\"id\":2,\"x\":5,\"y\":1,\"direction\":\"E\"}],\"activeRover\":{\"" +
            "id\":2,\"x\":5,\"y\":1,\"direction\":\"E\"}}]";
    client
            .get(MarsRestServer.httpPort, HOST, "/plateaus")
            .expect(ResponsePredicate.SC_SUCCESS)
            .expect(ResponsePredicate.JSON)
            .send(tc.succeeding(response -> tc.verify(() -> {
                      JsonArray array = response.bodyAsJsonArray();
                      assertThat(1).isEqualTo(array.size());
                      String received = array.toString();
                      assertThat(expected1).isEqualTo(received);
                      tc.completeNow();
                    })
                    )
            );
  }

  @DisplayName("when calling GET /plateaus/teste1 with a valid plateau then it will return the plateau")
  @Test
  void a2(VertxTestContext tc) {
    Plateau plateau = new Plateau("teste1");
    plateau.resize(new Coordinates(5, 5));
    storage.put(plateau.name, plateau);
    client
            .get(MarsRestServer.httpPort, HOST, "/plateaus/teste1")
            .expect(ResponsePredicate.SC_SUCCESS)
            .expect(ResponsePredicate.JSON)
            .as(BodyCodec.jsonObject())
            .send(tc.succeeding(response -> tc.verify(() -> {
                      JsonObject expected = new JsonObject().put("id", "teste1").put("x", 5).put("y", 5)
                              .put("rovers", emptyList()).putNull("activeRover");
                      assertThat(response.body()).isEqualTo(expected);
                      tc.completeNow();
                    })
                    )
            );
  }

  @DisplayName("when calling GET /plateaus/p0 with a missing plateau then it will return 404")
  @Test
  void a21(VertxTestContext tc) {
    storage.clear();
    client
            .get(MarsRestServer.httpPort, HOST, "/plateaus/p0")
            .expect(ResponsePredicate.SC_NOT_FOUND)
            .send(tc.succeeding(response -> tc.verify(() -> {
                      assertThat(response.body()).isNull();
                      tc.completeNow();
                    })
                    )
            );
  }

  @DisplayName("when calling POST /plateaus/p1/commands/create then it will issue a GET /plateaus/p1")
  @Test
  void a3(VertxTestContext tc) {
    storage.clear();
    JsonObject request = new JsonObject().put("x", 1).put("y", 2);
    client
            .post(MarsRestServer.httpPort, HOST, "/plateaus/p1/commands/create")
            .as(BodyCodec.jsonObject())
            .expect(ResponsePredicate.SC_SUCCESS)
            .expect(ResponsePredicate.JSON)
            .sendJson(request, tc.succeeding(response -> tc.verify(() -> {
                      JsonObject expected = new JsonObject().put("id", "p1").put("x", 1).put("y", 2)
                              .put("rovers", emptyList()).putNull("activeRover");
                      assertThat(response.body()).isEqualTo(expected);
                      tc.completeNow();
                    })
                    )
            );
  }

  @DisplayName("when calling POST /plateaus/p1/commands/activate then it will issue a GET /plateaus/p1")
  @Test
  void a31(VertxTestContext tc) {
    storage.clear();
    Plateau plateau = new Plateau("p1");
    plateau.resize(new Coordinates(5, 5));
    storage.put(plateau.name, plateau);
    JsonObject request = new JsonObject().put("x", 1).put("y", 2).put("direction", "N");
    client
            .post(MarsRestServer.httpPort, HOST, "/plateaus/p1/commands/activate")
            .as(BodyCodec.jsonObject())
            .expect(ResponsePredicate.SC_SUCCESS)
            .expect(ResponsePredicate.JSON)
            .sendJson(request, tc.succeeding(response -> tc.verify(() -> {
                      String expected = "{\"id\":\"p1\",\"x\":5,\"y\":5,\"rovers\":[{\"id\":1,\"x\":1,\"y\":2," +
                              "\"direction\":\"N\"}],\"activeRover\":{\"id\":1,\"x\":1,\"y\":2,\"direction\":\"N\"}}";
                      assertThat(response.body().encode()).isEqualTo(expected);
                      tc.completeNow();
                    })
                    )
            );
  }

  @DisplayName("when calling POST /plateaus/p1/commands/move then it will issue a GET /plateaus/p1")
  @Test
  void a32(VertxTestContext tc) {
    storage.clear();
    Plateau plateau = new Plateau("p1");
    plateau.resize(new Coordinates(5, 5));
    // rover 1
    plateau.activate(new Coordinates(1, 2), NORTH);
//    plateau.move(asList(TURN_LEFT, WALK, TURN_LEFT, WALK, TURN_LEFT, WALK, TURN_LEFT, WALK, WALK));
    storage.put(plateau.name, plateau);
    JsonObject request = new JsonObject().put("actions", "LMLMLMLMM");
    client
            .post(MarsRestServer.httpPort, HOST, "/plateaus/p1/commands/move")
            .as(BodyCodec.jsonObject())
            .expect(ResponsePredicate.SC_SUCCESS)
            .expect(ResponsePredicate.JSON)
            .sendJson(request, tc.succeeding(response -> tc.verify(() -> {
                      String expected = "{\"id\":\"p1\",\"x\":5,\"y\":5,\"rovers\":[{\"id\":1,\"x\":1,\"y\":3," +
                              "\"direction\":\"N\"}],\"activeRover\":{\"id\":1,\"x\":1,\"y\":3,\"direction\":\"N\"}}";
                      assertThat(response.body().encode()).isEqualTo(expected);
                      tc.completeNow();
                    })
                    )
            );
  }

}
