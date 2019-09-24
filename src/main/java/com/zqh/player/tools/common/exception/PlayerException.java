

package com.zqh.player.tools.common.exception;

/**
 * 异常类
 */
public class PlayerException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String msg;//国际化 消息文件对应的key
    private int code = 500;
	private Object[] args;//某些消息格式为xx{0}xx{1} args用来填充{0} {1}等

    public PlayerException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public PlayerException(String msg, Object... args) {
		super(msg);
		this.msg = msg;
		this.args = args;
	}

	public PlayerException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public PlayerException(int code, String msg) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public PlayerException(int code, String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
}
