/*
 * StoreModule.kt - 存储模块的依赖注入配置
 * 
 * 🎯 作用：配置存储相关的依赖注入
 * 📱 模块：core-store - 存储层
 * 🔗 功能：提供存储策略、管理器等实例
 */

package com.skydoves.pokedex.core.di

import android.content.Context
import com.skydoves.pokedex.core.store.strategy.StoreStrategy
import com.skydoves.pokedex.core.store.strategy.mmkv.MMKVStore
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 🔥 存储模块 Hilt 配置
 * 📌 @Module：标记为 Hilt 模块
 * 📌 @InstallIn：安装到应用级别的单例组件
 * 📌 object：单例对象，包含所有存储相关的依赖
 * 📌 注意：使用DatabaseModule中提供的Moshi实例，避免重复绑定
 */
@Module
@InstallIn(SingletonComponent::class)
internal object StoreModule {

    /**
     * 🔥 提供存储策略实现
     * 📌 作用：提供具体的存储策略实现
     * 📌 实现：使用 MMKV 作为默认存储引擎
     * 📌 依赖：使用DatabaseModule中提供的Moshi实例进行JSON序列化
     */
    @Provides
    @Singleton
    fun provideStoreStrategy(
        @ApplicationContext context: Context,
        moshi: Moshi
    ): StoreStrategy {
        val mmkvStore = MMKVStore(moshi)
        mmkvStore.init(context)  // 📌 初始化存储
        return mmkvStore
    }
}
