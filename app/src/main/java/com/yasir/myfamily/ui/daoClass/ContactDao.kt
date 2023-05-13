package com.yasir.myfamily.ui.daoClass

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yasir.myfamily.ui.dataClasses.ContactModel

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contactModel: ContactModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contactModelList: List<ContactModel>)

    @Query("SELECT * FROM contactModel")
     fun getAllContacts():LiveData<List<ContactModel>>
}