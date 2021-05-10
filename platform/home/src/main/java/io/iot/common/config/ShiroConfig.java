/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 
 */

package io.iot.common.config;

import io.renren.common.filter.LoginFilter;
import io.renren.modules.sys.shiro.UserRealm;
import io.renren.modules.sys.shiro.WechatRealm;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shiro的配置文件
 *
 * @author Mark sunlightcs@gmail.com
 */
@Configuration
public class ShiroConfig {

    /**
     * 单机环境，session交给shiro管理
     */
    @Bean
    @ConditionalOnProperty(prefix = "renren", name = "cluster", havingValue = "false")
    public DefaultWebSessionManager sessionManager(@Value("${renren.globalSessionTimeout:3600}") long globalSessionTimeout){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(false);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setSessionValidationInterval(globalSessionTimeout * 1000);
        sessionManager.setGlobalSessionTimeout(globalSessionTimeout * 1000);

        return sessionManager;
    }

    /**
     * 集群环境，session交给spring-session管理
     */
    @Bean
    @ConditionalOnProperty(prefix = "renren", name = "cluster", havingValue = "true")
    public ServletContainerSessionManager servletContainerSessionManager() {
        return new ServletContainerSessionManager();
    }

    @Bean("securityManager")
    public SecurityManager securityManager(UserRealm userRealm,WechatRealm wechatRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(new EhCacheManager());
        List<Realm> realms = new ArrayList<>();
        realms.add(userRealm);
        realms.add(wechatRealm);
//        securityManager.setRealm(userRealm);
        securityManager.setRealms(realms);
        securityManager.setSessionManager(sessionManager);
        securityManager.setRememberMeManager(null);
        return securityManager;
    }


    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setLoginUrl("/login.html");
        shiroFilter.setUnauthorizedUrl("/");

        
        
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/swagger-resources/**", "anon");

        filterMap.put("/statics/**", "anon");
        filterMap.put("/login.html", "anon");
        filterMap.put("/sys/login", "anon");
        filterMap.put("/sys/loginStatic", "anon");
        filterMap.put("/sessionout", "anon");
		filterMap.put("/visitorcenter/visitor/fileUpload", "anon");
        filterMap.put("/sys/appLogin", "anon");
        filterMap.put("/sys/appLoginByPassword", "anon");
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/captcha.jpg", "anon");
        filterMap.put("/mobiles/login.html", "anon");
        filterMap.put("/miniProgram/miniLogin", "anon");
        filterMap.put("/download/**", "anon");
        filterMap.put("/**", "authc");
        filterMap.put("/**", "loginFilter");
        filterMap.put("/statics/plugins/**", "anon");

        shiroFilter.setFilterChainDefinitionMap(filterMap);
        
        //session过滤
        Map<String, Filter> filtersMap = new LinkedHashMap<>();
        filtersMap.put("loginFilter", new LoginFilter());
        shiroFilter.setFilters(filtersMap);

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
