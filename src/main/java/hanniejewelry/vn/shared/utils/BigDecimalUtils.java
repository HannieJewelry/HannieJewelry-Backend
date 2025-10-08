package hanniejewelry.vn.shared.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class BigDecimalUtils {
    
    public static final int PRECISION = 16;
    public static final int SCALE = 1;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    

    public static BigDecimal of(double value) {
        return BigDecimal.valueOf(value).setScale(SCALE, ROUNDING_MODE);
    }
    

    public static BigDecimal of(long value) {
        return BigDecimal.valueOf(value).setScale(SCALE, ROUNDING_MODE);
    }
    

    public static BigDecimal of(String value) {
        return new BigDecimal(value).setScale(SCALE, ROUNDING_MODE);
    }
    

    public static BigDecimal of(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.setScale(SCALE, ROUNDING_MODE);
    }
    

    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        if (a == null) return of(b);
        if (b == null) return of(a);
        return a.add(b).setScale(SCALE, ROUNDING_MODE);
    }
    

    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        if (a == null) return of(BigDecimal.ZERO.subtract(b));
        if (b == null) return of(a);
        return a.subtract(b).setScale(SCALE, ROUNDING_MODE);
    }
    

    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        if (a == null || b == null) return BigDecimal.ZERO.setScale(SCALE, ROUNDING_MODE);
        return a.multiply(b).setScale(SCALE, ROUNDING_MODE);
    }
    

    public static BigDecimal multiply(BigDecimal a, long b) {
        if (a == null) return BigDecimal.ZERO.setScale(SCALE, ROUNDING_MODE);
        return a.multiply(BigDecimal.valueOf(b)).setScale(SCALE, ROUNDING_MODE);
    }
    

    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        if (a == null) return BigDecimal.ZERO.setScale(SCALE, ROUNDING_MODE);
        if (b == null || b.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return a.divide(b, SCALE, ROUNDING_MODE);
    }
    

    public static BigDecimal zero() {
        return BigDecimal.ZERO.setScale(SCALE, ROUNDING_MODE);
    }
    

    public static boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }
    

    public static BigDecimal normalize(BigDecimal value) {
        if (value == null) {
            return zero();
        }
        return value.setScale(SCALE, ROUNDING_MODE);
    }
} 