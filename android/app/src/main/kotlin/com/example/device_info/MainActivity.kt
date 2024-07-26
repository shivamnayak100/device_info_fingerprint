package com.example.device_info

import android.os.Build
import android.provider.Settings
import android.os.Bundle
import android.content.Context
import android.os.Environment
import android.os.StatFs
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject
import android.os.BatteryManager
import android.content.Intent
import android.content.IntentFilter


class MainActivity: FlutterActivity() {
    private val CHANNEL = "device_info"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "getDeviceInfo" -> {
                    val deviceId = fetchDeviceId()
                    result.success(deviceId)
                }
                "getRamInfo" -> {
                    val ramInfo = fetchRamInfo()
                    result.success(ramInfo)
                }
                "getCpuInfo" -> {
                    val cpuInfo = fetchCpuInfo()
                    result.success(cpuInfo)
                }
                "getCameraInfo" -> {
                    val cameraInfo = fetchCameraInfo()
                    result.success(cameraInfo)
                }
                "getManufacturerInfo" -> {
                    val manufacturerInfo = fetchManufacturerInfo()
                    result.success(manufacturerInfo)
                }
                "versionInfo" -> {
                    val versionInfo = fetchBuildVersionInfo()
                    result.success(versionInfo)
                }
                "getBatteryInfo" -> {
                    val batteryInfo = fetchBatteryInfo()
                    result.success(batteryInfo)
                }
                else -> result.notImplemented()
            }
        }
    }

    private fun fetchDeviceId(): String {
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }

   private fun fetchBatteryInfo(): String {
        val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        val batteryChargeCounter = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)
        val batteryCurrentAverage = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE)
        val batteryCurrentNow = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
        val batteryEnergyCounter = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER)
        val batteryHealth = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)
        val batteryStatus = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)
        val isCharging = batteryManager.isCharging

        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatusIntent = registerReceiver(null, intentFilter)
        val batteryVoltage = batteryStatusIntent?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)
        val batteryTemperature = batteryStatusIntent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)

        val batteryInfo = mapOf(
            "Battery Level" to "$batteryLevel%",
            "Battery Charge Counter" to "$batteryChargeCounter",
            "Battery Current Average" to "$batteryCurrentAverage µA",
            "Battery Current Now" to "$batteryCurrentNow µA",
            "Battery Energy Counter" to "$batteryEnergyCounter nWh",
            "Battery Health" to getBatteryHealthString(batteryHealth),
            "Battery Voltage" to "$batteryVoltage mV",
            "Battery Temperature" to "${batteryTemperature?.div(10.0)} °C",
            "Battery Status" to getBatteryStatusString(batteryStatus),
            "Is Charging" to isCharging.toString()
        )
        return JSONObject(batteryInfo).toString()
    }

    private fun getBatteryHealthString(health: Int): String {
        return when (health) {
            BatteryManager.BATTERY_HEALTH_COLD -> "Cold"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
            BatteryManager.BATTERY_HEALTH_UNKNOWN -> "Unknown"
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Unspecified Failure"
            else -> "Unknown"
        }
    }

    private fun getBatteryStatusString(status: Int): String {
        return when (status) {
            BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
            BatteryManager.BATTERY_STATUS_FULL -> "Full"
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "Not Charging"
            BatteryManager.BATTERY_STATUS_UNKNOWN -> "Unknown"
            else -> "Unknown"
        }
    }

    private fun fetchRamInfo(): String {
        val memInfo = android.app.ActivityManager.MemoryInfo()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        activityManager.getMemoryInfo(memInfo)
        val totalRam = memInfo.totalMem / (1024 * 1024)
        val availRam = memInfo.availMem / (1024 * 1024)
        return "Total RAM: ${totalRam}MB, Available RAM: ${availRam}MB"
    }

    private fun fetchCpuInfo(): String {
        return Build.HARDWARE
    }

    private fun fetchCameraInfo(): String {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraIds = cameraManager.cameraIdList
        val cameras = cameraIds.joinToString(", ") { id ->
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING)
            if (lensFacing == CameraCharacteristics.LENS_FACING_FRONT) "Front Camera" else "Back Camera"
        }
        return "Cameras: $cameras"
    }

    //******************************************** */

    private fun fetchManufacturerInfo(): String {
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

    // ********************************************* //
    private fun fetchBuildVersionInfo(): String {
        val versionInfo = mapOf(
            "BASE_OS" to Build.VERSION.BASE_OS,
            "CODENAME" to Build.VERSION.CODENAME,
            "INCREMENTAL" to Build.VERSION.INCREMENTAL,
            "PREVIEW_SDK_INT" to Build.VERSION.PREVIEW_SDK_INT.toString(),
            "RELEASE" to Build.VERSION.RELEASE,
            "SDK" to Build.VERSION.SDK,
            "SDK_INT" to Build.VERSION.SDK_INT.toString(),
            "SECURITY_PATCH" to Build.VERSION.SECURITY_PATCH
        )
        return JSONObject(versionInfo).toString()
    }
}
