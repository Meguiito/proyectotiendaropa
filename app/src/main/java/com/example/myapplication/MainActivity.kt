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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.zxing.integration.android.IntentIntegrator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class NewClassName(
    val qr_id: String,
    val producto: String,
    val precio: Double

)
data class ProductData(
    val qr_id: String
)
interface AddProductService {
    @POST("productos")
    fun addProduct(@Body productData: ProductData): Call<Map<String, String>>
    @POST("agregar-producto")
    fun addProductFromQR(@Body productData: NewClassName): Call<Map<String, String>>
    @GET("productos/qr/{qr_id}")
    fun getProductoByQrId(@Path("qr_id") qrId: String): Call<Producto>
}


class MainActivity : ComponentActivity() {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://127.0.0.1:5000/")
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
        val (qrId, setQrId) = remember { mutableStateOf("") }

        BoxWithBackgroundForAddProduct {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = qrId,
                    onValueChange = { setQrId(it) },
                    label = { Text("Ingresa el Código QR") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        addProduct(qrId)
                        setQrId("")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Buscar QR")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        startQRScanner()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Escanear QR")
                }

                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }


    private fun addProduct(qrId: String) {
        val addProductService = retrofit.create(AddProductService::class.java)
        val productData = ProductData(qrId)

        val call = addProductService.addProduct(productData)
        call.enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Producto agregado exitosamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Error al agregar el producto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startQRScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setOrientationLocked(false)
        integrator.setPrompt("Escanea el código QR")
        integrator.setBeepEnabled(false)
        integrator.setCaptureActivity(ScanActivity::class.java)
        integrator.addExtra("SCAN_WIDTH", 800)
        integrator.addExtra("SCAN_HEIGHT", 800)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val qrContent = result.contents
                processQRContent(qrContent)
            } else {
                Toast.makeText(this, "No se pudo escanear el código QR", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun processQRContent(qrContent: String) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://127.0.0.1:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val addProductService = retrofit.create(AddProductService::class.java)

        // Parsea el contenido del código QR
        val qrData = qrContent.split("\n")
        val qrId = qrData[0].split(": ")[1]

        // Crea un Intent para pasar los datos a la InicioActivity
        val intent = Intent(this@MainActivity, InicioActivity::class.java)
        intent.putExtra("qr_id", qrId)
        startActivity(intent)
    }
}

class ScanActivity : com.journeyapps.barcodescanner.CaptureActivity()

@Composable
fun BoxWithBackgroundForAddProduct(content: @Composable () -> Unit) {
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
