
plugins {
    alias(libs.plugins.ltthuc.android.feature)
    id("kotlin-parcelize")
}

android {
    namespace = "com.ltthuc.feature"
    lint {
        disable +=("MissingDefaultResource")
    }
}

dependencies {
    implementation(libs.androidx.datastore)
    implementation(libs.bundles.network)
    implementation(libs.androidx.paging)
}
