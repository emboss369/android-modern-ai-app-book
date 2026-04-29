// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.compose) apply false
  // 以下を追加
  alias(libs.plugins.googleDevToolsKsp) apply false
  alias(libs.plugins.androidx.room) apply false
}