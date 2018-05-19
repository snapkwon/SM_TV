# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Applications/Android Studio.app/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
# http://developer.android.com/guide/developing/tools/proguard.html
# Add any project specific keep options here:
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
# public *;
#}
# Obfuscation parameters:
#-dontobfuscate

-keepattributes EnclosingMethod
-keepattributes InnerClasses
-dontwarn InnerClasses
-dontoptimize

-useuniqueclassmembernames
-keepattributes SourceFile,LineNumberTable
-allowaccessmodification
# Ignore warnings:
# -dontwarn InnerClasses

-dontwarn org.mockito.**
-dontwarn org.junit.**
-dontwarn com.robotium.**
-dontwarn org.joda.convert.**
# Ignore warnings: We are not using DOM model
-dontwarn com.fasterxml.jackson.databind.ext.DOMSerializer
# Ignore warnings: https://github.com/square/okhttp/wiki/FAQs
-dontwarn com.squareup.okhttp.internal.huc.**
# Ignore warnings: https://github.com/square/okio/issues/60
-dontwarn okio.**
# Ignore warnings: https://github.com/square/retrofit/issues/435
-dontwarn com.google.appengine.api.urlfetch.**

# Ignore log4j warning
-dontwarn org.apache.log4j.** { *;}
-dontwarn org.apache.**

# Keep the pojos used by GSON or Jackson
-keep class com.futurice.project.models.pojo.** { *; }
# Keep GSON stuff
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }
# Keep Jackson stuff
-keep class org.codehaus.** { *; }
-keep class com.fasterxml.jackson.annotation.** { *; }
# Keep these for GSON and Jackson
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
# Keep Retrofit
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
@retrofit.** *;
}
-keepclassmembers class * {
@retrofit.** *;
}

-keep class vn.digital.signage.android.** {*;}
-keep com.jraska.console.** {*;}
-keep com.squareup.leakcanary.** {*;}

-keep class * implements java.io.Serializable { *; }

-keep class com.google.android.gms.**
-dontwarn com.google.android.gms.**