plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.wodo.gymapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wodo.gymapp"
        minSdk = 23
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // Lottie Animation
    implementation("com.airbnb.android:lottie:6.4.1")

    // Firebase BoM (Bill of Materials) to ensure compatible versions
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))

    // Firebase libraries
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-database")

    // Firestore dependency
    implementation("com.google.firebase:firebase-firestore")

    //Google dependency
    implementation ("com.google.android.gms:play-services-auth:21.2.0")

    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")

    // Kotlin Extensions for Activity
    implementation ("androidx.activity:activity-ktx:1.9.1")

    // Kotlin Extensions for Fragment (if needed)
    implementation ("androidx.fragment:fragment-ktx:1.8.2")

}