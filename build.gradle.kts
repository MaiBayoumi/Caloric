// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")

        // For Google sign-in with Firebase
        classpath("com.google.gms:google-services:4.3.14")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
//    id("com.android.application") version "7.4.2" apply false
//    id("com.android.library") version "7.4.2" apply false
}
