-keep class com.d.lib.common.** {*;}

# ----- ButterKnife -----
-keep class butterknife.** {*;}
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder {*;}
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# ----- nineoldandroids -----
-keep class com.nineoldandroids.** {*;}
-dontwarn com.nineoldandroids.*

# ----- glide -----
-keep class com.bumptech.glide.** {*;}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# ----- xrv -----
-keep class com.d.lib.xrv.** {*;}
