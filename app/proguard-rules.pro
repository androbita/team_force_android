-dontwarn javax.servlet.**
-dontwarn org.joda.time.**
-dontwarn org.w3c.dom.**

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepattributes Exceptions,InnerClasses,Signature

-keep class com.google.api.client.**
-keepclassmembers class com.google.api.client.** {
    *;
 }


-keep class com.google.android.gms.**
-keepclassmembers class com.google.android.gms.** {
    *;
 }
-keep class com.google.gson.**
-keepclassmembers class com.google.gson.** {
    *;
 }


-keep class com.google.ads.**
-keepclassmembers class com.google.ads.** {
    *;
 }

-keep class bolts.**
-keepclassmembers class bolts.** {
    *;
 }

-keep class com.google.api.client.** { *; }
-dontwarn com.google.api.client.*
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.*

#-injars libs/AudienceNetwork.jar
#-injars libs/google-api-client-1.18.0-rc.jar
#-injars libs/google-api-client-android-1.18.0-rc.jar
#-injars libs/google-http-client-android-1.18.0-rc.jar


 # The official support library.
-keep class android.support.v4.** { *; }
-keepclassmembers class android.support.v4.** {
    *;
 }
-keep interface android.support.v4.** { *; }
-keep class android.support.v7.** { *; }
-keepclassmembers class android.support.v7.** {
    *;
 }
-keep interface android.support.v7.** { *; }
