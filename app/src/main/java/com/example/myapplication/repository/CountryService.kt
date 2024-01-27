package com.example.myapplication.repository
import com.example.myapplication.repository.model.CountryResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryService {

    @GET("all")
    suspend fun getCountryResponse(): Response<List<CountryResponse>>

    @GET("name/{id}")
    suspend fun getCountryDetailsResponse(@Path("id") id: String): Response<List<CountryResponse>>

    companion object {
        private const val COUNTRIES_URL = "https://restcountries.com/v3.1/"

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(COUNTRIES_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val countryService: CountryService by lazy {
            retrofit.create(CountryService::class.java)
        }
    }


}