package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                    QRScannerButton(this)
                }
            }
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
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Escanear QR")
    }
}

@Preview(showBackground = true)
@Composable
fun QRScannerButtonPreview() {
    MyApplicationTheme {
        QRScannerButton(ComponentActivity())
    }
}
