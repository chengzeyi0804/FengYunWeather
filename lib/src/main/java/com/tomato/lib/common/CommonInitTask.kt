package com.tomato.lib.common

import android.app.Application

object CommonInitTask : InitTask {
    override fun init(application: Application) {
        /*val config = CommonConfig.Builder()
            .setApplication(application) // Set application
            .setVersionNameInvoker { "1.0.0" } // Set version name, java leak feature use it
            .build()

        MonitorManager.initCommonConfig(config)
            .apply { onApplicationCreate() }

        val monitorConfig = OOMMonitorConfig.Builder()
            .setThreadThreshold(50) //50 only for test! Please use default value!
            .setFdThreshold(300) // 300 only for test! Please use default value!
            .setHeapThreshold(0.9f) // 0.9f for test! Please use default value!
            .setVssSizeThreshold(1_000_000) // 1_000_000 for test! Please use default value!
            .setMaxOverThresholdCount(1) // 1 for test! Please use default value!
            .setAnalysisMaxTimesPerVersion(3) // Consider use default value！
            .setAnalysisPeriodPerVersion(15 * 24 * 60 * 60 * 1000) // Consider use default value！
            .setLoopInterval(5_000) // 5_000 for test! Please use default value!
            .setEnableHprofDumpAnalysis(true)
            .setHprofUploader(object : OOMHprofUploader {
                override fun upload(file: File, type: OOMHprofUploader.HprofType) {
                    LogUtil.e("OOMMonitor", "todo, upload hprof ${file.name} if necessary")
                    LogUtil.e("OOMMonitor", "todo, upload hprof ${file.name} if necessary")
                }
            })
            .setReportUploader(object : OOMReportUploader {
                override fun upload(file: File, content: String) {
                    LogUtil.i("OOMMonitor", content)
                    LogUtil.e("OOMMonitor", "todo, upload report ${file.name} if necessary")
                }
            })
            .build()

        MonitorManager.addMonitorConfig(monitorConfig)
        OOMMonitor.stopLoop()*/
    }
}