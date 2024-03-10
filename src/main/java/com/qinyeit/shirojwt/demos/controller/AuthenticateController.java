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
package com.qinyeit.shirojwt.demos.controller;

import com.qinyeit.shirojwt.demos.shiro.filter.AuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>ClassName: com.qinyeit.shirojwt.demos.controller.AuthenticateController
 * <p>Function: 登录
 * <p>date: 2024-03-08 15:26
 *
 * @author wuqing
 * @version 1.0
 * @since JDK 1.8
 */
@RestController
@Slf4j
public class AuthenticateController {

    @PostMapping("/login")
    public Map<String, String> login(HttpServletRequest req) {
        Subject             subject = SecurityUtils.getSubject();
        Map<String, String> map     = new HashMap<>();
        if (subject.isAuthenticated()) {
            // 主体的标识，可以有多个，但是需要具备唯一性。比如：用户名，手机号，邮箱等。
            PrincipalCollection principalCollection = subject.getPrincipals();
            log.info("是否认证：{}，当前登录用户主体信息:{}", subject.isAuthenticated(), principalCollection.getPrimaryPrincipal());
            map.put("name", principalCollection.getPrimaryPrincipal().toString());
            map.put("message", "登录成功");
        } else {
            String exceptionClassName = (String) req.getAttribute(AuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
            log.error("signinError:{}", exceptionClassName);
            String error = null;
            if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
                error = "用户名/密码错误";
            } else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
                error = "用户名/密码错误";
            } else if (ExcessiveAttemptsException.class.getName().equals(exceptionClassName)) {
                error = "登录次数过多";
            } else if (exceptionClassName != null) {
                error = "其他错误：" + exceptionClassName;
            }
            map.put("message", error);
        }
        return map;
    }

    @PostMapping("/logout")
    public Map<String, String> logout() {

        Subject subject = SecurityUtils.getSubject();
        // 主体的标识，可以有多个，但是需要具备唯一性。比如：用户名，手机号，邮箱等。
        PrincipalCollection principalCollection = subject.getPrincipals();
        String              name                = principalCollection.getPrimaryPrincipal().toString();
        // 退出登录
        subject.logout();
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("name", name);
        resultMap.put("message", "退出登录成功");
        return resultMap;
    }
}