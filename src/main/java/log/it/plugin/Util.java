package log.it.plugin;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Util {
    public static void printExecTime(String prefixMessage, LocalDateTime timeMark) {
        var millisDiff = ChronoUnit.MILLIS.between(timeMark, LocalDateTime.now());

        System.out.println(prefixMessage + "(" + millisDiff + " ms)");
        if (millisDiff > 100) {
            System.out.println("HIGH MILLIS DIFF");
        }
    }
}
