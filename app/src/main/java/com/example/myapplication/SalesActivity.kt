package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val context = LocalContext.current
    val products = listOf(
        Product("Producto 1", 9.10),
        Product("Producto 2", 14.50),
        Product("Producto 3", 7.25)
    )
    BoxWithBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // Botón de "volver"
            IconButton(
                onClick = {
                    val intent = Intent(context, InicioActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color(0xFF00668b), shape = RoundedCornerShape(8.dp))
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Título "Banana Shop"
            Text(
                text = "Banana Shop",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Título "Banana Shop"
            Text(
                text = "Venta",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            ProductTable(products = products)

            Spacer(modifier = Modifier.height(70.dp))
            // Botones Agregar mas productos o Terminar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Text(text = "Agregar más productos")
                }

                Button(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Text(text = "Terminar")
                }
            }
        }
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
