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

import org.apache.shiro.lang.codec.Base64;
import org.apache.shiro.lang.codec.CodecSupport;
import org.apache.shiro.lang.codec.Hex;
import org.apache.shiro.lang.util.ByteSource;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * <p>ClassName: com.qinyeit.shirojwt.demos.shiro.realm.SaltSimpleByteSource
 * <p>Function: 简单继承SimpleByteSource，然后实现 Serializable接口
 * <p>date: 2024-03-13 11:59
 *
 * @author wuqing
 * @version 1.0
 * @since JDK 1.8
 */
public class SaltSimpleByteSource extends CodecSupport implements ByteSource, Serializable {
    private byte[] bytes;
    private String cachedHex;
    private String cachedBase64;

    // 添加一个无参构造函数，反序列化会用到
    public SaltSimpleByteSource() {
    }

    public SaltSimpleByteSource(byte[] bytes) {
        this.bytes = bytes;
    }

    public SaltSimpleByteSource(char[] chars) {
        this.bytes = toBytes(chars);
    }

    public SaltSimpleByteSource(String string) {
        this.bytes = toBytes(string);
    }

    public SaltSimpleByteSource(ByteSource source) {
        this.bytes = source.getBytes();
    }

    public SaltSimpleByteSource(File file) {
        this.bytes = toBytes(file);
    }

    public SaltSimpleByteSource(InputStream stream) {
        this.bytes = toBytes(stream);
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public String toHex() {
        if (this.cachedHex == null) {
            this.cachedHex = Hex.encodeToString(this.getBytes());
        }

        return this.cachedHex;
    }

    @Override
    public String toBase64() {
        if (this.cachedBase64 == null) {
            this.cachedBase64 = Base64.encodeToString(this.getBytes());
        }
        return this.cachedBase64;
    }

    public String toString() {
        return this.toBase64();
    }

    public int hashCode() {
        return this.bytes != null && this.bytes.length != 0 ? Arrays.hashCode(this.bytes) : 0;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof ByteSource) {
            ByteSource bs = (ByteSource) o;
            return Arrays.equals(this.getBytes(), bs.getBytes());
        } else {
            return false;
        }
    }

    @Override
    public boolean isEmpty() {
        return this.bytes == null || this.bytes.length == 0;
    }
}