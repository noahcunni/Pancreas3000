package pump;
import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import pump.therapy.InMemoryTherapyLog;
import pump.therapy.TherapyLog;
import pump.therapy.bolus.BolusService;

public class App {
    public static void main(String[] args) throws IOException {
        TherapySettings therapySettings = new TherapySettings();
        TherapyLog therapyLog = new InMemoryTherapyLog(therapySettings);
        
        Pump pump = new Pump(therapySettings, therapyLog);
        BolusService bolusService = new BolusService(pump, therapySettings);
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        pump.tick(Instant.now());
        pump.requestBolus(0.20);

        new PumpHttpServer(8080, bolusService, pump, scheduler).start();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                pump.tick(Instant.now());
            } catch (Exception e) {
                // log.error("tick failed", e);
                // pump.enterFault(e);
            }
        }, 0, 1, TimeUnit.SECONDS);

    }
}