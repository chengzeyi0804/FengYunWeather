package com.tomato.weather.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Looper
import android.view.ViewPropertyAnimator
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.tomato.weather.databinding.ActivitySplashBinding
import com.tomato.weather.db.AppRepo
import com.tomato.weather.service.WidgetService
import com.tomato.weather.ui.base.BaseActivity
import com.tomato.weather.utils.ContentUtil
import com.tomato.lib.extension.startActivity
import per.wsj.commonlib.permission.PermissionUtil
import per.wsj.commonlib.utils.DisplayUtil

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    lateinit var animate: ViewPropertyAnimator

    private fun startIntent() {
        lifecycleScope.launch {
            var citySize: Int

//            DensityUtil.setDensity(application, 397f)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(Intent(this@SplashActivity, WidgetService::class.java))
            } else {
                startService(Intent(this@SplashActivity, WidgetService::class.java))
            }

            withContext(Dispatchers.IO) {
//                val start = System.currentTimeMillis()
                val cities = AppRepo.getInstance().getCities()
//                LogUtil.e("time use: " + (System.currentTimeMillis() - start))
                citySize = cities.size

                getScreenInfo()

                delay(1200L)
            }
            if (citySize == 0) {
                AddCityActivity.startActivity(this@SplashActivity, true)
            } else {
                startActivity<HomeActivity>()
            }
            finish()
        }
    }

    private fun getScreenInfo() {
        val screenRealSize = DisplayUtil.getScreenRealSize(this@SplashActivity).y

        val navHeight =
            if (DisplayUtil.isNavigationBarShowing(this@SplashActivity))
                DisplayUtil.getNavigationBarHeight(this@SplashActivity) else 0

        val statusBarHeight = DisplayUtil.getStatusBarHeight2(this@SplashActivity)
        val dp45 = DisplayUtil.dp2px(45f)
        ContentUtil.visibleHeight = screenRealSize - navHeight - statusBarHeight - dp45
    }

    override fun bindView() = ActivitySplashBinding.inflate(layoutInflater)

    override fun prepareData(intent: Intent?) {
        //To do sth.
    }

    override fun initView() {
        hideTitleBar()
        immersionStatusBar()

        animate = mBinding.ivLogo.animate()

        animate.apply {
            duration = 1200L
//            interpolator = BounceInterpolator()
            translationYBy(-80F)
            scaleXBy(0.2F)
            scaleYBy(0.2F)
        }
    }

    override fun initEvent() {
        Looper.myQueue().addIdleHandler {
            PermissionUtil.with(this).permission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
                .onGranted {
                    startIntent()
                }
                .onDenied {
                    startIntent()
                }.start()
            false
        }
    }

    override fun initData() {
        //To do sth.
    }

    override fun onDestroy() {
        super.onDestroy()
        animate.cancel()
    }
}