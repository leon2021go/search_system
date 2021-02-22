package com.leon.flying.schedule;

import com.leon.flying.common.redis.RedisClient;
import com.leon.flying.config.EmailConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * 定时任务服务
 */
@Service
public class ScheduledService {

    Logger logger = LoggerFactory.getLogger(ScheduledService.class);


    @Resource
    private JavaMailSenderImpl mailSender;


    @Resource
    private EmailConfig emailConfig;

//    @Resource
//    private RedisClient redisClient;

//    @Scheduled(cron = "0 0/1 * * * ? ")
//    public void schedule() {
//
//        String result = sendGet("https://www.hzfuyao.com/mengzhua/admin/health/check", null);
//        if (StringUtils.hasText(result) && result.equals("running ok")) {
//            String str = redisClient.get("health:check");
//            if (StringUtils.hasText(str) && str.equals("error")) {
//                SimpleMailMessage message = new SimpleMailMessage();
//                message.setSubject("萌爪服务运行恢复");
//                message.setText("萌爪应用访问已恢复正常！");
//
//                message.setTo(new String[]{"leon19940816@163.com","15700084332@163.com","897777604@qq.com"});
//                message.setFrom("leon19940816@163.com");
//                try {
//                    mailSender.send(message);
//                } catch (Exception e) {
//                    logger.error("mailSender.send error:{}", e.getMessage());
//                }
//                redisClient.del("health:check");
//            }
//            return;
//        }
//
//        String str = redisClient.get("health:check");
//        if (!StringUtils.hasText(str)) {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setSubject("萌爪服务运行告警");
//            message.setText("萌爪应用访问发生异常！！！请尽快排查！！");
//
//            message.setTo(new String[]{"leon19940816@163.com","15700084332@163.com","897777604@qq.com"});
//            message.setFrom("leon19940816@163.com");
//            try {
//                mailSender.send(message);
//            } catch (Exception e) {
//                logger.error("mailSender.send error:{}", e.getMessage());
//            }
//            redisClient.set("health:check", "error");
//        }
//    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            if(null == param){
                urlNameString = urlNameString + "?" + param;
            }
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }



}
