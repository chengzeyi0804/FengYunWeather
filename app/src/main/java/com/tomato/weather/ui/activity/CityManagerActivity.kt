package com.tomato.weather.ui.activity

import android.content.Intent
import androidx.recyclerview.widget.ItemTouchHelper
import com.tomato.weather.R
import com.tomato.weather.adapter.CityManagerAdapter
import com.tomato.weather.adapter.MyItemTouchCallback
import com.tomato.weather.databinding.ActivityCityManagerBinding
import com.tomato.weather.db.entity.CityEntity
import com.tomato.weather.ui.activity.vm.CityManagerViewModel
import com.tomato.weather.ui.base.BaseVmActivity
import com.tomato.weather.utils.ContentUtil

//@AndroidEntryPoint
class CityManagerActivity : BaseVmActivity<ActivityCityManagerBinding, CityManagerViewModel>() {

    private val datas by lazy { ArrayList<CityEntity>() }

    private var adapter: CityManagerAdapter? = null

    //    @Inject
    lateinit var itemTouchCallback: MyItemTouchCallback

    override fun bindView() = ActivityCityManagerBinding.inflate(layoutInflater)

    override fun prepareData(intent: Intent?) {
    }

    override fun initView() {
        setTitle(getString(R.string.control_city))

        itemTouchCallback = MyItemTouchCallback(this)

        adapter = CityManagerAdapter(datas) {
            viewModel.updateCities(it)
            ContentUtil.CITY_CHANGE = true
        }

        mBinding.recycleView.adapter = adapter

        mBinding.recycleView.setStateCallback {
            itemTouchCallback.dragEnable = it
        }

        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(mBinding.recycleView)
    }

    override fun initEvent() {
        adapter?.listener = object : CityManagerAdapter.OnCityRemoveListener {
            override fun onCityRemove(pos: Int) {
                viewModel.removeCity(datas[pos].cityId)
                datas.removeAt(pos)
                adapter?.notifyItemRemoved(pos)
                ContentUtil.CITY_CHANGE = true

            }
        }

        viewModel.cities.observe(this) {
            datas.clear()
            datas.addAll(it)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun initData() {
        viewModel.getCities()
    }
}