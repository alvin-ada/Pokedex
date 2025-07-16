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

package com.skydoves.pokedex.utils

import com.skydoves.pokedex.core.store.ZStore
import javax.inject.Inject
import javax.inject.Singleton

/**
 * UserManager - 用户管理器
 * 
 * 🎯 作用：管理用户登录状态和用户信息
 * 📱 特性：使用core-store模块进行数据持久化，替代SharedPreferences
 * 🔗 存储：基于MMKV的高性能存储，支持多进程访问
 * 
 * @author skydoves
 * 用户状态管理，基于高性能KV存储
 */
@Singleton
class UserManager @Inject constructor(
  private val ztkyStore: ZStore
) {

  companion object {
    private const val KEY_IS_LOGGED_IN = "user_is_logged_in"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_LOGIN_TIME = "user_login_time"
  }

  /**
   * 检查用户是否已登录
   * 📌 使用：ZtkyStore进行数据读取
   * 📌 默认：未登录状态
   */
  fun isLoggedIn(): Boolean {
    return ztkyStore.getBoolean(KEY_IS_LOGGED_IN, false)
  }

  /**
   * 用户登录
   * 📌 存储：登录状态、用户名、邮箱和登录时间
   * 📌 特性：一次性存储多个相关数据
   */
  fun login(userName: String, email: String) {
    val currentTime = System.currentTimeMillis()
    
    // 批量存储用户信息
    ztkyStore.putBoolean(KEY_IS_LOGGED_IN, true)
    ztkyStore.putString(KEY_USER_NAME, userName)
    ztkyStore.putString(KEY_USER_EMAIL, email)
    ztkyStore.putLong(KEY_LOGIN_TIME, currentTime)
  }

  /**
   * 用户登录（扩展版本）
   * 📌 支持：用户ID的存储
   * 📌 用途：支持更完整的用户信息管理
   */
  fun login(userId: String, userName: String, email: String) {
    val currentTime = System.currentTimeMillis()
    
    // 批量存储完整用户信息
    ztkyStore.putBoolean(KEY_IS_LOGGED_IN, true)
    ztkyStore.putString(KEY_USER_ID, userId)
    ztkyStore.putString(KEY_USER_NAME, userName)
    ztkyStore.putString(KEY_USER_EMAIL, email)
    ztkyStore.putLong(KEY_LOGIN_TIME, currentTime)
  }

  /**
   * 用户登出
   * 📌 清理：移除所有用户相关数据
   * 📌 安全：确保用户数据完全清除
   */
  fun logout() {
    ztkyStore.remove(KEY_IS_LOGGED_IN)
    ztkyStore.remove(KEY_USER_ID)
    ztkyStore.remove(KEY_USER_NAME)
    ztkyStore.remove(KEY_USER_EMAIL)
    ztkyStore.remove(KEY_LOGIN_TIME)
  }

  /**
   * 获取用户ID
   * 📌 返回：用户唯一标识符
   */
  fun getUserId(): String? {
    return ztkyStore.getString(KEY_USER_ID, null)
  }

  /**
   * 获取用户名
   * 📌 返回：用户显示名称
   */
  fun getUserName(): String? {
    return ztkyStore.getString(KEY_USER_NAME, null)
  }

  /**
   * 获取用户邮箱
   * 📌 返回：用户邮箱地址
   */
  fun getUserEmail(): String? {
    return ztkyStore.getString(KEY_USER_EMAIL, null)
  }

  /**
   * 获取登录时间
   * 📌 返回：最后登录的时间戳
   * 📌 用途：用于会话管理和安全检查
   */
  fun getLoginTime(): Long {
    return ztkyStore.getLong(KEY_LOGIN_TIME, 0L)
  }

  /**
   * 检查会话是否过期
   * 📌 参数：expireTimeMs - 过期时间（毫秒）
   * 📌 返回：true表示已过期，false表示未过期
   * 📌 用途：自动登出过期用户
   */
  fun isSessionExpired(expireTimeMs: Long = 30 * 24 * 60 * 60 * 1000L): Boolean { // 默认30天
    if (!isLoggedIn()) return true
    
    val loginTime = getLoginTime()
    if (loginTime == 0L) return true
    
    val currentTime = System.currentTimeMillis()
    return (currentTime - loginTime) > expireTimeMs
  }

  /**
   * 更新用户信息
   * 📌 用途：更新用户资料而不影响登录状态
   */
  fun updateUserInfo(userName: String, email: String) {
    if (isLoggedIn()) {
      ztkyStore.putString(KEY_USER_NAME, userName)
      ztkyStore.putString(KEY_USER_EMAIL, email)
    }
  }

  /**
   * 获取用户信息摘要
   * 📌 返回：格式化的用户信息字符串
   * 📌 用途：用于调试和日志记录
   */
  fun getUserSummary(): String {
    return if (isLoggedIn()) {
      "User(id=${getUserId()}, name=${getUserName()}, email=${getUserEmail()})"
    } else {
      "User(not logged in)"
    }
  }
} 