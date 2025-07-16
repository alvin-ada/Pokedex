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

package com.skydoves.pokedex.core.store.strategy.mmkv

import android.content.Context
import android.os.Parcelable
import com.skydoves.pokedex.core.store.strategy.StoreStrategy
import com.squareup.moshi.Moshi
import com.tencent.mmkv.MMKV
import javax.inject.Inject
import javax.inject.Singleton

/**
 * MMKV存储实现
 * 
 * 🎯 作用：基于MMKV的高性能KV存储实现
 * 📱 特性：使用Moshi进行JSON序列化，支持Hilt依赖注入
 * 🔗 优势：比SharedPreference性能更高，支持多进程
 */
@Singleton
class MMKVStore @Inject constructor(
    private val moshi: Moshi
) : StoreStrategy {

    companion object {
        @Volatile
        private var mmkv: MMKV? = null

        private fun getMMKV(): MMKV {
            return mmkv ?: synchronized(MMKVStore::class.java) {
                mmkv ?: MMKV.defaultMMKV(MMKV.MULTI_PROCESS_MODE, null).also { mmkv = it }
            }
        }
    }

    override fun init(context: Context) {
        MMKV.initialize(context)
    }

    // ==================== 泛型对象存储 ====================
    
    override fun <T> getObject(key: String, clazz: Class<T>): T? {
        return getObject(key, clazz, null)
    }

    override fun <T> getObject(key: String, clazz: Class<T>, defValue: T?): T? {
        return try {
            if (!contains(key)) {
                defValue
            } else {
                val jsonString = getString(key)
                if (jsonString.isNotEmpty()) {
                    val adapter = moshi.adapter(clazz)
                    adapter.fromJson(jsonString)
                } else {
                    defValue
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            defValue
        }
    }

    override fun <T> putObject(key: String, value: T?) {
        try {
            if (value != null) {
                val adapter = moshi.adapter(value.javaClass)
                val jsonString = adapter.toJson(value)
                putString(key, jsonString)
            } else {
                remove(key)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // ==================== Parcelable对象存储 ====================
    
    override fun <T : Parcelable> getParcelableObject(key: String, clazz: Class<T>): T? {
        return getMMKV().decodeParcelable(key, clazz)
    }

    override fun <T : Parcelable> getParcelableObject(key: String, clazz: Class<T>, defValue: T?): T? {
        return getMMKV().decodeParcelable(key, clazz, defValue)
    }

    override fun <T : Parcelable> putParcelableObject(key: String, value: T?) {
        getMMKV().encode(key, value)
    }

    // ==================== String Set类型 ====================
    
    override fun getStringSet(key: String): Set<String> {
        return getMMKV().getStringSet(key, HashSet()) ?: HashSet()
    }

    override fun getStringSet(key: String, defValues: Set<String>?): Set<String> {
        return getMMKV().getStringSet(key, defValues) ?: defValues ?: HashSet()
    }

    override fun putStringSet(key: String, values: Set<String>?) {
        getMMKV().putStringSet(key, values)
    }

    // ==================== Double类型 ====================
    
    override fun putDouble(key: String, value: Double) {
        getMMKV().encode(key, value)
    }

    override fun getDouble(key: String): Double {
        return getMMKV().decodeDouble(key)
    }

    override fun getDouble(key: String, defValue: Double): Double {
        return getMMKV().decodeDouble(key, defValue)
    }

    // ==================== ByteArray类型 ====================
    
    override fun putByte(key: String, value: ByteArray?) {
        getMMKV().encode(key, value)
    }

    override fun getBytes(key: String): ByteArray? {
        return getMMKV().decodeBytes(key)
    }

    override fun getBytes(key: String, defValue: ByteArray?): ByteArray? {
        return getMMKV().decodeBytes(key, defValue)
    }

    // ==================== String类型 ====================
    
    override fun getString(key: String): String {
        return getMMKV().getString(key, "") ?: ""
    }

    override fun getString(key: String, defValue: String?): String {
        return getMMKV().getString(key, defValue) ?: defValue ?: ""
    }

    override fun putString(key: String, value: String?) {
        getMMKV().putString(key, value)
    }

    // ==================== Boolean类型 ====================
    
    override fun getBoolean(key: String): Boolean {
        return getMMKV().getBoolean(key, false)
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        return getMMKV().getBoolean(key, defValue)
    }

    override fun putBoolean(key: String, value: Boolean) {
        getMMKV().putBoolean(key, value)
    }

    // ==================== Int类型 ====================
    
    override fun putInt(key: String, value: Int) {
        getMMKV().putInt(key, value)
    }

    override fun getInt(key: String): Int {
        return getMMKV().getInt(key, 0)
    }

    override fun getInt(key: String, defValue: Int): Int {
        return getMMKV().getInt(key, defValue)
    }

    // ==================== Float类型 ====================
    
    override fun putFloat(key: String, value: Float) {
        getMMKV().putFloat(key, value)
    }

    override fun getFloat(key: String): Float {
        return getMMKV().getFloat(key, 0f)
    }

    override fun getFloat(key: String, defValue: Float): Float {
        return getMMKV().getFloat(key, defValue)
    }

    // ==================== Long类型 ====================
    
    override fun putLong(key: String, value: Long) {
        getMMKV().putLong(key, value)
    }

    override fun getLong(key: String): Long {
        return getMMKV().getLong(key, 0L)
    }

    override fun getLong(key: String, defValue: Long): Long {
        return getMMKV().getLong(key, defValue)
    }

    // ==================== 其他方法 ====================
    
    override fun remove(key: String) {
        getMMKV().remove(key)
    }

    override fun clear() {
        getMMKV().clear()
    }

    override fun contains(key: String): Boolean {
        return getMMKV().contains(key)
    }

    override fun allKeys(): Array<String>? {
        return getMMKV().allKeys()
    }
}
