package com.tomato.weather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tomato.weather.R
import com.tomato.weather.bean.Daily
import com.tomato.weather.bean.TempUnit
import com.tomato.weather.databinding.ItemForecastBinding
import com.tomato.weather.utils.ContentUtil
import com.tomato.lib.utils.IconUtils
import com.tomato.lib.utils.WeatherUtil

class Forecast3dAdapter(val context: Context, val datas: List<Daily>) :
    RecyclerView.Adapter<Forecast3dAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemForecastBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = datas[position]
        if (ContentUtil.APP_SETTING_UNIT == TempUnit.HUA.tag) {
            val minHua = WeatherUtil.getF(item.tempMin)
            val maxHua = WeatherUtil.getF(item.tempMax)
            holder.binding.tvTemp.text = "${minHua}~${maxHua}°F"
        } else {
            holder.binding.tvTemp.text = "${item.tempMin}~${item.tempMax}°C"
        }

        var desc = item.textDay
        if (item.textDay != item.textNight) {
            desc += "转" + item.textNight
        }
        holder.binding.tvDesc.text = desc

        when (position) {
            0 -> {
                holder.binding.tvWeek.text = context.getString(R.string.today)
                if (IconUtils.isDay()) {
//                    holder.binding.ivDay.setImageResource(IconUtils.getDayIconDark(context, item.iconDay))
//                    holder.binding.iv3fDay.setImageDrawable(IconUtils.getDayIcon(context, item.iconDay))
                    holder.binding.iv3fDay.setImageResourceName(item.iconDay)
                } else {
//                    holder.binding.ivDay.setImageResource(IconUtils.getNightIconDark(context, item.iconDay))
//                    holder.binding.iv3fDay.setImageDrawable(IconUtils.getNightIcon(context, item.iconDay))
                    holder.binding.iv3fDay.setImageResourceName(item.iconNight)
                }
            }
            1 -> {
                holder.binding.tvWeek.text = context.getString(R.string.tomorrow)
//                holder.binding.ivDay.setImageResource(IconUtils.getDayIconDark(context, item.iconDay))
                holder.binding.iv3fDay.setImageResourceName(item.iconDay)

//                holder.binding.ivDay.setImageDrawable(IconUtils.getDayIcon(context,item.iconDay))
            }
            else -> {
                holder.binding.tvWeek.text = context.getString(R.string.after_t)
//                holder.binding.ivDay.setImageResource(IconUtils.getDayIconDark(context, item.iconDay))
//                holder.binding.iv3fDay.setImageDrawable(IconUtils.getDayIcon(context, item.iconDay))
                holder.binding.iv3fDay.setImageResourceName(item.iconDay)
            }
        }
    }

    override fun getItemCount(): Int = 3

    class ViewHolder(val binding: ItemForecastBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}