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

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>ClassName: com.qinyeit.shirojwt.demos.controller.HomeController
 * <p>Function: 主页
 * <p>date: 2024-03-08 16:13
 *
 * @author wuqing
 * @version 1.0
 * @since JDK 1.8
 */
@RestController
@Slf4j
public class HomeController {
    @GetMapping("/")
    public Map<String, String> home() {
        // 现在将 subject理解成当前用户
        Subject subject = SecurityUtils.getSubject();
        // 用户凭证，简单理解成用户名
        PrincipalCollection principalCollection = subject.getPrincipals();
        String              name                = principalCollection.getPrimaryPrincipal().toString();
        // 当前用户登录成功后，它的session中都存放了哪些key
        String sessionKeys = subject.getSession().getAttributeKeys().toString();
        // 返回结果
        return Map.of("name", name, "sessionKeys", sessionKeys);
    }
}