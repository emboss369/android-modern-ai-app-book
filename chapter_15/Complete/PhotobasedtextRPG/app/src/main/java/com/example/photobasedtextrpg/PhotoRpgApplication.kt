package com.example.photobasedtextrpg

import android.app.Application
import androidx.room.Room
import com.example.photobasedtextrpg.core.data.db.AppDatabase

class PhotoRpgApplication : Application() {
  val database: AppDatabase by lazy {    // ❶
    Room.databaseBuilder(this, AppDatabase::class.java, "photorpg.db").build()
  }
}