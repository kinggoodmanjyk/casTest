package com.king.castest.cas;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CASAutoConfig {

    @Value("${cas.server-url-prefix}")
    private String serverUrlPrefix;
    @Value("${cas.server-login-url}")
    private String serverLoginUrl;
    @Value("${cas.client-host-url}")
    private String clientHostUrl;
    @Value("${ignore-host-url}")
    private String ignoreHostUrl;

    public String getClientHostUrl() {
        return clientHostUrl;
    }

    public String getServerUrlPrefix() {
        return serverUrlPrefix;
    }

    /**
     * 单点登录登出
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean listenerSingleSignOutFilter() {
        ServletListenerRegistrationBean registration = new ServletListenerRegistrationBean();
        registration.setListener(new SingleSignOutHttpSessionListener());
        // 设定加载的顺序
        registration.setOrder(1);
        return registration;
    }

    /**
     * 单点登录登出
     * @return
     */
    @Bean
    public FilterRegistrationBean filterSingleSignOutFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SingleSignOutFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String,String> initParameters = new HashMap<>();
        initParameters.put("casServerUrlPrefix", serverUrlPrefix);
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(1);
        return registration;
    }

    /**
     *
     * @return 单点登录的认证工作
     */
    @Bean
    public FilterRegistrationBean filterAuthenticationRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AuthenticationFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String,String> initParameters = new HashMap<>();
        initParameters.put("casServerLoginUrl", serverLoginUrl);
        initParameters.put("serverName", clientHostUrl);
        //忽略的url，"|"分隔多个url
        initParameters.put("ignorePattern", ignoreHostUrl);
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(2);
        return registration;
    }

    /**
     * 该过滤器负责对Ticket的校验工作
     * @return
     */
    @Bean
    public FilterRegistrationBean filterCas20ProxyReceivingTicketValidationRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new Cas20ProxyReceivingTicketValidationFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String,String> initParameters = new HashMap<>();
        initParameters.put("casServerUrlPrefix", serverUrlPrefix);
        initParameters.put("serverName", clientHostUrl);
        //忽略的url，"|"分隔多个url
        initParameters.put("ignorePattern", ignoreHostUrl);
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(3);
        return registration;
    }


    /**
     * 该过滤器使得开发者可以通过org.jasig.cas.client.util.AssertionHolder来获取用户的登录名。
     * 比如AssertionHolder.getAssertion().getPrincipal().getName()。
     * @return
     */
    @Bean
    public FilterRegistrationBean filterAssertionThreadLocalFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AssertionThreadLocalFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        // 设定加载的顺序
        registration.setOrder(4);
        return registration;
    }

}
