package groupquattro.demo.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Round {
    /**
     * Given a double precision floating point number
     * return a double which has exactly a certain amount of final digits representing
     * the fractional part.
     * @param value the number to round
     * @param places the number of digits that represent the fractional part
     * @return
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
