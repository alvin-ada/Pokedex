/*
 * DetailViewModel.kt - 详情页面的视图模型
 * 
 * 🎯 作用：管理 Pokemon 详情页面的数据和业务逻辑
 * 📱 模块：app - 应用程序主模块
 * 🔗 功能：获取并提供 Pokemon 的详细信息
 */

package com.skydoves.pokedex.ui.details

import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.asBindingProperty
import com.skydoves.bindables.bindingProperty
import com.skydoves.pokedex.core.model.PokemonInfo
import com.skydoves.pokedex.core.repository.DetailRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

// 🔥 详情页面 ViewModel
// 📌 @AssistedInject：支持运行时参数的依赖注入
// 📌 构造函数：接收 DetailRepository 和 Pokemon 名称
// 📌 继承：BindingViewModel 支持 DataBinding
class DetailViewModel @AssistedInject constructor(
  detailRepository: DetailRepository,  // 📌 详情数据仓库
  @Assisted private val pokemonName: String,  // 📌 Pokemon 名称（运行时参数）
) : BindingViewModel() {

  // 🔥 加载状态属性
  // 📌 @get:Bindable：支持 DataBinding
  // 📌 bindingProperty：可观察属性
  // 📌 初始值：true（开始时显示加载状态）
  @get:Bindable
  var isLoading: Boolean by bindingProperty(true)
    private set
    // 💡 用法：在 XML 中控制加载指示器的显示

  // 🔥 错误消息属性
  // 📌 作用：存储网络请求或其他错误的消息
  // 📌 类型：String?（可能为空）
  @get:Bindable
  var toastMessage: String? by bindingProperty(null)
    private set
    // 💡 用法：在 XML 中通过 toast 绑定适配器显示错误

  // 🔥 Pokemon 详细信息 Flow
  // 📌 作用：从 Repository 获取 Pokemon 详细信息
  // 📌 参数：Pokemon 名称用于查询
  // 📌 回调：onComplete 和 onError 更新 UI 状态
  private val pokemonInfoFlow: Flow<PokemonInfo?> = detailRepository.fetchPokemonInfo(
    name = pokemonName,                    // 📌 查询的 Pokemon 名称
    onComplete = { isLoading = false },    // 📌 请求完成时隐藏加载状态
    onError = { toastMessage = it },       // 📌 请求失败时显示错误消息
  )

  // 🔥 Pokemon 详细信息属性
  // 📌 asBindingProperty：将 Flow 转换为 DataBinding 属性
  // 📌 viewModelScope：ViewModel 的协程作用域
  // 📌 默认值：null（数据加载前为空）
  @get:Bindable
  val pokemonInfo: PokemonInfo? by pokemonInfoFlow.asBindingProperty(viewModelScope, null)
  // 💡 用法：在 XML 中使用 @{vm.pokemonInfo.name} 显示 Pokemon 信息

  // 🔥 初始化块
  init {
    Timber.d("init DetailViewModel")
    // 📌 Timber：日志记录库
    // 📌 作用：记录 ViewModel 的创建，便于调试
    // 📌 级别：Debug 级别，只在调试版本输出
  }

  // 🔥 辅助工厂接口
  // 📌 @AssistedFactory：Hilt 辅助工厂
  // 📌 作用：创建带有运行时参数的 ViewModel
  // 📌 参数：pokemonName 在运行时传入
  @dagger.assisted.AssistedFactory
  interface AssistedFactory {
    fun create(pokemonName: String): DetailViewModel
  }

  // 🔥 伴生对象 - ViewModelProvider.Factory
  companion object {
    // 🔥 提供自定义 ViewModel 工厂
    // 📌 作用：为 by viewModels() 提供自定义工厂
    // 📌 参数：辅助工厂和 Pokemon 名称
    // 📌 返回：ViewModelProvider.Factory 实例
    fun provideFactory(
      assistedFactory: AssistedFactory,
      pokemonName: String,
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {

      // 🔥 创建 ViewModel 实例
      // 📌 @Suppress：抑制未检查的类型转换警告
      // 📌 作用：使用辅助工厂创建 ViewModel
      @Suppress("UNCHECKED_CAST")
      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return assistedFactory.create(pokemonName) as T
      }
    }
    // 💡 用法：在 DetailActivity 中使用此工厂创建 ViewModel
  }
}
