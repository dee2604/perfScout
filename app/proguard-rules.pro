# Keep the PerfScout public API (including all methods and fields)
-keep class com.deelib.perfScout.PerfScout { *; }

# Keep all API interfaces and delegates
-keep class com.deelib.perfScout.api.** { *; }

# Keep all metric/data/provider classes in all subpackages
-keep class com.deelib.perfScout.** { *; }

# Keep annotations (optional, but good for libraries)
-keepattributes *Annotation*

# Keep line number and source file for better stack traces (optional)
-keepattributes SourceFile,LineNumberTable