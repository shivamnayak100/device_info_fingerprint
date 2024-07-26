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
  String _deviceId = 'Unknown';
  String _batteryInfo = 'Unknown';
  String _ramInfo = 'Unknown';
  String _cpuInfo = 'Unknown';
  String _cameraInfo = 'Unknown';
  Map<String, String> _manufacturerInfo = {'Manufacturer': 'Unknown', 'Model': 'Unknown'};

  Future<void> _getDeviceInfo() async {
    String deviceId;
    String batteryInfo;
    String ramInfo;
    String cpuInfo;
    String cameraInfo;
    Map<String, String> manufacturerInfo;
    try {
      final String result = await platform.invokeMethod('getDeviceInfo');
      deviceId = 'Device ID: $result';

      final String batteryResult = await platform.invokeMethod('getBatteryInfo');
      batteryInfo = batteryResult;

      final String ramResult = await platform.invokeMethod('getRamInfo');
      ramInfo = ramResult;

      final String cpuResult = await platform.invokeMethod('getCpuInfo');
      cpuInfo = cpuResult;

      final String cameraResult = await platform.invokeMethod('getCameraInfo');
      cameraInfo = cameraResult;

      final String manufacturerResult = await platform.invokeMethod('getManufacturerInfo');
      manufacturerInfo = Map<String, String>.from(json.decode(manufacturerResult));
    } on PlatformException catch (e) {
      deviceId = "Failed to get device info: '${e.message}'.";
      batteryInfo = "Failed to get battery info: '${e.message}'.";
      ramInfo = "Failed to get RAM info: '${e.message}'.";
      cpuInfo = "Failed to get CPU info: '${e.message}'.";
      cameraInfo = "Failed to get camera info: '${e.message}'.";
      manufacturerInfo = {'Manufacturer': "Failed to get manufacturer info", 'Model': '${e.message}'};
    }

    setState(() {
      _deviceId = deviceId;
      _batteryInfo = batteryInfo;
      _ramInfo = ramInfo;
      _cpuInfo = cpuInfo;
      _cameraInfo = cameraInfo;
      _manufacturerInfo = manufacturerInfo;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Device Info'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(_deviceId),
            Text(_batteryInfo),
            Text(_ramInfo),
            Text(_cpuInfo),
            Text(_cameraInfo),
            Text('Manufacturer: ${_manufacturerInfo['Manufacturer']}'),
            Text('Model: ${_manufacturerInfo['Model']}'),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: _getDeviceInfo,
              child: Text('Get Device Info'),
            ),
          ],
        ),
      ),
    );
  }
}
