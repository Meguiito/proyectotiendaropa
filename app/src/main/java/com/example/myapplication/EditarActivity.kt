package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
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
        val context = LocalContext.current
        val products = listOf(
            Product("pantalon", "L", "Nike", 20.000),
        )
        BoxWithBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .fillMaxWidth(),
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

                // Título "Editar Producto" centrado
                Text(
                    "Editar Producto",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                products.forEach { product ->
                    ProductRow(product = product)
                    Spacer(modifier = Modifier.height(16.dp))
                }
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
