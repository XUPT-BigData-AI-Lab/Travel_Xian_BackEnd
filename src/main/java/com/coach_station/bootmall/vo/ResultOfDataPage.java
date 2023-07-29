package com.coach_station.bootmall.vo;

import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: yjw
 * @Date: 2022/01/09/21:01
 * @Description:
 */
@Data
public class ResultOfDataPage {

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<String, Object>();

    private Integer page;

    private Integer size;

    private Long total;

    private ResultOfDataPage(){}

    public static ResultOfDataPage ok(){
        ResultOfDataPage r = new ResultOfDataPage();
        r.setCode(ResultCodeEnum.SUCCESS.getCode());
        r.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return r;
    }

    public static ResultOfDataPage error(){
        ResultOfDataPage r = new ResultOfDataPage();
        r.setCode(ResultCodeEnum.UNKNOWN_REASON.getCode());
        r.setMessage(ResultCodeEnum.UNKNOWN_REASON.getMessage());
        return r;
    }

    public static ResultOfDataPage setResult(ResultCodeEnum resultCodeEnum){
        ResultOfDataPage r = new ResultOfDataPage();
        r.setCode(resultCodeEnum.getCode());
        r.setMessage(resultCodeEnum.getMessage());
        return r;
    }

    public ResultOfDataPage success(Boolean success){
//		this.setSuccess(success);
        return this;
    }

    public ResultOfDataPage message(String message){
        this.setMessage(message);
        return this;
    }

    public ResultOfDataPage code(Integer code){
        this.setCode(code);
        return this;
    }

    public ResultOfDataPage data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public ResultOfDataPage data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

    public ResultOfDataPage setDataInfo(Integer page, Integer size, Long total){
        this.page = page;
        this.size = size;
        this.total = total;
        return this;
    }
}
