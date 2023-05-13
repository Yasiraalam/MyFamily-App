package com.yasir.myfamily.ui.dataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactModel(
    val name:String,
    @PrimaryKey
    val Number:String
)
