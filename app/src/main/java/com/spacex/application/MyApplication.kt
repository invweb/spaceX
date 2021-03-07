package com.spacex.application

import android.app.Application
import com.spacex.database.AppDatabase

class MyApplication: Application() {

    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()

        database = AppDatabase.getInstance(this)!!
    }
}