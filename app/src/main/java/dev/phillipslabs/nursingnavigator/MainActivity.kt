package dev.phillipslabs.nursingnavigator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import dev.phillipslabs.nursingnavigator.ui.theme.NursingNavigatorTheme
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private const val LOG_TAG = "NursingNavigator"
private const val filename = "toggle_state.txt"

// encoding the defaults helps with being explicit about values, and provides some amount of resistance to changes
// TODO do we want to ignore unknown keys?
private val JSON = Json { encodeDefaults = true }

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val file = File(applicationContext.filesDir, filename)

        val initialStatus = try {
            JSON.decodeFromString(file.readText())
        } catch (_: Exception) {
            Log.d(
                LOG_TAG,
                "Either the state file wasn't found, or the data was corrupted. " +
                    "Falling back to the default values"
            )
            NursingStatus()
        }

        setContent {
            var status by remember { mutableStateOf(initialStatus) }

            NursingNavigatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val scope = rememberCoroutineScope()

                    Toggle(toggled = status.leftRightToggle, onToggleChange = {
                        status = status.copy(leftRightToggle = it)
                        // Is this safe? Do we need to worry about inopportune cancellation?
                        scope.launch(Dispatchers.IO) {
                            file.writeText(JSON.encodeToString(NursingStatus.serializer(), status))
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun Toggle(toggled: Boolean, onToggleChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "Left", modifier = Modifier.scale(3.0f))
        Switch(checked = toggled, onCheckedChange = onToggleChange, modifier = Modifier.scale(3.0f))
        Text(text = "Right", modifier = Modifier.scale(3.0f))
    }
}
