package com.example.sampleweatherapp.features.weatherdetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sampleweatherapp.*
import com.example.sampleweatherapp.databinding.FragmentWeatherDetailBinding
import com.example.sampleweatherapp.databinding.FragmentWeatherListBinding
import com.example.sampleweatherapp.features.weatherlist.ApiStatus
import com.example.sampleweatherapp.features.weatherlist.WeatherListViewModel
import com.example.sampleweatherapp.features.weatherlist.WeatherListViewModelFactory
import com.example.sampleweatherapp.network.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.await
import timber.log.Timber
import javax.inject.Inject

class WeatherDetailFragment : Fragment() {

    private lateinit var args: WeatherDetailFragmentArgs
    private var sampleApplication: CustomApplication? = null
    private var _binding: FragmentWeatherDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherListViewModel: WeatherListViewModel
    private var progressBarController: ProgressBarController? = null
    private var errorHandler: ErrorHandler? = null

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherDetailBinding.inflate(layoutInflater)

        weatherListViewModel = ViewModelProvider(
            requireActivity(),
            WeatherListViewModelFactory(WeatherApi.invoke(retrofit))
        )
            .get(WeatherListViewModel::class.java)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoriteFilled =
            ContextCompat.getDrawable(binding.root.context, R.drawable.ic_baseline_favorite_24)
        val favoriteEmpty = ContextCompat.getDrawable(
            binding.root.context,
            R.drawable.ic_baseline_favorite_border_24
        )

        args = WeatherDetailFragmentArgs.fromBundle(requireArguments())

        weatherListViewModel.weatherDetail.observe(viewLifecycleOwner, Observer {
            binding.tvWeatherCity.text = it.locationName
            binding.tvWeatherStatus.text = it.weatherStatus
            binding.tvWeatherTemp.text = it.currentTemp
            binding.tvHighAndLowTemperature.text = it.lowAndHighTemperature
            Timber.d("setting up")
            if (it.isFavorite) {
                binding.ivFavorite.setImageDrawable(favoriteFilled)
            } else {
                binding.ivFavorite.setImageDrawable(favoriteEmpty)
            }
        })

        binding.ivFavorite.setOnClickListener {
            weatherListViewModel.toggleFavoriteCity(args.city)
        }
        weatherListViewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                ApiStatus.DONE -> {
                    progressBarController?.hideProgressBar()
                }
                ApiStatus.LOADING -> {
                    progressBarController?.showProgressBar()
                }
                ApiStatus.ERROR -> {
                    errorHandler?.displayError(weatherListViewModel.apiErrorMessage)
                }
                else -> {
                    progressBarController?.hideProgressBar()
                }
            }
        })
        weatherListViewModel.getWeatherDetailFromNetwork(args.city)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sampleApplication = requireActivity().application as CustomApplication
        sampleApplication?.applicationComponent?.inject(this)

        if (context is MainActivity) {
            progressBarController = context
            errorHandler = context
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}