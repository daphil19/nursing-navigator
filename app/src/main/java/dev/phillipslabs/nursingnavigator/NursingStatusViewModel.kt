package dev.phillipslabs.nursingnavigator

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

private const val filename = "toggle_state.txt"

// encoding the defaults helps with being explicit about values, and provides some amount of resistance to changes
// TODO do we want to ignore unknown keys?
private val JSON = Json { encodeDefaults = true }

class NursingStatusViewModel(application: Application) : AndroidViewModel(application) {
    private val persistStateFile = File(getApplication<Application>().filesDir, filename)

    var state by mutableStateOf(loadPersistFile(persistStateFile))
        private set

    fun updateLeftRightToggle(newValue: Boolean) {
        state = state.copy(leftRightToggle = newValue)
        saveStateToDisk()
    }

    private fun saveStateToDisk() {
        viewModelScope.launch(Dispatchers.IO) {
            persistStateFile.writeText(JSON.encodeToString(NursingStatus.serializer(), state))
        }
    }
}

private fun loadPersistFile(file: File) = try {
    JSON.decodeFromString(file.readText())
} catch (_: Exception) {
    Log.d(
        LOG_TAG,
        "Either the state file wasn't found, or the data was corrupted. " +
            "Falling back to the default values"
    )
    NursingStatus()
}

@Serializable
data class NursingStatus(
    val leftRightToggle: Boolean = false,
)
