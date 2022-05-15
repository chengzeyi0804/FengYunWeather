package com.tomato.weather.ui.activity

import android.content.Intent
import com.tomato.weather.R
import com.tomato.weather.databinding.ActivityThemeBinding
import com.tomato.weather.ui.activity.vm.ThemeViewModel
import com.tomato.weather.ui.base.BaseVmActivity
import com.tomato.lib.extension.toast
import com.tomato.lib.net.LoadState
import com.tomato.lib.utils.SpUtil
import com.tomato.plugin_lib.SkinManager
import java.io.File

class ThemeActivity : BaseVmActivity<ActivityThemeBinding, ThemeViewModel>() {

    var curFlag = 0

    override fun bindView() = ActivityThemeBinding.inflate(layoutInflater)

    override fun prepareData(intent: Intent?) {

    }

    override fun initView() {
        setTitle("主题设置")
        curFlag = SpUtil.getThemeFlag(this)
        setSelected(curFlag)
    }

    override fun initEvent() {
        mBinding.llDefault.setOnClickListener {
            changeSelect(0)
        }
        mBinding.llColorful.setOnClickListener {
            changeSelect(1)
        }

        viewModel.loadState.observe(this) {
            when (it) {
                is LoadState.Error -> {
                    toast("下载失败，请重试")
                    changeSelect(0)
                }
                LoadState.Finish -> {
                    showLoading(false)
                }
                is LoadState.Start -> {
                    showLoading(true, "正在下载...")
                }
            }
        }

        viewModel.downloadStatus.observe(this) {
            toast("设置成功")
            SkinManager.getInstance().loadSkin(SpUtil.getPluginPath(this), false)
        }
    }

    private fun changeSelect(index: Int) {
        if (curFlag == index) {
            return
        }
        curFlag = index
        setSelected(index)
        SpUtil.setThemeFlag(this, index)
        if (index == 1) {
            val path = SpUtil.getPluginPath(this)
            if (path.isEmpty() || !File(path).exists()) {
                viewModel.downPlugin()
            } else {
                SkinManager.getInstance().loadSkin(path, false)
            }
        } else {
            SkinManager.getInstance().loadSkin("", false)
        }
    }

    private fun setSelected(index: Int) {
        if (index == 1) {
            mBinding.llDefault.background = resources.getDrawable(R.drawable.shape_theme_bg)
            mBinding.llColorful.background =
                resources.getDrawable(R.drawable.shape_theme_bg_selected)
        } else {
            mBinding.llDefault.background =
                resources.getDrawable(R.drawable.shape_theme_bg_selected)
            mBinding.llColorful.background = resources.getDrawable(R.drawable.shape_theme_bg)
        }
    }

    override fun initData() {

    }

}