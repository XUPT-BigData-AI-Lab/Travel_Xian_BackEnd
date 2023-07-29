package com.coach_station.bootmall.enumAndConst;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {

	SUCCESS(true, 10000,"success"),


	WITHOUT_PERMISSION(false, 20001, "无权限"),
	WITHOUT_USERID(false, 20002, "Session无UserId"),
	UNKNOWN_REASON(false, 30001, "未知错误"),

	LOGIN_INFO_ERROR(false, 30002, "用户名或密码错误"),
	LOGIN_CAPATA_ERROR(false, 30003, "图形验证码错误"),

	USERCENTER_GETPROFILE_ERROR(false, 40001, "获取个人基本资料失败，请稍后重试"),
	USERCENTER_UPDATEPROFILE_ERROR(false, 40002, "修改个人基本资料失败，请稍后重试"),
	USERCENTER_MODIFYPASSWORD_ERROR(false, 40003, "修改密码失败，请稍后重试"),
	USERCENTER_GETPASSGERS_ERROR(false, 40004, "获取常用乘车人资料失败，请稍后重试"),
	USERCENTER_ADDPASSGERS_ERROR(false, 40005, "添加乘车人资料失败，请稍后重试"),
	USERCENTER_CARDTYPE_ERROR(false, 40006, "证件类型与证件号码不匹配，请重新输入"),
	USERCENTER_CARDNUMBERREPEAT_ERROR(false, 40008, "此证件号码已存在您的常用乘车人中，请重新输入"),
	USERCENTER_MODIFYPASSGER_ERROR(false, 40009, "修改乘车人资料失败，请稍后重试"),
	USERCENTER_DELETEPASSGER_ERROR(false, 40010, "删除乘车人资料失败，请稍后重试"),
	USERCENTER_GETCONTACTPERSON_ERROR(false, 40011, "获取常用联系人资料失败，请稍后重试"),
	USERCENTER_ADDCONTACTPERSON_ERROR(false, 40012, "添加常用联系人资料失败，请稍后重试"),
	USERCENTER_PHONENUMBERREPEAT_ERROR(false, 40013, "此电话号码已存在您的常用联系人中，请重新输入"),
	USERCENTER_MODIFYCONTACTPERSON_ERROR(false, 40014, "此电话号码已存在您的常用联系人中，请重新输入"),
	USERCENTER_DELETECONTACTPERSON_ERROR(false, 40015, "删除常用联系人资料失败，请稍后重试"),
	USERCENTER_PASSGERCARDNUMBER_REPEAT_ERROR(false, 40016, "此证件号码已存在您的常用乘车人中，请重新输入"),

	PERMISSION_REGISTER_USEREXIT_ERROR(false, 40016, "此手机号已注册，请勿重复注册"),

	ORDER_GETORDERINFO_ERROR(false, 50001, "暂无匹配订单信息"),
	ORDER_GETORDERDETAILINFO_ERROR(false, 50002, "获取订单详情失败，请稍后重试"),
	ORDER_CANCELORDER_ERROR(false, 50003, "取消订单失败，请稍后重试"),
	ORDER_BOOKORDER_ERROR(false, 50004, "提交订单失败，请稍后重试"),
	ORDER_BOOKORDER_BOOKINFONOTENOUGH_ERROR(false, 50005, "提交订单失败，请保证信息齐全"),
	ORDER_BOOKORDER_HAVEARRANGEORDER_ERROR(false, 50006, "提交订单失败，您还有未支付订单"),
	ORDER_BOOKORDER_TICKETNOTENOUGH_ERROR(false, 50007, "提交订单失败，余票不足"),
	ORDER_PAYORDERCALLBACK_TICKETNOTENOUGH_ERROR(false, 50008, "回调失败"),
	ORDER_REFUNDORDER_ERROR(false, 50009, "订单退款失败，请稍后重试"),



	QUERY_GETFAMLIARSTATIONS_ERROR(false, 60001, "获取常用客运站信息失败，请稍后重试"),
	QUERY_GETALLSTATIONS_ERROR(false, 60002, "获取所有客运站信息失败，请稍后重试"),
	QUERY_GETFAMLIARSHUTTLES_ERROR(false, 60003, "获取所有客运站信息失败，请稍后重试"),
	QUERY_GETALLREGINONS_ERROR(false, 60004, "获取所有地区信息失败，请稍后重试"),
	QUERY_GETSHUTTLELIST_ERROR(false, 60005, "获取所有地区信息失败，请稍后重试"),
	QUERY_PHONENUMBERNOTEXIST_ERROR(false, 60006, "此手机号不存在，请检查后重试"),
	QUERY_NOTEXISTRIDECODE_ERROR(false, 60007, "此手机号无对应车票"),
	QUERY_GETARTCLEINFO_ERROR(false, 60008, "获取公告信息失败，请稍后重试"),


	MANAGE_PARAMNULL_ERROR(false, 70001, "请求部分参数为空"),
	MANAGE_PARAM_ERROR(false, 70002, "请求参数不符合规范"),
	MANAGE_WITHOUTLINE_ERROR(false, 70003, "暂无此线路信息，请更换始发地和目的地"),
	MANAGE_WITHOUTSHUTTLE_ERROR(false, 70004, "暂无符合条件的班次信息，请更换始发地和目的地"),
	MANAGE_SAVESHUTTLE_ERROR(false, 70005, "存储班次出错，请稍后重试"),
	MANAGE_DELETESHUTTLE_ERROR(false, 70006, "删除班次出错，请稍后重试"),
	MANAGE_WITHOUTPASSENGER_ERROR(false, 70007, "无此乘客信息，请校验信息是否正确"),
	MANAGE_MODIFYORDERINFO_ERROR(false, 70008, "修改订单出错，请稍后重试"),
	MANAGE_GETORDERLIST_ERROR(false, 70009, "获取订单出错，请稍后重试"),

//	LOGIN_PHONE_ERRROR(false, 20002, "手机号码不能为空"),
//	ACCOUNT_PHONE_ERRROR(false, 20002, "账号信息不能为空"),
	LOGIN_PHONE_PATTARN_ERRROR(false, 30003, "手机号码格式不正确"),
//	VALIDATION_CODE_ERROR(false, 20004, "验证码不正确"),
//	LOGIN_CODE_ERROR(false, 20005, "短信验证码不能为空"),
	LOGIN_CODE_FAIL_ERROR(false, 20007, "短信验证码失效，请重新发送"),
	LOGIN_CODE_INPUT_ERROR(false, 20008, "输入的短信码有误"),
	SEND_SMS_ERROR(false, 20009, "短信发送异常，请稍后重试"),
//	PHONE_ERROR_MSG(false, 20009, "该手机号未绑定账户"),
//	USER_FORBIDDEN(false, 20010, "该用户已被禁用，请联系平台客服"),
//	LOGIN_PWD_ERROR(false, 200011, "密码不允许为空"),
//	LOGIN_PWD_INPUT_ERROR(false, 200012, "密码输入有误"),
//	LOGIN_PWD_NO_INPUT_ERROR(false, 200013, "检测到没有完善密码信息"),


//	BAD_SQL_GRAMMAR(false, 21001, "sql语法错误"),
//	JSON_PARSE_ERROR(false, 21002, "json解析异常"),
	PARAM_ERROR(false, 21006, "参数不正确");
//	USER_PWD_ERROR(false, 21003, "尚未找到对应的用户信息");


	private Boolean success;
	private Integer code;
	private String message;

	private ResultCodeEnum(Boolean success, Integer code, String message) {
		this.success = success;
		this.code = code;
		this.message = message;
	}
}