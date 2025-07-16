/*
 * DetailRepository.kt - 详情页面数据仓库接口
 * 
 * 🎯 作用：定义 Pokemon 详细信息获取的抽象接口
 * 📱 模块：core-data - 数据层
 * 🔗 功能：获取单个 Pokemon 详细信息的规范
 */

package com.skydoves.pokedex.core.repository

import androidx.annotation.WorkerThread
import com.skydoves.pokedex.core.model.PokemonInfo
import kotlinx.coroutines.flow.Flow

// 🔥 详情页面 Repository 接口
// 📌 作用：专门处理 Pokemon 详细信息的数据获取
// 📌 设计原则：单一职责，只负责详情数据
// 📌 好处：与主页面数据分离，便于维护
interface DetailRepository {

  // 🔥 获取 Pokemon 详细信息
  // 📌 参数：Pokemon 名称作为唯一标识
  // 📌 回调：完成和错误回调
  // 📌 返回：PokemonInfo 的 Flow
  @WorkerThread
  fun fetchPokemonInfo(
    name: String,                 // 📌 Pokemon 名称
    onComplete: () -> Unit,       // 📌 完成回调
    onError: (String?) -> Unit,   // 📌 错误回调
  ): Flow<PokemonInfo>
  // 💡 用法：详情页面 ViewModel 调用此方法获取 Pokemon 详情
}
