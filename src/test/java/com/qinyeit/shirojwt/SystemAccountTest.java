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
package com.qinyeit.shirojwt;

import com.qinyeit.shirojwt.demos.shiro.entity.SystemAccount;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>ClassName: com.qinyeit.shirojwt.SystemAccountTest
 * <p>Function: TODO 功能描述
 * <p>date: 2024-03-11 18:10
 *
 * @author wuqing
 * @version 1.0
 * @since JDK 1.8
 */
@SpringBootTest
@Slf4j
public class SystemAccountTest {
    /**
     * 创建两个
     */
    @Test
    public void createSystemAccount() {
        // 创建两个系统账号，调用Shiro提供的散列算法计算出加密密码
        String account = "administrator";
        String pwd     = "admin";// 明文密码
        // 使用Shiro 提供的随机数生成器生成盐值，默认生成16字节，128位
        RandomNumberGenerator saltGenerator = new SecureRandomNumberGenerator();
        // 使用16进制字符串表示
        String salt = saltGenerator.nextBytes().toHex();

        // 使用 SHA-256 散列算法， 对 密码明文加密，加盐 salt，迭代次数为 2 次
        SimpleHash shiroHash = new SimpleHash("SHA-256", pwd, salt, 2);
        // 加密后的密码, 也可以通过 new Sha256Hash(pwd, salt, 2) 来实现
        String pwdEncrypt = shiroHash.toHex();

        SystemAccount admin = SystemAccount.builder()
                .account(account)
                .pwdEncrypt(pwdEncrypt)
                .salt(salt)
                .build();
        // SystemAccount(
        // account=administrator,
        // pwdEncrypt=0b188436fd5c434e3b8ed05cfe7c107250c1ff0ac034fad089db0f017ac3cacb,
        // salt=55ae2b2c63ddd6d4763e0c57bda9078e
        // )
        log.info("admin:{}", admin.toString());
        ///////////////////////////////////////////////////////////////////
        account = "zhangsan";
        pwd = "123456";// 明文密码
        // 使用16进制字符串表示
        salt = saltGenerator.nextBytes().toHex();
        // 使用 SHA-256 散列算法， 对 密码明文加密，加盐 salt，迭代次数为 2 次
        pwdEncrypt = new Sha256Hash(pwd, salt, 2).toHex();

        SystemAccount zhangsan = SystemAccount.builder()
                .account(account)
                .pwdEncrypt(pwdEncrypt)
                .salt(salt)
                .build();
        //zhangsan:SystemAccount(
        // account=zhangsan,
        // pwdEncrypt=3bff14c4279f01892165b96afed9b40ec7f14a9de55d9564c088bad3e04d6411,
        // salt=cbce2d1aad0867f8317e7ebeb3427999
        // )
        log.info("zhangsan:{}", zhangsan.toString());

    }
}