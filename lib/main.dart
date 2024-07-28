import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: DeviceInfoScreen(),
    );
  }
}

class DeviceInfoScreen extends StatefulWidget {
  @override
  _DeviceInfoScreenState createState() => _DeviceInfoScreenState();
}

class _DeviceInfoScreenState extends State<DeviceInfoScreen> {
  static const platform = MethodChannel('device_info');
  String _deviceId = "Unknow";
  String _ramInfo = 'Unknown';
  String _cpuInfo = 'Unknown';
  String _cameraInfo = 'Unknown';
  Map<String, String> _manufacturerInfo = {};
  Map<String, String> _versionInfo = {};
  Map<String, String> _batteryInfo = {};
  Map<String, String> _deviceSettingModeInfo = {};

  Future<void> _getDeviceInfo() async {
    String deviceId;
    String ramInfo;
    String cpuInfo;
    String cameraInfo;
    Map<String, String> manufacturerInfo;
    Map<String, String> versionInfo;
    Map<String, String> batteryInfo;
    Map<String, String> deviceSettingModeInfo;
    try {
      final String result = await platform.invokeMethod('getDeviceInfo');
      deviceId = 'Device ID: $result';


      final String ramResult = await platform.invokeMethod('getRamInfo');
      ramInfo = ramResult;

      final String cpuResult = await platform.invokeMethod('getCpuInfo');
      cpuInfo = cpuResult;

      final String cameraResult = await platform.invokeMethod('getCameraInfo');
      cameraInfo = cameraResult;

      final String manufacturerResult = await platform.invokeMethod('getManufacturerInfo');
      manufacturerInfo = Map<String, String>.from(json.decode(manufacturerResult));

      final String versionInfoResult = await platform.invokeMethod('versionInfo');
      versionInfo = Map<String, String>.from(json.decode(versionInfoResult));

      final String batterInfoResult = await platform.invokeMethod('getBatteryInfo');
      batteryInfo = Map<String, String>.from(json.decode(batterInfoResult));

      final String deviceSettingInfoResult = await platform.invokeMethod('getDeviceModeInfo');
      deviceSettingModeInfo = Map<String, String>.from(json.decode(deviceSettingInfoResult));


    } on PlatformException catch (e) {
      deviceId = "Failed to get device info: '${e.message}'.";
      ramInfo = "Failed to get RAM info: '${e.message}'.";
      cpuInfo = "Failed to get CPU info: '${e.message}'.";
      cameraInfo = "Failed to get camera info: '${e.message}'.";
      manufacturerInfo = {'MANUFACTURER':'${e.message}'};
      versionInfo = {"BASE_OS": '${e.message}'};
      batteryInfo = {'Error': "Failed to get battery info: '${e.message}'"};
      deviceSettingModeInfo = {'Error': "Failed to get Device Setting info: '${e.message}'"};
    }

    setState(() {
      _deviceId = deviceId;
      _ramInfo = ramInfo;
      _cpuInfo = cpuInfo;
      _cameraInfo = cameraInfo;
      _manufacturerInfo = manufacturerInfo;
      _versionInfo = versionInfo;
      _batteryInfo = batteryInfo;
      _deviceSettingModeInfo = deviceSettingModeInfo;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Device Info'),
      ),
      body: SingleChildScrollView(
        child: Center(
          child: Padding(
            padding: const EdgeInsets.all(20.0),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                ElevatedButton(
                  onPressed: _getDeviceInfo,
                  child: Text('Get Device Info'),
                ),
                const SizedBox(height: 10,),
                Text(_deviceId),
                Text(_ramInfo),
                Text(_cpuInfo),
                Text(_cameraInfo),

                // %%%%%%%%%%%%%%%%%%%%%%%
                const SizedBox(height: 10,),
                const Text('Manufacture Info:- ', style: TextStyle(fontWeight: FontWeight.bold),),

                ..._manufacturerInfo.entries.map((entry) => Text('${entry.key}: ${entry.value}')).toList(),

                // %%%%%%%%%%%%%%%%%%%%%%%
                const SizedBox(height: 10,),
                const Text('Version Info:- ', style: TextStyle(fontWeight: FontWeight.bold),),
                ..._versionInfo.entries.map((entry) => Text('${entry.key}: ${entry.value}')).toList(),

                // %%%%%%%%%%%%%%%%%%%%%%%
                const SizedBox(height: 10,),
                const Text('Battery Info:- ', style: TextStyle(fontWeight: FontWeight.bold),),
                ..._batteryInfo.entries.map((entry) => Text('${entry.key}: ${entry.value}')).toList(),

                // %%%%%%%%%%%%%%%%%%%%%%%
                const SizedBox(height: 10,),
                const Text('Device Mode Status Info:- ', style: TextStyle(fontWeight: FontWeight.bold),),
                ..._deviceSettingModeInfo.entries.map((entry) => Text('${entry.key}: ${entry.value}')).toList(),

              ],
            ),
          ),
        ),
      ),
    );
  }
}
