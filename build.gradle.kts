// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
/*
dependencies {
     Para los iconos básicos
    implementation("androidx.compose.material:material-icons-core")

    // Para el catálogo completo (aquí es donde vive la mayoría)
    implementation("androidx.compose.material:material-icons-extended")
}
