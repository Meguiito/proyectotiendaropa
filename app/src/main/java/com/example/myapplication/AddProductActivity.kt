package com.example.myapplication


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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class ProductData(
    val qr_id: String
)

interface AddProductService {
    @POST("ventas") // Endpoint actualizado
    fun addProduct(@Body productData: ProductData): Call<Map<String, String>>
}

class AddProductActivity : ComponentActivity() {
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
                    label = { Text("Código QR") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        addProduct(qrId)
                        // Limpiar el campo después de agregar el producto
                        setQrId("")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar producto")
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
}

@Composable
fun BoxWithBackgroundForAddProduct(content: @Composable () -> Unit) {
    val backgroundImage = painterResource(id = R.drawable.fotofondo) // Asegúrate de tener esta importación
    Box(
        modifier = Modifier.fillMaxSize(), // Esta línea hace que el contenedor Box ocupe todo el espacio disponible
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
