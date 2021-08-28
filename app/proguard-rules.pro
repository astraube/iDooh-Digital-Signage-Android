# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\adt-bundle-windows\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

-keepattributes Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

###
# gradle-retrolambda
###
#-dontwarn java.lang.invoke.*
#-dontwarn **$$Lambda$*

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

############
# Custom
############
-keep class br.com.i9algo.taxiadv.** { *; }
-keepclasseswithmembers class * {
    @br.com.i9algo.taxiadv.** *;
}
-keepclassmembers class * {
    @br.com.i9algo.taxiadv.** *;
}

############
# Glide LIB
############
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

############
# retrofit
############
-keepattributes Signature
-keepclassmembernames,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement


############
# otto
############
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}


############
# Android-Iconics LIB
############
-keep class .R
-keep class **.R$* {
    <fields>;
}

#############
# display.io
#############
-keep class io.display.sdk.Controller.** { *;}
-dontwarn io.display.sdk.Controller.*