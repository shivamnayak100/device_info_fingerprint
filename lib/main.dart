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

  Future<void> _getDeviceInfo() async {
    String deviceId;
    String ramInfo;
    String cpuInfo;
    String cameraInfo;
    Map<String, String> manufacturerInfo;
    Map<String, String> versionInfo;
    Map<String, String> batteryInfo;
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

    } on PlatformException catch (e) {
      deviceId = "Failed to get device info: '${e.message}'.";
      ramInfo = "Failed to get RAM info: '${e.message}'.";
      cpuInfo = "Failed to get CPU info: '${e.message}'.";
      cameraInfo = "Failed to get camera info: '${e.message}'.";
      manufacturerInfo = {'MANUFACTURER':'${e.message}'};
      versionInfo = {"BASE_OS": '${e.message}'};
      batteryInfo = {'Error': "Failed to get battery info: '${e.message}'"};
    }

    setState(() {
      _deviceId = deviceId;
      _batteryInfo = batteryInfo;
      _ramInfo = ramInfo;
      _cpuInfo = cpuInfo;
      _cameraInfo = cameraInfo;
      _manufacturerInfo = manufacturerInfo;
      _versionInfo = versionInfo;
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

                Text('MANUFACTURER: ${_manufacturerInfo['MANUFACTURER']}'),
                Text('MODEL: ${_manufacturerInfo['MODEL']}'),
                Text('BOARD: ${_manufacturerInfo['BOARD']}'),
                Text('BOOTLOADER: ${_manufacturerInfo['BOOTLOADER']}'),
                Text('BRAND: ${_manufacturerInfo['BRAND']}'),
                Text('DEVICE: ${_manufacturerInfo['DEVICE']}'),
                Text('DISPLAY: ${_manufacturerInfo['DISPLAY']}'),
                Text('FINGERPRINT: ${_manufacturerInfo['FINGERPRINT']}'),
                Text('HARDWARE: ${_manufacturerInfo['HARDWARE']}'),
                Text('HOST: ${_manufacturerInfo['HOST']}'),
                Text('ID: ${_manufacturerInfo['ID']}'),
                Text('PRODUCT: ${_manufacturerInfo['PRODUCT']}'),
                Text('TAGS: ${_manufacturerInfo['TAGS']}'),
                Text('TIME: ${_manufacturerInfo['TIME']}'),
                Text('TYPE: ${_manufacturerInfo['TYPE']}'),
                Text('USER: ${_manufacturerInfo['USER']}'),
                Text('RADIO: ${_manufacturerInfo['RADIO']}'),
                Text('SERIAL: ${_manufacturerInfo['SERIAL']}'),
                Text('SUPPORTED_32_BIT_ABIS: ${_manufacturerInfo['SUPPORTED_32_BIT_ABIS']}'),
                Text('SUPPORTED_64_BIT_ABIS: ${_manufacturerInfo['SUPPORTED_64_BIT_ABIS']}'),
                Text('SUPPORTED_ABIS: ${_manufacturerInfo['SUPPORTED_ABIS']}'),

                // %%%%%%%%%%%%%%%%%%%%%%%
                const SizedBox(height: 10,),
                const Text('Version Info:- ', style: TextStyle(fontWeight: FontWeight.bold),),
                Text('BASE_OS: ${_versionInfo['BASE_OS']}'),
                Text('CODENAME: ${_versionInfo['CODENAME']}'),
                Text('INCREMENTAL: ${_versionInfo['INCREMENTAL']}'),
                Text('PREVIEW_SDK_INT: ${_versionInfo['PREVIEW_SDK_INT']}'),
                Text('RELEASE: ${_versionInfo['RELEASE']}'),
                Text('SDK: ${_versionInfo['SDK']}'),
                Text('SDK_INT: ${_versionInfo['SDK_INT']}'),
                Text('SECURITY_PATCH: ${_versionInfo['SECURITY_PATCH']}'),

                // %%%%%%%%%%%%%%%%%%%%%%%
                const SizedBox(height: 10,),
                const Text('Battery Info:- ', style: TextStyle(fontWeight: FontWeight.bold),),
                ..._batteryInfo.entries.map((entry) => Text('${entry.key}: ${entry.value}')).toList(),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
