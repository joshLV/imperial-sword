package cn.lovike.tool.basic.framework.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author lovike
 * @since 2021/4/7
 */
public class ArithUtil {

    /**
     * 将Object转换为BigDecimal
     */
    public static BigDecimal toBigDecimal(Object obj) {
        if (obj == null) {
            throw new NullPointerException("Parameter is null");
        }
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        if (obj instanceof BigInteger) {
            return new BigDecimal((BigInteger) obj);
        }
        if (obj instanceof Float) {
            return new BigDecimal((Float) obj);
        }
        if (obj instanceof Double) {
            return new BigDecimal((Double) obj);
        }
        if (obj instanceof Byte) {
            return new BigDecimal((Byte) obj);
        }
        if (obj instanceof Short) {
            return new BigDecimal((Short) obj);
        }
        if (obj instanceof Integer) {
            return new BigDecimal((Integer) obj);
        }
        if (obj instanceof Long) {
            return new BigDecimal((Long) obj);
        }
        if (obj instanceof String) {
            return new BigDecimal((String) obj);
        }
        throw new RuntimeException("Unknown type of parameter");
    }

    /**
     * add 正常计算
     */
    public static BigDecimal add(Object o1, Object o2) {
        return toBigDecimal(o1).add(toBigDecimal(o2));
    }

    /**
     * add 四舍五入
     */
    public static BigDecimal addRounding(Object o1, Object o2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("Passed an invalid or incorrect argument");
        }
        BigDecimal db = toBigDecimal(o1).add(toBigDecimal(o2));
        return db.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * add 截断
     */
    public static BigDecimal addTrunc(Object o1, Object o2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("Passed an invalid or incorrect argument");
        }
        BigDecimal db = toBigDecimal(o1).add(toBigDecimal(o2));
        return db.setScale(scale, BigDecimal.ROUND_DOWN);
    }

    /**
     * subtract 正常计算
     */
    public static BigDecimal sub(Object o1, Object o2) {
        return toBigDecimal(o1).subtract(toBigDecimal(o2));
    }

    /**
     * subtract 四舍五入
     */
    public static BigDecimal subRounding(Object o1, Object o2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("Passed an invalid or incorrect argument");
        }
        BigDecimal db = toBigDecimal(o1).subtract(toBigDecimal(o2));
        return db.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * subtract 截断
     */
    public static BigDecimal subTrunc(Object o1, Object o2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("Passed an invalid or incorrect argument");
        }
        BigDecimal db = toBigDecimal(o1).subtract(toBigDecimal(o2));
        return db.setScale(scale, BigDecimal.ROUND_DOWN);
    }

    /**
     * multiply 正常计算
     */
    public static BigDecimal mul(Object o1, Object o2) {
        return toBigDecimal(o1).multiply(toBigDecimal(o2));
    }

    /**
     * multiply 四舍五入
     */
    public static BigDecimal mulRounding(Object o1, Object o2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("Passed an invalid or incorrect argument");
        }
        BigDecimal db = toBigDecimal(o1).multiply(toBigDecimal(o2));
        return db.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * multiply 截断
     */
    public static BigDecimal mulTrunc(Object o1, Object o2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("Passed an invalid or incorrect argument");
        }
        BigDecimal db = toBigDecimal(o1).multiply(toBigDecimal(o2));
        return db.setScale(scale, BigDecimal.ROUND_DOWN);
    }

    /**
     * divide 四舍五入
     */
    public static BigDecimal divRounding(Object o1, Object o2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("Passed an invalid or incorrect argument");
        }
        return toBigDecimal(o1).divide(toBigDecimal(o2), scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * divide 截断
     */
    public static BigDecimal divTrunc(Object o1, Object o2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("Passed an invalid or incorrect argument");
        }
        return toBigDecimal(o1).divide(toBigDecimal(o2), scale, BigDecimal.ROUND_DOWN);
    }

    /**
     * 保留小数点，四舍五入
     */
    public static BigDecimal toRounding(Object obj, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("Passed an invalid or incorrect argument");
        }
        return toBigDecimal(obj).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 保留小数点，截断
     */
    public static BigDecimal toTrunc(Object obj, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("Passed an invalid or incorrect argument");
        }
        return toBigDecimal(obj).setScale(scale, BigDecimal.ROUND_DOWN);
    }
}