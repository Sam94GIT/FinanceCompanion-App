// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    // OR for KSP (recommended)
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
}