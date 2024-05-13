package com.example.myapplication



import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "mongodb+srv://benjaz:benjaz@cluster0.qzervft.mongodb.net/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

interface ProductService {
    @GET("productos") // El nombre del endpoint que devuelve los productos
    suspend fun getProducts(): List<Product> // Método para obtener los productos
}