package dev.phillipslabs.nursingnavigator

import kotlinx.serialization.Serializable

// TODO would this make more sense as a view model?
@Serializable
data class NursingStatus(
    val leftRightToggle: Boolean = false,
)
