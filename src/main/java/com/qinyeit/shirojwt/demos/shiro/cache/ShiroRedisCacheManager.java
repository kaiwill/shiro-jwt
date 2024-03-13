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
package com.qinyeit.shirojwt.demos.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * <p>ClassName: com.qinyeit.shirojwt.demos.shiro.cache.ShiroRedisCacheManager
 * <p>Function:  Shiro的 redis cacheManager实现
 * <p>date: 2024-03-12 18:50
 *
 * @author wuqing
 * @version 1.0
 * @since JDK 1.8
 */
public class ShiroRedisCacheManager implements CacheManager {
    private RedisTemplate redisTemplate;

    public ShiroRedisCacheManager(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 这个方法由Shiro框加来调用，当需要用到缓存的时候，就会传入缓存的名字，比如我们可以为
    // 认证信息缓存，为“authenticationCache”；为授权信息缓存，为“authorizationCache”；为“sessionCache”等
    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        // 自动去RedisCahce中找具体实现
        return new ShiroRedisCache<K, V>(redisTemplate, name);
    }
}