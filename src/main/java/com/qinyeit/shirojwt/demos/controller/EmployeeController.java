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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>ClassName: com.qinyeit.shirojwt.demos.controller.EmployeeController
 * <p>Function: 员工管理
 * <p>date: 2024-03-12 14:31
 *
 * @author wuqing
 * @version 1.0
 * @since JDK 1.8
 */
@RestController
@Slf4j
public class EmployeeController {
    @PostMapping("/employees")
    // 需要employee:write 权限
    @RequiresPermissions("employee:write")
    public void addEmployee() {
        log.info("添加员工....");
    }

    // 需要employee:read 权限
    @GetMapping("/employees")
    @RequiresPermissions("employee:read")
    public void index() {
        log.info("员工管理....");
    }

    // 需要employee:delete 权限
    @DeleteMapping("/employees")
    @RequiresPermissions("employee:delete")
    public void destroy() {
        log.info("销毁....");
    }
}