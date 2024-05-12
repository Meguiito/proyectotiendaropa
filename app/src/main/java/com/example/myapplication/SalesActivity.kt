package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

data class Product(
    val name: String,
    val price: Double
)

class SalesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                BoxWithBackground {
                    SalesScreen()
                }
            }
        }
    }
}

@Composable
fun SalesScreen() {
    val products = listOf(
        Product("Producto 1", 9.10),
        Product("Producto 2", 14.50),
        Product("Producto 3", 7.25)
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        ProductTable(products = products)
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
                        color = Color.White // Cambiar el color del texto a blanco
                    )
                )
                Text(
                    "Precio",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color.White // Cambiar el color del texto a blanco
                    )
                )
            }
        }
        items(products.size) { index ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    products[index].name,
                    style = TextStyle(color = Color.White) // Cambiar el color del texto a blanco
                )
                Text(
                    products[index].price.toString(),
                    style = TextStyle(color = Color.White) // Cambiar el color del texto a blanco
                )
            }
        }
    }
}
