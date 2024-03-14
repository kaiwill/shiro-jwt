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
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Collection;

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
public class ShiroRedisSessionDAO extends EnterpriseCacheSessionDAO {
    //redis中session名称前缀
    private String redisKey = "shiro:session";

    private RedisTemplate<Object, Object> redisTemplate;

    public ShiroRedisSessionDAO(RedisTemplate<Object, Object> redisTemplate, String redisKey) {
        this.redisTemplate = redisTemplate;
        this.redisKey = redisKey;
    }

    // 创建session，保存到数据库
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.doCreate(session);
        log.debug("doCreate:" + session.getId());
        redisTemplate.opsForHash().put(redisKey, session.getId(), session);
        return sessionId;
    }

    // 获取session
    @Override
    protected Session doReadSession(Serializable sessionId) {
        log.debug("doReadSession:" + sessionId);
        // 先从缓存中获取session，如果没有再去数据库中获取
        Session session = super.doReadSession(sessionId);
        if (session == null) {
            session = (Session) redisTemplate.opsForHash().get(redisKey, sessionId);
        }
        return session;
    }

    // 更新session的最后一次访问时间
    @Override
    protected void doUpdate(Session session) {
        super.doUpdate(session);
        log.debug("doUpdate:" + session.getId());
        HashOperations<Object, Object, Object> hashOp = redisTemplate.opsForHash();
        if (!hashOp.hasKey(redisKey, session.getId())) {
            hashOp.put(redisKey, session.getId(), session);
        }
    }

    //删除session
    @Override
    protected void doDelete(Session session) {
        log.debug("doDelete:" + session.getId());
        super.doDelete(session);
        HashOperations<Object, Object, Object> hashOp = redisTemplate.opsForHash();
        hashOp.delete(redisKey, session.getId());
    }

    //获取当前活动的session
    @Override
    public Collection<Session> getActiveSessions() {
        return super.getActiveSessions();
    }
}