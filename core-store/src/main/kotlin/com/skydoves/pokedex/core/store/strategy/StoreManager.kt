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
import com.skydoves.pokedex.core.store.strategy.mmkv.MMKVStore
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 存储管理器
 * 
 * 🎯 作用：管理KV存储策略，提供统一的存储接口
 * 📱 特性：使用Hilt进行依赖注入，支持策略模式切换存储实现
 * 🔗 默认：使用MMKV作为默认存储策略
 */
@Singleton
class StoreManager @Inject constructor(
    private val storeStrategy: StoreStrategy
) : StoreStrategy {

    override fun init(context: Context) {
        storeStrategy.init(context)
    }

    // ==================== 泛型对象存储 ====================
    
    override fun <T> getObject(key: String, clazz: Class<T>): T? =
        storeStrategy.getObject(key, clazz)

    override fun <T> getObject(key: String, clazz: Class<T>, defValue: T?): T? =
        storeStrategy.getObject(key, clazz, defValue)

    override fun <T> putObject(key: String, value: T?) {
        storeStrategy.putObject(key, value)
    }

    // ==================== Parcelable对象存储 ====================
    
    override fun <T : Parcelable> getParcelableObject(key: String, clazz: Class<T>): T? =
        storeStrategy.getParcelableObject(key, clazz)

    override fun <T : Parcelable> getParcelableObject(key: String, clazz: Class<T>, defValue: T?): T? =
        storeStrategy.getParcelableObject(key, clazz, defValue)

    override fun <T : Parcelable> putParcelableObject(key: String, value: T?) {
        storeStrategy.putParcelableObject(key, value)
    }

    // ==================== String Set类型 ====================
    
    override fun getStringSet(key: String): Set<String> =
        storeStrategy.getStringSet(key)

    override fun getStringSet(key: String, defValues: Set<String>?): Set<String> =
        storeStrategy.getStringSet(key, defValues)

    override fun putStringSet(key: String, values: Set<String>?) {
        storeStrategy.putStringSet(key, values)
    }

    // ==================== Double类型 ====================
    
    override fun putDouble(key: String, value: Double) {
        storeStrategy.putDouble(key, value)
    }

    override fun getDouble(key: String): Double =
        storeStrategy.getDouble(key)

    override fun getDouble(key: String, defValue: Double): Double =
        storeStrategy.getDouble(key, defValue)

    // ==================== ByteArray类型 ====================
    
    override fun putByte(key: String, value: ByteArray?) {
        storeStrategy.putByte(key, value)
    }

    override fun getBytes(key: String): ByteArray? =
        storeStrategy.getBytes(key)

    override fun getBytes(key: String, defValue: ByteArray?): ByteArray? =
        storeStrategy.getBytes(key, defValue)

    // ==================== String类型 ====================
    
    override fun getString(key: String): String =
        storeStrategy.getString(key)

    override fun getString(key: String, defValue: String?): String =
        storeStrategy.getString(key, defValue)

    override fun putString(key: String, value: String?) {
        storeStrategy.putString(key, value)
    }

    // ==================== Boolean类型 ====================
    
    override fun getBoolean(key: String): Boolean =
        storeStrategy.getBoolean(key)

    override fun getBoolean(key: String, defValue: Boolean): Boolean =
        storeStrategy.getBoolean(key, defValue)

    override fun putBoolean(key: String, value: Boolean) {
        storeStrategy.putBoolean(key, value)
    }

    // ==================== Int类型 ====================
    
    override fun putInt(key: String, value: Int) {
        storeStrategy.putInt(key, value)
    }

    override fun getInt(key: String): Int =
        storeStrategy.getInt(key)

    override fun getInt(key: String, defValue: Int): Int =
        storeStrategy.getInt(key, defValue)

    // ==================== Float类型 ====================
    
    override fun putFloat(key: String, value: Float) {
        storeStrategy.putFloat(key, value)
    }

    override fun getFloat(key: String): Float =
        storeStrategy.getFloat(key)

    override fun getFloat(key: String, defValue: Float): Float =
        storeStrategy.getFloat(key, defValue)

    // ==================== Long类型 ====================
    
    override fun putLong(key: String, value: Long) {
        storeStrategy.putLong(key, value)
    }

    override fun getLong(key: String): Long =
        storeStrategy.getLong(key)

    override fun getLong(key: String, defValue: Long): Long =
        storeStrategy.getLong(key, defValue)

    // ==================== 其他方法 ====================
    
    override fun remove(key: String) {
        storeStrategy.remove(key)
    }

    override fun clear() {
        storeStrategy.clear()
    }

    override fun contains(key: String): Boolean =
        storeStrategy.contains(key)

    override fun allKeys(): Array<String>? =
        storeStrategy.allKeys()
} 