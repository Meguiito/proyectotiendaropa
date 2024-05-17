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
import retrofit2.http.POST

data class NewClassName(
    val qr_id: String,
    val producto: String,
    val precio: Double
)
data class ProductData(
    val qr_id: String
)
interface AddProductService {
    @POST("ventas")
    fun addProduct(@Body productData: ProductData): Call<Map<String, String>>
    @POST("agregar-venta")
    fun addProductFromQR(@Body productData: NewClassName): Call<Map<String, String>>
}

class AddProductActivity : ComponentActivity() {
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
                    label = { Text("Código QR") },
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
                    Text("Guardar producto")
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
                    Toast.makeText(this@AddProductActivity, "Producto agregado exitosamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@AddProductActivity, "Error al agregar el producto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Toast.makeText(this@AddProductActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
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
        val producto = qrData[1].split(": ")[1]
        val precio = qrData[2].split(": ")[1].toDouble()

        val productData = NewClassName(qrId, producto, precio)

        val call = addProductService.addProductFromQR(productData)
        call.enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddProductActivity, "Producto agregado exitosamente", Toast.LENGTH_SHORT).show()
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

class ScanActivity : com.journeyapps.barcodescanner.CaptureActivity()

@Composable
fun BoxWithBackgroundForAddProduct(content: @Composable () -> Unit) {
    val backgroundImage = painterResource(id = R.drawable.fotofondo) // Asegúrate de tener esta importación
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
