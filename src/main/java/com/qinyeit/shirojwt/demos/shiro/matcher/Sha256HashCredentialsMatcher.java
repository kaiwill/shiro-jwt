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
package com.qinyeit.shirojwt.demos.shiro.matcher;

import com.qinyeit.shirojwt.demos.shiro.entity.SystemAccount;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.lang.codec.CodecSupport;

/**
 * <p>ClassName: com.qinyeit.shirojwt.demos.shiro.matcher.Sha256HashCredentialsMatcher
 * <p>Function: 自定义的匹配器
 * <p>date: 2024-03-11 19:08
 *
 * @author wuqing
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j
public class Sha256HashCredentialsMatcher extends CodecSupport implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // 取出真实身份信息
        Object primaryPrincipal = info.getPrincipals().getPrimaryPrincipal();
        // 如果身份信息是 SystemAccount 对象
        // 此时要注意，Realm 中要将 SystemAccount 对象放入到 AuthenticationInfo 中
        if (primaryPrincipal instanceof SystemAccount account) {
            String accountPwd = account.getPwdEncrypt();
            // 获取盐值
            String accountSalt = account.getSalt();
            // 获取token中的密码
            String tokenPwd = new String(((UsernamePasswordToken) token).getPassword());
            //进行散列
            String tokenPwdSha = new Sha256Hash(tokenPwd, accountSalt, 2).toHex();
            return accountPwd.equals(tokenPwdSha);
        }
        return false;
    }
}