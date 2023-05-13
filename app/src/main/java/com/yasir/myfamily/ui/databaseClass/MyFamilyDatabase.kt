package com.yasir.myfamily.ui.databaseClass

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yasir.myfamily.ui.daoClass.ContactDao
import com.yasir.myfamily.ui.dataClasses.ContactModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [ContactModel::class], version = 1, exportSchema = false)
abstract class MyFamilyDatabase:RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object{
        @Volatile
        private var INSTANCE : MyFamilyDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context): MyFamilyDatabase {

            INSTANCE?.let {
                return it
            }
            return synchronized(MyFamilyDatabase::class.java){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyFamilyDatabase::class.java,
                    "My_family_DB"
                ).build()
                INSTANCE =instance
                instance
            }


      }
    }
}