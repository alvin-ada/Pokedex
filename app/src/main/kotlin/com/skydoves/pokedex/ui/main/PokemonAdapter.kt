/*
 * PokemonAdapter.kt - Pokemon 列表适配器
 * 
 * 🎯 作用：管理 RecyclerView 中 Pokemon 列表的显示
 * 📱 模块：app - 应用程序主模块
 * 🔗 功能：展示 Pokemon 网格列表，处理点击事件和共享元素动画
 */

package com.skydoves.pokedex.ui.main

import android.os.SystemClock
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.skydoves.bindables.BindingListAdapter
import com.skydoves.bindables.binding
import com.skydoves.pokedex.R
import com.skydoves.pokedex.core.model.Pokemon
import com.skydoves.pokedex.databinding.ItemPokemonBinding
import com.skydoves.pokedex.ui.details.DetailActivity

// 🔥 Pokemon 适配器类
// 📌 继承：BindingListAdapter 支持 DataBinding 和 DiffUtil
// 📌 泛型：Pokemon 数据类型，PokemonViewHolder 视图持有者
// 📌 参数：diffUtil 用于高效的列表更新
class PokemonAdapter : BindingListAdapter<Pokemon, PokemonAdapter.PokemonViewHolder>(diffUtil) {

  // 🔥 防重复点击的时间戳
  // 📌 作用：防止用户快速连续点击导致的重复导航
  // 📌 类型：Long 类型的时间戳
  private var onClickedAt = 0L

  // 🔥 创建 ViewHolder
  // 📌 作用：为每个列表项创建视图持有者
  // 📌 参数：parent 是 RecyclerView，viewType 是视图类型
  // 📌 返回：PokemonViewHolder 实例
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder =
    parent.binding<ItemPokemonBinding>(R.layout.item_pokemon).let(::PokemonViewHolder)
  // 💡 用法：RecyclerView 内部调用，创建列表项视图

  // 🔥 绑定数据到 ViewHolder
  // 📌 作用：将 Pokemon 数据绑定到视图
  // 📌 参数：holder 是视图持有者，position 是位置
  override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) =
    holder.bindPokemon(getItem(position))
  // 💡 用法：RecyclerView 内部调用，更新列表项显示

  // 🔥 内部 ViewHolder 类
  // 📌 作用：持有单个 Pokemon 列表项的视图
  // 📌 参数：ItemPokemonBinding 是 DataBinding 生成的绑定类
  inner class PokemonViewHolder constructor(
    private val binding: ItemPokemonBinding,
  ) : RecyclerView.ViewHolder(binding.root) {

    // 🔥 初始化块 - 设置点击监听
    init {
      // 🔥 设置列表项点击监听器
      binding.root.setOnClickListener {
        // 🔥 获取当前项的位置
        // 📌 bindingAdapterPosition：获取当前位置
        // 📌 takeIf：如果位置有效则返回，否则返回 null
        // 📌 NO_POSITION：无效位置常量
        val position = bindingAdapterPosition.takeIf { it != NO_POSITION }
          ?: return@setOnClickListener

        // 🔥 防重复点击检查
        // 📌 SystemClock.elapsedRealtime()：获取系统启动后经过的时间
        // 📌 作用：防止快速连续点击
        val currentClickedAt = SystemClock.elapsedRealtime()
        if (currentClickedAt - onClickedAt > binding.transformationLayout.duration) {
          // 🔥 启动详情页面
          // 📌 DetailActivity.startActivity：启动详情页面
          // 📌 transformationLayout：用于共享元素动画
          // 📌 getItem(position)：获取当前位置的 Pokemon 对象
          DetailActivity.startActivity(binding.transformationLayout, getItem(position))
          onClickedAt = currentClickedAt  // 📌 更新点击时间戳
        }
      }
    }

    // 🔥 绑定 Pokemon 数据
    // 📌 作用：将 Pokemon 对象绑定到视图
    // 📌 参数：pokemon 是要显示的 Pokemon 对象
    fun bindPokemon(pokemon: Pokemon) {
      binding.pokemon = pokemon        // 📌 设置 DataBinding 变量
      binding.executePendingBindings() // 📌 立即执行绑定更新
    }
    // 💡 用法：在 onBindViewHolder 中调用，更新列表项显示
  }

  // 🔥 伴生对象 - DiffUtil 回调
  companion object {
    // 🔥 DiffUtil 回调实现
    // 📌 作用：高效计算列表变化，只更新有变化的项目
    // 📌 类型：DiffUtil.ItemCallback<Pokemon>
    private val diffUtil = object : DiffUtil.ItemCallback<Pokemon>() {

      // 🔥 判断是否是同一个项目
      // 📌 作用：比较两个 Pokemon 是否代表同一个对象
      // 📌 标准：通过名称判
      override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
        oldItem.name == newItem.name

      override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
        oldItem == newItem
    }
  }
}