## Code to make this work in multi module envionment

```kotlin
rootProject.project(":nativelib").pluginManager.withPlugin("com.android.library") {
rootProject.project(":nativelib").extensions.configure<LibraryExtension> {
defaultConfig {
val properties = Properties()
val localPropertiesFile = project.file("${project.projectDir}/nativeLib.properties")
if (localPropertiesFile.exists()) {
properties.load(localPropertiesFile.inputStream())
} else {
throw Error("${project.projectDir}/nativeLib.properties file is missing!")
}
val debugSha = properties.getProperty("DEBUG_CERT_SHA256") as String
val releaseSha = properties.getProperty("RELEASE_CERT_SHA256") as String
val applicationId = android.namespace
if (applicationId == null) {
throw Error("ApplicationId/Namespace not found! Make sure you have set the namespace in the android block.")
}
val allowedCerts = listOf(debugSha, releaseSha).joinToString(";")
externalNativeBuild {
cmake {
cppFlags("")
arguments += listOf(
"-DEXPECTED_PKG=${applicationId}",
"-DALLOWED_CERTS=${allowedCerts}"
)
}
}
}
}
}
