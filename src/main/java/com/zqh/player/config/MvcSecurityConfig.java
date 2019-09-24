package com.zqh.player.config;

import com.alibaba.fastjson.JSONObject;
import com.zqh.player.tools.common.page.R;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @discription:
 * @date: 2019/09/22 17:23
 */
@EnableWebSecurity
public class MvcSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 定制请求授权的规则
        http.authorizeRequests().antMatchers("/**").hasRole("VIP1")
//                .antMatchers("/static/*").hasRole("VIP1")
//                .antMatchers("index.html").hasRole("VIP1")
                .and().logout().addLogoutHandler(logoutHandler()).permitAll()
                .invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessHandler(logoutSuccessHandler())

                .and().sessionManagement().maximumSessions(10).expiredUrl("/login")
        ;
        http.formLogin();
        http.csrf().disable();
    }

    // 自定义认证规则
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("").password(new BCryptPasswordEncoder().encode("")).roles("VIP1")
        ;
    }

    @Bean
    public LogoutHandler logoutHandler() {
        return (httpServletRequest, httpServletResponse, authentication) -> {
            System.out.println("\n\n退出登录\n\n");
        };
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request,response,authentication) -> {
            System.out.println("\n\n退出登录成功\n\n");
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSONObject.toJSONString(R.ok()));
            out.flush();
            out.close();
        };
    }
}
