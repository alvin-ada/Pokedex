/*
 * PokemonResponse.kt - Pokemon API 响应数据模型
 * 
 * 🎯 作用：定义 Pokemon 列表 API 的响应结构
 * 📱 模块：core-network - 网络层
 * 🔗 功能：映射 Pokemon API 的分页响应数据
 */

package com.skydoves.pokedex.core.network.model

import com.skydoves.pokedex.core.model.Pokemon
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// 🔥 API 响应数据模型
// 📌 作用：映射 Pokemon API 的 JSON 响应
// 📌 特点：包含分页信息和 Pokemon 列表
// 📌 @JsonClass：自动生成 JSON 解析代码
@JsonClass(generateAdapter = true)
data class PokemonResponse(
  // 🔥 分页信息
  @field:Json(name = "count") 
  val count: Int,           // 📌 总数量：所有 Pokemon 的数量
  
  @field:Json(name = "next") 
  val next: String?,        // 📌 下一页：下一页的 URL（可能为空）
  
  @field:Json(name = "previous") 
  val previous: String?,    // 📌 上一页：上一页的 URL（可能为空）
  
  @field:Json(name = "results") 
  val results: List<Pokemon>,  // 📌 结果列表：当前页的 Pokemon 列表
)
// 💡 用法：从 API 获取 Pokemon 列表时的响应格式
