# Keep the PerfScout public API (including all methods and fields)
-keep class io.github.dee2604.perfScout.PerfScoutMetrics { *; }

# Keep all API interfaces and delegates
-keep class io.github.dee2604.perfscout.api.** { *; }

# Keep all metric/data/provider classes in all subpackages
-keep class io.github.dee2604.perfscout.** { *; }

# Keep annotations (optional, but good for libraries)
-keepattributes *Annotation*

# Keep line number and source file for better stack traces (optional)
-keepattributes SourceFile,LineNumberTable