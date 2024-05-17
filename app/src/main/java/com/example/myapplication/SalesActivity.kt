package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import retrofit2.http.GET
import retrofit2.http.Query

data class Product(
    val _id: String,
    val name: String,
    val price: Double
)

data class PagedResponse(
    val ventas: List<Product>,
    val total_pages: Int,
    val current_page: Int
)

interface SalesProductService {
    @GET("ventas")
    fun getProducts(@Query("page") page: Int, @Query("limit") limit: Int): Call<PagedResponse>
}

class SalesActivity : ComponentActivity() {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://127.0.0.1:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private var products by mutableStateOf(emptyList<Product>())
    private var currentPage by mutableStateOf(1)
    private var isLoading by mutableStateOf(true)
    private var totalPages by mutableStateOf(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                SalesScreen()
            }
        }
        loadProducts()
    }

    private fun loadProducts(page: Int = 1) {
        isLoading = true
        val salesProductService = retrofit.create(SalesProductService::class.java)
        val call = salesProductService.getProducts(page, 15)

        call.enqueue(object : Callback<PagedResponse> {
            override fun onResponse(call: Call<PagedResponse>, response: Response<PagedResponse>) {
                isLoading = false
                if (response.isSuccessful) {
                    val pagedResponse = response.body()!!
                    products = pagedResponse.ventas
                    totalPages = pagedResponse.total_pages
                    currentPage = pagedResponse.current_page
                } else {
                    Toast.makeText(applicationContext, "Error al obtener productos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PagedResponse>, t: Throwable) {
                isLoading = false
                Toast.makeText(applicationContext, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToAddProductScreen() {
        val intent = Intent(this, AddProductActivity::class.java)
        startActivity(intent)
    }

    @Composable
    fun SalesScreen() {
        val context = LocalContext.current
        BoxWithBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                // Botón de "volver"
                IconButton(
                    onClick = { /* Acción al presionar el botón de volver */ },
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

                // Título "Banana Shop"
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
                // Título "Venta"
                Text(
                    text = "Venta",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    ProductTable(products = products)
                }

                Spacer(modifier = Modifier.height(16.dp))

                PaginationControls(
                    currentPage = currentPage,
                    totalPages = totalPages,
                    onPageSelected = { page ->
                        currentPage = page
                        loadProducts(page)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botones Agregar más productos o Terminar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { navigateToAddProductScreen() },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        Text(text = "Agregar más productos")
                    }
                }
            }
        }
    }

    @Composable
    fun ProductTable(products: List<Product>) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Producto",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        "Precio",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
            items(products) { product ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        product.name,
                        style = TextStyle(color = Color.White)
                    )
                    Text(
                        product.price.toString(),
                        style = TextStyle(color = Color.White)
                    )
                }
            }
        }
    }

    @Composable
    fun PaginationControls(
        currentPage: Int,
        totalPages: Int,
        onPageSelected: (Int) -> Unit
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            for (page in 1..totalPages) {
                Button(
                    onClick = { onPageSelected(page) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (page == currentPage) Color.Gray else Color.LightGray
                    ),
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(text = page.toString())
                }
            }
        }
    }
}
