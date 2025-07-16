/*
 * PokedexApp.kt - Pokedex 应用程序主类
 * 
 * 🎯 作用：应用程序的入口点和全局配置
 * 📱 模块：app - 应用程序主模块
 * 🔗 功能：初始化 Hilt 依赖注入系统
 */

package com.skydoves.pokedex

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// 🔥 @HiltAndroidApp 注解详解
// 📌 作用：启用 Hilt 依赖注入框架
// 📌 特点：Hilt 的根组件，所有依赖注入都从这里开始
// 📌 好处：自动生成应用级别的依赖注入组件
@HiltAndroidApp
class PokedexApp : Application() {
  // 🔥 继承 Application 类
  // 📌 作用：应用程序的全局单例类
  // 📌 生命周期：应用程序启动时创建，销毁时销毁
  // 📌 用途：全局初始化和配置
  
  // 🔥 当前实现为空
  // 📌 原因：Hilt 会自动处理初始化
  // 📌 扩展：可以添加全局初始化逻辑（如日志、崩溃报告等）
  // 💡 用法：在 AndroidManifest.xml 中声明为 android:name=".PokedexApp"
}
