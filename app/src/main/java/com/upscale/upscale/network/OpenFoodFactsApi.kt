package com.upscale.upscale.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.upscale.upscale.data.food.FoodReceiver
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(OpenFoodFactsApiService.BASE_URL)
    .build()

interface OpenFoodFactsApiService{

    companion object{
        const val BASE_URL = "https://world.openfoodfacts.org"
        private const val API_VER = "v0"
        private const val API_PREFIX = "api/$API_VER"
    }

    @GET("$API_PREFIX/product/{barcode}.json")
    suspend fun getFoodByBarcode(
        @Path("barcode") barcode: String,
    ) : FoodReceiver
}

object OpenFoodFactsApi{
    val retrofitService: OpenFoodFactsApiService by lazy { retrofit.create(OpenFoodFactsApiService::class.java) }
}