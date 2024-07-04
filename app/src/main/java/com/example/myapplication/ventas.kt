package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import retrofit2.http.POST
import retrofit2.http.Path

data class VentaData(
    val qr_id: String,
    val producto: String,
    val precio: Double,
    val talla: String,
    val tipo: String
)

data class Venta(
    val id_venta: String,
    val qr_id: String,
    val producto: String,
    val precio: Double,
    val talla: String,
    val tipo: String
)

interface VentasService {
    @POST("agregar-venta")
    fun addVenta(@Body ventaData: VentaData): Call<Map<String, String>>

    @GET("ventas")
    fun getVentas(@retrofit2.http.Query("page") page: Int): Call<List<Venta>>
}

interface ProductService {
    @GET("productos/qr/{qr_id}")
    fun getProductoByQrId(@Path("qr_id") qrId: String): Call<Producto>
}

class VentasActivity : ComponentActivity() {
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
                VentasScreen(qrId)
            }
        }
    }

    @Composable
    fun VentasScreen(qrId: String) {
        var screenState by remember { mutableStateOf("initial") }
        var ventas by remember { mutableStateOf<List<Venta>>(emptyList()) }
        var currentPage by remember { mutableStateOf(1) }
        var totalPages by remember { mutableStateOf(1) }

        BoxWithBackgroundForVentas {
            when (screenState) {
                "initial" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "¿Qué deseas hacer?",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                screenState = "addVenta"
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Añadir una venta")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                fetchVentas(1) { fetchedVentas, fetchedTotalPages ->
                                    ventas = fetchedVentas
                                    totalPages = fetchedTotalPages
                                    currentPage = 1
                                    screenState = "showVentas"
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Abrir tabla de ventas")
                        }
                    }
                }
                "addVenta" -> {
                    if (qrId.isNotEmpty()) {
                        addVenta(qrId) {
                            screenState = "initial"
                        }
                    } else {
                        Text("No se encontró el código QR")
                    }
                }
                "showVentas" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        VentasTable(ventas)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (currentPage > 1) {
                                Button(
                                    onClick = {
                                        fetchVentas(currentPage - 1) { fetchedVentas, _ ->
                                            ventas = fetchedVentas
                                            currentPage -= 1
                                        }
                                    }
                                ) {
                                    Text("Anterior")
                                }
                            }
                            if (currentPage < totalPages) {
                                Button(
                                    onClick = {
                                        fetchVentas(currentPage + 1) { fetchedVentas, _ ->
                                            ventas = fetchedVentas
                                            currentPage += 1
                                        }
                                    }
                                ) {
                                    Text("Siguiente")
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                screenState = "initial"
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Volver")
                        }
                    }
                }
            }
        }
    }

    private fun addVenta(qrId: String, onComplete: () -> Unit) {
        val productService = retrofit.create(ProductService::class.java)
        productService.getProductoByQrId(qrId).enqueue(object : Callback<Producto> {
            override fun onResponse(call: Call<Producto>, response: Response<Producto>) {
                if (response.isSuccessful && response.body() != null) {
                    val producto = response.body()!!
                    val ventaData = VentaData(qrId, producto.Producto, producto.Precio, producto.talla, producto.tipo)

                    val ventasService = retrofit.create(VentasService::class.java)
                    ventasService.addVenta(ventaData).enqueue(object : Callback<Map<String, String>> {
                        override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                            if (response.isSuccessful && response.body() != null) {
                                Toast.makeText(this@VentasActivity, "Venta agregada correctamente", Toast.LENGTH_SHORT).show()
                                onComplete()
                            } else {
                                Toast.makeText(this@VentasActivity, "Error al agregar la venta", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                            Toast.makeText(this@VentasActivity, "Error de red al agregar la venta", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(this@VentasActivity, "Producto no encontrado", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Producto>, t: Throwable) {
                Toast.makeText(this@VentasActivity, "Error de red al obtener el producto", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchVentas(page: Int, callback: (List<Venta>, Int) -> Unit) {
        val ventasService = retrofit.create(VentasService::class.java)
        ventasService.getVentas(page).enqueue(object : Callback<List<Venta>> {
            override fun onResponse(call: Call<List<Venta>>, response: Response<List<Venta>>) {
                if (response.isSuccessful && response.body() != null) {
                    val fetchedVentas = response.body()!!
                    val totalPages = response.headers()["Total-Pages"]?.toInt() ?: 1
                    callback(fetchedVentas, totalPages)
                } else {
                    Toast.makeText(this@VentasActivity, "Error al obtener las ventas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Venta>>, t: Throwable) {
                Toast.makeText(this@VentasActivity, "Error de red al obtener las ventas", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @Composable
    fun VentasTable(ventas: List<Venta>) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(ventas) { venta ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(venta.id_venta, color = Color.White)
                    Text(venta.qr_id, color = Color.White)
                    Text(venta.producto, color = Color.White)
                    Text(venta.precio.toString(), color = Color.White)
                    Text(venta.talla, color = Color.White)
                    Text(venta.tipo, color = Color.White)
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }

    @Composable
    fun BoxWithBackgroundForVentas(content: @Composable () -> Unit) {
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
}
