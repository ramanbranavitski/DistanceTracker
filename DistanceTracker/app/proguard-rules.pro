# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\JavaDevelopmentTools\AndroidSDK\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# from https://github.com/google/gcm/blob/master/samples/android/gcm-demo/proguard.flags
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-assumenosideeffects class android.util.Log { *; }

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keep class android.support.v7.widget.SearchView { *; }

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
  public static final *** CREATOR;
}

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

# Retrofit http://square.github.io/retrofit/
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Realm https://realm.io/docs/java/latest/#proguard
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class * { *; }
-dontwarn javax.**
-dontwarn io.realm.**

# Crittercism http://docs.crittercism.com/android/android.html#configuring-proguard-symbolication
-keep public class com.crittercism.**
-keepclassmembers public class com.crittercism.* {
    *;
}
-keepattributes SourceFile, LineNumberTable

# Google GCM
-keep class * extends java.util.ListResourceBundle {
   protected Object[][] getContents();
}
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
   public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
   @com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable {
   public static final ** CREATOR;
}

# Amazon S3
-keep class org.apache.** { *; }
-keep class com.amazonaws.** { *; }
-keep class org.codehaus.** { *; }
-keep class org.joda.convert.* { *; }
-keepattributes Signature,*Annotation*,EnclosingMethod

-dontwarn com.amazonaws.auth.policy.conditions.S3ConditionFactory
-dontwarn org.joda.time.**
-dontwarn com.fasterxml.jackson.databind.**
-dontwarn javax.xml.stream.events.**
-dontwarn org.codehaus.jackson.**
-dontwarn org.apache.commons.logging.impl.**
-dontwarn org.ietf.jgss.**
-dontwarn org.w3c.dom.bootstrap.**

-dontwarn com.fasterxml.jackson.core.**
-dontwarn org.apache.http.conn.**
-dontwarn com.amazonaws.metrics.**
-dontwarn com.amazonaws.http.**

# temporally workaround for API23(apache classes removed)
# https://code.google.com/p/android-developer-preview/issues/detail?id=3001
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
-dontwarn org.apache.**

# javaMail
-keep class javamail.** {*;}
-keep class javax.mail.** {*;}
-keep class javax.activation.** {*;}

-keep class com.sun.mail.dsn.** {*;}
-keep class com.sun.mail.handlers.** {*;}
-keep class com.sun.mail.smtp.** {*;}
-keep class com.sun.mail.util.** {*;}
-keep class mailcap.** {*;}
-keep class mimetypes.** {*;}
-keep class myjava.awt.datatransfer.** {*;}
-keep class org.apache.harmony.awt.** {*;}
-keep class org.apache.harmony.misc.** {*;}