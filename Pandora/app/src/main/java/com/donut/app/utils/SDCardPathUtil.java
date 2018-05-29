/**   
 *  Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * @Title: SDCardPathUtil.java 
 * @Package com.bis.sportedu.utils 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 亓振刚  
 * @date 2015-7-24 下午5:20:05 
 * @version V1.0   
 */
package com.donut.app.utils;

import android.os.Environment;

import java.io.File;

/**
 * @ClassName: SDCardPathUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 亓振刚
 * @date 2015-7-24 下午5:20:05
 * @version 1.0
 */
public class SDCardPathUtil
{
    public static String getPath()
    {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist)
        {// 外置存储卡
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        else
        {// 内置存储卡
            sdDir = Environment.getExternalStorageDirectory();
        }
        return sdDir.getPath();
    }
}
