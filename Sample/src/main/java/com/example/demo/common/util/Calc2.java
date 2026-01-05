package com.example.demo.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class BigDecimalCalc {

    private static final int SCALE = 16;
    private static final RoundingMode RM = RoundingMode.HALF_UP;

    private final BigDecimal value;

    // 内部コンストラクタ
    private BigDecimalCalc(BigDecimal value) {
        this.value = value;
    }

    /* =========================
     * factory
     * ========================= */
    public static BigDecimalCalc from(Object input) {
        return new BigDecimalCalc(toBigDecimal(input));
    }

    /* =========================
     * operations（イミュータブル）
     * ========================= */
    public BigDecimalCalc add(Object n) {
        return new BigDecimalCalc(
            this.value.add(toBigDecimal(n))
        );
    }

    public BigDecimalCalc sub(Object n) {
        return new BigDecimalCalc(
            this.value.subtract(toBigDecimal(n))
        );
    }

    public BigDecimalCalc mul(Object n) {
        return new BigDecimalCalc(
            this.value.multiply(toBigDecimal(n))
        );
    }

    public BigDecimalCalc div(Object n) {
        return new BigDecimalCalc(
            this.value.divide(toBigDecimal(n), SCALE, RM)
        );
    }

    /* =========================
     * result
     * ========================= */
    public BigDecimal result() {
        return this.value;
    }

    /* =========================
     * conversion（責務集約）
     * ========================= */
    private static BigDecimal toBigDecimal(Object input) {
        Objects.requireNonNull(input, "nullは変換できません");

        if (input instanceof BigDecimal bd) {
            return bd;
        }
        if (input instanceof String s) {
            return new BigDecimal(s);
        }
        if (input instanceof Integer || input instanceof Long) {
            return BigDecimal.valueOf(((Number) input).longValue());
        }
        if (input instanceof Double || input instanceof Float) {
            // double/floatは精度保証しない前提で明示的に許可
            return BigDecimal.valueOf(((Number) input).doubleValue());
        }

        throw new IllegalArgumentException("変換できない型: " + input.getClass());
    }
}
