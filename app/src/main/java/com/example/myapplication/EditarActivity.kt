package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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


class EditarActivity : ComponentActivity() {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.0.4:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val service: EditarActivityService by lazy {
        retrofit.create(EditarActivityService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val productId = intent.getStringExtra("PRODUCT_ID")
        setContent {
            MyApplicationTheme {
                EditarScreen(productId)
            }
        }
    }

    data class Product(
        val _id: String = "",
        val Producto: String = "",
        val Precio: Double = 0.0
    )

    interface EditarActivityService {
        @GET("productos/{id}")
        fun getProduct(@Path("id") id: String): Call<Product>

        @PUT("productos/{id}")
        fun updateProduct(@Path("id") id: String, @Body productData: Product): Call<Map<String, String>>
    }

    @Composable
    fun EditarScreen(initialProductId: String?) {
        val context = LocalContext.current

        var productId by remember { mutableStateOf(initialProductId ?: "") }
        var product by remember { mutableStateOf<Product?>(null) }
        var isLoading by remember { mutableStateOf(false) }

        BoxWithBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color(0xFF00668b), shape = RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Banana Shop",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = productId,
                    onValueChange = { productId = it },
                    label = { Text("ID del producto") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        fetchProduct(productId) {
                            product = it
                            isLoading = false

                            // Abrir la actividad para editar el producto
                            val intent = Intent(context, EditarProductoActivity::class.java)
                            intent.putExtra("PRODUCT_ID", productId)
                            startActivity(intent)
                        }
                    }
                ) {
                    Text(text = "Editar Producto")
                }

                if (isLoading) {
                    LoadingIndicator()
                }
            }
        }
    }

    @Composable
    fun LoadingIndicator() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    @Composable
    fun BoxWithBackground(
        content: @Composable () -> Unit,
        background: Painter = painterResource(id = R.drawable.fotofondo)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = background,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            content()
        }
    }

    private fun fetchProduct(id: String, callback: (Product) -> Unit) {
        service.getProduct(id).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    callback(response.body()!!)
                } else {
                    // Handle error response
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                // Handle failure
            }
        })
    }
}
