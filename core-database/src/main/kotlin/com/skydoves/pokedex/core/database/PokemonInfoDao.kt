/*
 * PokemonInfoDao.kt - Pokemon 详细信息数据访问对象
 * 
 * 🎯 作用：定义 Pokemon 详细信息的数据库操作
 * 📱 模块：core-database - 数据库层
 * 🔗 功能：缓存 Pokemon 的详细信息数据
 */

package com.skydoves.pokedex.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skydoves.pokedex.core.database.entity.PokemonInfoEntity

// 🔥 @Dao 注解详解
// 📌 作用：标记这是 Pokemon 详细信息的数据访问对象
// 📌 特点：专门处理单个 Pokemon 的详细信息
// 📌 用途：避免重复请求相同的 Pokemon 详情
@Dao
interface PokemonInfoDao {

  // 🔥 插入 Pokemon 详细信息
  // 📌 OnConflictStrategy.REPLACE：如果已存在同名 Pokemon，替换数据
  // 📌 用途：缓存从网络获取的 Pokemon 详细信息
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPokemonInfo(pokemonInfo: PokemonInfoEntity)
  // 💡 用法：当用户点击 Pokemon 获取详情后，缓存到本地

  // 🔥 查询 Pokemon 详细信息
  // 📌 根据 Pokemon 名称查询：name 是唯一标识
  // 📌 返回类型：PokemonInfoEntity?（可能为空）
  @Query("SELECT * FROM PokemonInfoEntity WHERE name = :name_")
  suspend fun getPokemonInfo(name_: String): PokemonInfoEntity?
  // 💡 用法：用户点击 Pokemon 时，先检查本地是否有缓存的详细信息
}
