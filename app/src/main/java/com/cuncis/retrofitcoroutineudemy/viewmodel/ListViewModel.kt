package com.cuncis.retrofitcoroutineudemy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cuncis.retrofitcoroutineudemy.model.ApiClient
import com.cuncis.retrofitcoroutineudemy.model.Country
import kotlinx.coroutines.*

class ListViewModel: ViewModel() {

    val coroutineService = ApiClient.theCountriesApi
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError("Exception: ${throwable.localizedMessage}")
    }

    val countries = MutableLiveData<List<Country>>()
    val countryLoadError = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        fetchCountry()
    }

    private fun fetchCountry() {
        loading.value = true

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = coroutineService.getCountries()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    countries.value = response.body()
                    countryLoadError.value = null
                    loading.value = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    private fun onError(message: String) {
        countryLoadError.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}