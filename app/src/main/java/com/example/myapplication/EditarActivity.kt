package com.example.myapplication
import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme


class EditarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                    EditarScreen()
                }
            }
        }


    data class Product(
        val type: String="",
        val size: String="",
        val model: String="",
        val price: String="",
    )

    @Composable
    fun EditarScreen() {
        val context = LocalContext.current

        var products by remember { mutableStateOf(listOf(Product())) }

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
                        fontSize = 30.sp,
                        color = Color.White

                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)

                )
                Spacer(modifier = Modifier.height(32.dp))
                products.forEachIndexed { index, product ->
                    ProductRow(
                        product = product,
                        onEditClick = { editedProduct ->
                            products = products.toMutableList().also { it[index] = editedProduct }
                        }
                    )
                }
            }
        }
    }




            @Composable
            fun ProductRow(product: Product, onEditClick: (Product) -> Unit) {
                var editedProduct by remember { mutableStateOf(product) }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    TextField(
                        value = editedProduct.type,
                        onValueChange = { editedProduct = editedProduct.copy(type = it) },
                        label = { Text("Tipo") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextField(
                        value = editedProduct.size,
                        onValueChange = { editedProduct = editedProduct.copy(size = it) },
                        label = { Text("Talla") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextField(
                        value = editedProduct.model,
                        onValueChange = { editedProduct = editedProduct.copy(model = it) },
                        label = { Text("Modelo") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextField(
                        value = editedProduct.price,
                        onValueChange = { editedProduct = editedProduct.copy(price = it) },
                        label = { Text("Precio") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Botón de edición
                    Button(
                        onClick = { onEditClick(editedProduct) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Editar")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
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
