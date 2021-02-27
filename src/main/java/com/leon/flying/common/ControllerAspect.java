package com.leon.flying.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static com.leon.flying.common.WXConstants.CURRENT_ADMIN_SESSION;

@Aspect
@Configuration
public class ControllerAspect {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;


    @Around("execution(* com.leon.flying.controller.admin.*.*(..))&&@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object adminControllerBeforeValidation(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        AdminPermission adminPermission = method.getAnnotation(AdminPermission.class);
        if(null == adminPermission){
            return joinPoint.proceed();
        }
        String email = (String)httpServletRequest.getSession().getAttribute(CURRENT_ADMIN_SESSION);
        if(null == email){
            if(adminPermission.produceType().equals("text/html")){
                httpServletResponse.sendRedirect("/admin/admin/loginpage");
                return null;
            }else{
                return MzRespose.error(-1, "管理员需要先登录");
            }
//            httpServletResponse.sendRedirect("/admin/admin/loginpage");
//            return null;
        }else {
            return joinPoint.proceed();
        }
    }
}
