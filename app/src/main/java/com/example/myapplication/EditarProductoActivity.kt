package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

// Definición del data class ProductoEdit dentro del archivo EditarProductoActivity.kt
data class ProductoEdit(
    val _id: String,
    val Producto: String,
    val Precio: Double,
    val qr_id: String,
    val talla: String,
    val tipo: String
)

interface EditProductService {
    @GET("productos/qr/{qr_id}")
    fun getProductByQrId(@Path("qr_id") qrId: String): Call<ProductoEdit>

    @PUT("productoss/{id}")
    fun updateProduct(@Path("id") id: String, @Body product: ProductoEdit): Call<Map<String, String>>
}

class EditarProductoActivity : ComponentActivity() {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.13:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val qrId = intent.getStringExtra("qr_id") ?: ""
        setContent {
            MyApplicationTheme {
                EditProductScreen(qrId)
            }
        }
    }

    @Composable
    fun EditProductScreen(qrId: String) {
        var product by remember { mutableStateOf<ProductoEdit?>(null) }
        var productName by remember { mutableStateOf("") }
        var productPrice by remember { mutableStateOf("") }
        var productSize by remember { mutableStateOf("") }
        var productType by remember { mutableStateOf("") }

        LaunchedEffect(qrId) {
            fetchProductDetails(qrId) { fetchedProduct ->
                product = fetchedProduct
                if (fetchedProduct != null) {
                    productName = fetchedProduct.Producto
                    productPrice = fetchedProduct.Precio.toString()
                    productSize = fetchedProduct.talla
                    productType = fetchedProduct.tipo
                }
            }
        }

        BoxWithBackgroundForeditar {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (product != null) {
                    TextField(
                        value = productName,
                        onValueChange = { productName = it },
                        label = { Text("Producto") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = productPrice,
                        onValueChange = { productPrice = it },
                        label = { Text("Precio") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = productSize,
                        onValueChange = { productSize = it },
                        label = { Text("Talla") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = productType,
                        onValueChange = { productType = it },
                        label = { Text("Tipo") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (product != null) {
                                val updatedPrice = productPrice.toDoubleOrNull()
                                if (updatedPrice != null) {
                                    updateProduct(product!!._id, productName, updatedPrice, productSize, productType, qrId)
                                } else {
                                    Toast.makeText(this@EditarProductoActivity, "Precio inválido", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Actualizar Producto")
                    }
                } else {
                    Text("Buscando producto...")
                }
            }
        }
    }

    private fun fetchProductDetails(qrId: String, onProductFetched: (ProductoEdit?) -> Unit) {
        val productService = retrofit.create(EditProductService::class.java)
        val call = productService.getProductByQrId(qrId)

        call.enqueue(object : Callback<ProductoEdit> {
            override fun onResponse(call: Call<ProductoEdit>, response: Response<ProductoEdit>) {
                if (response.isSuccessful) {
                    onProductFetched(response.body())
                } else {
                    onProductFetched(null)
                    Toast.makeText(this@EditarProductoActivity, "Producto no encontrado", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductoEdit>, t: Throwable) {
                onProductFetched(null)
                Toast.makeText(this@EditarProductoActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateProduct(id: String, name: String, price: Double, size: String, type: String, qrId: String) {
        val productService = retrofit.create(EditProductService::class.java)
        val updatedProduct = ProductoEdit(id, name, price, qrId, size, type)
        val call = productService.updateProduct(id, updatedProduct)

        call.enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditarProductoActivity, "Producto actualizado exitosamente", Toast.LENGTH_SHORT).show()
                    navigateBackToInicio(qrId)
                } else {
                    Toast.makeText(this@EditarProductoActivity, "Error al actualizar el producto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Toast.makeText(this@EditarProductoActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateBackToInicio(qrId: String) {
        val intent = Intent(this, InicioActivity::class.java)
        intent.putExtra("qr_id", qrId)
        startActivity(intent)
        finish()  // Finaliza la actividad actual para evitar que el usuario regrese a ella con el botón de retroceso
    }
}

@Composable
fun BoxWithBackgroundForeditar(content: @Composable () -> Unit) {
    val backgroundImage = painterResource(id = R.drawable.fotofondo)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = backgroundImage,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        content()
    }
}
