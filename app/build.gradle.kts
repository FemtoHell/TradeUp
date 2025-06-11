plugins {
    alias(libs.plugins.android.application)

}

android {
    namespace = "com.example.tradeupsprojecy"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tradeupsprojecy"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // ✅ Thêm buildFeatures nếu cần
    buildFeatures {
        viewBinding = true // Optional: for ViewBinding
    }


}

dependencies {
    // ✅ Cập nhật Core dependencies
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.15.0"))


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries


    // ✅ Thêm Fragment support
    implementation("androidx.fragment:fragment:1.8.1")

    // Navigation - ✅ Cập nhật version
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")

    // Lifecycle - ✅ Cập nhật version
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.2")
    implementation("androidx.lifecycle:lifecycle-livedata:2.8.2")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.8.2")

    // Network - ✅ GIỮ NGUYÊN (OK)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Google Services - ✅ Cập nhật versions
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Image loading - ✅ GIỮ NGUYÊN (OK)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0") // ✅ Thêm

    // Circle ImageView - ✅ GIỮ NGUYÊN (OK)
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // ✅ Thêm RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // ✅ Thêm SwipeRefreshLayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // ✅ Thêm ViewPager2 cho image galleries
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    // Security - ✅ Cập nhật version
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // ✅ Thêm Permission handling
    implementation("pub.devrel:easypermissions:3.0.0")

    // ✅ Thêm Preference cho Settings
    implementation("androidx.preference:preference:1.2.1")

    // Testing - ✅ GIỮ NGUYÊN (OK)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}