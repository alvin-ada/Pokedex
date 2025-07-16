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

/**
 * ZtkyBaseStore - 带键前缀的存储基类
 * 
 * 🎯 作用：为不同业务模块提供独立的存储命名空间
 * 📱 特性：通过键前缀实现数据隔离，支持依赖注入
 * 🔗 用法：继承此类并实现getKeyPrefix()方法定义前缀
 * 
 * @author skydoves
 * 支持键前缀的KV存储抽象基类
 */
abstract class ZBaseStore(
    private val baseStore: BaseStore
) {
    
    /**
     * 获取键前缀
     * 📌 作用：为当前模块定义唯一的键前缀
     * 📌 用途：实现不同模块间的数据隔离
     */
    protected abstract fun getKeyPrefix(): String

    // ==================== 泛型对象存储 ====================
    
    /**
     * 获取对象（使用JSON序列化）
     */
    fun <T> getObject(key: String, clazz: Class<T>): T? {
        return baseStore.getObject("${getKeyPrefix()}$key", clazz)
    }

    /**
     * 获取对象，带默认值
     */
    fun <T> getObject(key: String, clazz: Class<T>, defValue: T?): T? {
        return baseStore.getObject("${getKeyPrefix()}$key", clazz, defValue)
    }

    /**
     * 存储对象（使用JSON序列化）
     */
    fun <T> putObject(key: String, value: T?) {
        baseStore.putObject("${getKeyPrefix()}$key", value)
    }

    // ==================== Parcelable对象存储 ====================
    
    /**
     * 获取Parcelable对象
     */
    fun <T : Parcelable> getParcelableObject(key: String, clazz: Class<T>): T? {
        return baseStore.getParcelableObject("${getKeyPrefix()}$key", clazz)
    }

    /**
     * 获取Parcelable对象，带默认值
     */
    fun <T : Parcelable> getParcelableObject(key: String, clazz: Class<T>, defValue: T?): T? {
        return baseStore.getParcelableObject("${getKeyPrefix()}$key", clazz, defValue)
    }

    /**
     * 存储Parcelable对象
     */
    fun <T : Parcelable> putParcelableObject(key: String, value: T?) {
        baseStore.putParcelableObject("${getKeyPrefix()}$key", value)
    }

    // ==================== String Set类型 ====================
    
    fun getStringSet(key: String): Set<String> {
        return baseStore.getStringSet("${getKeyPrefix()}$key")
    }

    fun getStringSet(key: String, defValues: Set<String>?): Set<String> {
        return baseStore.getStringSet("${getKeyPrefix()}$key", defValues)
    }

    fun putStringSet(key: String, values: Set<String>?) {
        baseStore.putStringSet("${getKeyPrefix()}$key", values)
    }

    // ==================== Double类型 ====================
    
    fun putDouble(key: String, value: Double) {
        baseStore.putDouble("${getKeyPrefix()}$key", value)
    }

    fun getDouble(key: String): Double {
        return baseStore.getDouble("${getKeyPrefix()}$key")
    }

    fun getDouble(key: String, defValue: Double): Double {
        return baseStore.getDouble("${getKeyPrefix()}$key", defValue)
    }

    // ==================== ByteArray类型 ====================
    
    fun putByte(key: String, value: ByteArray?) {
        baseStore.putByte("${getKeyPrefix()}$key", value)
    }

    fun getBytes(key: String): ByteArray? {
        return baseStore.getBytes("${getKeyPrefix()}$key")
    }

    fun getBytes(key: String, defValue: ByteArray?): ByteArray? {
        return baseStore.getBytes("${getKeyPrefix()}$key", defValue)
    }

    // ==================== String类型 ====================
    
    fun getString(key: String): String {
        return baseStore.getString("${getKeyPrefix()}$key")
    }

    fun getString(key: String, defValue: String?): String {
        return baseStore.getString("${getKeyPrefix()}$key", defValue)
    }

    fun putString(key: String, value: String?) {
        baseStore.putString("${getKeyPrefix()}$key", value)
    }

    // ==================== Boolean类型 ====================
    
    fun getBoolean(key: String): Boolean {
        return baseStore.getBoolean("${getKeyPrefix()}$key")
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return baseStore.getBoolean("${getKeyPrefix()}$key", defValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        baseStore.putBoolean("${getKeyPrefix()}$key", value)
    }

    // ==================== Int类型 ====================
    
    fun putInt(key: String, value: Int) {
        baseStore.putInt("${getKeyPrefix()}$key", value)
    }

    fun getInt(key: String): Int {
        return baseStore.getInt("${getKeyPrefix()}$key")
    }

    fun getInt(key: String, defValue: Int): Int {
        return baseStore.getInt("${getKeyPrefix()}$key", defValue)
    }

    // ==================== Float类型 ====================
    
    fun putFloat(key: String, value: Float) {
        baseStore.putFloat("${getKeyPrefix()}$key", value)
    }

    fun getFloat(key: String): Float {
        return baseStore.getFloat("${getKeyPrefix()}$key")
    }

    fun getFloat(key: String, defValue: Float): Float {
        return baseStore.getFloat("${getKeyPrefix()}$key", defValue)
    }

    // ==================== Long类型 ====================
    
    fun putLong(key: String, value: Long) {
        baseStore.putLong("${getKeyPrefix()}$key", value)
    }

    fun getLong(key: String): Long {
        return baseStore.getLong("${getKeyPrefix()}$key")
    }

    fun getLong(key: String, defValue: Long): Long {
        return baseStore.getLong("${getKeyPrefix()}$key", defValue)
    }

    // ==================== 其他方法 ====================
    
    /**
     * 移除指定key的数据
     */
    fun remove(key: String) {
        baseStore.remove("${getKeyPrefix()}$key")
    }

    /**
     * 清空当前前缀下的所有数据
     */
    fun clear() {
        if (getKeyPrefix().isEmpty()) {
            baseStore.clear()
            return
        }
        val allKeys = baseStore.allKeys()
        allKeys.forEach { key ->
            if (key.startsWith(getKeyPrefix())) {
                baseStore.remove(key)
            }
        }
    }

    /**
     * 获取当前前缀下的所有key
     */
    fun allKeys(): Set<String> {
        val allKeys = baseStore.allKeys()
        if (getKeyPrefix().isEmpty()) {
            return allKeys
        }
        return allKeys.filter { key -> 
            key.startsWith(getKeyPrefix()) 
        }.map { key ->
            key.removePrefix(getKeyPrefix())
        }.toSet()
    }

    /**
     * 检查是否包含指定key
     */
    fun contains(key: String): Boolean {
        return baseStore.contains("${getKeyPrefix()}$key")
    }

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