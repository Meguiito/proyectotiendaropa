package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path

data class VentaData(
    val qr_id: String,
    val producto: String,
    val precio: Double
)

interface VentasService {
    @POST("agregar-venta")
    fun addVenta(@Body ventaData: VentaData): Call<Map<String, String>>
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
        BoxWithBackgroundForVentas {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (qrId.isNotEmpty()) {
                    addVenta(qrId)
                } else {
                    Text("No se encontró el código QR")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val intent = Intent(this@VentasActivity, InicioActivity::class.java)
                        startActivity(intent)
                        finish()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver a Inicio")
                }
            }
        }
    }

    private fun addVenta(qrId: String) {
        val ventasService = retrofit.create(VentasService::class.java)
        val productService = retrofit.create(ProductService::class.java)

        val call = productService.getProductoByQrId(qrId)
        call.enqueue(object : Callback<Producto> {
            override fun onResponse(call: Call<Producto>, response: Response<Producto>) {
                if (response.isSuccessful) {
                    val producto = response.body()
                    if (producto != null) {
                        val ventaData = VentaData(qrId, producto.Producto, producto.Precio)
                        val ventaCall = ventasService.addVenta(ventaData)
                        ventaCall.enqueue(object : Callback<Map<String, String>> {
                            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(this@VentasActivity, "Venta registrada exitosamente", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@VentasActivity, "Error al registrar la venta", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                                Toast.makeText(this@VentasActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } else {
                        Toast.makeText(this@VentasActivity, "Producto no encontrado", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@VentasActivity, "Error al obtener el producto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Producto>, t: Throwable) {
                Toast.makeText(this@VentasActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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
