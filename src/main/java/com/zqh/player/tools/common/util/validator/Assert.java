

package com.zqh.player.tools.common.util.validator;

import com.zqh.player.tools.common.exception.PlayerException;
import org.apache.commons.lang3.StringUtils;

/**
 * 数据校验
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new PlayerException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new PlayerException(message);
        }
    }
}
