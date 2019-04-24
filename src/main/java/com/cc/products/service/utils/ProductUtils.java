package com.cc.products.service.utils;

import com.cc.jlproduct.dto.JLProduct;
import com.cc.jlproduct.dto.Price;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Optional;

/**
 * Product utility class to
 * Format various Prices and
 * Price Label
 */
public abstract class ProductUtils {
    private static final DecimalFormat df2 = new DecimalFormat("0.00");
    private static final DecimalFormat dfx = new DecimalFormat("#.##");
    private static final DecimalFormat df0 = new DecimalFormat("0");

    private static final String SHOW_WAS_THEN_NOW = "ShowWasThenNow";
    private static final String SHOW_PERC_DISCOUNT = "ShowPercDscount";
    private static final String SHOW_WAS_THEN_NOW_LABEL = "Was %s, Then %s, now %s";
    private static final String SHOW_PERC_OFF_LABEL = "%s%% off, now %s";
    private static final String SHOW_WAS_NOW_LABEL = "Was %s, now %s";

    public static String getPriceLabel(final JLProduct jlProduct, final String labelType) {
        if (labelType.equalsIgnoreCase(SHOW_WAS_THEN_NOW) &&
                (Optional.ofNullable(jlProduct.getPrice().getThen2()).isPresent() ||
                        Optional.ofNullable(jlProduct.getPrice().getThen1()).isPresent())) {
            return String.format(SHOW_WAS_THEN_NOW_LABEL, formatWasPrice(jlProduct), formatThenPrice(jlProduct),
                    formatNowPrice(jlProduct));
        }
        Optional<BigDecimal> was = Optional.ofNullable(jlProduct.getPrice().getWas());
        if (labelType.equalsIgnoreCase(SHOW_PERC_DISCOUNT) && was.isPresent()) {
            BigDecimal percent = BigDecimal.ONE
                    .subtract(jlProduct.getPrice().getNow()
                            .divide(was.orElse(BigDecimal.ONE), RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.CEILING);
            String format = dfx.format(percent);
            return String.format(SHOW_PERC_OFF_LABEL, format, formatNowPrice(jlProduct));
        }
        else {
            return String.format(SHOW_WAS_NOW_LABEL, formatWasPrice(jlProduct), formatNowPrice(jlProduct));
        }
    }

    public static String formatNowPrice(final JLProduct jlProduct) {
        BigDecimal price = jlProduct.getPrice().getNow();
        String symbol = jlProduct.getPrice().getCurrency().getSymbol();
        return formatPrice(symbol, price);
    }

    public static boolean isProductPriceReduced(final JLProduct jlProduct) {
        Price price = jlProduct.getPrice();
        return Optional.ofNullable(price.getWas()).orElse(price.getNow()).subtract(price.getNow()).compareTo(BigDecimal.ZERO) > 0;
    }

    private static String formatThenPrice(JLProduct jlProduct) {
        BigDecimal then = Optional.ofNullable(jlProduct.getPrice().getThen2())
                .orElseGet(() -> Optional.ofNullable(jlProduct.getPrice().getThen1()).orElse(BigDecimal.ZERO));

        return formatPrice(jlProduct.getPrice().getCurrency().getSymbol(), then);
    }

    private static String formatPrice(String symbol, BigDecimal price) {
        StringBuilder priceFormat = new StringBuilder();
        priceFormat.append(symbol);
        if (price.compareTo(BigDecimal.TEN) == -1) {
            priceFormat.append(df2.format(price));
        } else {
            priceFormat.append(df0.format(price));
        }
        return priceFormat.toString();
    }

    private static String formatWasPrice(JLProduct jlProduct) {
        BigDecimal price = Optional.ofNullable(jlProduct.getPrice().getWas()).orElse(BigDecimal.ZERO);
        String symbol = jlProduct.getPrice().getCurrency().getSymbol();
        return formatPrice(symbol, price);
    }
}
