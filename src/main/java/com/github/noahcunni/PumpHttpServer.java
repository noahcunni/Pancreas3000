package com.github.noahcunni;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledExecutorService;

import com.github.noahcunni.therapy.bolus.BolusService;

public class PumpHttpServer {
    private final int port;
    private final BolusService bolusService;
    private final Pump pump;
    private final ScheduledExecutorService pumpThread;

    public PumpHttpServer(int port, BolusService bolusService,
        Pump pump, ScheduledExecutorService pumpThread) {
            this.port = port;
            this.bolusService = bolusService;
            this.pump = pump;
            this.pumpThread = pumpThread;
        }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/status", this::handleStatus);
            server.start();
        System.out.println("Pump control listening on port " + port);
    }

    private void handleStatus(HttpExchange ex) throws IOException {
        try {
            var status = pumpThread.submit(() -> pump.status()).get();
            respond(ex, 200, status.toJson());
        } catch (Exception e) {
        }
    }
}
