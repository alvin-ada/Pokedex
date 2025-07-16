/*
 * NetworkModule.kt - 网络模块的依赖注入配置
 * 
 * 🎯 作用：配置网络相关的依赖注入
 * 📱 模块：core-network - 网络层
 * 🔗 功能：提供 HTTP 客户端、Retrofit、服务等实例
 */

package com.skydoves.pokedex.core.network.di

import com.skydoves.pokedex.core.network.interceptor.HttpRequestInterceptor
import com.skydoves.pokedex.core.network.service.PokedexClient
import com.skydoves.pokedex.core.network.service.PokedexService
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

// 🔥 网络模块 Hilt 配置
// 📌 @Module：标记为 Hilt 模块
// 📌 @InstallIn：安装到应用级别的单例组件
// 📌 object：单例对象，包含所有网络相关的依赖
@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

  // 🔥 提供 OkHttp 客户端
  // 📌 作用：配置 HTTP 客户端
  // 📌 拦截器：添加请求拦截器用于日志记录
  @Provides
  @Singleton
  fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor(HttpRequestInterceptor())  // 📌 添加请求拦截器
      .build()
  }
  // 💡 用法：为 Retrofit 提供 HTTP 客户端

  // 🔥 提供 Retrofit 实例
  // 📌 作用：配置 Retrofit 网络库
  // 📌 baseUrl：Pokemon API 的基础地址
  // 📌 转换器：Moshi JSON 转换器
  // 📌 适配器：ApiResponse 调用适配器
  @Provides
  @Singleton
  fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .client(okHttpClient)                                    // 📌 使用配置的 HTTP 客户端
      .baseUrl("https://pokeapi.co/api/v2/")                  // 📌 Pokemon API 基础URL
      .addConverterFactory(MoshiConverterFactory.create())    // 📌 JSON 转换器
      .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())  // 📌 API 响应适配器
      .build()
  }
  // 💡 用法：为服务接口提供网络实现

  // 🔥 提供 Pokemon 服务
  // 📌 作用：创建 Pokemon API 服务实例
  // 📌 方式：通过 Retrofit 动态代理创建
  @Provides
  @Singleton
  fun providePokedexService(retrofit: Retrofit): PokedexService {
    return retrofit.create(PokedexService::class.java)
  }
  // 💡 用法：为客户端提供 API 服务

  // 🔥 提供 Pokemon 客户端
  // 📌 作用：创建 Pokemon API 客户端实例
  // 📌 依赖：需要 Pokemon 服务实例
  @Provides
  @Singleton
  fun providePokedexClient(pokedexService: PokedexService): PokedexClient {
    return PokedexClient(pokedexService)
  }
  // 💡 用法：为 Repository 提供网络客户端
}
