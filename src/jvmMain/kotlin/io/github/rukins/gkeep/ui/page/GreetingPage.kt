package io.github.rukins.gkeep.ui.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import io.github.rukins.gkeep.objects.icons.outlined.DarkMode
import io.github.rukins.gkeep.objects.icons.outlined.LightMode
import io.github.rukins.gkeep.objects.theme.ColorSchemes
import io.github.rukins.gkeep.viewmodel.AppViewModel
import io.github.rukins.gpsoauth.exception.AuthError
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GreetingPage(viewModel: AppViewModel) {
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current

    MaterialTheme(
        colorScheme = ColorSchemes.getColorSchemeByOrdinalAndTheme(
            viewModel.settings.themeOrdinal.value,
            viewModel.settings.enableDarkMode.value
        )
    ) {
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .absolutePadding(20.dp, 20.dp, 20.dp, 20.dp)
            ) {
                Text(
                    "Enter your token:",
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = TextUnit(2.0f, TextUnitType.Em)
                )

                val authTokenValue = remember { mutableStateOf("") }
                val masterTokenValue = remember { mutableStateOf("") }

                val enabledFirst = remember { mutableStateOf(true) }

                OutlinedTextField(
                    singleLine = true,
                    enabled = enabledFirst.value,
                    placeholder = { Text("oauth2_4/***", modifier = Modifier.alpha(0.5f)) },
                    value = authTokenValue.value,
                    onValueChange = { authTokenValue.value = it },
                    label = { Text("Authentication token") },
                    modifier = Modifier
                        .onClick {
                            enabledFirst.value = true
                            masterTokenValue.value = ""
                        }
                        .fillMaxWidth()
                        .padding(10.dp)
                )

                Text(
                    "or",
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = TextUnit(1.5f, TextUnitType.Em)
                )

                OutlinedTextField(
                    singleLine = true,
                    enabled = !enabledFirst.value,
                    placeholder = { Text("aas_et/***", modifier = Modifier.alpha(0.5f)) },
                    value = masterTokenValue.value,
                    onValueChange = { masterTokenValue.value = it },
                    label = { Text("Master token") },
                    modifier = Modifier
                        .onClick {
                            enabledFirst.value = false
                            authTokenValue.value = ""
                        }
                        .fillMaxWidth()
                        .padding(10.dp)
                )

                ElevatedButton(
                    onClick = {
                        scope.launch {
                            try {
                                viewModel.onConfirmLogin(masterTokenValue.value, authTokenValue.value)
                            } catch (e: AuthError) {
                                snackbarHostState.showSnackbar("Authentication error: " + e.errorObject.cause)
                            }
                        }
                    }
                ) {
                    Text("Confirm")
                }

                ElevatedButton(
                    onClick = {
                        scope.launch { viewModel.onChangeTheme() }
                    }
                ) {
                    if (viewModel.settings.enableDarkMode.value) {
                        Icon(Icons.Outlined.LightMode, "LightMode")
                    } else {
                        Icon(Icons.Outlined.DarkMode, "DarkMode")
                    }

                    Text("Change mode", modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp))
                }

                val projectLink: AnnotatedString = getAnnotatedLinkString(
                    "Project on GitHub",
                    "https://github.com/rukins/GKeep-Desktop"
                )
                val howToGetAuthTokenLink: AnnotatedString = getAnnotatedLinkString(
                    "How to get authentication token",
                    "https://github.com/rukins/gpsoauth-java"
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Box(Modifier.weight(0.5f)) {
                        ClickableText(
                            modifier = Modifier.align(Alignment.TopStart),
                            text = howToGetAuthTokenLink,
                            onClick = {
                                howToGetAuthTokenLink.getStringAnnotations("URL", it, it)
                                    .firstOrNull()?.let { s -> uriHandler.openUri(s.item) }
                            }
                        )
                    }

                    Box(Modifier.weight(0.5f)) {
                        ClickableText(
                            modifier = Modifier.align(Alignment.TopEnd),
                            text = projectLink,
                            onClick = {
                                projectLink.getStringAnnotations("URL", it, it)
                                    .firstOrNull()?.let { s -> uriHandler.openUri(s.item) }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun getAnnotatedLinkString(text: String, link: String) = buildAnnotatedString {
    append(text)
    addStyle(
        style = SpanStyle(
            color = MaterialTheme.colorScheme.outline,
            fontSize = TextUnit(1.0f, TextUnitType.Em),
            fontStyle = FontStyle.Italic,
            textDecoration = TextDecoration.Underline
        ), start = 0, end = text.length
    )

    addStringAnnotation(
        tag = "URL",
        annotation = link,
        start = 0,
        end = text.length
    )
}