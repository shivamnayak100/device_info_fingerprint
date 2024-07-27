package com.example.device_info

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import org.json.JSONObject

fun fetchBatteryInfo(context: Context): String {
    val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    val batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    val batteryChargeCounter = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)
    val batteryCurrentAverage = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE)
    val batteryCurrentNow = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
    val batteryEnergyCounter = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER)
    val batteryHealth = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)
    val batteryStatus = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)
    val isCharging = batteryManager.isCharging

    val chargeTimeRemaining = if (isCharging) {
        batteryManager.computeChargeTimeRemaining()
    } else {
        -1
    }

    val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    val batteryStatusIntent = context.registerReceiver(null, intentFilter)
    val batteryVoltage = batteryStatusIntent?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)
    val batteryTemperature = batteryStatusIntent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
    val batteryPlugged = batteryStatusIntent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
    val batteryTechnology = batteryStatusIntent?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)

    val batteryCapacity = getBatteryCapacity(context)

    val estimatedDischargeTime = estimateDischargeTime(batteryLevel, batteryCurrentNow, batteryCapacity)

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
        "Is Charging" to isCharging.toString(),
        "Charge Time Remaining" to if (chargeTimeRemaining != -1L) "${chargeTimeRemaining / 1000 / 60} minutes" else "Not Charging or Unsupported",
        "Estimated Discharge Time" to if (estimatedDischargeTime != -1) "$estimatedDischargeTime minutes" else "Unknown",
        "Battery Plugged" to getBatteryPluggedString(batteryPlugged),
        "Battery Technology" to batteryTechnology,
        "Battery Capacity" to "$batteryCapacity mAh"
    )
    return JSONObject(batteryInfo).toString()
}

private fun getBatteryCapacity(context: Context): Double {
    val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    val energyCounter = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER).toDouble()
    val capacity = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY).toDouble()
    return if (capacity > 0) energyCounter / capacity / 1000.0 else getBatteryCapacityOld(context)  // Convert to mAh
}

private fun getBatteryCapacityOld(context: Context): Double {
    return try {
        val powerProfileClass = Class.forName("com.android.internal.os.PowerProfile")
        val constructor = powerProfileClass.getConstructor(Context::class.java)
        val powerProfile = constructor.newInstance(context)
        powerProfileClass.getMethod("getBatteryCapacity").invoke(powerProfile) as Double
    } catch (e: Exception) {
        4000.0  // Default to 4000 mAh if we can't retrieve it
    }
}

private fun estimateDischargeTime(batteryLevel: Int, batteryCurrentNow: Int, batteryCapacity: Double): Int {
    return if (batteryCurrentNow != 0) {
        val remainingCapacity = (batteryLevel / 100.0) * batteryCapacity
        val dischargeRate = Math.abs(batteryCurrentNow) / 1000.0  // Convert µA to mA and take absolute value
        if (dischargeRate > 0) {
            (remainingCapacity / dischargeRate * 60).toInt()  // Convert hours to minutes
        } else {
            -1
        }
    } else {
        -1
    }
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

private fun getBatteryPluggedString(plugged: Int?): String {
    return when (plugged) {
        BatteryManager.BATTERY_PLUGGED_AC -> "AC"
        BatteryManager.BATTERY_PLUGGED_USB -> "USB"
        BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Wireless"
        else -> "Unknown"
    }
}
