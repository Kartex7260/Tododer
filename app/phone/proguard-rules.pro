# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepattributes LineNumberTable,SourceFile
-renamesourcefileattribute SourceFile

#-keep class kanti.sl.** { *; }
#-keep interface kanti.sl.** { *; }
#-keep enum kanti.sl.** { *; }
#
#-keep class kanti.tododer.data.** { *; }
#-keep interface kanti.tododer.data.** { *; }
#-keep enum kanti.tododer.data.** { *; }

-keep class kanti.tododer.data.model.plan.PlanState { *; }
-keep class * extends kanti.tododer.data.model.plan.PlanState { *; }

-keep class kanti.tododer.data.model.todo.TodoState { *; }
-keep class * extends kanti.tododer.data.model.todo.TodoState { *; }

-assumenosideeffects class kanti.tododer.util.log.Logger { *; }