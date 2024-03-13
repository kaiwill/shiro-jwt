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
package com.qinyeit.shirojwt.demos.shiro.realm;

import com.qinyeit.shirojwt.demos.shiro.entity.SystemAccount;
import com.qinyeit.shirojwt.demos.shiro.matcher.Sha256HashCredentialsMatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>ClassName: com.qinyeit.shirojwt.demos.shiro.realm.SystemAccountRealm
 * <p>Function: 系统用户认证授权
 * <p>date: 2024-03-11 18:46
 *
 * @author wuqing
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j
public class SystemAccountRealm extends AuthorizingRealm {
    // 模拟数据库中的账号信息 key为账号
    private Map<String, SystemAccount> systemAccountMap = new HashedMap();
    // 一个账号可以拥有多种角色
    private Map<String, Set<String>>   roles            = Map.of(
            "administrator", Set.of("admin"),//管理员
            "zhangsan", Set.of("normal") // 普通用户
    );
    // 角色权限
    private Map<String, Set<String>>   permissions      = Map.of(
            "admin", Set.of("*", "*:*"), //所有权限
            "normal", Set.of("employee:write", "employee:read") //执行查看
    );

    // 构造方法中构建出账号信息
    public SystemAccountRealm() {
        // 指定密码匹配器
        super(new Sha256HashCredentialsMatcher());
        systemAccountMap.put("administrator", SystemAccount.builder()
                .account("administrator")
                .pwdEncrypt("0b188436fd5c434e3b8ed05cfe7c107250c1ff0ac034fad089db0f017ac3cacb")
                .salt("55ae2b2c63ddd6d4763e0c57bda9078e")
                .build());
        systemAccountMap.put("zhangsan", SystemAccount.builder()
                .account("zhangsan")
                .pwdEncrypt("3bff14c4279f01892165b96afed9b40ec7f14a9de55d9564c088bad3e04d6411")
                .salt("cbce2d1aad0867f8317e7ebeb3427999")
                .build());

    }

    // 当前Realm 只支持 UsernamePasswordToken类型的Token
    public boolean supports(AuthenticationToken token) {
        return token != null && UsernamePasswordToken.class.isAssignableFrom(token.getClass());
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (getCacheManager() == null) {
            log.info("================>cacheManager为空");
        } else {
            log.info("================cacheManager:{}", getCacheManager().getClass().getName());
        }
        // 1.从传过来的认证Token信息中，获得账号
        String account = token.getPrincipal().toString();

        // 2.通过用户名到数据库中获取整个用户对象
        SystemAccount systemAccount = systemAccountMap.get(account);
        if (systemAccount == null) {
            throw new UnknownAccountException();
        }
        // 3. 创建认证信息，即用户正确的用户名和密码。
        // 四个参数：
        // 第一个参数为主体，第二个参数为凭证，第三个参数为Realm的名称
        // 因为上面将凭证信息和主体身份信息都保存在 SystemAccount中了，所以这里直接将 SystemAccount对象作为主体信息即可

        // 第二个参数表示凭证，匹配器中会从 SystemAccount中获取盐值，密码登凭证信息，所以这里直接传null。

        // 第三个参数，表示盐值，这里使用了自定义的SaltSimpleByteSource，之所以在这里new了一个自定义的SaltSimpleByteSource，
        // 是因为开启redis缓存的情况下，序列化会报错

        // 第四个参数表示 Realm的名称
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                systemAccount,
                null,
                new SaltSimpleByteSource(systemAccount.getSalt()),
                getName()
        );
        // authenticationInfo.setCredentialsSalt(null);
        return authenticationInfo;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 1. 获取用户信息
        SystemAccount account = (SystemAccount) principals.getPrimaryPrincipal();
        // 2. 获取用户角色
        Set<String> accountRoles = roles.get(account.getAccount());
        // 3. 获取角色拥有的权限
        Set<String> accountPermissions = new HashSet<>();
        accountRoles.forEach(role -> {
            accountPermissions.addAll(permissions.get(role));
        });
        // 4. 授权信息
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //指定角色
        authorizationInfo.setRoles(accountRoles);
        // 权限字符串
        authorizationInfo.setStringPermissions(accountPermissions);
        return authorizationInfo;
    }


}