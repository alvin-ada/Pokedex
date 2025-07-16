/*
 * LoginViewModel.kt - 登录页面的视图模型
 * 
 * 🎯 作用：管理登录页面的表单验证和登录逻辑
 * 📱 模块：app - 应用程序主模块
 * 🔗 功能：处理用户输入、表单验证、登录状态保存
 */

package com.skydoves.pokedex.ui.login

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.bindingProperty
import com.skydoves.pokedex.core.store.ZStore
import com.skydoves.pokedex.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 🔥 登录页面 ViewModel
// 📌 @HiltViewModel：启用 Hilt 依赖注入
// 📌 构造函数：注入 UserManager 用于保存登录状态
// 📌 继承：BindingViewModel 支持 DataBinding
@HiltViewModel
class LoginViewModel @Inject constructor(
  private val userManager: UserManager,  // 📌 用户管理器，保存登录状态
  private val ztkyStore: ZStore
) : BindingViewModel() {

  // 🔥 用户名输入属性
  // 📌 @get:Bindable：支持 DataBinding 双向绑定
  // 📌 bindingProperty：可观察属性
  // 📌 初始值：空字符串
  @get:Bindable
  var userName: String by bindingProperty("")
  // 💡 用法：在 XML 中使用 @={vm.userName} 实现双向绑定

  // 🔥 邮箱输入属性
  // 📌 作用：存储用户输入的邮箱地址
  // 📌 验证：会进行邮箱格式验证
  @get:Bindable
  var userEmail: String by bindingProperty("")
  // 💡 用法：在 XML 中绑定到 EditText 的 text 属性

  // 🔥 加载状态属性
  // 📌 作用：控制登录过程中的加载指示器
  // 📌 private set：只能在 ViewModel 内部修改
  @get:Bindable
  var isLoading: Boolean by bindingProperty(false)
    private set
    // 💡 用法：在 XML 中控制按钮的可点击状态和加载动画

  // 🔥 登录结果状态流
  // 📌 MutableStateFlow：内部可变状态流
  // 📌 初始值：LoginResult.Idle（空闲状态）
  private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.Idle)
  
  // 🔥 登录结果公开接口
  // 📌 StateFlow：外部只读状态流
  // 📌 作用：通知 Activity 登录结果
  val loginResult: StateFlow<LoginResult> = _loginResult.asStateFlow()

  // 🔥 执行登录操作
  // 📌 作用：处理登录按钮点击事件
  // 📌 流程：输入验证 → 网络请求模拟 → 状态保存 → 结果通知
  fun login() {
    // 🔥 用户名验证
    if (userName.isBlank()) {
      _loginResult.value = LoginResult.Error("请输入用户名")
      return
    }

    // 🔥 邮箱验证
    if (userEmail.isBlank()) {
      _loginResult.value = LoginResult.Error("请输入邮箱")
      return
    }

    // 🔥 邮箱格式验证
    if (!isValidEmail(userEmail)) {
      _loginResult.value = LoginResult.Error("请输入有效的邮箱地址")
      return
    }

    // 🔥 执行登录流程
    viewModelScope.launch {
      isLoading = true  // 📌 开始加载状态
      
      try {
        // 🔥 模拟网络请求
        // 📌 delay(1500)：模拟网络延迟
        // 📌 作用：给用户真实的登录体验
        kotlinx.coroutines.delay(1500)
        
        // 🔥 保存用户登录状态
        // 📌 userManager.login()：保存到本地存储
        // 📌 参数：用户名和邮箱
        userManager.login(userName, userEmail)


        // 🔥 通知登录成功
        _loginResult.value = LoginResult.Success
      } catch (e: Exception) {
        // 🔥 异常处理
        _loginResult.value = LoginResult.Error("登录失败，请重试")
      } finally {
        // 🔥 最终处理
        isLoading = false  // 📌 结束加载状态
      }
    }
  }

  // 🔥 邮箱格式验证
  // 📌 作用：验证邮箱地址的格式是否正确
  // 📌 使用：Android 内置的邮箱格式验证器
  private fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
  }
  // 💡 用法：在登录前验证用户输入的邮箱格式
}

// 🔥 登录结果密封类
// 📌 作用：定义所有可能的登录结果
// 📌 sealed class：确保类型安全的枚举
sealed class LoginResult {
  object Idle : LoginResult()                    // 📌 空闲状态
  object Success : LoginResult()                 // 📌 登录成功
  data class Error(val message: String) : LoginResult()  // 📌 登录失败，包含错误消息
}
// 💡 用法：Activity 监听此结果并执行相应的操作（跳转或显示错误） 