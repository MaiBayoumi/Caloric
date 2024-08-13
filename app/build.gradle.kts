
plugins {
    alias(libs.plugins.android.application)
//   id("com.android.application")
    id ("com.google.gms.google-services")
    id ("androidx.navigation.safeargs")
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
    implementation(libs.room.common)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //retrofit and gson
    implementation ("com.google.code.gson:gson:2.8.5")
    implementation ("com.squareup.retrofit2:retrofit:2.4.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.4.0")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")

    // Import the Firebase BoM
    // Import the Firebase BoM
    implementation (platform("com.google.firebase:firebase-bom:31.1.1"))

    // For example, add the dependencies for Firebase Authentication
    implementation ("com.google.firebase:firebase-auth:21.1.0")

    //RXJAVA
    implementation ("io.reactivex.rxjava3:rxjava:2.5.0")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")

    //Navigation

    implementation ("androidx.navigation:navigation-fragment:2.3.5")
    implementation ("androidx.navigation:navigation-ui:2.3.5")
    //Circular Image
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //Lottie
    implementation ("com.airbnb.android:lottie:3.4.0")

    //Onboarding
    implementation ("com.ramotion.paperonboarding:paper-onboarding:1.1.3")

    // Google play service library (to enabling signing in using google)
    implementation ("com.google.android.gms:play-services-auth:20.4.0")

    //gif
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.22")

    //Viewpager2
    implementation ("androidx.viewpager2:viewpager2:1.0.0")

    //Rounded image view (also related to the view pager)
    implementation ("com.makeramen:roundedimageview:2.3.0")

    //rx retrofit
    implementation ("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")

    //val room_version = ("2.5.0")

    //Room
    implementation ("androidx.room:room-runtime:$2.5.0")
    annotationProcessor ("androidx.room:room-compiler:$2.5.0")

    //RX Room
    implementation("androidx.room:room-rxjava3:$2.5.0")
    implementation ("androidx.sqlite:sqlite:2.3.0")
    testImplementation("androidx.room:room-testing:$2.5.0")

    //Display video
    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:11.1.0")

    //Firestore
    implementation ("com.google.firebase:firebase-firestore:24.4.1")

    //Shimmer effect
    implementation ("com.facebook.shimmer:shimmer:0.5.0")

}


