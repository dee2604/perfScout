# Keep the PerfScout public API (including companion object methods)
-keep class com.deelib.perfScout.PerfScout { *; }

# Keep all model classes (data classes used as return types)
-keep class com.deelib.perfScout.model.** { *; }

# Keep all provider classes (if you want to allow reflection or debugging)
#-keep class com.deelib.perfScout.provider.** { *; }

# Keep annotations (optional, but good for libraries)
-keepattributes *Annotation*

# Keep line number and source file for better stack traces (optional)
-keepattributes SourceFile,LineNumberTable

