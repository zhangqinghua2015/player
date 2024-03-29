

package com.zqh.player.tools.common.page;

import com.zqh.player.tools.common.util.MapUtils;

import java.util.HashMap;

public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public R() {
		put("result", 100);
		put("message", "success");
		put("data",new MapUtils());
	}
	
	public static R error() {
		return error(500, "未知异常，请联系管理员");
	}
	
	public static R error(String msg) {
		return error(500, msg);
	}
	
	public static R error(int code, String msg) {
		R r = new R();
		r.put("result", code);
		r.put("message", msg);
		r.remove("data");
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("message", msg);
		return r;
	}
	
	public static R data(Object obj) {
		R r = new R();
		r.put("data",obj);
		return r;
	}
	
	public static R ok() {
		return new R();
	}

	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
