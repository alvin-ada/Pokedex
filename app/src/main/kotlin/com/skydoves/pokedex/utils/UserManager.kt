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

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
  @ApplicationContext private val context: Context
) {

  companion object {
    private const val PREF_NAME = "pokedex_user_prefs"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
  }

  private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

  /**
   * 检查用户是否已登录
   */
  fun isLoggedIn(): Boolean {
    return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
  }

  /**
   * 用户登录
   */
  fun login(userName: String, email: String) {
    prefs.edit()
      .putBoolean(KEY_IS_LOGGED_IN, true)
      .putString(KEY_USER_NAME, userName)
      .putString(KEY_USER_EMAIL, email)
      .apply()
  }

  /**
   * 用户登出
   */
  fun logout() {
    prefs.edit()
      .putBoolean(KEY_IS_LOGGED_IN, false)
      .remove(KEY_USER_NAME)
      .remove(KEY_USER_EMAIL)
      .apply()
  }

  /**
   * 获取用户名
   */
  fun getUserName(): String? {
    return prefs.getString(KEY_USER_NAME, null)
  }

  /**
   * 获取用户邮箱
   */
  fun getUserEmail(): String? {
    return prefs.getString(KEY_USER_EMAIL, null)
  }
} 