package com.example.myapplication

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.Icons


// Definir un estilo de texto personalizado
val titleTextStyle = TextStyle(
    fontSize = 30.sp,
    fontWeight = FontWeight.Bold,
    color = Color.Black
)

// Composable pantalla de Banana Shop
@Composable
fun BananaShopScreen() {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Botón de "volver"
            IconButton(
                onClick = {},
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
                    onClick = {}
                ) {
                    Text(text = "Ingresar")
                }
                Spacer(modifier = Modifier.height(50.dp))
                Button(
                    onClick = {}
                ) {
                    Text(text = "Consultar")
                }
                Spacer(modifier = Modifier.height(50.dp))
                Button(
                    onClick = {}
                ) {
                    Text(text = "Venta")
                }
                Spacer(modifier = Modifier.height(50.dp))
                Button(
                    onClick = {}
                ) {
                    Text(text = "Editar")
                }
            }
        }
    }
}
