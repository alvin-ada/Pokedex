/*
 * StartupBenchmark.kt - 应用启动性能基准测试
 * 
 * 🎯 作用：测量应用在不同状态下的启动性能
 * 📱 模块：benchmark - 性能测试模块
 * 🔗 功能：冷启动、热启动、温启动性能测试
 */

package com.github.skydoves.benchmark

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// 🔥 冷启动基准测试
// 📌 作用：测量应用从完全关闭状态启动的性能
// 📌 @RunWith：指定 JUnit 测试运行器
// 📌 继承：AbstractStartupBenchmark 抽象基类
/**
 * 🔥 冷启动测试详解
 * 📌 定义：应用进程完全不存在，需要从头创建
 * 📌 场景：设备重启后首次启动应用
 * 📌 特点：启动时间最长，包含应用初始化的所有步骤
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class ColdStartupBenchmark : AbstractStartupBenchmark(StartupMode.COLD)

// 🔥 温启动基准测试
// 📌 作用：测量应用进程存在但Activity被销毁后的启动性能
// 📌 场景：用户离开应用一段时间后重新打开
/**
 * 🔥 温启动测试详解
 * 📌 定义：应用进程存在，但Activity需要重新创建
 * 📌 场景：应用在后台运行，用户重新打开
 * 📌 特点：启动时间居中，跳过进程创建步骤
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class WarmStartupBenchmark : AbstractStartupBenchmark(StartupMode.WARM)

// 🔥 热启动基准测试
// 📌 作用：测量应用和Activity都存在时的启动性能
// 📌 场景：用户快速切换应用后返回
/**
 * 🔥 热启动测试详解
 * 📌 定义：应用进程和Activity都存在，只需要恢复可见性
 * 📌 场景：用户按Home键后立即返回应用
 * 📌 特点：启动时间最短，只涉及UI恢复
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class HotStartupBenchmark : AbstractStartupBenchmark(StartupMode.HOT)

// 🔥 抽象启动基准测试基类
// 📌 作用：提供不同启动模式的通用测试逻辑
// 📌 参数：startupMode 指定启动模式类型
// 📌 设计：模板方法模式，复用共同逻辑
/**
 * 🔥 抽象基类详解
 * 📌 作用：封装不同编译模式下的启动性能测试
 * 📌 模式：支持基线配置文件和编译模式的组合测试
 * 📌 好处：统一测试逻辑，减少代码重复
 */
abstract class AbstractStartupBenchmark(private val startupMode: StartupMode) {
  
  // 🔥 基准测试规则
  // 📌 @get:Rule：JUnit 规则注解
  // 📌 MacrobenchmarkRule：宏基准测试规则
  // 📌 作用：提供基准测试的基础设施
  @get:Rule
  val benchmarkRule = MacrobenchmarkRule()

  // 🔥 无编译模式测试
  // 📌 @RequiresApi：需要 Android N 及以上版本
  // 📌 CompilationMode.None：不进行任何编译优化
  // 📌 用途：测试应用在解释执行模式下的性能
  @Test
  @RequiresApi(Build.VERSION_CODES.N)
  fun startupNoCompilation() = startup(CompilationMode.None())

  // 🔥 禁用基线配置文件测试
  // 📌 BaselineProfileMode.Disable：禁用基线配置文件
  // 📌 warmupIterations：预热迭代次数
  // 📌 用途：测试没有基线配置文件时的性能
  @Test
  @RequiresApi(Build.VERSION_CODES.N)
  fun startupBaselineProfileDisabled() = startup(
    CompilationMode.Partial(
      baselineProfileMode = BaselineProfileMode.Disable,  // 📌 禁用基线配置文件
      warmupIterations = 1,                               // 📌 预热1次
    ),
  )

  // 🔥 启用基线配置文件测试
  // 📌 BaselineProfileMode.Require：要求基线配置文件
  // 📌 用途：测试有基线配置文件时的性能提升
  @Test
  @RequiresApi(Build.VERSION_CODES.N)
  fun startupBaselineProfile() =
    startup(CompilationMode.Partial(baselineProfileMode = BaselineProfileMode.Require))

  // 🔥 完全编译模式测试
  // 📌 CompilationMode.Full：完全编译模式
  // 📌 用途：测试应用在完全编译状态下的性能
  @Test
  fun startupFullCompilation() = startup(CompilationMode.Full())

  // 🔥 启动测试核心方法
  // 📌 作用：执行具体的启动性能测试
  // 📌 参数：compilationMode 指定编译模式
  private fun startup(compilationMode: CompilationMode) = benchmarkRule.measureRepeated(
    // 🔥 测试配置参数
    packageName = PACKAGE_NAME,                    // 📌 目标应用包名
    metrics = listOf(StartupTimingMetric()),       // 📌 测量指标：启动时间
    compilationMode = compilationMode,             // 📌 编译模式
    iterations = 5,                                // 📌 测试迭代次数
    startupMode = startupMode,                     // 📌 启动模式
    
    // 🔥 测试前置操作
    setupBlock = {
      pressHome()  // 📌 返回主屏幕，确保测试环境一致
    },
  ) {
    // 🔥 测试主体操作
    // 📌 启动应用并等待完成
    // 📌 这是实际测量的操作
    startActivityAndWait()
  }
  // 💡 用法：通过不同的编译模式组合，全面评估应用启动性能
}
