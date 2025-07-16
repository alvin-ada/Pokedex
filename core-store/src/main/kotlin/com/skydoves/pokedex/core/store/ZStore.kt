/*
 * Designed and developed by 2022 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.pokedex.core.store

import javax.inject.Inject
import javax.inject.Singleton

/**
 * ZtkyStore - 全局数据存储
 *
 * 🎯 作用：用于存储一些全局数据，不和用户/角色绑定
 * 📱 特性：使用Hilt依赖注入，数据持久化到本地
 * 🔗 生命周期：只有当用户卸载或者通过系统设置清除应用存储数据时，这些数据才会被清除
 * 
 * @author skydoves
 * 全局KV存储，用于应用级别的配置和缓存
 */
@Singleton
class ZStore @Inject constructor(
    baseStore: BaseStore
) : ZBaseStore(baseStore) {

    /**
     * 获取键前缀
     * 📌 返回空字符串：表示这是全局存储，不使用前缀
     * 📌 用途：直接存储在根命名空间下
     */
    override fun getKeyPrefix(): String {
        return ""
    }

    // ==================== 全局配置存储 ====================
    
    /**
     * 应用版本相关配置
     */
    fun getAppVersion(): String {
        return getString("app_version", "1.0.0")
    }
    
    fun setAppVersion(version: String) {
        putString("app_version", version)
    }

    /**
     * 首次启动标记
     */
    fun isFirstLaunch(): Boolean {
        return getBoolean("is_first_launch", true)
    }
    
    fun setFirstLaunch(isFirst: Boolean) {
        putBoolean("is_first_launch", isFirst)
    }

    /**
     * 应用主题设置
     */
    fun getThemeMode(): String {
        return getString("theme_mode", "auto")
    }
    
    fun setThemeMode(mode: String) {
        putString("theme_mode", mode)
    }

    /**
     * 语言设置
     */
    fun getLanguage(): String {
        return getString("language", "zh")
    }
    
    fun setLanguage(language: String) {
        putString("language", language)
    }

    /**
     * 网络缓存设置
     */
    fun getCacheSize(): Long {
        return getLong("cache_size", 50 * 1024 * 1024) // 默认50MB
    }
    
    fun setCacheSize(size: Long) {
        putLong("cache_size", size)
    }

    /**
     * 调试模式开关
     */
    fun isDebugMode(): Boolean {
        return getBoolean("debug_mode", false)
    }
    
    fun setDebugMode(isDebug: Boolean) {
        putBoolean("debug_mode", isDebug)
    }
}