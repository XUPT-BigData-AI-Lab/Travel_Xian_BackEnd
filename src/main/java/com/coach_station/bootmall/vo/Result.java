package com.coach_station.bootmall.vo;

import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {

	private Integer code;

	private String message;

	private Map<String, Object> data = new HashMap<String, Object>();

	private Result(){}

	public static Result ok(){
		Result r = new Result();
		r.setCode(ResultCodeEnum.SUCCESS.getCode());
		r.setMessage(ResultCodeEnum.SUCCESS.getMessage());
		return r;
	}

	public static Result error(){
		Result r = new Result();
		r.setCode(ResultCodeEnum.UNKNOWN_REASON.getCode());
		r.setMessage(ResultCodeEnum.UNKNOWN_REASON.getMessage());
		return r;
	}

	public static Result setResult(ResultCodeEnum resultCodeEnum){
		Result r = new Result();
		r.setCode(resultCodeEnum.getCode());
		r.setMessage(resultCodeEnum.getMessage());
		return r;
	}

	public Result success(Boolean success){
//		this.setSuccess(success);
		return this;
	}

	public Result message(String message){
		this.setMessage(message);
		return this;
	}

	public Result code(Integer code){
		this.setCode(code);
		return this;
	}

	public Result data(String key, Object value){
		this.data.put(key, value);
		return this;
	}

	public Result data(Map<String, Object> map){
		this.setData(map);
		return this;
	}
}