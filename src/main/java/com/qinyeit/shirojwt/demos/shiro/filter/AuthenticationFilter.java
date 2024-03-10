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
package com.qinyeit.shirojwt.demos.shiro.filter;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>ClassName: com.qinyeit.shirojwt.demos.shiro.filter.AuthenticationFilter
 * <p>Function: 自定义的认证过滤器
 * <p>date: 2024-03-10 19:14
 *
 * @author wuqing
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j
public class AuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {
    private void responseJsonResult(Map<String, ?> result, ServletResponse response) {
        if (response instanceof HttpServletResponse res) {
            res.setContentType("application/json;charset=UTF-8");
            res.setStatus(200);
            res.setCharacterEncoding("UTF-8");
            try {
                // 输出JSON 数据
                res.getWriter().write(JSON.toJSONString(result));
                res.getWriter().flush();
                res.getWriter().close();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                return executeLogin(request, response);
            } else {
                return true;
            }
        } else {
            Map<String, ?> result = Map.of("code", 401, "msg", "未登录或登录已过期");
            responseJsonResult(result, response);
            //saveRequestAndRedirectToLogin(request, response);
            return false;
        }
    }

    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) throws Exception {
        // issueSuccessRedirect(request, response);
        // 登录成功直接放行,让请求到达Controller
        return true;
    }

}