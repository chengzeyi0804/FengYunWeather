package com.tomato.weather.ui.activity

import android.content.Intent
import com.tomato.weather.R
import com.tomato.weather.databinding.ActivitySettingsBinding
import com.tomato.weather.ui.base.BaseActivity
import com.tomato.weather.ui.fragment.SettingsFragment

class SettingsActivity : BaseActivity<ActivitySettingsBinding>() {

    override fun bindView() = ActivitySettingsBinding.inflate(layoutInflater)

    override fun prepareData(intent: Intent?) {

    }

    override fun initView() {
        setTitle(getString(R.string.setting))

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
    }

    override fun initEvent() {

    }

    override fun initData() {

    }
}