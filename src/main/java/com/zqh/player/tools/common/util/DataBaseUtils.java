package com.zqh.player.tools.common.util;

/**
 * @discription:
 * @date: 2018/08/14 下午3:35
 */
public class DataBaseUtils {

    public static final int ORDER_SPLIT = 100;  //订单表

    public static final int RAW_ORDER_SPLIT = 100;  //订单原始数据表

    public static final int PRODUCT_SPLIT = 100;  //产品统一分表数量

    public static final int TJ_SPLIT = 10; //统计表默认数量


    public static Integer getOrderTbId(Integer founderId) {
        if (founderId != null) {
            return founderId % ORDER_SPLIT;
        }
        return null;
    }

    public static Integer getRawOrderTbId(Integer tableRoutingId) {
        if (tableRoutingId != null) {
            return tableRoutingId % RAW_ORDER_SPLIT;
        }
        return null;
    }

    public static Integer getProductTbId(Integer founderId) {
        if (founderId != null) {
            return founderId % PRODUCT_SPLIT;
        }
        return null;
    }

    public static Integer getTJTbId(Integer founderId) {
        if (founderId != null) {
            return founderId % TJ_SPLIT;
        }
        return null;
    }
}
