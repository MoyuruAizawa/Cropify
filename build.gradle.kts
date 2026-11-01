buildscript {
  dependencies {
    // AGP 9.5.0's published POM depends on kotlin-gradle-plugin 2.2.10.
    classpath(libs.kotlin.gradle.plugin)
  }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.compose) apply false
}
