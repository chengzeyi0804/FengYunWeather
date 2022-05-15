package com.tomato.lib.common

import android.app.Application

interface InitTask {
  fun init(application: Application)
}