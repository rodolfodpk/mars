package com.rdpk.mars.console;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.time.Duration;

class MarsServerClient {

  private static final MediaType JSON
          = MediaType.get("application/json");

  private static final MediaType TEXT
          = MediaType.get("plain/text");

  private static final String BASE_URL = "http://localhost:8080";

  // logger
  final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

  final OkHttpClient client = new OkHttpClient.Builder()
          .addInterceptor(loggingInterceptor)
          .callTimeout(Duration.ofSeconds(1))
          .build();

  final String plateauId;

  MarsServerClient(String plateauId) {
    this.plateauId = plateauId;
  }

  String create(Tuple<Integer, Integer> tuple) throws IOException {
    String json = String.format("{ \"x\":%s, \"y\":%s }", tuple.getLeft(), tuple.getRight());
    RequestBody body = RequestBody.create(JSON, json);
    String url = String.format("%s/%s/%s/%s", BASE_URL, "plateaus", plateauId, "commands/create");
    Request request = new Request.Builder()
            .url(url)
            .post(body)
            .addHeader("accept", "plain/text")
            .build();
    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }

  String activate(ActivateRoover activate) throws IOException {
    String json = String.format("{ \"x\":%s, \"y\":%s, \"direction\": \"%s\"}",
            activate.x, activate.y, activate.direction);
    RequestBody body = RequestBody.create(JSON, json);
    String url = String.format("%s/%s/%s/%s", BASE_URL, "plateaus", plateauId, "commands/activate");
    Request request = new Request.Builder()
            .url(url)
            .post(body)
            .addHeader("accept", "plain/text")
            .build();
    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }

  String move(String actions) throws IOException {
    String json = String.format("{ \"actions\":\"%s\" }", actions);
    RequestBody body = RequestBody.create(JSON, json);
    String url = String.format("%s/%s/%s/%s", BASE_URL, "plateaus", plateauId, "commands/move");
    Request request = new Request.Builder()
            .url(url)
            .post(body)
            .addHeader("accept", "plain/text")
            .build();
    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }
}
