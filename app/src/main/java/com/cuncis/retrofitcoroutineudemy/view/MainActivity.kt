package com.cuncis.retrofitcoroutineudemy.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cuncis.retrofitcoroutineudemy.R
import com.cuncis.retrofitcoroutineudemy.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: ListViewModel
    private val countriesAdapter = CountryListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        mainViewModel.refresh()

        rv_countries.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = countriesAdapter
        }

        observeViewModel()

    }

    private fun observeViewModel() {
        mainViewModel.countries.observe(this, Observer { countries ->
            countries.let {
                rv_countries.visibility = View.VISIBLE
                countriesAdapter.updateCountries(it)
            }
        })

        mainViewModel.countryLoadError.observe(this, Observer { isError ->
            tv_error.visibility = if (isError == "") View.GONE else View.VISIBLE
        })

        mainViewModel.loading.observe(this, Observer { isLoading ->
            isLoading.let {
                loading_view.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    tv_error.visibility = View.GONE
                    rv_countries.visibility = View.GONE
                }
            }
        })
    }
}
