package com.corexero.nativelib

import android.util.Log

object NativeLib {

    init {
        try {
            System.loadLibrary("nativelib")
            System.loadLibrary("sqlcipher")
        } catch (e: UnsatisfiedLinkError) {
            Log.e("NativeLib", "Failed to load native library", e)
        }
    }

    @JvmStatic
    external fun getDBEncryptKey(): String

    @JvmStatic
    external fun getReviewApiKey(): String

    @JvmStatic
    external fun getSignerSha256(): Array<String>

    @JvmStatic
    external fun isSelfSignedWithTrustedKey(): Boolean
}