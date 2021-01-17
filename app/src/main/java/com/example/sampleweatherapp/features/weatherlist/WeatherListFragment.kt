package com.example.sampleweatherapp.features.weatherlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampleweatherapp.CustomApplication
import com.example.sampleweatherapp.ErrorHandler
import com.example.sampleweatherapp.MainActivity
import com.example.sampleweatherapp.ProgressBarController
import com.example.sampleweatherapp.databinding.FragmentWeatherListBinding
import com.example.sampleweatherapp.network.WeatherApi
import retrofit2.Retrofit
import javax.inject.Inject

class WeatherListFragment : Fragment(), WeatherAdapter.WeatherAdapterListener {

    private var _binding: FragmentWeatherListBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherListViewModel: WeatherListViewModel
    private lateinit var adapter: WeatherAdapter
    private var progressBarController: ProgressBarController? = null
    private var errorHandler: ErrorHandler? = null

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherListBinding.inflate(layoutInflater)

        adapter = WeatherAdapter(this)
        binding.rvWeather.adapter = adapter
        binding.rvWeather.layoutManager = LinearLayoutManager(binding.root.context)

        weatherListViewModel = ViewModelProvider(
            requireActivity(),
            WeatherListViewModelFactory(WeatherApi.invoke(retrofit))
        )
            .get(WeatherListViewModel::class.java)

        weatherListViewModel.weatherList.observe(viewLifecycleOwner, Observer {
            adapter.updateList(it)
        })
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
                    progressBarController?.hideProgressBar()
                }
                else -> {
                    progressBarController?.hideProgressBar()
                }
            }
        })
        binding.swipeContainer.setOnRefreshListener {
            weatherListViewModel.getWeatherListFromNetwork()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherListViewModel.getWeatherListFromNetwork()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val sampleApplication = requireActivity().application as CustomApplication
        sampleApplication.applicationComponent.inject(this)

        if (context is MainActivity) {
            progressBarController = context
            errorHandler = context
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun itemClicked(weatherModel: WeatherModel) {

        findNavController().navigate(
            WeatherListFragmentDirections.actionWeatherListFragmentToWeatherDetailFragment(
                weatherModel.locationName
            )
        )
    }
}