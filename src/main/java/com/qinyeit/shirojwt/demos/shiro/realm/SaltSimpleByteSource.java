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

import org.apache.shiro.lang.util.ByteSource;
import org.apache.shiro.lang.util.SimpleByteSource;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

/**
 * <p>ClassName: com.qinyeit.shirojwt.demos.shiro.realm.SaltSimpleByteSource
 * <p>Function: 简单继承SimpleByteSource，然后实现 Serializable接口
 * <p>date: 2024-03-13 11:59
 *
 * @author wuqing
 * @version 1.0
 * @since JDK 1.8
 */
public class SaltSimpleByteSource extends SimpleByteSource implements Serializable {
    public SaltSimpleByteSource(byte[] bytes) {
        super(bytes);
    }

    public SaltSimpleByteSource(char[] chars) {
        super(chars);
    }

    public SaltSimpleByteSource(String string) {
        super(string);
    }

    public SaltSimpleByteSource(ByteSource source) {
        super(source);
    }

    public SaltSimpleByteSource(File file) {
        super(file);
    }

    public SaltSimpleByteSource(InputStream stream) {
        super(stream);
    }
}