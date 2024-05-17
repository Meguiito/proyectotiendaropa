package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class MainActivity2 : AppCompatActivity() {

    // Definir la interfaz del servicio
    interface SalesProductService {
        @GET("productos/{id}")
        fun getProduct(@Path("id") id: String): Call<Product>

    }

    // Definir la clase de datos Product
    data class Product(
        val _id: String,
        val name: String,
        val price: Double
    )

    // Declarar la variable products
    private var products: List<Product> = emptyList()

    // Inicializar Retrofit
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://127.0.0.1:5000/") // Asegúrate de que esta URL sea accesible desde tu dispositivo
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Llamar a la función para obtener productos
        getProducts()
    }

    private fun getProducts() {
        val salesProductService = retrofit.create(SalesProductService::class.java)
        val call = salesProductService.getProducts()

        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    products = response.body() ?: emptyList()
                    // Actualizar la UI o realizar acciones con los productos obtenidos
                    Toast.makeText(applicationContext, "Productos obtenidos: ${products.size}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Error al obtener productos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(applicationContext, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
