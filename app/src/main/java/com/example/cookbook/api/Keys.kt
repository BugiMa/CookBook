package com.example.cookbook.api

object Keys {
    init {
        System.loadLibrary("native-lib")
    }

    external fun apiKey(): String
}