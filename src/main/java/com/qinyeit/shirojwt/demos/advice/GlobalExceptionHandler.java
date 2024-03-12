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
package com.qinyeit.shirojwt.demos.advice;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>ClassName: com.qinyeit.shirojwt.demos.advice.GlobalExceptionHandler
 * <p>Function: 全局异常处理
 * <p>date: 2024-03-12 15:38
 *
 * @author wuqing
 * @version 1.0
 * @since JDK 1.8
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UnauthorizedException.class)
    public Map<String, Object> handleAccessDeniedException(UnauthorizedException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        return Map.of("requestPath", requestUri,
                "message", "没有权限访问该资源，请联系管理员授权"
        );
    }
}