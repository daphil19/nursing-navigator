package dev.phillipslabs.nursingnavigator

import android.content.Context
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import dev.phillipslabs.nursingnavigator.ui.theme.NursingNavigatorTheme
import java.io.FileNotFoundException

private const val LOG_TAG = "NursingNavigator"
private const val filename = "toggle_state.txt"


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val initialToggle = try {
            applicationContext.openFileInput(filename).bufferedReader().use { buf ->
                buf.readLine().toBooleanStrictOrNull().also {
                    if (it == null) {
                        Log.d(LOG_TAG, "File data was corrupted. UI will fall back to default state.")
                    }
                }
            }
        } catch (_: FileNotFoundException) {
            null.also { Log.d(LOG_TAG, "File was not found. UI will fall back to default state.") }
        }

        setContent {

            NursingNavigatorTheme {
                var toggled by rememberSaveable { mutableStateOf(initialToggle ?: (false.also { Log.d(LOG_TAG, "No initial value provided... using default fallback of $it.") })) }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                        Text(text = "Left", modifier = Modifier.scale(3.0f))
                        Switch(checked = toggled, onCheckedChange = {
                            toggled = it
                            applicationContext.openFileOutput(filename, Context.MODE_PRIVATE).use { file ->
                                file.write(it.toString().toByteArray())
                            }
                        }, modifier = Modifier.scale(3.0f))
                        Text(text = "Right", modifier = Modifier.scale(3.0f))
                    }
                }
            }
        }
    }
}
