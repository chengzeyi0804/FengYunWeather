package com.tomato.weather.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import com.tomato.lib.extension.expand
import com.tomato.weather.R
import com.tomato.weather.adapter.SearchAdapter
import com.tomato.weather.adapter.TopCityAdapter
import com.tomato.weather.bean.CityBean
import com.tomato.weather.bean.Location
import com.tomato.weather.databinding.ActivityAddCityBinding
import com.tomato.weather.databinding.ActivityAddCityBinding.*
import com.tomato.weather.ui.activity.vm.SearchViewModel
import com.tomato.weather.ui.base.BaseVmActivity
import com.tomato.weather.ui.fragment.PermissionFragment
import com.tomato.weather.utils.ContentUtil
import com.tomato.lib.extension.startActivity
import com.tomato.lib.extension.toast
import com.tomato.lib.net.LoadState
import per.wsj.commonlib.permission.PermissionUtil
import java.util.*

class AddCityActivity : BaseVmActivity<ActivityAddCityBinding, SearchViewModel>() {

    private var searchAdapter: SearchAdapter? = null

    private var topCityAdapter: TopCityAdapter? = null

    private val searchCities by lazy { ArrayList<CityBean>() }

    private val topCities by lazy { ArrayList<String>() }

    private var fromSplash = false

    private var requestedGPS = false

    override fun bindView() = inflate(layoutInflater)

    override fun prepareData(intent: Intent?) {
        intent?.let {
            fromSplash = it.getBooleanExtra("fromSplash", false)
        }
    }

    override fun initView() {
        setTitle("添加城市")
        mBinding.etSearch.threshold = 2

        searchAdapter = SearchAdapter(
            this@AddCityActivity,
            searchCities,
            mBinding.etSearch.text.toString()
        ) {
            viewModel.addCity(it)
        }
        mBinding.rvSearch.adapter = searchAdapter

        topCityAdapter = TopCityAdapter(topCities) {
            viewModel.getCityInfo(it)
        }
        val layoutManager = GridLayoutManager(context, 3)
        mBinding.rvTopCity.adapter = topCityAdapter
        mBinding.rvTopCity.layoutManager = layoutManager

        mBinding.tvGetPos.expand(10, 10)
        //编辑框输入监听
        mBinding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val keywords = mBinding.etSearch.text.toString()
                if (!TextUtils.isEmpty(keywords)) {
                    viewModel.searchCity(keywords)
                } else {
                    mBinding.rvSearch.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        mBinding.tvGetPos.setOnClickListener {
            hideKeyboard()
            checkAndOpenGPS()
        }
    }

    override fun initEvent() {
        viewModel.cacheLocation.observe(this) {
            mBinding.tvCurLocation.visibility = View.VISIBLE
            mBinding.tvCurLocation.text = it
        }

        // 定位获取的数据
        viewModel.curLocation.observe(this) {
            mBinding.tvCurLocation.visibility = View.VISIBLE
            mBinding.tvCurLocation.text = it
            viewModel.getCityInfo(it, true)
        }

        // 根据定位城市获取详细信息
        viewModel.curCity.observe(this) { item ->
            val curCity = location2CityBean(item)
            // 显示城市详细位置
            mBinding.tvCurLocation.text = curCity.cityName
            viewModel.addCity(curCity, true)
        }

        // 选中的热门城市的信息
        viewModel.choosedCity.observe(this) { item ->
            val curCity = location2CityBean(item)
            viewModel.addCity(curCity)
        }

        viewModel.loadState.observe(this) {
            when (it) {
                is LoadState.Start -> {
                    showLoading(true, it.tip)
                }
                is LoadState.Error -> {
                    toast(it.msg)
                }
                is LoadState.Finish -> {
                    if (viewModel.isStopped()) {
                        showLoading(false)
                    }
                }
            }
        }

        viewModel.addFinish.observe(this) {
            if (it) {
                if (fromSplash) {
                    startActivity<HomeActivity>()
                }
                ContentUtil.CITY_CHANGE = true
                finish()
            }
        }

        viewModel.searchResult.observe(this) {
            showSearchResult(it)
        }

        viewModel.topCity.observe(this) {
            showTopCity(it)
        }
    }

    fun checkAndOpenGPS() {
        if (checkGPSPermission()) {
            checkGetLocation()
        } else {
            PermissionUtil.with(this).permission(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
                .onGranted {
                    checkGetLocation()
                }
                .onDenied {
                    toast("没有定位权限，无法获取您的位置")
                }.start()
        }
    }

    /**
     * 检查并获取位置
     */
    fun checkGetLocation() {
        if (checkGPSOpen()) {
            viewModel.getLocation()
        } else {
            requestedGPS = true
            openGPS()
        }
    }

    /**
     * 检查GPS权限
     */
    fun checkGPSPermission(): Boolean {
        val pm1 = PermissionUtil.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val pm2 = PermissionUtil.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        return (pm1 || pm2)
    }

    /**
     * 检查GPS状态
     */
    fun checkGPSOpen(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val pr1 = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val pr2 = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return (pr1 || pr2)
    }

    /**
     * 启动GPS
     */
    private fun openGPS() {
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.add(PermissionFragment.newInstance(), "permission_fragment")
        beginTransaction.commitAllowingStateLoss()
    }

    /**
     * 展示热门城市
     */
    private fun showTopCity(locations: List<String>) {
        topCities.clear()
//        locations.forEach { item ->
//            var parentCity = item.adm2
//            val adminArea = item.adm1
//            val city = item.country
//            if (TextUtils.isEmpty(parentCity)) {
//                parentCity = adminArea
//            }
//            if (TextUtils.isEmpty(adminArea)) {
//                parentCity = city
//            }
//            val cityBean = CityBean()
//            cityBean.cityName = parentCity + " - " + item.name
//            cityBean.cityId = item.id
//            cityBean.cnty = city
//            cityBean.adminArea = adminArea
//            topCities.add(cityBean)
//        }
        topCities.addAll(locations)
        topCityAdapter?.notifyDataSetChanged()
    }

    /**
     * 展示搜索结果
     */
    private fun showSearchResult(basic: List<Location>) {
        mBinding.rvSearch.visibility = View.VISIBLE

        searchCities.clear()

        basic.forEach { item ->
            searchCities.add(location2CityBean(item))
        }
        searchAdapter?.notifyDataSetChanged()
    }

    /**
     * location转citybean
     */
    private fun location2CityBean(location: Location): CityBean {
        var parentCity = location.adm2
        val adminArea = location.adm1
        val city = location.country
        if (TextUtils.isEmpty(parentCity)) {
            parentCity = adminArea
        }
        if (TextUtils.isEmpty(adminArea)) {
            parentCity = city
        }
        val cityBean = CityBean()
        cityBean.cityName = parentCity + " - " + location.name
        cityBean.cityId = location.id
        cityBean.cnty = city
        cityBean.adminArea = adminArea
        return cityBean
    }

    override fun initData() {
        viewModel.getTopCity()
        viewModel.getCacheLocation()
    }

    override fun onResume() {
        super.onResume()
        if (requestedGPS) {
            requestedGPS = false
            if (checkGPSOpen()) {
                viewModel.getLocation()
            } else {
                toast("无法获取位置信息")
            }
        }
    }

    private fun hideKeyboard() {
        currentFocus?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    override fun finish() {
        hideKeyboard()
        super.finish()
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    companion object {
        fun startActivity(context: Context, fromSplash: Boolean = false) {
            val intent = Intent(context, AddCityActivity::class.java)
            intent.putExtra("fromSplash", fromSplash)
            context.startActivity(intent)
        }
    }
}