package com.zqh.player.tools.common.exception;

/**
 * Created by orange on 18/8/10.
 */
public class ExceptionCode {
    /**
     * 通用系统异常
     */
    public static final Integer SYS_ERROR = 500;
    /**
     * 没有图片验证码
     */
    public static final Integer NO_CAPTCHA = 501;
    /**
     * 余额不足(前端需要根据code去做逻辑判断的异常码 可以单独拿出来,其余用通用系统异常即可)
     */
    public static final Integer NO_MONEY = 407;
    /**
     * 权限不足
     */
    public static final Integer NO_PERMISSION = 600;
    /**
     * 会话失效
     */
    public static final Integer NO_SESSION = 700;

    /**
     * 平台sdk异常
     */
    public static final Integer PLATFORM_SDK_ERROR = 800;

    /**
     * 平台sdk网络异常
     */
    public static final Integer PLATFORM_SDK_NET_ERROR = 900;

}
