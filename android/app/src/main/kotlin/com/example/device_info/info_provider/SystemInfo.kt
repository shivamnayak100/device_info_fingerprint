package com.example.device_info

import android.content.ContentResolver
import android.os.Build
import android.provider.Settings
import org.json.JSONObject

object SystemInfoHelper {
    fun fetchSystemInfo(contentResolver: ContentResolver): String {
        val systemInfo = mutableMapOf<String, String>()

        systemInfo["ACCELEROMETER_ROTATION"] = getSystemSetting(contentResolver, Settings.System.ACCELEROMETER_ROTATION, "System")
        systemInfo["ALARM_ALERT"] = getSystemSetting(contentResolver, Settings.System.ALARM_ALERT, "System")
        systemInfo["BLUETOOTH_DISCOVERABILITY"] = getSystemSetting(contentResolver, Settings.System.BLUETOOTH_DISCOVERABILITY, "System")
        systemInfo["BLUETOOTH_DISCOVERABILITY_TIMEOUT"] = getSystemSetting(contentResolver, Settings.System.BLUETOOTH_DISCOVERABILITY_TIMEOUT, "System")
        systemInfo["DATE_FORMAT"] = getSystemSetting(contentResolver, Settings.System.DATE_FORMAT, "System")
        systemInfo["DIM_SCREEN"] = getSystemSetting(contentResolver, Settings.System.DIM_SCREEN, "System")
        systemInfo["DTMF_TONE_TYPE_WHEN_DIALING"] = getSystemSetting(contentResolver, Settings.System.DTMF_TONE_TYPE_WHEN_DIALING, "System")
        systemInfo["DTMF_TONE_WHEN_DIALING"] = getSystemSetting(contentResolver, Settings.System.DTMF_TONE_WHEN_DIALING, "System")
        systemInfo["END_BUTTON_BEHAVIOR"] = getSystemSetting(contentResolver, Settings.System.END_BUTTON_BEHAVIOR, "System")
        systemInfo["FONT_SCALE"] = getSystemSetting(contentResolver, Settings.System.FONT_SCALE, "System")
        systemInfo["HAPTIC_FEEDBACK_ENABLED"] = getSystemSetting(contentResolver, Settings.System.HAPTIC_FEEDBACK_ENABLED, "System")
        systemInfo["MODE_RINGER_STREAMS_AFFECTED"] = getSystemSetting(contentResolver, Settings.System.MODE_RINGER_STREAMS_AFFECTED, "System")
        systemInfo["MUTE_STREAMS_AFFECTED"] = getSystemSetting(contentResolver, Settings.System.MUTE_STREAMS_AFFECTED, "System")
        systemInfo["NOTIFICATION_SOUND"] = getSystemSetting(contentResolver, Settings.System.NOTIFICATION_SOUND, "System")
        systemInfo["RINGTONE"] = getSystemSetting(contentResolver, Settings.System.RINGTONE, "System")
        systemInfo["SCREEN_BRIGHTNESS"] = getSystemSetting(contentResolver, Settings.System.SCREEN_BRIGHTNESS, "System")
        systemInfo["SCREEN_BRIGHTNESS_MODE"] = getSystemSetting(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, "System")
        systemInfo["SCREEN_OFF_TIMEOUT"] = getSystemSetting(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, "System")
        systemInfo["SOUND_EFFECTS_ENABLED"] = getSystemSetting(contentResolver, Settings.System.SOUND_EFFECTS_ENABLED, "System")
        systemInfo["TEXT_AUTO_CAPS"] = getSystemSetting(contentResolver, Settings.System.TEXT_AUTO_CAPS, "System")
        systemInfo["TEXT_AUTO_PUNCTUATE"] = getSystemSetting(contentResolver, Settings.System.TEXT_AUTO_PUNCTUATE, "System")
        systemInfo["TEXT_AUTO_REPLACE"] = getSystemSetting(contentResolver, Settings.System.TEXT_AUTO_REPLACE, "System")
        systemInfo["TEXT_SHOW_PASSWORD"] = getSystemSetting(contentResolver, Settings.System.TEXT_SHOW_PASSWORD, "System")
        systemInfo["TIME_12_24"] = getSystemSetting(contentResolver, Settings.System.TIME_12_24, "System")
        systemInfo["VIBRATE_ON"] = getSystemSetting(contentResolver, Settings.System.VIBRATE_ON, "System")
        systemInfo["VIBRATE_WHEN_RINGING"] = getSystemSetting(contentResolver, Settings.System.VIBRATE_WHEN_RINGING, "System")

        // Handle moved settings with appropriate checks
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            systemInfo["DATA_ROAMING"] = getSystemSetting(contentResolver, Settings.Global.DATA_ROAMING, "Global")
        }

        return JSONObject(systemInfo as Map<Any?, Any?>).toString()
    }

    private fun getSystemSetting(contentResolver: ContentResolver, setting: String, type: String): String {
        return try {
            when (type) {
                "System" -> Settings.System.getString(contentResolver, setting) ?: "N/A"
                "Secure" -> Settings.Secure.getString(contentResolver, setting) ?: "N/A"
                "Global" -> Settings.Global.getString(contentResolver, setting) ?: "N/A"
                else -> "N/A"
            }
        } catch (e: SecurityException) {
            "Permission Denied"
        }
    }
}
