/*
 * PokemonInfo.kt - Pokemon 详细信息数据模型
 * 
 * 🎯 作用：定义 Pokemon 的完整详细信息结构
 * 📱 模块：core-model - 数据模型层
 * 🔗 功能：表示 Pokemon 的所有属性和战斗数据
 */

package com.skydoves.pokedex.core.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlin.random.Random

// 🔥 Pokemon 详细信息数据类
// 📌 @JsonClass：支持 JSON 序列化和反序列化
// 📌 特点：包含 Pokemon 的所有详细属性
@JsonClass(generateAdapter = true)
data class PokemonInfo(
  // 🔥 Pokemon 基本信息
  @field:Json(name = "id")
  val id: Int,                    // 📌 Pokemon ID
  
  @field:Json(name = "name") 
  val name: String,               // 📌 Pokemon 名称
  
  @field:Json(name = "height") 
  val height: Int,                // 📌 身高（分米）
  
  @field:Json(name = "weight") 
  val weight: Int,                // 📌 体重（百克）
  
  @field:Json(name = "base_experience") 
  val experience: Int,            // 📌 基础经验值
  
  @field:Json(name = "types") 
  val types: List<TypeResponse>,  // 📌 Pokemon 类型列表
  
  // 🔥 Pokemon 战斗属性（随机生成）
  // 📌 原因：API 不提供这些数据，使用随机值模拟
  val hp: Int = Random.nextInt(MAX_HP),           // 📌 生命值
  val attack: Int = Random.nextInt(MAX_ATTACK),   // 📌 攻击力
  val defense: Int = Random.nextInt(MAX_DEFENSE), // 📌 防御力
  val speed: Int = Random.nextInt(MAX_SPEED),     // 📌 速度
  val exp: Int = Random.nextInt(MAX_EXP),         // 📌 经验值
) {

  // 🔥 数据格式化方法
  // 📌 作用：将原始数据格式化为用户友好的显示格式
  
  // 📌 格式化 ID：显示为 #001 格式
  fun getIdString(): String = String.format("#%03d", id)
  
  // 📌 格式化体重：转换为公斤并保留一位小数
  fun getWeightString(): String = String.format("%.1f KG", weight.toFloat() / 10)
  
  // 📌 格式化身高：转换为米并保留一位小数
  fun getHeightString(): String = String.format("%.1f M", height.toFloat() / 10)
  
  // 🔥 战斗属性显示方法
  // 📌 格式：当前值/最大值
  fun getHpString(): String = " $hp/$MAX_HP"
  fun getAttackString(): String = " $attack/$MAX_ATTACK"
  fun getDefenseString(): String = " $defense/$MAX_DEFENSE"
  fun getSpeedString(): String = " $speed/$MAX_SPEED"
  fun getExpString(): String = " $exp/$MAX_EXP"
  // 💡 用法：在详情页面显示属性条

  // 🔥 内部数据类 - Pokemon 类型响应
  // 📌 作用：表示 Pokemon 的类型信息
  @JsonClass(generateAdapter = true)
  data class TypeResponse(
    @field:Json(name = "slot") 
    val slot: Int,        // 📌 类型槽位
    
    @field:Json(name = "type") 
    val type: Type,       // 📌 具体类型信息
  )

  // 🔥 内部数据类 - 类型详情
  // 📌 作用：表示单个类型的详细信息
  @JsonClass(generateAdapter = true)
  data class Type(
    @field:Json(name = "name") 
    val name: String,     // 📌 类型名称（如 "grass", "poison"）
  )

  // 🔥 常量定义
  // 📌 作用：定义各种属性的最大值
  // 📌 用途：用于随机生成属性值和显示进度条
  companion object {
    const val MAX_HP = 300
    const val MAX_ATTACK = 300
    const val MAX_DEFENSE = 300
    const val MAX_SPEED = 300
    const val MAX_EXP = 1000
  }
  // 💡 用法：在UI中显示属性进度条的满值
}
