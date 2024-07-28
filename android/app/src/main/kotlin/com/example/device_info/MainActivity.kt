package com.example.device_info

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


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
                    val batteryInfo = fetchBatteryInfo(this)
                    result.success(batteryInfo)
                }
                "getDeviceModeInfo" -> {
                    val deviceSettingModeInfo = DeviceSettingsHelper.fetchDeviceSettingInfo(contentResolver)
                    result.success(deviceSettingModeInfo)
                }
                "getModeStatusInfo" -> {
                    val modeStatusInfo = fetchModeStatusInfo()
                    result.success(modeStatusInfo)
                }
                else -> result.notImplemented()
            }
        }
    }

    private fun fetchDeviceId(): String {
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
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

    private fun fetchModeStatusInfo(): Boolean {
        return Settings.Global.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
    }

    
}
