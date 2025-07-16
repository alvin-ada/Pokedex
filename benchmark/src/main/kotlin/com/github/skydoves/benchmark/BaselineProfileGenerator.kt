/*
 * BaselineProfileGenerator.kt - 基线配置文件生成器
 * 
 * 🎯 作用：生成应用的基线配置文件以优化性能
 * 📱 模块：benchmark - 性能测试模块
 * 🔗 功能：模拟用户关键操作路径，生成性能优化配置
 */

package com.github.skydoves.benchmark

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test

// 🔥 基线配置文件生成器
// 📌 @RequiresApi：需要 Android P 及以上版本
// 📌 作用：通过模拟用户操作生成基线配置文件
/**
 * 🔥 基线配置文件详解
 * 📌 定义：包含应用关键代码路径的配置文件
 * 📌 作用：告诉 ART 运行时哪些代码应该预编译
 * 📌 好处：显著提升应用启动速度和运行性能
 * 📌 位置：生成后复制到 `app/src/main/baseline-prof.txt`
 */
@RequiresApi(Build.VERSION_CODES.P)
class BaselineProfileGenerator {
  
  // 🔥 基线配置文件规则
  // 📌 @get:Rule：JUnit 规则注解
  // 📌 BaselineProfileRule：基线配置文件生成规则
  // 📌 作用：提供配置文件生成的基础设施
  @get:Rule
  val baselineProfileRule = BaselineProfileRule()

  // 🔥 启动流程配置文件生成
  // 📌 @Test：JUnit 测试方法
  // 📌 作用：通过模拟用户操作生成基线配置文件
  @Test
  fun startup() = baselineProfileRule.collect(
    // 🔥 配置参数
    packageName = PACKAGE_NAME,          // 📌 目标应用包名
    stableIterations = 2,                // 📌 稳定迭代次数
    maxIterations = 8,                   // 📌 最大迭代次数
  ) {
    // 🔥 用户关键操作路径
    // 📌 这个块定义了应用的关键用户操作流程
    // 📌 目标：覆盖应用的核心功能路径
    
    pressHome()              // 📌 返回主屏幕
    
    // 🔥 启动应用
    // 📌 这是最重要的操作，用于启动性能优化
    startActivityAndWait()   // 📌 启动应用并等待完成
    device.waitForIdle()     // 📌 等待设备空闲状态
    
    // 🔥 导航到详情页面
    // 📌 模拟用户浏览 Pokemon 详情的操作
    // 📌 如果发现失败，则提前返回
    device.testDiscover() || return@collect
    device.navigateFromMainToDetails()   // 📌 从主页导航到详情页
    device.pressBack()                   // 📌 返回主页
  }
  // 💡 用法：运行此测试生成基线配置文件，复制到应用中以提升性能
}

// 🔥 UI 发现测试扩展函数
// 📌 作用：测试是否能发现主页面的关键 UI 元素
// 📌 返回：Boolean 类型，成功发现返回 true
private fun UiDevice.testDiscover(): Boolean {
  // 🔥 UI 自动化库的滚动问题说明
  // 📌 注释：UI automator 库在滚动方面存在问题
  // 📌 原计划：使用 RecyclerView 滚动功能
  // 📌 实际：直接查找变换布局元素
  
  // 🔥 等待关键元素出现
  // 📌 By.res：通过资源 ID 查找元素
  // 📌 transformationLayout：用于页面转换动画的布局
  // 📌 timeout：等待超时时间 1000ms
  return wait(Until.hasObject(By.res(PACKAGE_NAME, "transformationLayout")), 1_000)
}

// 🔥 主页到详情页导航扩展函数
// 📌 作用：模拟用户从主页面导航到详情页面的操作
// 📌 流程：点击列表项 → 等待详情页 → 滚动查看 → 返回
private fun UiDevice.navigateFromMainToDetails() {
  // 🔥 点击列表项
  // 📌 waitForObject：等待元素出现并获取
  // 📌 transformationLayout：具有转换动画的布局
  // 📌 click()：模拟点击操作
  waitForObject(By.res(PACKAGE_NAME, "transformationLayout")).click()
  
  // 🔥 等待详情页面加载
  // 📌 nestedScroll：详情页面的嵌套滚动视图
  // 📌 timeout：等待超时时间 1000ms
  wait(Until.hasObject(By.res(PACKAGE_NAME, "nestedScroll")), 1_000)
  
  // 🔥 滚动查看详情内容
  // 📌 scroll：滚动操作
  // 📌 Direction.DOWN：向下滚动
  // 📌 1f：滚动距离（完整页面）
  waitForObject(By.res(PACKAGE_NAME, "nestedScroll")).scroll(Direction.DOWN, 1f)
  
  // 🔥 等待操作完成
  waitForIdle()    // 📌 等待设备空闲
  pressBack()      // 📌 返回上一页
}

// 🔥 等待 UI 对象出现的扩展函数
// 📌 作用：等待指定的 UI 元素出现
// 📌 参数：selector 元素选择器，timeout 超时时间
// 📌 返回：UiObject2 对象，如果超时则抛出异常
private fun UiDevice.waitForObject(selector: BySelector, timeout: Long = 5_000): UiObject2 {
  // 🔥 等待元素出现
  if (wait(Until.hasObject(selector), timeout)) {
    // 📌 如果元素在超时时间内出现，返回该对象
    return findObject(selector)
  }

  // 🔥 超时处理
  // 📌 如果元素未在超时时间内出现，抛出错误
  error("Object with selector [$selector] not found")
}
// 💡 用法：这些扩展函数帮助自动化测试模拟真实用户操作
