package com.github.noahcunni;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ScheduledExecutorService;

import com.github.noahcunni.therapy.BolusRequest;
import com.github.noahcunni.therapy.bolus.BolusProposal;
import com.github.noahcunni.therapy.bolus.BolusService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
            server.createContext("/", this::handleRoot);
            server.createContext("/status", this::handleStatus);
            server.createContext("/bolus/preview", this::handleBolusRequest);
            server.createContext("/bolus/confirmation", this::handleBolusConfirmation);
            server.start();
        System.out.println("Pump control listening on port " + port);
    }

    private void handleRoot(HttpExchange ex) throws IOException {
        try (var in = PumpHttpServer.class.getResourceAsStream("/web/index.html")) {
            if (in == null) throw new FileNotFoundException("/web/index.html not on classpath");
            respondHtml(ex, 200, new String(in.readAllBytes(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            respond(ex, 500, "Error getting root page: " + e);
        }
    }

    private void handleStatus(HttpExchange ex) throws IOException {
        try {
            var status = pumpThread.submit(() -> pump.status()).get();
            respond(ex, 200, GSON.toJson(status));
        } catch (Exception e) {
            respond(ex, 500, "{\"error\": Status could not be retreieved: " + e + "}");
        }
    }

    private void handleBolusRequest(HttpExchange ex) throws IOException {
        if (!ex.getRequestMethod().equals("POST")) {
            respond(ex, 405, "{\"error\": POST only}");
            return;
        }
        BolusRequest req;
        try {
            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            req = GSON.fromJson(body, BolusRequest.class);

            BolusProposal proposal = bolusService.preview(req);

            respond(ex, 200, GSON.toJson(proposal));
        } catch (Exception e) {
            respond(ex, 500, "{\"error\": " + e + " }");
        } 
    }

    private void handleBolusConfirmation(HttpExchange ex) throws IOException {
        if (!ex.getRequestMethod().equals("POST")) {
            respond(ex, 405, "{\"error\": POST only}");
            return;
        }

        try {
            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

            int bolusId = jsonObject.get("proposalId").getAsInt();
            bolusService.review(bolusId);
            respond(ex, 200, "SENT FOR REVIEW");
        } catch (Exception e) {
    
            respond(ex, 500, "{\"error\": " + e + "}");
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

    private void respondHtml(HttpExchange ex, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        ex.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }
}


//(var reader = new InputStreamReader(ex.getRequestBody(), StandardCharsets.UTF_8)) 