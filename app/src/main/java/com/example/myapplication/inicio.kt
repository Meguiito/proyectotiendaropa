package com.example.myapplication



import android.content.Intent
import android.os.Bundle
//import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme


class InicioActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
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
        color = Color.Black
    )

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Botón de "volver"
            IconButton(
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.align(Alignment.TopStart)
                    .padding(16.dp)
                    .background(Color(0xFF00668b), shape = RoundedCornerShape(8.dp))
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
            // Título "Banana Shop"
            Text(
                text = "Banana Shop",
                style = titleTextStyle,
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 60.dp)
            )

            // Columna que contiene los botones de la pantalla
            Column(
                modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Botones con texto "Ingresar", "Consultar", "Venta" y "Editar"
                Spacer(modifier = Modifier.height(8.dp))
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
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    MyApplicationTheme {
        InicioScreen()
    }
}
