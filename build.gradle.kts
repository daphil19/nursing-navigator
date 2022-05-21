// Top-level build file where you can add configuration options common to all sub-projects/modules.
val composeVersion by extra("1.1.1")

plugins {
    id("com.android.application") version "7.0.4" apply false
    id("com.android.library") version "7.0.4" apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    kotlin("plugin.serialization") version "1.6.10" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
