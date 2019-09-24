package com.zqh.player.tools.common.util;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @discription:
 * @date: 2018/09/26 下午2:55
 */
public class CurrencyUtils {

    public static HashMap<String, BigDecimal> USD_CURRENCY_MAP = new HashMap<String, BigDecimal>() {
        {
            put("", new BigDecimal(1));
            put("USD", new BigDecimal(1));
            put("CNY", new BigDecimal("0.1455")); // 人民币 2018-09-26
            put("HKD", new BigDecimal("0.128")); // 港元 2018-09-26
            put("TWD", new BigDecimal("0.0327")); // 新台币 2018-09-26
            put("CAD", new BigDecimal("0.7722")); // 加元 2018-09-26
            put("EUR", new BigDecimal("1.1763")); // 欧元 2018-09-26
            put("JPY", new BigDecimal("0.0089")); // 日元 2018-09-26
            put("GBP", new BigDecimal("1.3172")); // 英镑 2018-09-26
            put("KRW", new BigDecimal("0.0009")); // 韩元 2018-09-26
            put("AUD", new BigDecimal("0.726")); // 澳元 2018-09-26
            put("VND", new BigDecimal("0.00004285")); // 越南盾 2018-09-26
            put("LAK", new BigDecimal("0.0001")); // 老挝基普 2018-09-26
            put("KHR", new BigDecimal("0.0002")); // 柬埔寨瑞尔 2018-09-26
            put("BUK", new BigDecimal("0.0006")); // 缅甸元 2018-09-26
            put("THB", new BigDecimal("0.0308")); // 泰铢 2018-09-26
            put("MYR", new BigDecimal("0.2416")); // 马来西亚林吉特 2018-09-26
            put("SGD", new BigDecimal("0.7328")); // 新加坡元 2018-09-26
            put("IDR", new BigDecimal("0.0001")); // 印尼卢比 2018-09-26
            put("PHP", new BigDecimal("0.0184")); // 菲律宾比索 2018-09-26
            put("BND", new BigDecimal("0.7326")); // 文莱元 2018-09-26
            put("RUB", new BigDecimal("0.0152")); // 卢布 2018-09-28
        }
    };

    /**
     * 货币值转换
     * @param currency 当前金额币种
     * @param money 当前金额
     * @param localCurrency 转换后币种
     * @return
     */
    public static Double getLocalCurrencyMoney(String currency, Double money, String localCurrency) {
        return new BigDecimal(money).multiply(USD_CURRENCY_MAP.get(currency))
                .divide(USD_CURRENCY_MAP.get(localCurrency), 10, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
