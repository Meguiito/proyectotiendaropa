package com.example.myapplication



import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme


class InicioActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                BoxWithBackground {
                    InicioScreen()
                }
            }
        }
    }
}

@Composable
fun InicioScreen() {
    val context = LocalContext.current
    // Definir un estilo de texto personalizado
    val titleTextStyle = TextStyle(
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Botón de "volver"
        IconButton(
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
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

        // Columna que contiene los botones de la pantalla
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título "Banana Shop"
            Text(
                text = "Banana Shop",
                style = titleTextStyle,
                modifier = Modifier.padding(top = 40.dp)
            )
            // Botones con texto "Ingresar", "Consultar", "Venta" y "Editar"
            Spacer(modifier = Modifier.height(50.dp))
            Button(
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
            ) {
                Text(text = "Agregar")
            }
            Spacer(modifier = Modifier.height(50.dp))
            Button(
                onClick = {
                    val intent = Intent(context, SalesActivity::class.java)
                    context.startActivity(intent)
                }
            ) {
                Text(text = "Consultar")
            }
            Spacer(modifier = Modifier.height(50.dp))
            Button(
                onClick = {
                    val intent = Intent(context, SalesActivity::class.java)
                    context.startActivity(intent)
                }
            ) {
                Text(text = "Venta")
            }
            Spacer(modifier = Modifier.height(50.dp))
            Button(
                onClick = {
                    val intent = Intent(context, EditarActivity::class.java)
                    context.startActivity(intent)
                    // Implementar lógica de edición
                }
            ) {
                Text(text = "Editar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    MyApplicationTheme {
        InicioScreen()
    }
}
