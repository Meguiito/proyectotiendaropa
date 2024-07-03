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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import retrofit2.http.Query

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
    fun getVentas(@Query("page") page: Int): Call<List<Venta>>
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
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
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
                                fetchVentas(currentPage) { fetchedVentas, total, pages ->
                                    ventas = fetchedVentas
                                    totalPages = pages
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
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        VentasTable(ventas)
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    if (currentPage > 1) {
                                        currentPage--
                                        fetchVentas(currentPage) { fetchedVentas, _, _ ->
                                            ventas = fetchedVentas
                                        }
                                    }
                                },
                                enabled = currentPage > 1
                            ) {
                                Text("Anterior")
                            }

                            Button(
                                onClick = {
                                    if (currentPage < totalPages) {
                                        currentPage++
                                        fetchVentas(currentPage) { fetchedVentas, _, _ ->
                                            ventas = fetchedVentas
                                        }
                                    }
                                },
                                enabled = currentPage < totalPages
                            ) {
                                Text("Siguiente")
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

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                IconButton(
                    onClick = {
                        val intent = Intent(this@VentasActivity, InicioActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver a Inicio",
                        tint = Color.White
                    )
                }
            }
        }
    }

    private fun addVenta(qrId: String, onComplete: () -> Unit) {
        val ventasService = retrofit.create(VentasService::class.java)
        val ventaData = VentaData(qrId, "Producto Ejemplo", 10.0, "XL", "Tipo Ejemplo")
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
    }

    private fun fetchVentas(page: Int, callback: (List<Venta>, Int, Int) -> Unit) {
        val ventasService = retrofit.create(VentasService::class.java)
        ventasService.getVentas(page).enqueue(object : Callback<List<Venta>> {
            override fun onResponse(call: Call<List<Venta>>, response: Response<List<Venta>>) {
                if (response.isSuccessful && response.body() != null) {
                    val totalPages = response.headers()["Total-Pages"]?.toIntOrNull() ?: 1
                    val totalItems = response.headers()["Total-Items"]?.toIntOrNull() ?: 0
                    callback(response.body()!!, totalItems, totalPages)
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
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            items(ventas) { venta ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(venta.qr_id, modifier = Modifier.weight(1f), Color.White)
                    Text(venta.producto, modifier = Modifier.weight(1f), Color.White)
                    Text(venta.precio.toString(), modifier = Modifier.weight(1f), Color.White)
                    Text(venta.talla, modifier = Modifier.weight(1f), Color.White)
                    Text(venta.tipo, modifier = Modifier.weight(1f), Color.White)
                }
            }
        }
    }

    @Composable
    fun BoxWithBackgroundForVentas(content: @Composable () -> Unit) {
        val backgroundImage = painterResource(id = R.drawable.fotofondo)
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = backgroundImage,
                contentDescription = "Background Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            content()
        }
    }
}
