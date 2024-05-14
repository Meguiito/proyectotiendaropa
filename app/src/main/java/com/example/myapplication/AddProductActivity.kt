package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class ProductData(
    val Producto: String,
    val Precio: Double
)



interface AddProductService {
    @POST("productos")
    fun addProduct(@Body productData: ProductData): Call<Map<String, String>>
}

class AddProductActivity : ComponentActivity() {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.13:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                AddProductScreen()
            }
        }
    }

    @Composable
    fun AddProductScreen() {
        val (productName, setProductName) = remember { mutableStateOf("") }
        val (productPrice, setProductPrice) = remember { mutableStateOf(0.0) }
        val context = LocalContext.current

        BoxWithBackground {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = productName,
                    onValueChange = setProductName,
                    label = { Text("Nombre del producto") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = productPrice.toString(),
                    onValueChange = { setProductPrice(it.toDoubleOrNull() ?: 0.0) },
                    label = { Text("Precio del producto") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        addProduct(productName, productPrice)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar producto")
                }
            }
        }
    }

    private fun addProduct(name: String, price: Double) {
        val addProductService = retrofit.create(AddProductService::class.java)
        val productData = ProductData(name, price)

        val call = addProductService.addProduct(productData)
        call.enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddProductActivity, "Producto agregado exitosamente", Toast.LENGTH_SHORT).show()
                    finish() // Regresa a la pantalla anterior
                } else {
                    Toast.makeText(this@AddProductActivity, "Error al agregar el producto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Toast.makeText(this@AddProductActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
