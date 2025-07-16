/*
 * DetailActivity.kt - Pokemon 详情页面活动
 * 
 * 🎯 作用：展示单个 Pokemon 的详细信息
 * 📱 模块：app - 应用程序主模块
 * 🔗 功能：显示 Pokemon 的完整属性、类型、战斗数据等
 */

package com.skydoves.pokedex.ui.details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import com.skydoves.bindables.BindingActivity
import com.skydoves.bundler.bundleNonNull
import com.skydoves.bundler.intentOf
import com.skydoves.pokedex.R
import com.skydoves.pokedex.core.model.Pokemon
import com.skydoves.pokedex.databinding.ActivityDetailBinding
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationCompat.onTransformationEndContainerApplyParams
import com.skydoves.transformationlayout.TransformationLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// 🔥 详情页面 Activity
// 📌 @AndroidEntryPoint：启用 Hilt 依赖注入
// 📌 继承 BindingActivity：支持 DataBinding
// 📌 布局：activity_detail.xml
@AndroidEntryPoint
class DetailActivity : BindingActivity<ActivityDetailBinding>(R.layout.activity_detail) {

  // 🔥 ViewModel 工厂注入
  // 📌 @Inject：Hilt 依赖注入
  // 📌 AssistedFactory：支持运行时参数的 ViewModel 工厂
  // 📌 用途：传递 Pokemon 名称给 ViewModel
  @Inject
  internal lateinit var detailViewModelFactory: DetailViewModel.AssistedFactory

  // 🔥 ViewModel 创建
  // 📌 @get:VisibleForTesting：测试可见性
  // 📌 by viewModels：使用自定义工厂创建 ViewModel
  // 📌 参数：pokemon.name 传递给 ViewModel
  @get:VisibleForTesting
  internal val viewModel: DetailViewModel by viewModels {
    DetailViewModel.provideFactory(detailViewModelFactory, pokemon.name)
  }

  // 🔥 Pokemon 对象获取
  // 📌 bundleNonNull：从 Intent 中获取非空的 Pokemon 对象
  // 📌 EXTRA_POKEMON：Intent 参数的键名
  // 📌 类型：Pokemon 对象，从主页面传递过来
  private val pokemon: Pokemon by bundleNonNull(EXTRA_POKEMON)

  // 🔥 Activity 创建时的初始化
  override fun onCreate(savedInstanceState: Bundle?) {
    // 🔥 变换动画配置
    // 📌 作用：处理共享元素动画的结束
    // 📌 用途：从主页面到详情页面的过渡动画
    onTransformationEndContainerApplyParams(this)
    super.onCreate(savedInstanceState)
    
    // 🔥 DataBinding 配置
    binding.pokemon = pokemon    // 📌 绑定 Pokemon 对象到布局
    binding.vm = viewModel       // 📌 绑定 ViewModel 到布局
    // 💡 用法：XML 中可以使用 @{pokemon.name} 和 @{vm.pokemonInfo} 访问数据
  }

  // 🔥 伴生对象 - 静态方法和常量
  companion object {
    // 🔥 Intent 参数键名
    // 📌 @VisibleForTesting：测试可见性
    // 📌 作用：Intent 中 Pokemon 对象的键名
    @VisibleForTesting
    internal const val EXTRA_POKEMON = "EXTRA_POKEMON"

    // 🔥 启动详情页面的便捷方法
    // 📌 参数：TransformationLayout 用于共享元素动画
    // 📌 参数：Pokemon 对象包含基本信息
    // 📌 返回：无返回值，直接启动 Activity
    fun startActivity(transformationLayout: TransformationLayout, pokemon: Pokemon) =
      transformationLayout.context.intentOf<DetailActivity> {
        putExtra(EXTRA_POKEMON to pokemon)  // 📌 传递 Pokemon 对象
        // 🔥 启动变换动画
        // 📌 作用：执行共享元素动画
        // 📌 效果：从列表项到详情页面的平滑过渡
        TransformationCompat.startActivity(transformationLayout, intent)
      }
    // 💡 用法：在 PokemonAdapter 中点击列表项时调用
  }
}
