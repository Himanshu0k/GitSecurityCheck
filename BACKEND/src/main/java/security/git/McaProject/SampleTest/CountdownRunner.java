package security.git.McaProject.SampleTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountdownRunner {

    private static final Logger logger = LoggerFactory.getLogger(CountdownRunner.class);

    public void runCountdown() {

        int countdown = 5;

        while (countdown > 0) {
            logger.info("Countdown: {}", countdown);
            countdown--;
        }

        logger.info("Countdown completed successfully.");
    }
}