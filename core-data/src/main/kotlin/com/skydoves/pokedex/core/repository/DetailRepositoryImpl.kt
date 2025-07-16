/*
 * DetailRepositoryImpl.kt - 详情页面数据仓库实现
 * 
 * 🎯 作用：实现 Pokemon 详细信息获取的具体逻辑
 * 📱 模块：core-data - 数据层
 * 🔗 功能：结合网络和本地数据库提供 Pokemon 详细信息
 */

package com.skydoves.pokedex.core.repository

import androidx.annotation.VisibleForTesting
import androidx.annotation.WorkerThread
import com.skydoves.pokedex.core.database.PokemonInfoDao
import com.skydoves.pokedex.core.database.entity.mapper.asDomain
import com.skydoves.pokedex.core.database.entity.mapper.asEntity
import com.skydoves.pokedex.core.network.Dispatcher
import com.skydoves.pokedex.core.network.PokedexAppDispatchers
import com.skydoves.pokedex.core.network.model.mapper.ErrorResponseMapper
import com.skydoves.pokedex.core.network.service.PokedexClient
import com.skydoves.sandwich.map
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

// 🔥 详情页面 Repository 实现
// 📌 策略：本地缓存优先，避免重复网络请求
// 📌 特点：更详细的错误处理机制
@VisibleForTesting
class DetailRepositoryImpl @Inject constructor(
  // 🔥 依赖注入的组件
  private val pokedexClient: PokedexClient,           // 📌 网络客户端
  private val pokemonInfoDao: PokemonInfoDao,         // 📌 详情数据库DAO
  @Dispatcher(PokedexAppDispatchers.IO)               // 📌 IO调度器
  private val ioDispatcher: CoroutineDispatcher,
) : DetailRepository {

  // 🔥 获取 Pokemon 详细信息的实现
  // 📌 策略：先检查本地缓存，没有再请求网络
  @WorkerThread
  override fun fetchPokemonInfo(name: String, onComplete: () -> Unit, onError: (String?) -> Unit) =
    flow {
      // 🔥 第一步：检查本地缓存
      val pokemonInfo = pokemonInfoDao.getPokemonInfo(name)
      
      if (pokemonInfo == null) {
        // 🔥 第二步：本地没有，从网络获取
        val response = pokedexClient.fetchPokemonInfo(name = name)
        response.suspendOnSuccess {
          // 🔥 成功获取数据
          pokemonInfoDao.insertPokemonInfo(data.asEntity())  // 📌 缓存到本地
          emit(data)                                         // 📌 发射数据
        }
        // 🔥 错误处理 - API 响应错误
        .onError {
          // 📌 使用错误映射器处理API错误响应
          map(ErrorResponseMapper) { onError("[Code: $code]: $message") }
        }
        // 🔥 错误处理 - 网络异常
        .onException { 
          // 📌 处理网络连接异常
          onError(message) 
        }
      } else {
        // 🔥 第三步：本地有缓存，直接返回
        emit(pokemonInfo.asDomain())
      }
    }
    .onCompletion { onComplete() }  // 📌 完成时的回调
    .flowOn(ioDispatcher)           // 📌 在IO线程执行
    // 💡 用法：提供详情页面数据，支持离线查看
}
