package jyanoos.lol_bottom.service;

import jyanoos.lol_bottom.domain.member.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.info("로그인 인증 >>{}",requestURI);

        HttpSession session = request.getSession(false);//세션이 있으면 반환, 없으면 null

        if(session==null||session.getAttribute(SessionConst.LOGIN_MEMBER)==null){
            log.info("미인증 사용자 요청");
            response.sendRedirect("/member/login?redirectURL="+requestURI);
            return false;
        }

        //request.setAttribute(SessionConst.LOGIN_MEMBER,session.getAttribute(SessionConst.LOGIN_MEMBER));
        //log.info("앞{} 뒤 {}",SessionConst.LOGIN_MEMBER,session.getAttribute(SessionConst.LOGIN_MEMBER));

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse
            response, Object handler, ModelAndView modelAndView) throws Exception {
//
//        HttpSession session = request.getSession(false);//세션이 있으면 반환, 없으면 null
//
//
//        if(session!=null&&session.getAttribute(SessionConst.LOGIN_MEMBER)!=null){
//            log.info("앞{}           뒤{}",SessionConst.LOGIN_MEMBER,session.getAttribute(SessionConst.LOGIN_MEMBER));
//            modelAndView.addObject(SessionConst.LOGIN_MEMBER,session.getAttribute(SessionConst.LOGIN_MEMBER));
//        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String)request.getAttribute(LOG_ID);
        if (ex != null) {
            log.error("afterCompletion error!!", ex);
        }
    }
}
