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

import android.os.Parcelable
import com.skydoves.pokedex.core.store.strategy.StoreManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * BaseStore - KV存储核心类
 * 
 * 🎯 作用：提供KV存储的统一接口，替代SharedPreference
 * 📱 特性：基于MMKV，性能优越，支持多进程，使用Hilt依赖注入
 * 🔗 架构：采用策略模式，支持不同存储引擎的切换
 * 
 * @author skydoves
 * 基于MMKV的高性能KV存储方案
 * 
 * https://github.com/Tencent/MMKV
 */
@Singleton
class BaseStore @Inject constructor(
    private val storeManager: StoreManager
) {

    // ==================== 泛型对象存储 ====================
    
    /**
     * 获取对象（使用JSON序列化）
     */
    fun <T> getObject(key: String, clazz: Class<T>): T? =
        storeManager.getObject(key, clazz)

    /**
     * 获取对象，带默认值
     */
    fun <T> getObject(key: String, clazz: Class<T>, defValue: T?): T? =
        storeManager.getObject(key, clazz, defValue)

    /**
     * 存储对象（使用JSON序列化）
     */
    fun <T> putObject(key: String, value: T?) {
        storeManager.putObject(key, value)
    }

    // ==================== Parcelable对象存储 ====================
    
    /**
     * 获取Parcelable对象
     */
    fun <T : Parcelable> getParcelableObject(key: String, clazz: Class<T>): T? =
        storeManager.getParcelableObject(key, clazz)

    /**
     * 获取Parcelable对象，带默认值
     */
    fun <T : Parcelable> getParcelableObject(key: String, clazz: Class<T>, defValue: T?): T? =
        storeManager.getParcelableObject(key, clazz, defValue)

    /**
     * 存储Parcelable对象
     */
    fun <T : Parcelable> putParcelableObject(key: String, value: T?) {
        storeManager.putParcelableObject(key, value)
    }

    // ==================== String Set类型 ====================
    
    fun getStringSet(key: String): Set<String> =
        storeManager.getStringSet(key)

    fun getStringSet(key: String, defValues: Set<String>?): Set<String> =
        storeManager.getStringSet(key, defValues)

    fun putStringSet(key: String, values: Set<String>?) {
        storeManager.putStringSet(key, values)
    }

    // ==================== Double类型 ====================
    
    fun putDouble(key: String, value: Double) {
        storeManager.putDouble(key, value)
    }

    fun getDouble(key: String): Double =
        storeManager.getDouble(key)

    fun getDouble(key: String, defValue: Double): Double =
        storeManager.getDouble(key, defValue)

    // ==================== ByteArray类型 ====================
    
    fun putByte(key: String, value: ByteArray?) {
        storeManager.putByte(key, value)
    }

    fun getBytes(key: String): ByteArray? =
        storeManager.getBytes(key)

    fun getBytes(key: String, defValue: ByteArray?): ByteArray? =
        storeManager.getBytes(key, defValue)

    // ==================== String类型 ====================
    
    fun getString(key: String): String =
        storeManager.getString(key)

    fun getString(key: String, defValue: String?): String =
        storeManager.getString(key, defValue)

    fun putString(key: String, value: String?) {
        storeManager.putString(key, value)
    }

    // ==================== Boolean类型 ====================
    
    fun getBoolean(key: String): Boolean =
        storeManager.getBoolean(key)

    fun getBoolean(key: String, defValue: Boolean): Boolean =
        storeManager.getBoolean(key, defValue)

    fun putBoolean(key: String, value: Boolean) {
        storeManager.putBoolean(key, value)
    }

    // ==================== Int类型 ====================
    
    fun putInt(key: String, value: Int) {
        storeManager.putInt(key, value)
    }

    fun getInt(key: String): Int =
        storeManager.getInt(key)

    fun getInt(key: String, defValue: Int): Int =
        storeManager.getInt(key, defValue)

    // ==================== Float类型 ====================
    
    fun putFloat(key: String, value: Float) {
        storeManager.putFloat(key, value)
    }

    fun getFloat(key: String): Float =
        storeManager.getFloat(key)

    fun getFloat(key: String, defValue: Float): Float =
        storeManager.getFloat(key, defValue)

    // ==================== Long类型 ====================
    
    fun putLong(key: String, value: Long) {
        storeManager.putLong(key, value)
    }

    fun getLong(key: String): Long =
        storeManager.getLong(key)

    fun getLong(key: String, defValue: Long): Long =
        storeManager.getLong(key, defValue)

    // ==================== 其他方法 ====================
    
    /**
     * 移除指定key的数据
     */
    fun remove(key: String) {
        storeManager.remove(key)
    }

    /**
     * 清空所有数据
     */
    fun clear() {
        storeManager.clear()
    }

    /**
     * 检查是否包含指定key
     */
    fun contains(key: String): Boolean =
        storeManager.contains(key)

    /**
     * 获取所有key
     */
    fun allKeys(): Set<String> =
        storeManager.allKeys()?.toSet() ?: emptySet()

    // ==================== Kotlin扩展方法支持 ====================
    
    /**
     * Kotlin内联扩展 - 获取对象
     */
    inline fun <reified T> getObject(key: String): T? =
        getObject(key, T::class.java)

    /**
     * Kotlin内联扩展 - 获取对象，带默认值
     */
    inline fun <reified T> getObject(key: String, defValue: T?): T? =
        getObject(key, T::class.java, defValue)

    /**
     * Kotlin内联扩展 - 获取Parcelable对象
     */
    inline fun <reified T : Parcelable> getParcelableObject(key: String): T? =
        getParcelableObject(key, T::class.java)

    /**
     * Kotlin内联扩展 - 获取Parcelable对象，带默认值
     */
    inline fun <reified T : Parcelable> getParcelableObject(key: String, defValue: T?): T? =
        getParcelableObject(key, T::class.java, defValue)
}