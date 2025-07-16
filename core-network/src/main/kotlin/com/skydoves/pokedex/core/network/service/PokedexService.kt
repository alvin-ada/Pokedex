/*
 * PokedexService.kt - Pokemon API 服务接口
 * 
 * 🎯 作用：定义与 Pokemon API 的通信接口
 * 📱 模块：core-network - 网络层
 * 🔗 功能：封装所有 Pokemon API 调用
 */

package com.skydoves.pokedex.core.network.service

import com.skydoves.pokedex.core.model.PokemonInfo
import com.skydoves.pokedex.core.network.model.PokemonResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// 🔥 Retrofit 服务接口
// 📌 作用：定义 RESTful API 的调用方法
// 📌 特点：使用 Retrofit 注解声明 HTTP 请求
// 📌 返回类型：ApiResponse<T>，支持统一的错误处理
interface PokedexService {

  // 🔥 获取 Pokemon 列表
  // 📌 @GET：HTTP GET 请求
  // 📌 路径：pokemon（相对于 base URL）
  // 📌 支持分页：通过 limit 和 offset 参数
  @GET("pokemon")
  suspend fun fetchPokemonList(
    @Query("limit") limit: Int = 20,     // 📌 每页数量，默认20
    @Query("offset") offset: Int = 0,    // 📌 偏移量，用于分页
  ): ApiResponse<PokemonResponse>
  // 💡 用法：获取 Pokemon 列表，支持分页加载

  // 🔥 获取 Pokemon 详细信息
  // 📌 @GET：HTTP GET 请求
  // 📌 @Path：路径参数，将 name 插入到 URL 中
  // 📌 路径：pokemon/{name}（如 pokemon/bulbasaur）
  @GET("pokemon/{name}")
  suspend fun fetchPokemonInfo(@Path("name") name: String): ApiResponse<PokemonInfo>
  // 💡 用法：根据 Pokemon 名称获取详细信息
}
