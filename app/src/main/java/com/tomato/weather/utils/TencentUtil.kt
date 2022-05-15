package com.tomato.weather.utils

import com.tencent.tauth.Tencent
import com.tomato.lib.BaseApp

object TencentUtil {
    val sTencent by lazy {
        Tencent.createInstance(
            ContentUtil.TC_APP_ID,
            BaseApp.context,
            "${BaseApp.context}.fileprovider"
        )
    }
}