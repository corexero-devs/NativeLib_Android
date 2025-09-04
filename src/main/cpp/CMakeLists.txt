# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.
# For more examples on how to use CMake, see https://github.com/android/ndk-samples.

# Sets the minimum CMake version required for this project.
cmake_minimum_required(VERSION 3.22.1)

# Declares the project name. The project name can be accessed via ${ PROJECT_NAME},
# Since this is the top level CMakeLists.txt, the project name is also accessible
# with ${CMAKE_PROJECT_NAME} (both CMake variables are in-sync within the top level
# build script scope).
project("nativelib")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
#
# In this top level CMakeLists.txt, ${CMAKE_PROJECT_NAME} is used to define
# the target library name; in the sub-module's CMakeLists.txt, ${PROJECT_NAME}
# is preferred for the same purpose.
#
# In order to load a library into your app from Java/Kotlin, you must call
# System.loadLibrary() and pass the name of the library defined here;
# for GameActivity/NativeActivity derived applications, the same library name must be
# used in the AndroidManifest.xml file.
#add_library(${CMAKE_PROJECT_NAME} SHARED
#        # List C/C++ source files with relative paths to this CMakeLists.txt.
#        nativelib.cpp)

# Specifies libraries CMake should link to your target library. You
# can link libraries from various origins, such as libraries defined in this
# build script, prebuilt third-party libraries, or Android system libraries.
#target_link_libraries(${CMAKE_PROJECT_NAME}
#        # List libraries link to the target library
#        android
#        log)


#It’s the build script for your native (C/C++) code.
#Every time Gradle builds your app, it asks CMake:
#
#“How do I compile these .cpp files into a .so library?”
#
#CMake reads CMakeLists.txt to know:
#
#Which source files to compile
#
#Which libraries to link (log, android, etc.)
#
#Which headers to include
#
#(optional) How to generate extra files, like security_config.h

# --------- Inputs passed from Gradle (per app / per buildType) ----------
# EXPECTED_PKG  : the exact applicationId to allow (e.g., org.corexero.delhi.metroyatri)
# ALLOWED_CERTS : semicolon-separated list of SHA-256 fingerprints (any case; colons OK)
set(EXPECTED_PKG "" CACHE STRING "Expected package name for gate")
set(ALLOWED_CERTS "" CACHE STRING "Semicolon-separated SHA256 fingerprints")

# Build initializer lines for the header (lowercase, no colons)
set(_LINES "")
if (ALLOWED_CERTS)
    # Splits on ; or whitespace
    separate_arguments(ALLOWED_CERTS)
    foreach(S IN LISTS ALLOWED_CERTS)
        string(TOLOWER "${S}" SLOW)
        string(REPLACE ":" "" SNORM "${SLOW}")
        set(_LINES "${_LINES}  \"${SNORM}\",\n")
    endforeach()
endif()

# --------- Generate security_config.h from the template ----------
# Place security_config.h.in next to this CMakeLists.txt
configure_file(
        ${CMAKE_CURRENT_LIST_DIR}/security_config.h.in
        ${CMAKE_CURRENT_BINARY_DIR}/generated/security_config.h
        @ONLY
)

# --------- Build the shared library ----------
add_library(${CMAKE_PROJECT_NAME} SHARED
        nativelib.cpp
)

# Make the generated header visible to your sources
target_include_directories(${CMAKE_PROJECT_NAME} PRIVATE
        ${CMAKE_CURRENT_BINARY_DIR}/generated
)

# Link Android system libs you need
target_link_libraries(${CMAKE_PROJECT_NAME}
        android
        log
)
