package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import retrofit2.http.GET
import retrofit2.http.Path

data class Producto(
    val _id: String,
    val Producto: String,
    val Precio: Double,
    val talla: String,
    val tipo: String
)

interface ProductoService {
    @GET("productos/qr/{qr_id}")
    fun getProductoByQrId(@Path("qr_id") qrId: String): Call<Producto>
}

class ConsultarProductoActivity : ComponentActivity() {
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
                BoxWithBackgroundForConsultar {
                    ConsultarProductoScreen(qrId)
                }
            }
        }
    }

    @Composable
    fun ConsultarProductoScreen(qrId: String) {
        val (product, setProduct) = remember { mutableStateOf<Producto?>(null) }

        if (qrId.isNotEmpty()) {
            fetchProductDetails(qrId, setProduct)
        }

        val typography = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (product != null) {
                    Text(
                        text = "Producto: ${product.Producto}",
                        style = typography,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Precio: ${product.Precio}",
                        style = typography,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Talla: ${product.talla}",
                        style = typography,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Tipo: ${product.tipo}",
                        style = typography,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                } else {
                    Text(
                        text = "Buscando producto...",
                        style = typography,
                        color = Color.White
                    )
                }
            }
        }
    }

    private fun fetchProductDetails(qrId: String, setProduct: (Producto?) -> Unit) {
        val productoService = retrofit.create(ProductoService::class.java)
        val call = productoService.getProductoByQrId(qrId)

        call.enqueue(object : Callback<Producto> {
            override fun onResponse(call: Call<Producto>, response: Response<Producto>) {
                if (response.isSuccessful) {
                    setProduct(response.body())
                } else {
                    setProduct(null)
                    Toast.makeText(this@ConsultarProductoActivity, "Producto no encontrado", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Producto>, t: Throwable) {
                setProduct(null)
                Toast.makeText(this@ConsultarProductoActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

@Composable
fun BoxWithBackgroundForConsultar(content: @Composable () -> Unit) {
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
