package com.example.sampleweatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.sampleweatherapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(),ProgressBarController,
    ErrorHandler {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun showProgressBar() {
        binding.customProgress.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.customProgress.visibility = View.GONE
    }


    override fun displayError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}