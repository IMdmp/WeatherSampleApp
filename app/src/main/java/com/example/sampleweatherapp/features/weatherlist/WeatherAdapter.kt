package com.example.sampleweatherapp.features.weatherlist

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleweatherapp.R
import com.example.sampleweatherapp.databinding.ItemWeatherBinding
import com.example.sampleweatherapp.network.CELCIUS_SYMBOL


class WeatherAdapter(val weatherAdapterListener: WeatherAdapterListener) :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    val weatherList = mutableListOf<WeatherModel>()

    class ViewHolder(val binding: ItemWeatherBinding) : RecyclerView.ViewHolder(binding.root) {

        val freezing = ContextCompat.getColor(binding.root.context, R.color.freezing)
        val cold = ContextCompat.getColor(binding.root.context, R.color.cold)
        val warm = ContextCompat.getColor(binding.root.context, R.color.warm)
        val hot = ContextCompat.getColor(binding.root.context, R.color.hot)


        fun bind(
            item: WeatherModel,
            weatherAdapterListener: WeatherAdapterListener
        ) {
            val temp = item.currentTemp

            val tempInt = temp.toFloat()

            binding.tvTemp.text = "${item.currentTemp} $CELCIUS_SYMBOL"
            binding.tvCity.text = item.locationName
            binding.tvStatus.text = item.weatherStatus

            if (item.isFavorite) {
                binding.ivFavorite.visibility = View.VISIBLE
            } else {
                binding.ivFavorite.visibility = View.GONE
            }

            if (isFreezing(tempInt)) {
                binding.clContainer.background.setColorFilter(
                    freezing,
                    PorterDuff.Mode.SRC_ATOP
                )
            } else if (isCold(tempInt)) {
                binding.clContainer.background.setColorFilter(
                    cold,
                    PorterDuff.Mode.SRC_ATOP
                )

            } else if (isWarm(tempInt)) {
                binding.clContainer.background.setColorFilter(
                    warm,
                    PorterDuff.Mode.SRC_ATOP
                )

            } else {
                // its hot.
                binding.clContainer.background.setColorFilter(
                    hot,
                    PorterDuff.Mode.SRC_ATOP
                )
            }
            binding.clContainer.setOnClickListener {
                weatherAdapterListener.itemClicked(item)
            }
        }

        private fun isFreezing(tempInt: Float): Boolean {
            return tempInt <= 0
        }

        private fun isCold(tempInt: Float): Boolean {
            return tempInt in 1.0..15.0
        }

        private fun isWarm(tempInt: Float): Boolean {
            return tempInt in 16.0..30.0
        }


    }

    fun updateList(jobList: List<WeatherModel>) {
        this.weatherList.clear()
        this.weatherList.addAll(jobList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = weatherList[position]
        holder.bind(item, weatherAdapterListener)

    }

    interface WeatherAdapterListener {
        fun itemClicked(weatherModel: WeatherModel)
    }
}