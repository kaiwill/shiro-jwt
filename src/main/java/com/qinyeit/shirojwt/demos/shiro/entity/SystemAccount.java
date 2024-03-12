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
package com.qinyeit.shirojwt.demos.shiro.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>ClassName: com.qinyeit.shirojwt.demos.shiro.entity.SystemAccount
 * <p>Function: 系统用户
 * <p>date: 2024-03-11 18:04
 *
 * @author wuqing
 * @version 1.0
 * @since JDK 1.8
 */
@Data
@ToString
@Builder
public class SystemAccount implements Serializable {
    private String account;//账号
    private String pwdEncrypt;//密码密文
    private String salt;// 对密码加密的时候使用的salt值
}