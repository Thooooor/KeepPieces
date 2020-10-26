package com.keeppieces.android.logic.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlin.concurrent.thread

@Database(
    version = 5,
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
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        thread {
                            instance?.accountDao()?.insertAccount(Account("微信",0.00))
                            instance?.accountDao()?.insertAccount(Account("支付宝",0.00))
                            instance?.memberDao()?.insertMember(Member("无成员"))
                            instance?.memberDao()?.insertMember(Member("自己"))
                            instance?.primaryCategoryDao()?.insertPrimaryCategory(PrimaryCategory("其他"))
                            instance?.primaryCategoryDao()?.insertPrimaryCategory(PrimaryCategory("转账"))
                            instance?.secondaryCategoryDao()?.insertSecondaryCategory(SecondaryCategory("其他", "其他"))
                            instance?.secondaryCategoryDao()?.insertSecondaryCategory(SecondaryCategory("微信", "转账"))
                            instance?.secondaryCategoryDao()?.insertSecondaryCategory(SecondaryCategory("支付宝", "转账"))
                            instance?.typeDao()?.insertType(Type("支出"))
                            instance?.typeDao()?.insertType(Type("收入"))
                            instance?.typeDao()?.insertType(Type("转账"))
                        }
                    }
                })
                .build().apply {
                    instance = this
                }
        }
    }
}