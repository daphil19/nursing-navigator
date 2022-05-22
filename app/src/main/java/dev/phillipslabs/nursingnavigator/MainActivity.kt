package dev.phillipslabs.nursingnavigator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import dev.phillipslabs.nursingnavigator.ui.theme.NursingNavigatorTheme

const val LOG_TAG = "NursingNavigator"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: NursingStatusViewModel by viewModels()

        setContent {
            val state = viewModel.state

            NursingNavigatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Toggle(toggled = state.leftRightToggle, onToggleChange = { viewModel.updateLeftRightToggle(it) })
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
