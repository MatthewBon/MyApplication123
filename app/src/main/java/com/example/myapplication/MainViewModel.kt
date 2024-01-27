package com.example.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.CountryRepository
import com.example.myapplication.repository.model.CountryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger


class MainViewModel : ViewModel() {
    private val countryRepository = CountryRepository()
    private val mutableCountriesData = MutableLiveData<UiState<List<CountryResponse>>>()
    val immutableCountriesData: LiveData<UiState<List<CountryResponse>>> = mutableCountriesData

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = countryRepository.getCountryResponse()
                Log.d("com.example.myapplication.MainViewModel", "request: ${request.raw()}")

                if(request.isSuccessful){
                    request.message()
                    val countries = request.body()
                    Log.d("com.example.myapplication.MainViewModel", "Request body: $countries")
                    mutableCountriesData.postValue(UiState(data = countries))
                }


            } catch (e: Exception) {
                Log.e("com.example.myapplication.MainViewModel", "Operation fault $e", e)
            }
        }
    }

    data class UiState<T>(
        val data: T? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}

