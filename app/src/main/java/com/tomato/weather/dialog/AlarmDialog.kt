package com.tomato.weather.dialog

import android.content.Context
import com.tomato.weather.databinding.DialogAlarmBinding
import com.tomato.lib.dialog.BaseDialog

class AlarmDialog(context: Context) : BaseDialog<DialogAlarmBinding>(context, 0.66f, 0f) {

    override fun bindView() = DialogAlarmBinding.inflate(layoutInflater)

    override fun initView() {
        setCanceledOnTouchOutside(true)
    }

    fun setContent(title: String, content: String) {
        mBinding.tvTitle.text = title
//        mBinding.tvTitle.setTextColor(titleColor)
        mBinding.tvContent.text = content
    }

    override fun initEvent() {
        mBinding.ivClose.setOnClickListener {
            dismiss()
        }
    }
}