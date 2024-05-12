package com.example.myapplication

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null && result.contents != null) {
                val scannedContent = result.contents
                if (Patterns.WEB_URL.matcher(scannedContent).matches()) {
                    // El contenido escaneado es una URL
                    showConfirmationDialog(scannedContent)
                } else {
                    // El contenido escaneado es una cadena (por ejemplo, el nombre del paquete de una aplicación)
                    // Abrir la aplicación utilizando un Intent explícito
                    val appIntent = packageManager.getLaunchIntentForPackage(scannedContent)
                    if (appIntent != null) {
                        showConfirmationDialog(scannedContent)
                    } else {
                        // La aplicación no está instalada en el dispositivo
                        Toast.makeText(this, "La aplicación no está instalada en el dispositivo", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Manejar el caso en que el escaneo haya sido cancelado o no haya producido ningún resultado
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showConfirmationDialog(scannedContent: String) {
        val context = this
        AlertDialog.Builder(this)
            .setMessage("¿Desea abrir el contenido escaneado?")
            .setPositiveButton("Sí") { dialog, which ->
                if (Patterns.WEB_URL.matcher(scannedContent).matches()) {
                    // Abrir la URL en un navegador web
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(scannedContent))
                    startActivity(browserIntent)
                } else {
                    // Abrir la aplicación utilizando un Intent explícito
                    val appIntent = packageManager.getLaunchIntentForPackage(scannedContent)
                    if (appIntent != null) {
                        startActivity(appIntent)
                    } else {
                        // La aplicación no está instalada en el dispositivo
                        Toast.makeText(context, "La aplicación no está instalada en el dispositivo", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("No", null)
            .show()
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

@Preview(showBackground = true)
@Composable
fun QRScannerAndButtonsPreview() {
    MyApplicationTheme {
        QRScannerAndButtons(ComponentActivity())
    }
}
