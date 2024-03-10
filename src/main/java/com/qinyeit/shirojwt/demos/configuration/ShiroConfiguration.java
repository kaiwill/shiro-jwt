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

import com.qinyeit.shirojwt.demos.shiro.filter.AuthenticationFilter;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.TextConfigurationRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
public class ShiroConfiguration {
    @Bean
    public Realm realm() {
        TextConfigurationRealm realm = new TextConfigurationRealm();
        // 定义了admin 和 user 两个角色， admin 用户可以读写， user 用户只能读
        realm.setRoleDefinitions("""
                admin=read,write
                user=read
                """);
        // 定义了两个用户 joe.coder 和 jill.coder， joe.coder 用户可以读写， jill.coder 用户可以读
        realm.setUserDefinitions("""
                joe.coder=123,user
                jill.coder=456,admin
                """);
        // 开启了缓存,其实默认已经开启
        realm.setCachingEnabled(true);
        // 给realm设置名称
        realm.setName("userRealm");
        return realm;
    }

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