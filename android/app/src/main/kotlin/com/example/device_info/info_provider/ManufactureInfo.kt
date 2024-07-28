 package com.example.device_info

import android.content.ContentResolver
import android.os.Build
import android.provider.Settings
import org.json.JSONObject
 
  object DeviceManufactureInfoHelper{
    fun fetchManufacturerInfo(contentResolver: ContentResolver): String {
        val buildInfo = mapOf(
            "BOARD" to Build.BOARD,
            "BOOTLOADER" to Build.BOOTLOADER,
            "BRAND" to Build.BRAND,
            "DEVICE" to Build.DEVICE,
            "DISPLAY" to Build.DISPLAY,
            "FINGERPRINT" to Build.FINGERPRINT,
            "HARDWARE" to Build.HARDWARE,
            "HOST" to Build.HOST,
            "ID" to Build.ID,
            "MANUFACTURER" to Build.MANUFACTURER,
            "MODEL" to Build.MODEL,
            "PRODUCT" to Build.PRODUCT,
            "TAGS" to Build.TAGS,
            "TIME" to Build.TIME.toString(),
            "TYPE" to Build.TYPE,
            "USER" to Build.USER,
            "RADIO" to Build.RADIO,
            "SERIAL" to Build.SERIAL,
            "SUPPORTED_32_BIT_ABIS" to Build.SUPPORTED_32_BIT_ABIS.joinToString(", "),
            "SUPPORTED_64_BIT_ABIS" to Build.SUPPORTED_64_BIT_ABIS.joinToString(", "),
            "SUPPORTED_ABIS" to Build.SUPPORTED_ABIS.joinToString(", ")
        )
        return JSONObject(buildInfo).toString()
    }

  }