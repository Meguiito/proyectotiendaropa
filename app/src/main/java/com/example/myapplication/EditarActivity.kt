package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class EditarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                BoxWithBackground {
                    EditarScreen()
                }
            }
        }
    }

    data class Product(
        val type: String,
        val size: String,
        val model: String,
        val price: Double
    )

    @Composable
    fun EditarScreen() {
        val products = listOf(
            Product("pantalon", "L", "Nike", 20.000),
            // Agrega más productos si es necesario
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Editar Producto",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.White
                ),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            products.forEach { product ->
                ProductRow(product = product)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    @Composable
    fun ProductRow(product: Product) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Tipo: ${product.type}",
                style = TextStyle(
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Talla: ${product.size}",
                style = TextStyle(
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Modelo: ${product.model}",
                style = TextStyle(
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Precio: ${product.price}",
                style = TextStyle(
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Botón de edición
        }
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
