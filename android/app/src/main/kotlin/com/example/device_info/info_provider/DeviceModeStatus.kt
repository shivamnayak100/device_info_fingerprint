package com.example.device_info

import android.content.ContentResolver
import android.os.Build
import android.provider.Settings
import org.json.JSONObject

object DeviceSettingsHelper {
    fun fetchDeviceSettingInfo(contentResolver: ContentResolver): String {
        val globalSettings = mutableMapOf<String, String?>(
            "ADB_ENABLED" to Settings.Global.getString(contentResolver, Settings.Global.ADB_ENABLED),
            "AIRPLANE_MODE_ON" to Settings.Global.getString(contentResolver, Settings.Global.AIRPLANE_MODE_ON),
            "AIRPLANE_MODE_RADIOS" to Settings.Global.getString(contentResolver, Settings.Global.AIRPLANE_MODE_RADIOS),
            "ALWAYS_FINISH_ACTIVITIES" to Settings.Global.getString(contentResolver, Settings.Global.ALWAYS_FINISH_ACTIVITIES),
            "ANIMATOR_DURATION_SCALE" to Settings.Global.getString(contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE),
            "APPLY_RAMPING_RINGER" to Settings.Global.getString(contentResolver, Settings.Global.APPLY_RAMPING_RINGER),
            "AUTO_TIME" to Settings.Global.getString(contentResolver, Settings.Global.AUTO_TIME),
            "AUTO_TIME_ZONE" to Settings.Global.getString(contentResolver, Settings.Global.AUTO_TIME_ZONE),
            "BLUETOOTH_ON" to Settings.Global.getString(contentResolver, Settings.Global.BLUETOOTH_ON),
            "BOOT_COUNT" to Settings.Global.getString(contentResolver, Settings.Global.BOOT_COUNT),
            "CONTACT_METADATA_SYNC_ENABLED" to Settings.Global.getString(contentResolver, Settings.Global.CONTACT_METADATA_SYNC_ENABLED),
            "DEBUG_APP" to Settings.Global.getString(contentResolver, Settings.Global.DEBUG_APP),
            "DEVELOPMENT_SETTINGS_ENABLED" to Settings.Global.getString(contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED),
            "DEVICE_NAME" to Settings.Global.getString(contentResolver, Settings.Global.DEVICE_NAME),
            "DEVICE_PROVISIONED" to Settings.Global.getString(contentResolver, Settings.Global.DEVICE_PROVISIONED),
            "HTTP_PROXY" to Settings.Global.getString(contentResolver, Settings.Global.HTTP_PROXY),
            "INSTALL_NON_MARKET_APPS" to Settings.Global.getString(contentResolver, Settings.Global.INSTALL_NON_MARKET_APPS),
            "MODE_RINGER" to Settings.Global.getString(contentResolver, Settings.Global.MODE_RINGER),
            "NETWORK_PREFERENCE" to Settings.Global.getString(contentResolver, Settings.Global.NETWORK_PREFERENCE),
            "RADIO_BLUETOOTH" to Settings.Global.getString(contentResolver, Settings.Global.RADIO_BLUETOOTH),
            "RADIO_CELL" to Settings.Global.getString(contentResolver, Settings.Global.RADIO_CELL),
            "RADIO_NFC" to Settings.Global.getString(contentResolver, Settings.Global.RADIO_NFC),
            "RADIO_WIFI" to Settings.Global.getString(contentResolver, Settings.Global.RADIO_WIFI),
            "SHOW_PROCESSES" to Settings.Global.getString(contentResolver, Settings.Global.SHOW_PROCESSES),
            "STAY_ON_WHILE_PLUGGED_IN" to Settings.Global.getString(contentResolver, Settings.Global.STAY_ON_WHILE_PLUGGED_IN),
            "TRANSITION_ANIMATION_SCALE" to Settings.Global.getString(contentResolver, Settings.Global.TRANSITION_ANIMATION_SCALE),
            "USB_MASS_STORAGE_ENABLED" to Settings.Global.getString(contentResolver, Settings.Global.USB_MASS_STORAGE_ENABLED),
            "USE_GOOGLE_MAIL" to Settings.Global.getString(contentResolver, Settings.Global.USE_GOOGLE_MAIL),
            "WAIT_FOR_DEBUGGER" to Settings.Global.getString(contentResolver, Settings.Global.WAIT_FOR_DEBUGGER),
            "WIFI_DEVICE_OWNER_CONFIGS_LOCKDOWN" to Settings.Global.getString(contentResolver, Settings.Global.WIFI_DEVICE_OWNER_CONFIGS_LOCKDOWN),
            "WIFI_MAX_DHCP_RETRY_COUNT" to Settings.Global.getString(contentResolver, Settings.Global.WIFI_MAX_DHCP_RETRY_COUNT),
            "WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS" to Settings.Global.getString(contentResolver, Settings.Global.WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS),
            "WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON" to Settings.Global.getString(contentResolver, Settings.Global.WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON),
            "WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY" to Settings.Global.getString(contentResolver, Settings.Global.WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY),
            "WIFI_NUM_OPEN_NETWORKS_KEPT" to Settings.Global.getString(contentResolver, Settings.Global.WIFI_NUM_OPEN_NETWORKS_KEPT),
            "WIFI_ON" to Settings.Global.getString(contentResolver, Settings.Global.WIFI_ON),
            "WIFI_SLEEP_POLICY" to Settings.Global.getString(contentResolver, Settings.Global.WIFI_SLEEP_POLICY),
            "WIFI_WATCHDOG_ON" to Settings.Global.getString(contentResolver, Settings.Global.WIFI_WATCHDOG_ON),
            "WINDOW_ANIMATION_SCALE" to Settings.Global.getString(contentResolver, Settings.Global.WINDOW_ANIMATION_SCALE)
        )

        // Remove settings that are restricted in higher SDK versions
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            globalSettings.remove("DATA_ROAMING")
        }

        return JSONObject(globalSettings.filterValues { it != null }).toString()
    }
}
