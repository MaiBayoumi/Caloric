plugins {
    alias(libs.plugins.android.application)
    id("androidx.navigation.safeargs")
    alias(libs.plugins.google.gms.google.services)

}

android {
    namespace = "com.example.caloric"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.caloric"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }



    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    implementation(libs.lottie)

    // RXJava Dependency
    implementation(libs.rxjava)
    implementation(libs.rxandroid)


//retrofit and gson converter
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.adapter.rxjava3)

    ///glide dependeny for images
    implementation(libs.glide)

    ///room database
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    // optional - RxJava3 support for Room
    implementation(libs.room.rxjava3)

    //google auth
    implementation(libs.play.services.auth)
    implementation(platform(libs.firebase.bom))
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)

    implementation ("com.airbnb.android:lottie:3.4.0")
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.22")
    implementation ("com.airbnb.android:lottie:latest_version")
    implementation ("com.google.firebase:firebase-firestore:24.5.1")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.0")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.0")

    implementation ("com.github.bumptech.glide:glide:4.13.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.0")

    //youtube player
    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.0.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation ("androidx.navigation:navigation-ui-ktx:2.5.3")

    implementation("io.reactivex.rxjava3:rxjava:3.1.3")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation ("com.jakewharton.rxbinding4:rxbinding:4.0.0")

    implementation("com.google.firebase:firebase-storage")


   // RxJava with Room
    implementation ("androidx.room:room-rxjava3:2.5.0")
    implementation ("io.reactivex.rxjava3:rxjava:3.1.6")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")

}