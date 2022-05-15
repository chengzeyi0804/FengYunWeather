package com.tomato.weather

import com.tomato.lib.BaseApp
import com.tomato.lib.utils.SpUtil
import com.tomato.plugin_lib.SkinManager

//@HiltAndroidApp
open class MyApp : BaseApp() {

    override fun onCreate() {
        super.onCreate()

        SkinManager.init(this, object : SkinManager.OnPluginCallback {
            override fun setSkin(skinPath: String?) {
                SpUtil.setPluginPath(context, skinPath)
            }

            override fun reset() {
                SpUtil.setThemeFlag(context, 0)
            }

            override fun getSkin(): String {
                return SpUtil.getPluginPath(context)
            }
        })

        /*Looper.getMainLooper().setMessageLogging(object : Printer {
            var startWorkTimeMillis = 0L
            override fun println(it: String) {
                if (it.startsWith(">>>>> Dispatching")) {
                    startWorkTimeMillis = System.currentTimeMillis()
                } else if (it.startsWith("<<<<< Finished")) {
                    val duration = System.currentTimeMillis() - startWorkTimeMillis
                    if (duration > 300) {
                        LogUtil.e("耗时：$duration")
                        val stackTrace = Thread.currentThread().stackTrace
//                        val stackTraces = Thread.getAllStackTraces()
                        val sb = StringBuilder()

                        stackTrace.forEach {
                            sb.append("\n" + it.toString())
                        }
                        Log.e("wsjLib", sb.toString());
//                        LogUtil.e(sb.toString())
                    }
                }
            }
        })*/
    }
}