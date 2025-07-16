/*
 * MainActivity.kt - 主页面活动
 * 
 * 🎯 作用：展示 Pokemon 列表的主界面
 * 📱 模块：app - 应用程序主模块
 * 🔗 功能：显示 Pokemon 网格列表，支持分页加载和导航
 */

package com.skydoves.pokedex.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import com.skydoves.bindables.BindingActivity
import com.skydoves.pokedex.R
import com.skydoves.pokedex.databinding.ActivityMainBinding
import com.skydoves.pokedex.ui.splash.SplashActivity
import com.skydoves.pokedex.utils.UserManager
import com.skydoves.transformationlayout.onTransformationStartContainer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// 🔥 @AndroidEntryPoint 注解详解
// 📌 作用：启用 Hilt 依赖注入功能
// 📌 特点：可以在 Activity 中使用 @Inject 注解
// 📌 好处：自动管理依赖的生命周期
@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
  // 🔥 继承 BindingActivity
  // 📌 作用：支持 DataBinding 的 Activity 基类
  // 📌 特点：自动处理布局绑定和生命周期
  // 📌 泛型：ActivityMainBinding 是自动生成的绑定类

  // 🔥 ViewModel 注入
  // 📌 @get:VisibleForTesting：在测试中可见
  // 📌 by viewModels()：Hilt 提供的 ViewModel 创建方式
  // 📌 生命周期：与 Activity 绑定，自动管理
  @get:VisibleForTesting
  internal val viewModel: MainViewModel by viewModels()

  // 🔥 用户管理器注入
  // 📌 @Inject：Hilt 依赖注入注解
  // 📌 作用：管理用户登录状态和信息
  @Inject
  lateinit var userManager: UserManager

  // 🔥 Activity 创建时的初始化
  override fun onCreate(savedInstanceState: Bundle?) {
    // 🔥 变换动画初始化
    // 📌 作用：支持页面间的共享元素动画
    // 📌 用途：从启动页到主页的过渡动画
    onTransformationStartContainer()
    super.onCreate(savedInstanceState)
    
    // 🔥 DataBinding 配置
    binding {
      adapter = PokemonAdapter()  // 📌 设置 RecyclerView 适配器
      vm = viewModel              // 📌 绑定 ViewModel 到布局
    }
    // 💡 用法：XML 中可以使用 @{vm.pokemonList} 访问数据
  }

  // 🔥 创建选项菜单
  // 📌 作用：在 ActionBar 中显示菜单选项
  // 📌 功能：包含登出功能
  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  // 🔥 处理菜单项点击事件
  // 📌 作用：响应用户点击菜单项
  // 📌 功能：处理登出操作
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_logout -> {
        logout()  // 📌 执行登出逻辑
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  // 🔥 用户登出功能
  // 📌 作用：清除用户登录状态并跳转到启动页
  // 📌 流程：调用 UserManager → 清除任务栈 → 跳转启动页
  private fun logout() {
    userManager.logout()  // 📌 清除本地登录状态
    
    // 🔥 Intent 标志详解
    // 📌 FLAG_ACTIVITY_NEW_TASK：创建新任务栈
    // 📌 FLAG_ACTIVITY_CLEAR_TASK：清除现有任务栈
    // 📌 好处：确保用户无法通过返回键回到主页
    val intent = Intent(this, SplashActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    finish()  // 📌 结束当前 Activity
  }
  // 💡 用法：用户点击菜单中的登出按钮触发此流程
}
