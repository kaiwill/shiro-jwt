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

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;

/**
 * <p>ClassName: com.qinyeit.shirojwt.demos.shiro.cache.ShiroRedisCache
 * <p>Function: Shiro的 redis cache实现
 * <p>date: 2024-03-12 18:50
 *
 * @author wuqing
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j
public class ShiroRedisCache<K, V> implements Cache<K, V> {
    private RedisTemplate redisTemplate;
    private String        cacheName;

    public ShiroRedisCache(RedisTemplate redisTemplate, String cacheName) {
        this.redisTemplate = redisTemplate;
        this.cacheName = cacheName;
    }

    @Override
    public V get(K key) throws CacheException {
        // 取hash中的值
        return (V) redisTemplate.opsForHash().get(this.cacheName, key.toString());
    }

    @Override
    public V put(K key, V value) throws CacheException {
        log.info("cacheName:{}, ShiroRedisCache===>put==>{}  ==={} ====value:{}", cacheName, key.toString(), key.getClass().getName(), value);
        redisTemplate.opsForHash().put(this.cacheName, key.toString(), value);
        return value;
    }

    @Override
    public V remove(K key) throws CacheException {
        return (V) redisTemplate.opsForHash().delete(this.cacheName, key.toString());
    }

    @Override
    public void clear() throws CacheException {
        redisTemplate.delete(this.cacheName);
    }

    @Override
    public int size() {
        return redisTemplate.opsForHash().size(this.cacheName).intValue();
    }

    @Override
    public Set<K> keys() {
        return redisTemplate.opsForHash().keys(this.cacheName);
    }

    @Override
    public Collection<V> values() {
        return redisTemplate.opsForHash().values(this.cacheName);
    }
}