package com.example.myapplication

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                // Navegación a la pantalla de Ingresar
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Ingresar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Navegación a la pantalla de Consulta
                val intent = Intent(context, SalesActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Consulta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Navegación a la pantalla de Venta
                val intent = Intent(context, SalesActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Venta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Navegación a la pantalla de Editar
                val intent = Intent(context, EditarActivity::class.java)
                context.startActivity(intent)
                // Implementar lógica de edición
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Editar")
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
