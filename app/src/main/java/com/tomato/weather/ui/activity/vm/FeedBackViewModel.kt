package com.tomato.weather.ui.activity.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tomato.weather.ui.base.BaseViewModel
import com.tomato.weather.utils.ContentUtil
import com.tomato.lib.net.HttpUtils

class FeedBackViewModel(val app: Application) : BaseViewModel(app) {

    val feedBackResult = MutableLiveData<String>()

    fun sendFeedBack(content: String) {
        val param = HashMap<String, Any>()
        param["content"] = content
//        param["app_version"] = CommonUtil.getVersionCode(app)
//        param["device_brand"] = DeviceUtil.getDeviceBrand()
//        param["system_model"] = DeviceUtil.getSystemModel()
//        param["system_version"] = DeviceUtil.getSystemVersion()

        val url = ContentUtil.BASE_URL + "api/feedback"

        launch {
            val result = HttpUtils.post<String>(url, param)
            result?.let {
                feedBackResult.postValue(it)
            }
        }
    }
}