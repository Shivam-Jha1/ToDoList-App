package com.coder178.notes.Model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Notes")
class Notes(

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var title: String,
    var subTitile: String,
    var notes: String,
    var date: String,
    var priority: String,
    var dueDate:String

):Parcelable
