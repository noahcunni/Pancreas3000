package com.github.noahcunni;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ScheduledExecutorService;

import com.github.noahcunni.therapy.BolusRequest;
import com.github.noahcunni.therapy.bolus.BolusService;
import com.github.noahcunni.therapy.bolus.SafetyDecision;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class PumpHttpServer {
    private final int port;
    private final BolusService bolusService;
    private final Pump pump;
    private final ScheduledExecutorService pumpThread;

    private final Gson GSON;

    public PumpHttpServer(int port, BolusService bolusService,
        Pump pump, ScheduledExecutorService pumpThread) {
            this.port = port;
            this.bolusService = bolusService;
            this.pump = pump;
            this.pumpThread = pumpThread;

            this.GSON = new Gson();
        }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/status", this::handleStatus);
            server.createContext("/bolus/preview", this::handleBolusRequest);
            server.start();
        System.out.println("Pump control listening on port " + port);
    }

    private void handleStatus(HttpExchange ex) throws IOException {
        try {
            var status = pumpThread.submit(() -> pump.status()).get();
            respond(ex, 200, GSON.toJson(status));
        } catch (Exception e) {
            respond(ex, 500, "Error getting pump status");
        }
    }

    private void handleBolusRequest(HttpExchange ex) throws IOException {
        if (!ex.getRequestMethod().equals("POST")) {
            respond(ex, 405, "{\"error\"POST only\"}");
            return;
        }

        BolusRequest req;
        try (var reader = new InputStreamReader(ex.getRequestBody(),
            StandardCharsets.UTF_8)) {
                req = GSON.fromJson(reader, BolusRequest.class);
                SafetyDecision decision = bolusService.preview(req);
                respond(ex, 200, GSON.toJson(decision));
        } catch (Exception e) {
            respond(ex, 500, "{\"error\": " + e);
        }
    }

    private void respond(HttpExchange ex, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }
}
