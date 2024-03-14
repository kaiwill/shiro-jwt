/*******************************************************************************
 * Copyright (c) 2010, 2024 西安秦晔信息科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/
package com.qinyeit.shirojwt.demos.configuration;

import com.qinyeit.shirojwt.demos.shiro.cache.ShiroRedisCacheManager;
import com.qinyeit.shirojwt.demos.shiro.cache.ShiroRedisSessionDAO;
import com.qinyeit.shirojwt.demos.shiro.filter.AuthenticationFilter;
import com.qinyeit.shirojwt.demos.shiro.realm.SystemAccountRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * <p>ClassName: com.qinyeit.shirojwt.demos.configuration.ShiroConfiguration
 * <p>Function: Shiro bean配置
 * <p>date: 2024-03-08 14:11
 *
 * @author wuqing
 * @version 1.0
 * @since JDK 1.8
 */
@Configuration
@Slf4j
public class ShiroConfiguration {
    @Bean
    public Realm realm() {
        SystemAccountRealm realm = new SystemAccountRealm();
        // 开启全局缓存
        realm.setCachingEnabled(true);
        // 打开认证缓存
        realm.setAuthenticationCachingEnabled(true);
        // 认证缓存的名字，不设置也可以，默认由
        realm.setAuthenticationCacheName("shiro:authentication:cache");

        // 打开授权缓存
        realm.setAuthorizationCachingEnabled(true);
        // 授权缓存的名字， 不设置也可以，默认由
        realm.setAuthorizationCacheName("shiro:authorization:cache");
        return realm;
    }

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisSerializer<String> stringSerializer = RedisSerializer.string();
        // 设置key的序列化器
        redisTemplate.setKeySerializer(stringSerializer);
        // 设置 Hash 结构中 key 的序列化器
        redisTemplate.setHashKeySerializer(stringSerializer);
        return new ShiroRedisCacheManager(redisTemplate);
    }

    // 配置SessionDAO
    @Bean
    public SessionDAO shiroRedisSessionDAO(RedisTemplate redisTemplate, CacheManager cacheManager) {
        ShiroRedisSessionDAO sessionDAO = new ShiroRedisSessionDAO(redisTemplate, "shiro:session");
        // 活跃session缓存的名字
        sessionDAO.setActiveSessionsCacheName("shiro:active:session");
        sessionDAO.setCacheManager(cacheManager);
        return sessionDAO;
    }

    // sessionManager配置
    @Bean
    public SessionManager sessionManager(
            SessionFactory sessionFactory,
            @Qualifier("sessionCookieTemplate") Cookie cookieTemplate,
            SessionDAO sessionDAO) {
        DefaultWebSessionManager webSessionManager = new DefaultWebSessionManager();
        // 开启Cookie，即由Cookie来传递 sessionID保持会话
        webSessionManager.setSessionIdCookieEnabled(true);
        // 开启URL重写，即可以从URL中获取sessionID来保持会话
        webSessionManager.setSessionIdUrlRewritingEnabled(true);
        // 自动配置中已经配置了cookieTemplate 直接注入进来,具体看 ShiroWebAutoConfiguration 类中bean的定义
        webSessionManager.setSessionIdCookie(cookieTemplate);
        // 自动配置中已经配置了sessionFactory 直接注入进来
        webSessionManager.setSessionFactory(sessionFactory);
        // 使用自定义的ShiroRedisSessionDAO
        webSessionManager.setSessionDAO(sessionDAO);
        // 清理无效的session
        webSessionManager.setDeleteInvalidSessions(true);
        // 开启session定时检查
        webSessionManager.setSessionValidationSchedulerEnabled(true);
        webSessionManager.setSessionValidationScheduler(new ExecutorServiceSessionValidationScheduler());
        return webSessionManager;
    }

//    @Bean
//    public DefaultWebSecurityManager securityManager(SessionManager sessionManager,
//                                                     CacheManager cacheManager,
//                                                     Realm realm) {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        // 设置自定义的Realm实现
//        securityManager.setRealm(realm);
//        log.info("sessionManager=============>{}  realm====>{}", securityManager.getSessionManager(), realm.getClass().getName());
//        // 设置SessionManager
//        securityManager.setSessionManager(sessionManager);
//        securityManager.setCacheManager(cacheManager);
//        // 可能还会有其他相关配置，比如CacheManager、RememberMeManager等
//        return securityManager;
//    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
//        chainDefinition.addPathDefinition("/test/**", "anon");
//        chainDefinition.addPathDefinition("/login", "authc");
//        // need to accept POSTs from the login form
        chainDefinition.addPathDefinition("/**", "authc");
        return chainDefinition;
    }

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> customShiroFilterRegistration(ShiroFilterFactoryBean shiroFilterFactoryBean) {
        FilterRegistrationBean<AuthenticationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new AuthenticationFilter());
        AuthenticationFilter authcFilter = new AuthenticationFilter();
        // 设置登录请求的URL, application.properties 中的 loginUrl 配置项就可以去掉了
        authcFilter.setLoginUrl("/login");
        // 可以设置过滤器名称、顺序等属性
        //使用这个名称,覆盖掉内置的authc过滤器
        registration.setName("authc");
        // 设置过滤器执行顺序
        registration.setOrder(Integer.MAX_VALUE - 1);

        shiroFilterFactoryBean.getFilters().put("authc", authcFilter);
        return registration;
    }

}