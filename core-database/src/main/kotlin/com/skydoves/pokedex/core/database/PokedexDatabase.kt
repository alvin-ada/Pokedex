/*
 * PokedexDatabase.kt - Pokedex 应用的主数据库类
 * 
 * 🎯 作用：定义应用的 Room 数据库配置
 * 📱 模块：core-database - 数据库层
 * 🔗 依赖：包含 Pokemon 相关的所有数据表
 */

package com.skydoves.pokedex.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.skydoves.pokedex.core.database.entity.PokemonEntity
import com.skydoves.pokedex.core.database.entity.PokemonInfoEntity

// 🔥 @Database 注解详解
// 📌 entities：指定数据库中包含的所有实体类（数据表）
// 📌 version：数据库版本号，用于数据库升级和迁移
// 📌 exportSchema：是否导出数据库架构到 JSON 文件（用于版本控制）
@Database(
  entities = [PokemonEntity::class, PokemonInfoEntity::class],
  version = 2,
  exportSchema = true,
)
// 🔥 @TypeConverters 注解详解
// 📌 作用：处理 Room 不支持的数据类型转换
// 📌 用途：将复杂对象（如 List<TypeResponse>）转换为 Room 可存储的类型
@TypeConverters(value = [TypeResponseConverter::class])
abstract class PokedexDatabase : RoomDatabase() {
  // 继承 RoomDatabase：Room 数据库的基类

  // 🔥 DAO（数据访问对象）提供方法
  // 📌 作用：获取各个数据表的操作接口
  // 📌 抽象方法：Room 会自动生成实现
  abstract fun pokemonDao(): PokemonDao
  // 💡 用法：提供 Pokemon 列表的增删改查操作
  
  abstract fun pokemonInfoDao(): PokemonInfoDao
  // 💡 用法：提供 Pokemon 详细信息的增删改查操作
}
