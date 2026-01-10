# ===================================
# FloatView 库混淆配置
# ===================================

# 保留公开 API 类和方法
-keep class com.yzq.floating_view.FloatingViewManager { *; }
-keep class com.yzq.floating_view.FloatingViewManager$Builder { *; }
-keep class com.yzq.floating_view.FloatingViewConfig { *; }
-keep class com.yzq.floating_view.FloatingViewListener { *; }
-keep class com.yzq.floating_view.FloatViewLog { *; }

# 保留 Builder 模式的链式调用
-keepclassmembers class com.yzq.floating_view.FloatingViewManager$Builder {
    public <methods>;
}

-keepclassmembers class com.yzq.floating_view.FloatingViewConfig {
    public <methods>;
}

# 保留接口实现
-keep interface com.yzq.floating_view.FloatingViewListener { *; }

# 保留 Kotlin 相关
-keep class kotlin.Metadata { *; }
-keepclassmembers class **$WhenMappings {
    <fields>;
}

# 保留 Kotlin 扩展函数（如果有公开的）
-keepclassmembers class com.yzq.floating_view.ActivityExtKt {
    public <methods>;
}

# 保留枚举
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留 Parcelable
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

# 保留 Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
