package com.tomato.weather.ui.activity

import android.content.Intent
import coil.load
import com.tomato.weather.databinding.ActivityUserInfoBinding
import com.tomato.weather.ui.base.BaseActivity
import com.tomato.weather.utils.TencentUtil
import com.tomato.lib.utils.SpUtil

class UserInfoActivity : BaseActivity<ActivityUserInfoBinding>() {
    override fun bindView() = ActivityUserInfoBinding.inflate(layoutInflater)

    override fun prepareData(intent: Intent?) {

    }

    override fun initView() {
        setTitle("用户信息")

        mBinding.tvName.text = SpUtil.getAccount(this)
        mBinding.ivAvatar.load(SpUtil.getAvatar(this))
    }

    override fun initEvent() {
        mBinding.btnLogout.setOnClickListener {
            TencentUtil.sTencent.logout(this)
            SpUtil.logout(this)
            val intent = Intent()
            intent.putExtra("login", false)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun initData() {

    }

}