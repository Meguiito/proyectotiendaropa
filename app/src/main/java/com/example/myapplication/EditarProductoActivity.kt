package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

data class ProductDataa(
    val Producto: String,
    val Precio: String
)

interface EditProductService {
    @PUT("productos/{id}")
    fun editProduct(@Path("id") id: String, @Body productData: ProductDataa): Call<Map<String, String>>
}

class EditarProductoActivity : ComponentActivity() {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.0.4:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                EditarProductoScreen()
            }
        }
    }

    @Composable
    fun EditarProductoScreen() {
        val (productName, setProductName) = remember { mutableStateOf("") }
        val (productPrice, setProductPrice) = remember { mutableStateOf("") }

        BoxWithBackground {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = productName,
                    onValueChange = setProductName,
                    label = { Text("Nuevo nombre del producto") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = productPrice,
                    onValueChange = setProductPrice,
                    label = { Text("Nuevo precio del producto") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        editProduct(productName, productPrice)
                        setProductName("")
                        setProductPrice("")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar cambios")
                }
            }
        }
    }

    private fun editProduct(name: String, price: String) {
        val editProductService = retrofit.create(EditProductService::class.java)

        // Convierte el precio a Double
        val parsedPrice = price.toDoubleOrNull()
        if (parsedPrice == null) {
            Toast.makeText(
                this,
                "Error: Precio no válido",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Obtener la ID del producto actual que se está editando
        val productId = intent.getStringExtra("PRODUCT_ID")

        if (productId.isNullOrEmpty()) {
            Toast.makeText(
                this,
                "Error: ID del producto no válida",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Crear un objeto ProductData con los datos proporcionados
        val productData = ProductDataa(name, price.toString())

        val call = editProductService.editProduct(productId, productData)
        call.enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(
                call: Call<Map<String, String>>,
                response: Response<Map<String, String>>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@EditarProductoActivity,
                        "Producto editado exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@EditarProductoActivity,
                        "Error al editar el producto: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Toast.makeText(
                    this@EditarProductoActivity,
                    "Error de red: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
