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
                "getBatteryInfo" -> {
                    val batteryInfo = fetchBatteryInfo()
                    result.success(batteryInfo)
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
                else -> result.notImplemented()
            }
        }
    }

    private fun fetchDeviceId(): String {
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun fetchBatteryInfo(): String {
        val batteryManager = getSystemService(Context.BATTERY_SERVICE) as android.os.BatteryManager
        val batteryLevel = batteryManager.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY)
        return "Battery Level: $batteryLevel%"
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

    private fun fetchManufacturerInfo(): String {
        val manufacturerInfo = mapOf(
            "Manufacturer" to Build.MANUFACTURER,
            "Model" to Build.MODEL
        )
        return JSONObject(manufacturerInfo).toString()
    }
}
