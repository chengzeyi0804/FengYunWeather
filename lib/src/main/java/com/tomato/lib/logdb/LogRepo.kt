package com.tomato.lib.logdb


import com.tomato.lib.BaseApp
import com.tomato.lib.logdb.dao.LogDao
import com.tomato.lib.logdb.entity.LogEntity


class LogRepo {


    private val logDao: LogDao = LogDatabase.getInstance(BaseApp.context).logDao()

    fun addLog(content: String) {
        logDao.addLog(LogEntity(content))
    }

    companion object {
        @Volatile
        private var instance: LogRepo? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance
                    ?: LogRepo()
                        .also { instance = it }
            }
    }
}