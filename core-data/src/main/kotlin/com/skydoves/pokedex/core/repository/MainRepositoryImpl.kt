/*
 * MainRepositoryImpl.kt - 主页面数据仓库实现
 * 
 * 🎯 作用：实现主页面数据获取的具体逻辑
 * 📱 模块：core-data - 数据层
 * 🔗 功能：结合网络和本地数据库提供 Pokemon 数据
 */

package com.skydoves.pokedex.core.repository

import androidx.annotation.VisibleForTesting
import androidx.annotation.WorkerThread
import com.skydoves.pokedex.core.database.PokemonDao
import com.skydoves.pokedex.core.database.entity.mapper.asDomain
import com.skydoves.pokedex.core.database.entity.mapper.asEntity
import com.skydoves.pokedex.core.network.Dispatcher
import com.skydoves.pokedex.core.network.PokedexAppDispatchers
import com.skydoves.pokedex.core.network.service.PokedexClient
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

// 🔥 Repository 实现类详解
// 📌 @VisibleForTesting：标记这个类在测试中可见
// 📌 @Inject constructor：构造函数注入，Hilt 自动提供依赖
@VisibleForTesting
class MainRepositoryImpl @Inject constructor(
  // 🔥 依赖注入的组件
  private val pokedexClient: PokedexClient,           // 📌 网络客户端：从API获取数据
  private val pokemonDao: PokemonDao,                 // 📌 数据库DAO：本地数据操作
  @Dispatcher(PokedexAppDispatchers.IO)               // 📌 IO调度器：处理网络和数据库操作
  private val ioDispatcher: CoroutineDispatcher,
) : MainRepository {

  // 🔥 数据获取策略详解
  // 📌 策略：本地优先，网络补充
  // 📌 流程：先查本地 → 如果为空则网络请求 → 缓存到本地 → 返回数据
  @WorkerThread
  override fun fetchPokemonList(
    page: Int,
    onStart: () -> Unit,
    onComplete: () -> Unit,
    onError: (String?) -> Unit,
  ) = flow {
    // 🔥 第一步：从本地数据库获取数据
    // 📌 asDomain()：将数据库实体转换为业务模型
    var pokemons = pokemonDao.getPokemonList(page).asDomain()
    
    if (pokemons.isEmpty()) {
      // 🔥 第二步：本地没有数据，从网络获取
      // 📌 suspendOnSuccess：成功时执行的挂起函数
      // 📌 Sandwich 库：优雅的 API 响应处理
      val response = pokedexClient.fetchPokemonList(page = page)
      response.suspendOnSuccess {
        // 🔥 网络请求成功的处理
        pokemons = data.results                    // 📌 获取响应数据
        pokemons.forEach { pokemon -> 
          pokemon.page = page                      // 📌 设置页码信息
        }
        pokemonDao.insertPokemonList(pokemons.asEntity())  // 📌 缓存到本地
        emit(pokemonDao.getAllPokemonList(page).asDomain()) // 📌 发射累积数据
      }.onFailure { 
        // 🔥 网络请求失败的处理
        onError(message())                         // 📌 通知UI显示错误
      }
    } else {
      // 🔥 第三步：本地有数据，直接返回
      emit(pokemonDao.getAllPokemonList(page).asDomain())
    }
  }
  // 🔥 Flow 操作符详解
  .onStart { onStart() }      // 📌 开始时的回调
  .onCompletion { onComplete() }  // 📌 完成时的回调
  .flowOn(ioDispatcher)       // 📌 在IO线程执行
  // �� 用法：确保数据获取不阻塞主线程
}
