/*
 * PokemonEntity.kt - Pokemon 实体类
 * 
 * 🎯 作用：定义 Pokemon 数据表的结构
 * 📱 模块：core-database - 数据库层
 * 🔗 功能：映射 Pokemon 基本信息到数据库表
 */

package com.skydoves.pokedex.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 🔥 @Entity 注解详解
// 📌 作用：标记这是一个 Room 数据库实体类
// 📌 特点：Room 会自动创建对应的数据库表
// 📌 表名：默认使用类名作为表名
@Entity
data class PokemonEntity(
  // 🔥 分页信息
  // 📌 作用：记录这个 Pokemon 属于哪一页
  // 📌 用途：支持分页加载功能
  var page: Int = 0,
  
  // 🔥 @PrimaryKey 注解详解
  // 📌 作用：标记这是数据库表的主键
  // 📌 特点：保证每个 Pokemon 名称唯一
  // 📌 类型：String 类型，Pokemon 的名称
  @PrimaryKey val name: String,
  // 💡 用法：作为唯一标识，防止重复数据
  
  // 🔥 Pokemon API URL
  // 📌 作用：存储 Pokemon 的详细信息 API 地址
  // 📌 用途：点击 Pokemon 时，可以通过这个 URL 获取详细信息
  val url: String,
  // 💡 用法：例如 "https://pokeapi.co/api/v2/pokemon/1/"
)
