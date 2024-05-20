package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class InicioActivity : ComponentActivity() {
    private var qrId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        qrId = intent.getStringExtra("qr_id") ?: ""
        setContent {
            MyApplicationTheme {
                InicioScreen(qrId)
            }
        }
    }

    @Composable
    fun InicioScreen(qrId: String) {
        BoxWithBackgroundForinicio {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    val intent = Intent(this@InicioActivity, NewProductActivity::class.java)
                    intent.putExtra("qr_id", qrId)
                    startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Agregar Producto")
            }

            Button(
                onClick = {
                    val intent = Intent(this@InicioActivity, ConsultarProductoActivity::class.java)
                    intent.putExtra("qr_id", qrId)
                    startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Consultar Producto")
            }

            Button(
                onClick = {
                    val intent = Intent(this@InicioActivity, EditarProductoActivity::class.java)
                    intent.putExtra("qr_id", qrId)
                    startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Editar Productos")
            }

            Button(
                onClick = {
                    val intent = Intent(this@InicioActivity, VentasActivity::class.java)
                    intent.putExtra("qr_id", qrId)
                    startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Ventas")
            }

            Button(
                onClick = {
                    val intent = Intent(this@InicioActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Volver a Main")
            }
        }
    }
}
}
@Composable
fun BoxWithBackgroundForinicio(content: @Composable () -> Unit) {
    val backgroundImage = painterResource(id = R.drawable.fotofondo) // Asegúrate de tener esta importación
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

