/*
 * build.gradle.kts - Benchmark 模块构建配置
 * 
 * 🎯 作用：配置 Android 性能基准测试模块
 * 📱 模块：benchmark - 性能测试模块
 * 🔗 功能：应用启动性能测试、基线配置文件生成、宏基准测试
 */

// 🔥 插件配置
// 📌 android.test：Android 测试插件，用于性能测试
// 📌 kotlin.android：Kotlin Android 插件，支持 Kotlin 语言
plugins {
  alias(libs.plugins.android.test)
  alias(libs.plugins.kotlin.android)
}

// 🔥 Android 配置
android {
  // 📌 命名空间：唯一标识这个测试模块
  namespace = "com.skydoves.pokedex.benchmark"

  // 🔥 默认配置
  defaultConfig {
    // 📌 测试运行器：Android 标准的 JUnit 测试运行器
    // 📌 作用：执行性能测试和基准测试
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  // 🔥 构建类型配置
  buildTypes {
    // 🔥 创建 benchmark 构建类型
    // 📌 作用：专门用于性能基准测试的构建类型
    // 📌 特点：模拟发布版本的性能特征
    create("benchmark") {
      // 🔥 构建类型属性
      isDebuggable = true                           // 📌 可调试：便于本地开发和CI测试
      signingConfig = getByName("debug").signingConfig  // 📌 签名配置：使用调试签名
      matchingFallbacks += listOf("release")       // 📌 回退匹配：性能接近发布版本
    }
    // 💡 用法：./gradlew :benchmark:connectedBenchmarkAndroidTest
  }

  // 🔥 目标项目配置
  // 📌 targetProjectPath：指定要测试的目标应用
  // 📌 值：":app" 表示测试主应用模块
  targetProjectPath = ":app"
  
  // 🔥 实验性功能
  // 📌 self-instrumenting：启用自检测功能
  // 📌 作用：提高基准测试的准确性
  experimentalProperties["android.experimental.self-instrumenting"] = true
}

// 🔥 Kotlin 编译配置
// 📌 作用：配置 Kotlin 编译选项
// 📌 目标：JVM 17 版本
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  compilerOptions {
    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
  }
}

// 🔥 依赖配置
dependencies {
  // 🔥 基线配置文件相关
  // 📌 profileinstaller：基线配置文件安装器
  // 📌 作用：在运行时安装基线配置文件
  implementation(libs.profileinstaller)
  
  // 🔥 宏基准测试相关
  // 📌 macrobenchmark：Android 宏基准测试库
  // 📌 作用：测量应用启动时间、操作性能等
  implementation(libs.macrobenchmark)
  
  // 🔥 UI 自动化相关
  // 📌 uiautomator：UI 自动化测试库
  // 📌 作用：模拟用户操作，自动化测试流程
  implementation(libs.uiautomator)
  
  // 🔥 测试运行器
  // 📌 android.test.runner：Android 测试运行器
  // 📌 作用：执行测试用例
  implementation(libs.android.test.runner)
}

// 🔥 Android 组件配置
// 📌 作用：配置构建变体的启用条件
// 📌 规则：只有 benchmark 构建类型才启用
androidComponents {
  beforeVariants(selector().all()) {
    // 🔥 变体启用条件
    // 📌 只有构建类型为 "benchmark" 时才启用
    // 📌 好处：避免在其他构建类型中执行性能测试
    it.enable = it.buildType == "benchmark"
  }
}
// 💡 用法：专门用于性能基准测试的独立模块
