package com.keeppieces.android.logic.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    version = 3,
    entities = [Bill::class, Account::class, Member::class, PrimaryCategory::class, SecondaryCategory::class, Type::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun billDao(): BillDao
    abstract fun accountDao(): AccountDao
    abstract fun memberDao(): MemberDao
    abstract fun primaryCategoryDao(): PrimaryCategoryDao
    abstract fun secondaryCategoryDao(): SecondaryCategoryDao
    abstract fun typeDao(): TypeDao

    companion object {

        private var instance: AppDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database")
                .fallbackToDestructiveMigration()
                .build().apply {
                    instance = this
                }
        }
    }
}