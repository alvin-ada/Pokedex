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

package com.skydoves.pokedex.ui.login

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.bindingProperty
import com.skydoves.pokedex.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val userManager: UserManager
) : BindingViewModel() {

  @get:Bindable
  var userName: String by bindingProperty("")

  @get:Bindable
  var userEmail: String by bindingProperty("")

  @get:Bindable
  var isLoading: Boolean by bindingProperty(false)
    private set

  private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.Idle)
  val loginResult: StateFlow<LoginResult> = _loginResult.asStateFlow()

  fun login() {
    if (userName.isBlank()) {
      _loginResult.value = LoginResult.Error("请输入用户名")
      return
    }

    if (userEmail.isBlank()) {
      _loginResult.value = LoginResult.Error("请输入邮箱")
      return
    }

    if (!isValidEmail(userEmail)) {
      _loginResult.value = LoginResult.Error("请输入有效的邮箱地址")
      return
    }

    viewModelScope.launch {
      isLoading = true
      
      try {
        // 模拟网络延迟
        kotlinx.coroutines.delay(1500)
        
        // 保存用户登录状态
        userManager.login(userName, userEmail)
        
        _loginResult.value = LoginResult.Success
      } catch (e: Exception) {
        _loginResult.value = LoginResult.Error("登录失败，请重试")
      } finally {
        isLoading = false
      }
    }
  }

  private fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
  }
}

sealed class LoginResult {
  object Idle : LoginResult()
  object Success : LoginResult()
  data class Error(val message: String) : LoginResult()
} 