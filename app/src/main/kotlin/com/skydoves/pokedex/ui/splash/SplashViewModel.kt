/*
 * SplashViewModel.kt - 启动页面的视图模型
 * 
 * 🎯 作用：管理启动页面的逻辑和导航
 * 📱 模块：app - 应用程序主模块
 * 🔗 功能：检查用户登录状态，决定跳转到主页还是登录页
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

// 🔥 启动页面 ViewModel
// 📌 @HiltViewModel：启用 Hilt 依赖注入
// 📌 构造函数：注入 UserManager 用于检查登录状态
// 📌 继承：BindingViewModel 支持 DataBinding
@HiltViewModel
class SplashViewModel @Inject constructor(
  private val userManager: UserManager,  // 📌 用户管理器，检查登录状态
) : BindingViewModel() {

  // 🔥 加载状态属性
  // 📌 @get:Bindable：支持 DataBinding
  // 📌 初始值：true（启动时显示加载状态）
  @get:Bindable
  var isLoading: Boolean by bindingProperty(true)
    private set
    // 💡 用法：在 XML 中控制加载指示器的显示

  // 🔥 欢迎消息属性
  // 📌 作用：显示不同的欢迎文本
  // 📌 初始值：默认应用名称
  // 📌 动态：根据用户登录状态变化
  @get:Bindable
  var welcomeMessage: String by bindingProperty("Pokédx")
    private set
    // 💡 用法：在 XML 中显示欢迎消息

  // 🔥 导航事件状态流
  // 📌 MutableStateFlow：内部可变状态流
  // 📌 初始值：NavigationEvent.None（无导航）
  private val _navigationEvent = MutableStateFlow<NavigationEvent>(NavigationEvent.None)
  
  // 🔥 导航事件公开接口
  // 📌 StateFlow：外部只读状态流
  // 📌 作用：通知 Activity 执行导航操作
  val navigationEvent: StateFlow<NavigationEvent> = _navigationEvent.asStateFlow()

  // 🔥 初始化块
  init {
    Timber.d("init SplashViewModel")
    // 📌 启动登录状态检查流程
    checkLoginStatusAndNavigate()
  }

  // 🔥 检查登录状态并导航
  // 📌 作用：核心业务逻辑，决定跳转方向
  // 📌 流程：延迟 → 检查状态 → 更新消息 → 导航
  private fun checkLoginStatusAndNavigate() {
    viewModelScope.launch {
      try {
        // 🔥 显示启屏页面
        // 📌 delay(2000)：显示2秒启屏页面
        // 📌 作用：给用户足够时间看到应用logo和品牌
        delay(2000)
        
        // 🔥 检查用户登录状态
        // 📌 userManager.isLoggedIn()：检查本地存储的登录状态
        // 📌 返回：Boolean 类型
        val isLoggedIn = userManager.isLoggedIn()
        
        if (isLoggedIn) {
          // 🔥 用户已登录的处理
          val userName = userManager.getUserName()  // 📌 获取用户名
          welcomeMessage = "欢迎回来，$userName!"    // 📌 个性化欢迎消息
          
          // 📌 再延迟500ms显示欢迎信息
          delay(500)
          
          // 🔥 跳转到主页
          _navigationEvent.value = NavigationEvent.NavigateToMain
        } else {
          // 🔥 用户未登录的处理
          welcomeMessage = "欢迎使用 Pokédx"  // 📌 通用欢迎消息
          
          // 🔥 跳转到登录页
          _navigationEvent.value = NavigationEvent.NavigateToLogin
        }
      } catch (e: Exception) {
        // 🔥 异常处理
        Timber.e(e, "Error during splash screen")
        // 📌 发生错误时默认跳转到登录页
        _navigationEvent.value = NavigationEvent.NavigateToLogin
      } finally {
        // 🔥 最终处理
        // 📌 无论成功还是失败都隐藏加载状态
        isLoading = false
      }
    }
  }
}

// 🔥 导航事件密封类
// 📌 作用：定义所有可能的导航事件
// 📌 sealed class：确保类型安全的枚举
sealed class NavigationEvent {
  object None : NavigationEvent()           // 📌 无导航操作
  object NavigateToMain : NavigationEvent() // 📌 跳转到主页
  object NavigateToLogin : NavigationEvent() // 📌 跳转到登录页
}
// 💡 用法：Activity 监听此事件并执行相应的导航操作
