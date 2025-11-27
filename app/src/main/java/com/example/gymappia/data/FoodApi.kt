package com.example.gymappia.data


import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodApi {
    @GET("product/{barcode}?fields=code,image_url,nutriments,product_name,product_quantity,product_quantity_unit,status")
    suspend fun getFoodByBarcode(@Path("barcode") barcode:String): FoodBarcodeResponse

    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int = 1,
        @Query("fields") fields: String? = null
    )
}