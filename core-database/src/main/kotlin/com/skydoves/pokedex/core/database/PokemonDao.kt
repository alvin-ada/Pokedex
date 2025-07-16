/*
 * PokemonDao.kt - Pokemon 数据访问对象
 * 
 * 🎯 作用：定义 Pokemon 数据表的操作方法
 * 📱 模块：core-database - 数据库层
 * 🔗 功能：增删改查 Pokemon 列表数据
 */

package com.skydoves.pokedex.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skydoves.pokedex.core.database.entity.PokemonEntity

// 🔥 @Dao 注解详解
// 📌 作用：标记这是一个数据访问对象（Data Access Object）
// 📌 特点：Room 会自动生成实现类
// 📌 用途：定义数据库操作的接口方法
@Dao
interface PokemonDao {

  // 🔥 @Insert 注解详解
  // 📌 作用：插入数据到数据库
  // 📌 OnConflictStrategy.REPLACE：如果主键冲突，替换原有数据
  // 📌 suspend：协程支持，避免阻塞主线程
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPokemonList(pokemonList: List<PokemonEntity>)
  // 💡 用法：批量插入从网络获取的 Pokemon 数据

  // 🔥 @Query 注解详解
  // 📌 作用：执行 SQL 查询语句
  // 📌 参数绑定：使用 :参数名 的方式绑定参数
  // 📌 类型安全：编译时检查 SQL 语法
  @Query("SELECT * FROM PokemonEntity WHERE page = :page_")
  suspend fun getPokemonList(page_: Int): List<PokemonEntity>
  // 💡 用法：获取指定页面的 Pokemon 数据（用于分页加载）

  @Query("SELECT * FROM PokemonEntity WHERE page <= :page_")
  suspend fun getAllPokemonList(page_: Int): List<PokemonEntity>
  // 💡 用法：获取从第一页到指定页面的所有 Pokemon 数据（累积加载）
}
