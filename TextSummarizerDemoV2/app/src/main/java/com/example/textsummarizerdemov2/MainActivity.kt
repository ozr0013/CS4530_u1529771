package com.example.textsummarizerdemov2

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.textsummarizerdemov2.ui.theme.TextSummarizerDemoV2Theme
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    private val summaryMutable = MutableStateFlow("")
    val summary: StateFlow<String> = summaryMutable.asStateFlow()

    private val isSummarizingMutable = MutableStateFlow(false)
    val isSummarizing: StateFlow<Boolean> = isSummarizingMutable.asStateFlow()

    fun summarizeText(apiKeyInput: String, input: String) {
        val keyToUse = apiKeyInput.trim().ifBlank { BuildConfig.GEMINI_API_KEY }

        if (keyToUse.isBlank()) {
            summaryMutable.value = "Error: Please enter a valid Gemini API Key or set GEMINI_API_KEY in secrets.properties."
            return
        }

        if (input.isBlank()) {
            summaryMutable.value = "Please enter some text to summarize."
            return
        }

        viewModelScope.launch {
            isSummarizingMutable.value = true
            summaryMutable.value = ""

            try {
                // Initialize Google Generative AI Gemini Model
                val generativeModel = GenerativeModel(
                    modelName = "gemini-3.5-flash",
                    apiKey = keyToUse
                )

                val prompt = "Please summarize the following text into clear, concise bullet points or a short paragraph:\n\n$input"
                val response = generativeModel.generateContent(prompt)

                summaryMutable.value = response.text ?: "No summary generated."
            } catch (e: Exception) {
                summaryMutable.value = "Error: ${e.localizedMessage ?: e.message ?: "Failed to generate summary"}"
                Log.e("SummarizeError", e.toString())
            } finally {
                isSummarizingMutable.value = false
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TextSummarizerDemoV2Theme {
                val vm: MyViewModel = viewModel()
                SummarizerUI(vm)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummarizerUI(myVM: MyViewModel) {
    val context = LocalContext.current
    val summary by myVM.summary.collectAsState()
    val isSummarizing by myVM.isSummarizing.collectAsState()

    var userApiKey by remember { mutableStateOf(BuildConfig.GEMINI_API_KEY) }
    var inputText by remember { mutableStateOf("") }
    var showApiKey by remember { mutableStateOf(false) }

    val wordCount = if (inputText.isBlank()) 0 else inputText.trim().split("\\s+".toRegex()).size

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "AI Text Summarizer",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // API Key Input Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Gemini API Key Configuration",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = userApiKey,
                        onValueChange = { userApiKey = it },
                        label = { Text("Enter Gemini API Key") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Key, contentDescription = null)
                        },
                        trailingIcon = {
                            IconButton(onClick = { showApiKey = !showApiKey }) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Toggle Key Visibility"
                                )
                            }
                        },
                        visualTransformation = if (showApiKey) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Input Text Field
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("Original Text to Summarize") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Notes, contentDescription = null)
                },
                placeholder = { Text("Paste long articles, essays, or paragraphs here...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                minLines = 5
            )

            // Character & Word Counter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Words: $wordCount | Characters: ${inputText.length}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            // Summarize Action Button
            Button(
                onClick = { myVM.summarizeText(userApiKey, inputText) },
                enabled = !isSummarizing && inputText.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSummarizing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Summarizing with Gemini AI...")
                } else {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate AI Summary")
                }
            }

            // Result Summary Card
            if (summary.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "AI Summary",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Summary", summary)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Summary copied to clipboard!", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy Summary",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = summary,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SummarizerUIPreview() {
    TextSummarizerDemoV2Theme {
        SummarizerUI(MyViewModel())
    }
}
