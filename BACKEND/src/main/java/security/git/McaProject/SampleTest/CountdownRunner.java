package security.git.McaProject.SampleTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountdownRunner {

    private static final Logger logger = LoggerFactory.getLogger(CountdownRunner.class);

    public void run() {
        while(true) {
            System.out.println("hello");
        }
    }
}