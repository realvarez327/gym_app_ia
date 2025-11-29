package com.example.gymappia.data


import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodApi {
    @GET("api/v2/product/{barcode}")
    suspend fun getFoodByBarcode(
        @Path("barcode") barcode:String,
        @Query("fields")fields:String ="code,image_url,nutriments,product_name,product_quantity,product_quantity_unit,status"
    ): FoodApiResponse

    @GET("cgi/search.pl")
    suspend fun search(
        @Query("search_terms") searchTerms: String,
        @Query("page_size") pageSize: Int? = 5,
        @Query("search_simple") simpleSearch:Int? = 1,
        @Query("action")action:String? = "process",
        @Query("json")json:Int? = 1
    ): FoodApiResponse
}