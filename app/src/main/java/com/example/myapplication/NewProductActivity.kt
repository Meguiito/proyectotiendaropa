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

class NewProductActivity : ComponentActivity() {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://127.0.0.1:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val qrId = intent.getStringExtra("qr_id") ?: ""
        val producto = intent.getStringExtra("producto") ?: ""
        val precio = intent.getDoubleExtra("precio", 0.0)
        setContent {
            MyApplicationTheme {
                AddNewProductScreen(qrId, producto, precio)
            }
        }
    }


    @Composable
    fun AddNewProductScreen(qrId: String, producto: String, precio: Double) {
        val (productName, setProductName) = remember { mutableStateOf(producto) }
        val (productPrice, setProductPrice) = remember { mutableStateOf(precio.toString()) }
        BoxWithBackgroundForeditar {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = qrId,
                onValueChange = {},
                label = { Text("Código QR") },
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = productName,
                onValueChange = { setProductName(it) },
                label = { Text("Producto") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = productPrice,
                onValueChange = { setProductPrice(it) },
                label = { Text("Precio") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    addNewProduct(qrId, productName, productPrice.toDoubleOrNull() ?: 0.0)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Nuevo Producto")
            }
        }
    }
        }

    private fun addNewProduct(qrId: String, productName: String, productPrice: Double) {
        val addProductService = retrofit.create(AddProductService::class.java)
        val newProduct = NewClassName(qrId, productName, productPrice)

        val call = addProductService.addProductFromQR(newProduct)
        call.enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@NewProductActivity, "Nuevo producto agregado exitosamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@NewProductActivity, "Producto ya existente o codigo invalido", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Toast.makeText(this@NewProductActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
@Composable
fun BoxWithBackgroundForagregar(content: @Composable () -> Unit) {
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