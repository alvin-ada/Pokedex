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

package com.skydoves.pokedex.core.store.strategy

import android.content.Context
import android.os.Parcelable

/**
 * 存储策略接口
 * 
 * 🎯 作用：定义KV存储的抽象接口，支持多种存储实现
 * 📱 用途：用于替代SharedPreference，提供更高性能的存储方案
 * 🔗 实现：MMKV等高性能存储引擎
 */
interface StoreStrategy {
    
    /**
     * 初始化存储
     */
    fun init(context: Context)

    // ==================== 泛型对象存储 ====================
    
    /**
     * 获取对象（使用JSON序列化）
     */
    fun <T> getObject(key: String, clazz: Class<T>): T?
    
    /**
     * 获取对象，带默认值
     */
    fun <T> getObject(key: String, clazz: Class<T>, defValue: T?): T?
    
    /**
     * 存储对象（使用JSON序列化）
     */
    fun <T> putObject(key: String, value: T?)

    // ==================== Parcelable对象存储 ====================
    
    /**
     * 获取Parcelable对象
     */
    fun <T : Parcelable> getParcelableObject(key: String, clazz: Class<T>): T?
    
    /**
     * 获取Parcelable对象，带默认值
     */
    fun <T : Parcelable> getParcelableObject(key: String, clazz: Class<T>, defValue: T?): T?
    
    /**
     * 存储Parcelable对象
     */
    fun <T : Parcelable> putParcelableObject(key: String, value: T?)

    // ==================== String Set类型 ====================
    
    fun getStringSet(key: String): Set<String>
    fun getStringSet(key: String, defValues: Set<String>?): Set<String>
    fun putStringSet(key: String, values: Set<String>?)

    // ==================== Double类型 ====================
    
    fun putDouble(key: String, value: Double)
    fun getDouble(key: String): Double
    fun getDouble(key: String, defValue: Double): Double

    // ==================== ByteArray类型 ====================
    
    fun putByte(key: String, value: ByteArray?)
    fun getBytes(key: String): ByteArray?
    fun getBytes(key: String, defValue: ByteArray?): ByteArray?

    // ==================== String类型 ====================
    
    fun getString(key: String): String
    fun getString(key: String, defValue: String?): String
    fun putString(key: String, value: String?)

    // ==================== Boolean类型 ====================
    
    fun getBoolean(key: String): Boolean
    fun getBoolean(key: String, defValue: Boolean): Boolean
    fun putBoolean(key: String, value: Boolean)

    // ==================== Int类型 ====================
    
    fun putInt(key: String, value: Int)
    fun getInt(key: String): Int
    fun getInt(key: String, defValue: Int): Int

    // ==================== Float类型 ====================
    
    fun putFloat(key: String, value: Float)
    fun getFloat(key: String): Float
    fun getFloat(key: String, defValue: Float): Float

    // ==================== Long类型 ====================
    
    fun putLong(key: String, value: Long)
    fun getLong(key: String): Long
    fun getLong(key: String, defValue: Long): Long

    // ==================== 其他方法 ====================
    
    /**
     * 移除指定key的数据
     */
    fun remove(key: String)
    
    /**
     * 清空所有数据
     */
    fun clear()
    
    /**
     * 检查是否包含指定key
     */
    fun contains(key: String): Boolean
    
    /**
     * 获取所有key
     */
    fun allKeys(): Array<String>?
} 