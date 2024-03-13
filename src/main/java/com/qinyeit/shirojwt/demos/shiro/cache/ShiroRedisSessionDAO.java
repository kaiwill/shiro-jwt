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
import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.MapCache;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>ClassName: com.qinyeit.shirojwt.demos.shiro.cache.ShiroRedisSessionDAO
 * <p>Function: 将Session数据保存到Redis中, Redis中存储的数据是Hash结构
 * <p>date: 2024-03-13 17:52
 *
 * @author wuqing
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j
public class ShiroRedisSessionDAO extends CachingSessionDAO {
    // redis Key
    private String        redisKey = "shiro:session";
    private RedisTemplate redisTemplate;

    public ShiroRedisSessionDAO(RedisTemplate redisTemplate, String redisKey) {

        RedisSerializer<String> stringSerializer = RedisSerializer.string();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        this.redisTemplate = redisTemplate;

        // 设置一个本地缓存，使用内存，减少对redis的访问
        setCacheManager(new AbstractCacheManager() {
            @Override
            protected Cache<Serializable, Session> createCache(String name) throws CacheException {
                return new MapCache<Serializable, Session>(name, new ConcurrentHashMap<Serializable, Session>());
            }
        });

        this.redisKey = redisKey;

    }

    @Override
    protected Serializable doCreate(Session session) {
        // 此时sessionID还没有生成，所以需要生成sessionID
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        //添加进redis
        this.hadd(sessionId.toString(), session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        return this.hget(sessionId.toString());
    }

    // 重写了父类的update方法，所以下面的 doUpdate 不会被调用
    @Override
    public void update(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            return;
        }
        Serializable sessionId = session.getId();
        this.hadd(sessionId.toString(), session);
    }

    @Override
    protected void doUpdate(Session session) {
        //log.debug("无需做任何操作,该方法永远不会被调用");
    }

    // 重写了父类的delete方法，所以下面的 doDelete 不会被调用
    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }
        this.hdelete(session.getId().toString());
    }

    @Override
    protected void doDelete(Session session) {
        //log.debug("无需做任何操作,该方法永远不会被调用");
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return null;
    }

    public void hadd(String sessionId, Session session) {
        log.info("=========================================================={}", sessionId);
        redisTemplate.boundHashOps(redisKey).put(sessionId, session);
    }

    public Session hget(String sessionId) {
        return (Session) redisTemplate.boundHashOps(redisKey).get(sessionId);
    }

    public void hdelete(String sessionId) {
        redisTemplate.boundHashOps(redisKey).delete(sessionId);
    }
}