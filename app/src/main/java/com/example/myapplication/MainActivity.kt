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
import com.google.zxing.integration.android.IntentIntegrator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QRScannerAndButtons(this)
                }
            }
        }
    }
}

@Composable
fun QRScannerAndButtons(activity: ComponentActivity) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        QRScannerButton(activity)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Navegación a la pantalla de Inicio
                val intent = Intent(context, InicioActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Ir a Inicio")
        }
    }
}

@Composable
fun QRScannerButton(activity: ComponentActivity) {
    Button(
        onClick = {
            // Abrir el escáner QR
            val integrator = IntentIntegrator(activity)
            integrator.setPrompt("Escanea un código QR")
            integrator.initiateScan()
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Escanear QR")
    }
}

@Preview(showBackground = true)
@Composable
fun QRScannerAndButtonsPreview() {
    MyApplicationTheme {
        QRScannerAndButtons(ComponentActivity())
    }
}
