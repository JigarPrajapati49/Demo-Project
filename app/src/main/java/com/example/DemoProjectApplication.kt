package com.example

import android.app.Application
import com.example.demoproject.BuildConfig
import com.example.geofance.ReminderRepository
import com.example.utils.PreferenceManager
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class DemoProjectApplication : Application() {

    /*private lateinit var quoteRepository: ApiRepository*/
    private lateinit var repository: ReminderRepository

    companion object {
        lateinit var instance: DemoProjectApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
//        initialize()

        repository = ReminderRepository(this)
        if (BuildConfig.DEBUG) {
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        }
        PreferenceManager.init(this)
    }

    fun getRepository() = repository


    /*private fun initialize() {
        val networkConnectionInterceptor = NetworkConnectionInterceptor(this)
        val quoteService = RetrofitClient.webService(networkConnectionInterceptor)
        val database = ProductDatabase.getDatabase(applicationContext)
        quoteRepository = ApiRepository(quoteService, database)
    }*/


}