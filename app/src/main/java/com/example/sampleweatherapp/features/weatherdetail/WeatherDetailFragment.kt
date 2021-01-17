package com.example.sampleweatherapp.features.weatherdetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sampleweatherapp.CustomApplication
import com.example.sampleweatherapp.databinding.FragmentWeatherDetailBinding
import com.example.sampleweatherapp.databinding.FragmentWeatherListBinding

class WeatherDetailFragment : Fragment() {

    private var _binding: FragmentWeatherDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherDetailBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val geoSampleApplication = requireActivity().application as CustomApplication
        geoSampleApplication.applicationComponent.inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}