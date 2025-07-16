/*
 * DatabaseModule.kt - 数据库模块的依赖注入配置
 * 
 * 🎯 作用：配置 Room 数据库相关的依赖注入
 * 📱 模块：core-database - 数据库层
 * 🔗 功能：提供数据库、DAO、类型转换器的单例实例
 */

package com.skydoves.pokedex.core.database.di

import android.app.Application
import androidx.room.Room
import com.skydoves.pokedex.core.database.PokedexDatabase
import com.skydoves.pokedex.core.database.PokemonDao
import com.skydoves.pokedex.core.database.PokemonInfoDao
import com.skydoves.pokedex.core.database.TypeResponseConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// 🔥 Hilt 模块注解详解
// 📌 @Module：标记这是一个 Hilt 依赖注入模块
// 📌 @InstallIn：指定模块的安装范围
// 📌 SingletonComponent：应用级别的单例组件
@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

  // 🔥 提供 Moshi JSON 解析器
  // 📌 作用：用于 JSON 数据的序列化和反序列化
  // 📌 @Singleton：确保整个应用只有一个实例
  // 📌 KotlinJsonAdapterFactory：支持 Kotlin 数据类
  @Provides
  @Singleton
  fun provideMoshi(): Moshi {
    return Moshi.Builder()
      .addLast(KotlinJsonAdapterFactory())
      .build()
  }
  // 💡 用法：在类型转换器中转换复杂对象

  // 🔥 提供 Room 数据库实例
  // 📌 作用：创建和配置应用的主数据库
  // 📌 参数：Application 上下文和类型转换器
  @Provides
  @Singleton
  fun provideAppDatabase(
    application: Application,
    typeResponseConverter: TypeResponseConverter,
  ): PokedexDatabase {
    return Room
      .databaseBuilder(application, PokedexDatabase::class.java, "Pokedex.db")
      // 🔥 数据库配置详解
      // 📌 fallbackToDestructiveMigration：数据库升级时允许破坏性迁移
      // 📌 addTypeConverter：添加自定义类型转换器
      .fallbackToDestructiveMigration()
      .addTypeConverter(typeResponseConverter)
      .build()
  }
  // 💡 用法：整个应用的数据库实例

  // 🔥 提供 Pokemon DAO
  // 📌 作用：获取 Pokemon 数据表的操作接口
  // 📌 依赖：从数据库实例中获取
  @Provides
  @Singleton
  fun providePokemonDao(appDatabase: PokedexDatabase): PokemonDao {
    return appDatabase.pokemonDao()
  }
  // 💡 用法：在 Repository 中进行 Pokemon 数据的增删改查

  // 🔥 提供 Pokemon 详细信息 DAO
  // 📌 作用：获取 Pokemon 详细信息表的操作接口
  // 📌 依赖：从数据库实例中获取
  @Provides
  @Singleton
  fun providePokemonInfoDao(appDatabase: PokedexDatabase): PokemonInfoDao {
    return appDatabase.pokemonInfoDao()
  }
  // 💡 用法：在 Repository 中缓存和获取 Pokemon 详细信息

  // 🔥 提供类型转换器
  // 📌 作用：处理 Room 不支持的数据类型转换
  // 📌 依赖：需要 Moshi 实例进行 JSON 转换
  @Provides
  @Singleton
  fun provideTypeResponseConverter(moshi: Moshi): TypeResponseConverter {
    return TypeResponseConverter(moshi)
  }
  // 💡 用法：将 List<TypeResponse> 转换为 JSON 字符串存储
}
