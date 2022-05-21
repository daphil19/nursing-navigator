package dev.phillipslabs.nursingnavigator

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class NursingStatus(
    val leftRightToggle: Boolean = false,
)