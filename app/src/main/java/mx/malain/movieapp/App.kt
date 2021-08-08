package mx.malain.movieapp

import android.app.Application
import android.util.Log
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        AppCenter.start(this, "2dfdd7ca-d46e-47af-bbef-b6971a16a508", Analytics::class.java, Crashes::class.java)
        if (Crashes.hasCrashedInLastSession().get()){
            Log.e("MALAINP", Crashes.getLastSessionCrashReport().get().stackTrace)
        }
    }
}
