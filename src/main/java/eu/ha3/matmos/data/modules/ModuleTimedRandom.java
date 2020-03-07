package eu.ha3.matmos.data.modules;

import java.util.Random;

import eu.ha3.matmos.core.sheet.DataPackage;

/**
 * @author dags_ <dags@dags.me>
 */

public class ModuleTimedRandom extends ModuleProcessor implements Module {
    private static final Random RANDOM = new Random();

    private final TimedValue[] timedRandoms = new TimedValue[5];

    public ModuleTimedRandom(DataPackage data) {
        super(data, "timed_random");
        timedRandoms[0] = new TimedValue(1);
        timedRandoms[1] = new TimedValue(2);
        timedRandoms[2] = new TimedValue(5);
        timedRandoms[3] = new TimedValue(10);
        timedRandoms[4] = new TimedValue(20);
    }

    @Override
    protected void doProcess() {
        for (TimedValue timedRandom : timedRandoms) {
            timedRandom.process(this);
        }
    }

    private static class TimedValue {
        private final String playlistId;
        private final long period;

        private int activeValue = -1;
        private long endTime;

        public TimedValue(int mins) {
            playlistId = "timed_random_" + (mins < 10 ? "0" + mins : mins) + "mins";
            period = 1000 * 60 * mins;
        }

        public void process(ModuleTimedRandom timedRandom) {
            if (activeValue != -1) {
                if (endTime < System.currentTimeMillis()) {
                    activeValue = RANDOM.nextInt(100);
                    endTime = System.currentTimeMillis() + period;
                    timedRandom.setValue(playlistId, activeValue);
                }
            } else {
                activeValue = RANDOM.nextInt(100);
            }
        }
    }
}
