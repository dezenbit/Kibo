package com.dezenbit.habitos.ui.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dezenbit.habitos.ui.theme.Purple40

private const val GITHUB_URL = "https://github.com/dezenbit"
private const val X_URL = "https://x.com/Dezenbitw"
private const val INSTAGRAM_URL = "https://www.instagram.com/dezenbit2"
private const val CONTACT_EMAIL = "dezen.dev@gmail.com"

// Reemplaza este enlace por el de tu página real de apoyo (Ko-fi, Buy Me a Coffee, PayPal.me...).
private const val COFFEE_URL = "https://www.buymeacoffee.com/dezenbit"

private const val DEVELOPER_MESSAGE = """¡Hola! Soy Dezen 👋

Si llegaste hasta aquí, probablemente ya estás usando mi proyecto indie.

Hice esta app porque quería crear algo que me gustara, experimentar un poco y, con suerte, hacer algo que también pudiera ser útil para alguien más. ✦

No hay un gran estudio detrás, ni un equipo enorme. Solo yo, una idea y probablemente demasiadas horas frente a una computadora. :v

Espero que disfrutes este proyecto tanto como disfruté haciéndolo.

Y si te gustó y quieres apoyar para que pueda seguir creando cosas así, puedes invitarme un café ☕.
Es completamente voluntario y no desbloquea ninguna función especial ni cambia nada dentro de la app, pero creeme, que se aprecia muchisimo.
Esta app es y seguirá siendo completamente gratis.

Gracias por usar mi app. :)

— Dezen"""

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Acerca de") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(color = Color(0xFF6750A4).copy(alpha = 0.15f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("\uD83C\uDF31", style = MaterialTheme.typography.headlineMedium)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Dezen", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(
                "Developer independiente",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))
            Card(shape = RoundedCornerShape(16.dp)) {
                Text(
                    text = DEVELOPER_MESSAGE,
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { uriHandler.openUri(COFFEE_URL) },
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Purple40)
            ) {
                Icon(Icons.Filled.Coffee, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Invitarme un café")
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Contacto",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            ContactRow(label = "GitHub", value = "github.com/dezenbit") { uriHandler.openUri(GITHUB_URL) }
            ContactRow(label = "X (Twitter)", value = "@Dezenbitw") { uriHandler.openUri(X_URL) }
            ContactRow(label = "Instagram", value = "@dezenbit2") { uriHandler.openUri(INSTAGRAM_URL) }
            ContactRow(label = "Email", value = CONTACT_EMAIL) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$CONTACT_EMAIL")
                }
                context.startActivity(intent)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Hábitos v1.0",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ContactRow(label: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
