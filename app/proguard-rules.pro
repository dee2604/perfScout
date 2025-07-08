# Keep the PerfScout facade (public API)
-keep class com.deelib.perfScout.PerfScout { *; }

# Keep all public model classes (data returned to host apps)
-keep class com.deelib.perfScout.model.** { *; }

# If you use @Keep annotations, keep those members
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

# (Optional) Keep enums used in public API
-keepclassmembers enum * { *; }

# Remove provider keep rules (they are now internal and will be obfuscated/removed)

# (Optional) Keep line numbers for better stack traces in crash reports
#-keepattributes SourceFile,LineNumberTable

