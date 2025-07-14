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

package com.skydoves.pokedex.ui.splash

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.bindingProperty
import com.skydoves.pokedex.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
  private val userManager: UserManager,
) : BindingViewModel() {

  @get:Bindable
  var isLoading: Boolean by bindingProperty(true)
    private set

  @get:Bindable
  var welcomeMessage: String by bindingProperty("Pokédx")
    private set

  private val _navigationEvent = MutableStateFlow<NavigationEvent>(NavigationEvent.None)
  val navigationEvent: StateFlow<NavigationEvent> = _navigationEvent.asStateFlow()

  init {
    Timber.d("init SplashViewModel")
    checkLoginStatusAndNavigate()
  }

  private fun checkLoginStatusAndNavigate() {
    viewModelScope.launch {
      try {
        // 显示启屏页面2秒
        delay(2000)
        
        // 检查用户登录状态
        val isLoggedIn = userManager.isLoggedIn()
        
        if (isLoggedIn) {
          val userName = userManager.getUserName()
          welcomeMessage = "欢迎回来，$userName!"
          
          // 再延迟500ms显示欢迎信息
          delay(500)
          
          // 跳转到主页
          _navigationEvent.value = NavigationEvent.NavigateToMain
        } else {
          welcomeMessage = "欢迎使用 Pokédex"
          
          // 跳转到登录页
          _navigationEvent.value = NavigationEvent.NavigateToLogin
        }
      } catch (e: Exception) {
        Timber.e(e, "Error during splash screen")
        // 发生错误时跳转到登录页
        _navigationEvent.value = NavigationEvent.NavigateToLogin
      } finally {
        isLoading = false
      }
    }
  }
}

sealed class NavigationEvent {
  object None : NavigationEvent()
  object NavigateToMain : NavigationEvent()
  object NavigateToLogin : NavigationEvent()
}
