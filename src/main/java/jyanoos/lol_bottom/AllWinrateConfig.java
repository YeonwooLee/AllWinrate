package jyanoos.lol_bottom;

import jyanoos.lol_bottom.service.LoginInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan
//awr관련만 하는거 아님!
public class AllWinrateConfig implements WebMvcConfigurer {
    //AllWinrateMapper.interface에 현재버전정보 기입 필요
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error","/member/login","/member/join","/member/join-form");
    }
}
