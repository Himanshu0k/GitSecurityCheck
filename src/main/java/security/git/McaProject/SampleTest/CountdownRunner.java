package security.git.McaProject.SampleTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountdownRunner {

    private static final Logger logger = LoggerFactory.getLogger(CountdownRunner.class);

    public void run() {
        int countdown = 10;
        while (countdown > 0) {
            logger.info("Countdown: {}", countdown);
            countdown--;
        }
    }
}