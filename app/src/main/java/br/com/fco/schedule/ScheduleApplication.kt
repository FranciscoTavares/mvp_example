package br.com.fco.schedule

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class ScheduleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this

        Realm.init(this)
        val config = RealmConfiguration.Builder().build()
        Realm.setDefaultConfiguration(config)
    }

    companion object {
        private var appInstance: ScheduleApplication? = null

        fun getInstance(): ScheduleApplication {
            return appInstance as ScheduleApplication
        }
    }
}
