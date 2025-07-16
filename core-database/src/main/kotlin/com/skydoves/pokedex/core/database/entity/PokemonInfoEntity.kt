/*
 * PokemonInfoEntity.kt - Pokemon 详细信息实体类
 * 
 * 🎯 作用：定义 Pokemon 详细信息的数据库表结构
 * 📱 模块：core-database - 数据库层
 * 🔗 功能：缓存 Pokemon 的完整属性信息
 */

package com.skydoves.pokedex.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skydoves.pokedex.core.model.PokemonInfo

// 🔥 @Entity 注解详解
// 📌 作用：定义 Pokemon 详细信息的数据库表
// 📌 特点：包含 Pokemon 的所有属性信息
// 📌 用途：避免重复请求相同 Pokemon 的详细信息
@Entity
data class PokemonInfoEntity(
  // 🔥 Pokemon ID（主键）
  // 📌 作用：Pokemon 的唯一标识符
  // 📌 类型：Int，来自 Pokemon API
  @PrimaryKey val id: Int,
  
  // 🔥 Pokemon 基本信息
  // 📌 名称：Pokemon 的名字
  val name: String,
  // 📌 身高：单位是分米（1分米=0.1米）
  val height: Int,
  // 📌 体重：单位是百克（1百克=0.1公斤）
  val weight: Int,
  // 📌 基础经验值：Pokemon 的基础经验
  val experience: Int,
  
  // 🔥 Pokemon 类型信息
  // 📌 作用：存储 Pokemon 的类型（如草系、毒系等）
  // 📌 类型：List<TypeResponse>，需要类型转换器
  val types: List<PokemonInfo.TypeResponse>,
  // 💡 用法：显示 Pokemon 的属性类型标签
  
  // 🔥 Pokemon 战斗属性
  // 📌 HP：生命值（Health Points）
  val hp: Int,
  // 📌 攻击力：物理攻击强度
  val attack: Int,
  // 📌 防御力：物理防御强度
  val defense: Int,
  // 📌 速度：行动速度
  val speed: Int,
  // 📌 经验值：当前经验值
  val exp: Int,
  // 💡 用法：在详情页面显示 Pokemon 的战斗属性
)
