package com.example.myapplication

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
                    QRScannerAndSalesButtons(this)
                }
            }
        }
    }
}

@Composable
fun QRScannerAndSalesButtons(activity: ComponentActivity) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        QRScannerButton(activity)
        Spacer(modifier = Modifier.height(16.dp))
        SalesButton()
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

@Composable
fun SalesButton() {
    Button(
        onClick = {
            // Lógica para la funcionalidad de "Ventas" (puede estar vacía por ahora)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Ventas")
    }
}

@Preview(showBackground = true)
@Composable
fun QRScannerAndSalesButtonsPreview() {
    MyApplicationTheme {
        QRScannerAndSalesButtons(ComponentActivity())
    }
}
