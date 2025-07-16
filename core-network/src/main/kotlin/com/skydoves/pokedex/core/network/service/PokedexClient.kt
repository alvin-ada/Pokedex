/*
 * PokedexClient.kt - Pokemon API 客户端
 * 
 * 🎯 作用：封装 Pokemon API 调用的业务逻辑
 * 📱 模块：core-network - 网络层
 * 🔗 功能：为 Repository 提供便捷的网络调用方法
 */

package com.skydoves.pokedex.core.network.service

import com.skydoves.pokedex.core.model.PokemonInfo
import com.skydoves.pokedex.core.network.model.PokemonResponse
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

// 🔥 网络客户端类
// 📌 作用：包装 Retrofit 服务，提供业务级别的网络调用
// 📌 好处：隐藏网络实现细节，提供更友好的API
// 📌 依赖注入：通过 Hilt 自动注入
class PokedexClient @Inject constructor(
  private val pokedexService: PokedexService,
) {

  // 🔥 获取 Pokemon 列表
  // 📌 参数：页码（从0开始）
  // 📌 逻辑：自动计算 limit 和 offset
  suspend fun fetchPokemonList(page: Int): ApiResponse<PokemonResponse> =
    pokedexService.fetchPokemonList(
      limit = PAGING_SIZE,          // 📌 每页固定20个
      offset = page * PAGING_SIZE,  // 📌 计算偏移量
    )
  // 💡 用法：Repository 调用此方法获取指定页面的 Pokemon

  // 🔥 获取 Pokemon 详细信息
  // 📌 参数：Pokemon 名称
  // 📌 直接调用：不需要额外的业务逻辑
  suspend fun fetchPokemonInfo(name: String): ApiResponse<PokemonInfo> =
    pokedexService.fetchPokemonInfo(
      name = name,
    )
  // 💡 用法：Repository 调用此方法获取 Pokemon 详情

  // 🔥 常量定义
  companion object {
    // 📌 分页大小：每页显示的 Pokemon 数量
    private const val PAGING_SIZE = 20
  }
}
