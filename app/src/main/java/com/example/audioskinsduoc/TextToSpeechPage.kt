import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun TextToSpeechPage(context: android.content.Context) {
    var text by remember { mutableStateOf("") }
    val quickMessages = listOf(
        "Hola, ¿cómo estás?",
        "Necesito ayuda.",
        "Por favor, espera un momento.",
        "Gracias, buen día.",
        "¿Podrías repetir eso, por favor?",
        "Disculpa, no entendí."
    )

    // Inicializa y configura TextToSpeech
    val textToSpeech = remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(Unit) {
        textToSpeech.value = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                // Establecer el idioma a español
                textToSpeech.value?.language = Locale("es", "ES")
            } else {
                Toast.makeText(context, "Error inicializando Text-to-Speech", Toast.LENGTH_SHORT).show()
            }
        }

        // Liberar recursos de TextToSpeech cuando se elimina el Composable
        onDispose {
            textToSpeech.value?.stop()
            textToSpeech.value?.shutdown()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Ingresa el texto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (text.isNotEmpty()) {
                textToSpeech.value?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                Toast.makeText(context, "Por favor, ingresa algún texto", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Reproducir Texto")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Mensajes Rápidos",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        quickMessages.forEach { message ->
            Button(
                onClick = {
                    textToSpeech.value?.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(message)
            }
        }
    }
}
