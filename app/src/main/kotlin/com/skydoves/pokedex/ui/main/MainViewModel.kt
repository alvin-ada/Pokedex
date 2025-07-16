/*
 * MainViewModel.kt - 主页面的视图模型
 * 
 * 🎯 作用：管理主页面的数据和业务逻辑
 * 📱 MVVM模式：View(Activity) ↔ ViewModel ↔ Model(Repository)
 */

package com.skydoves.pokedex.ui.main

import androidx.annotation.MainThread
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.asBindingProperty
import com.skydoves.bindables.bindingProperty
import com.skydoves.pokedex.core.model.Pokemon
import com.skydoves.pokedex.core.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import timber.log.Timber
import javax.inject.Inject

// 🔥 @HiltViewModel 注解详解
// 📌 作用：告诉 Hilt 这是一个需要依赖注入的 ViewModel
// 📌 好处：Hilt 会自动创建这个 ViewModel 的实例，并注入所需的依赖
// 📌 生命周期：与 Activity/Fragment 绑定，自动管理生命周期
@HiltViewModel
class MainViewModel @Inject constructor(
  // 🔥 @Inject constructor 详解
  // 📌 作用：构造函数注入，Hilt 会自动提供 mainRepository 实例
  // 📌 好处：不需要手动创建 Repository，Hilt 自动管理依赖关系
  // 📌 类型：MainRepository 是数据层，负责获取 Pokemon 数据
  private val mainRepository: MainRepository,
) : BindingViewModel() {
  // 继承 BindingViewModel：支持 DataBinding 的 ViewModel 基类

  // 🔥 DataBinding 属性详解
  // 📌 @get:Bindable：告诉 DataBinding 这个属性可以在 XML 中使用
  // 📌 bindingProperty：创建可观察的属性，当值改变时会通知 UI 更新
  // 📌 private set：只能在 ViewModel 内部修改，外部只能读取
  @get:Bindable
  var isLoading: Boolean by bindingProperty(false)
    private set
    // 💡 用法：在 XML 中可以写 android:visibility="@{vm.isLoading ? View.VISIBLE : View.GONE}"

  @get:Bindable
  var toastMessage: String? by bindingProperty(null)
    private set
    // 💡 用法：用于显示错误消息或提示信息

  // 🔥 Flow 相关概念详解
  // 📌 MutableStateFlow：可变的状态流，类似于 LiveData 但更强大
  // 📌 作用：当页面索引改变时，会触发新的数据获取
  // 📌 初始值：0（第一页）
  private val pokemonFetchingIndex: MutableStateFlow<Int> = MutableStateFlow(0)
  
  // 🔥 flatMapLatest 操作符详解
  // 📌 作用：当 pokemonFetchingIndex 的值改变时，会取消之前的请求，开始新的请求
  // 📌 好处：避免多次快速点击导致的重复请求
  // 📌 流程：页面索引改变 → 取消旧请求 → 开始新请求 → 返回新数据
  private val pokemonListFlow = pokemonFetchingIndex.flatMapLatest { page ->
    // 当页面索引改变时，这个 lambda 会被调用
    mainRepository.fetchPokemonList(
      page = page,                        // 当前页面索引
      onStart = { isLoading = true },     // 开始请求时显示加载状态
      onComplete = { isLoading = false }, // 请求完成时隐藏加载状态
      onError = { toastMessage = it },    // 请求失败时显示错误消息
    )
    // 返回一个 Flow<List<Pokemon>>
  }

  // 🔥 asBindingProperty 详解
  // 📌 作用：将 Flow 转换为 DataBinding 可用的属性
  // 📌 viewModelScope：ViewModel 的协程作用域，ViewModel 销毁时自动取消
  // 📌 emptyList()：默认值，在数据加载完成前显示空列表
  @get:Bindable
  val pokemonList: List<Pokemon> by pokemonListFlow.asBindingProperty(viewModelScope, emptyList())
  // 💡 用法：在 XML 中可以绑定到 RecyclerView 的适配器

  // 🔥 初始化块
  init {
    Timber.d("init MainViewModel")
    // 📌 Timber：日志库，用于调试
    // 📌 作用：记录 ViewModel 的创建时间，便于调试
    // 📌 级别：d = debug，只在调试版本中输出
  }

  // 🔥 @MainThread 注解详解
  // 📌 作用：确保这个方法只在主线程中调用
  // 📌 原因：UI 相关操作必须在主线程进行
  // 📌 好处：Android Studio 会检查调用是否在主线程
  @MainThread
  fun fetchNextPokemonList() {
    // 防止重复请求的保护机制
    if (!isLoading) {
      // 🔥 Flow 更新机制
      // 📌 pokemonFetchingIndex.value++：修改 StateFlow 的值
      // 📌 触发链：值改变 → flatMapLatest 执行 → 新的网络请求 → UI 更新
      pokemonFetchingIndex.value++
    }
  }
}
