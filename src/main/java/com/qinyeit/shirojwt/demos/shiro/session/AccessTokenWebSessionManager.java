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
package com.qinyeit.shirojwt.demos.shiro.session;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * <p>ClassName: com.qinyeit.shirojwt.demos.shiro.session.AccessTokenWebSessionManager
 * <p>Function: 从请求头 X-Access-Token 获取SessionID
 * <p>date: 2024-03-16 12:02
 *
 * @author wuqing
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j
public class AccessTokenWebSessionManager extends DefaultWebSessionManager {
    public AccessTokenWebSessionManager() {
        // 禁用Cookie
        super.setSessionIdCookieEnabled(false);
        // 禁用URL重写
        super.setSessionIdUrlRewritingEnabled(false);
        // 自动配置中已经配置了cookieTemplate 直接注入进来,具体看 ShiroWebAutoConfiguration 类中bean的定义
        // super.setSessionIdCookie(cookieTemplate);
        // 清理无效的session
        super.setDeleteInvalidSessions(true);
        // 开启session定时检查
        super.setSessionValidationSchedulerEnabled(true);
        super.setSessionValidationScheduler(new ExecutorServiceSessionValidationScheduler());
    }

    /**
     * 从请求头 X-Access-Token 获取SessionID
     *
     * @param request
     * @param response
     * @return
     */
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        log.info("AccessTokenWebSessionManager:getSessionId===================>");
        String sessionId = WebUtils.toHttp(request).getHeader("X-Access-Token");
        if (sessionId != null) {
            return sessionId;
        }
        return super.getSessionId(request, response);
    }
}