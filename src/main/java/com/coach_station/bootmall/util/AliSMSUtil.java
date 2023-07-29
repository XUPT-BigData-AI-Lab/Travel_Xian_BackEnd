package com.coach_station.bootmall.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;

import java.util.HashMap;

/**
 * @Auther: yjw
 * @Date: 2022/03/12/14:36
 * @Description:
 */
public class AliSMSUtil {
    public static SendSmsResponse sendSMS(String phoneNumebr, Object code, String templateCode) throws Exception {
        System.out.println(phoneNumebr);
        System.out.println(code);
        System.out.println(templateCode);
        com.aliyun.dysmsapi20170525.Client client = AliSMSUtil.createClient("LTAI5t7WWh9FfcDj8ruXeGfo", "8vasYpXgHdQNYphFIXoeIGIAwzaPmg");
        HashMap<String, Object> params = new HashMap<>();
        params.put("code",code);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phoneNumebr)
                .setSignName("个人练手客运售票伪系统")
                .setTemplateCode(templateCode)
                .setTemplateParam(JSONObject.toJSONString(params));
        // 复制代码运行请自行打印 API 的返回值
        SendSmsResponse sendResp = client.sendSms(sendSmsRequest);
        System.out.println(sendResp.getBody());
		return sendResp;

    }


    //使用AK&SK初始化账号Client
    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }
}
