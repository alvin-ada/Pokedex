/*
 * Pokemon.kt - Pokemon 基础数据模型
 * 
 * 🎯 作用：定义 Pokemon 的基本数据结构
 * 📱 模块：core-model - 数据模型层
 * 🔗 功能：表示 Pokemon 列表中的单个项目
 */

package com.skydoves.pokedex.core.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

// 🔥 数据类注解详解
// 📌 @Parcelize：自动生成 Parcelable 实现，支持 Activity 间传递
// 📌 @JsonClass：Moshi 注解，自动生成 JSON 序列化代码
// 📌 generateAdapter = true：生成性能优化的适配器
@Parcelize
@JsonClass(generateAdapter = true)
data class Pokemon(
  // 🔥 分页信息
  // 📌 作用：记录这个 Pokemon 来自第几页
  // 📌 用途：支持分页加载和数据管理
  var page: Int = 0,
  
  // 🔥 JSON 字段映射
  // 📌 @field:Json：将 JSON 字段映射到 Kotlin 属性
  // 📌 name：Pokemon 的名称（如 "bulbasaur"）
  @field:Json(name = "name")
  val name: String,
  
  // 📌 url：Pokemon 详细信息的 API 地址
  @field:Json(name = "url") 
  val url: String,
) : Parcelable {
  // 继承 Parcelable：支持在 Activity 间传递对象

  // 🔥 获取 Pokemon 图片 URL
  // 📌 作用：从 API URL 中提取 Pokemon ID，构建图片地址
  // 📌 算法：解析 URL 最后一个路径段作为 ID
  fun getImageUrl(): String {
    val index = url.split("/".toRegex()).dropLast(1).last()
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
      "pokemon/other/official-artwork/$index.png"
  }
  // 💡 用法：在列表中显示 Pokemon 图片
  
  // 🔥 格式化 Pokemon 名称
  // 📌 作用：将首字母大写，用于显示
  // 📌 例子：bulbasaur → Bulbasaur
  fun name(): String = name.replaceFirstChar { it.uppercase() }
  // �� 用法：在UI中显示更友好的名称
}
