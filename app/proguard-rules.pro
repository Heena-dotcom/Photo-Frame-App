-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile,LineNumberTable, *Annotation*, EnclosingMethod
 -dontwarn android.webkit.JavascriptInterface
 -dontwarn com.googlecode.mp4parser.**
-dontwarn android.support.v4.**
#
 ##---------------Begin: proguard configuration common for all Android apps ----------
 -optimizationpasses 10
 -dontusemixedcaseclassnames
 -dontskipnonpubliclibraryclasses
 -dontskipnonpubliclibraryclassmembers
 -dontpreverify
 -verbose
 -dump class_files.txt
 -printseeds seeds.txt
 -printusage unused.txt
 -printmapping mapping.txt
 -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

 -allowaccessmodification
 -renamesourcefileattribute SourceFile
 -keepattributes SourceFile,LineNumberTable
 -repackageclasses ''

 -dontnote com.android.vending.licensing.ILicensingService

 -dontwarn okio.**
 -dontwarn org.apache.http.**
 -keep public class com.google.android.gms.* { public *; }
 -dontwarn com.google.android.gms.**
 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }