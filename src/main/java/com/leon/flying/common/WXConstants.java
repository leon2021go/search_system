package com.leon.flying.common;

/**
 * 微信登录支付相关信息常量
 * @author leon
 */
public class WXConstants {

    public static final String CURRENT_ADMIN_SESSION = "currentAdminSession";

    public static final String CONTROLLER_NAME = "CONTROLLER_NAME";
    public static final String ACTION_NAME = "ACTION_NAME";
    /**
     * 微信支付返回信息码
     */
    public static final String WX_PAY_RETURN_CODE = "return_code";
    public static final String WX_PAY_RETURN_CODE_SUCCESS = "SUCCESS";
    public static final String WX_PAY_RETURN_CODE_FAIL = "FAIL";
    /**
     * 微信支付统一下单 参数 key
     */
    public static final String WX_PAY_APP_ID = "appid";
    public static final String WX_PAY_FIELD_SIGN = "sign";
    public static final String WX_PAY_OPEN_ID = "openid";
    public static final String WX_PAY_NONCE_STR = "nonce_str";
    public static final String WX_PAY_MCH_ID = "mch_id";
    public static final String WX_PAY_OUT_TRADE_NO = "out_trade_no";
    public static final String WX_PAY_BODY = "body";
    public static final String WX_PAY_TOTAL_FEE = "total_fee";
    public static final String WX_PAY_NOTIFY_URL = "notify_url";
    public static final String WX_PAY_SP_BILL_CREATE_IP = "spbill_create_ip";
    public static final String WX_PAY_TRADE_TYPE = "trade_type";
    public static final String WX_PAY_SIGN_TYPE = "sign_type";

    /**
     * 微信小程序(萌爪)
     */
    public static final String MZ_APP_ID = "wx2e838087ccf0e611";
    public static final String MZ_APP_SECRET = "b96b7f13d037a28d3d87ea796cf39546";

    /**
     * 微信登录
     */
    public static final String WX_AUTH_TOKEN_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    public static final String WX_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    public static final String WX_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + MZ_APP_ID + "&secret=" + MZ_APP_SECRET;
    public static final String WX_IMAGE_CHECK_URL = "https://api.weixin.qq.com/wxa/img_sec_check?access_token=";
    public static final String WX_TEXT_CHECK_URL = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=";

    public enum SignType {
        MD5,
        HMACSHA256
    }
}
