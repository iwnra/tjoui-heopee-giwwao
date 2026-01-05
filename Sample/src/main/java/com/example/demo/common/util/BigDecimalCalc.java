package com.example.demo.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

/**
 * BigDecimal計算を直感的に記述するためのラッパークラス
 */
public final class Calc {

    private final BigDecimal value;

    // デフォルトのスケールと丸めモード（必要に応じてプロジェクトの規約に合わせる）
    private static final int DEFAULT_SCALE = 2;
    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_UP;

    // コンストラクタを private にして外部からの直接生成を禁止
    private Calc(BigDecimal value) {
        this.value = Optional.ofNullable(value).orElse(BigDecimal.ZERO);
    }

    /**
     * 計算を開始するための静的ファクトリメソッド
     */
    public static Calc from(Object input) {
        return new Calc(toBigDecimal(input));
    }

    /** 加算: (this + n) */
    public Calc add(Object n) {
        return new Calc(this.value.add(toBigDecimal(n)));
    }

    /** 減算: (this - n) */
    public Calc sub(Object n) {
        return new Calc(this.value.subtract(toBigDecimal(n)));
    }

    /** 乗算: (this * n) */
    public Calc mul(Object n) {
        return new Calc(this.value.multiply(toBigDecimal(n)));
    }

    /** 除算: (this / n) 小数点第2位で四捨五入する例 */
    public Calc div(Object n) {
        return new Calc(this.value.divide(toBigDecimal(n), DEFAULT_SCALE, DEFAULT_ROUNDING));
    }

    /** 結果をBigDecimalで取得 */
    public BigDecimal result() {
        return this.value;
    }

    /** ユーティリティ: さまざまな型をBigDecimalに変換 */
    private static BigDecimal toBigDecimal(Object input) {
        if (input == null) return BigDecimal.ZERO;
        if (input instanceof BigDecimal) return (BigDecimal) input;
        
        // 整数系（精度の欠落を防ぐために longValue で受ける）
        if (input instanceof Integer || input instanceof Long || input instanceof Short || input instanceof Byte) {
            return BigDecimal.valueOf(((Number) input).longValue());
        }
        
        // 浮動小数点系
        if (input instanceof Double || input instanceof Float) {
            // Double.toString() を経由するか、valueOf を使う
            return BigDecimal.valueOf(((Number) input).doubleValue());
        }

        if (input instanceof String) {
            String s = (String) input;
            return s.isEmpty() ? BigDecimal.ZERO : new BigDecimal(s);
        }
        
        throw new IllegalArgumentException("Unsupported type: " + input.getClass());
    }
}