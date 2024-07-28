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
  String _deviceId = "Unknown";
  String _ramInfo = 'Unknown';
  String _cameraInfo = 'Unknown';
  String _privateIP = "Unknown";
  String _publicIP = "Unknown";
  Map<String, String> _manufacturerInfo = {};
  Map<String, String> _versionInfo = {};
  Map<String, String> _batteryInfo = {};
  Map<String, String> _deviceSettingModeInfo = {};
  Map<String, String> _systemInfo = {};
  List<String> _installedAppsList = [];

  Future<void> _getDeviceInfo() async {
    try {
      final String deviceId = await platform.invokeMethod('getDeviceInfo');
      final String ramInfo = await platform.invokeMethod('getRamInfo');
      final String cameraInfo = await platform.invokeMethod('getCameraInfo');
      final String privateIpResult = await platform.invokeMethod('getPrivateIPAddress');
      final String publicIpResult = await platform.invokeMethod('getPublicIPAddress');
      final String manufacturerResult = await platform.invokeMethod('getManufacturerInfo');
      final String versionInfoResult = await platform.invokeMethod('versionInfo');
      final String batteryInfoResult = await platform.invokeMethod('getBatteryInfo');
      final String deviceSettingInfoResult = await platform.invokeMethod('getDeviceModeInfo');
      final String systemInfoResult = await platform.invokeMethod('getSystemInfo');
      final List<dynamic> installedAppListResult = await platform.invokeMethod('getInstalledApps');
      setState(() {
        _deviceId = 'Device ID: $deviceId';
        _ramInfo = ramInfo;
        _cameraInfo = cameraInfo;
        _privateIP = privateIpResult;
        _publicIP = publicIpResult;
        _manufacturerInfo = Map<String, String>.from(json.decode(manufacturerResult));
        _versionInfo = Map<String, String>.from(json.decode(versionInfoResult));
        _batteryInfo = Map<String, String>.from(json.decode(batteryInfoResult));
        _deviceSettingModeInfo = Map<String, String>.from(json.decode(deviceSettingInfoResult));
        _systemInfo = Map<String, String>.from(json.decode(systemInfoResult));
        _installedAppsList = installedAppListResult.cast<String>();
      });
    } on PlatformException catch (e) {
      setState(() {
        _deviceId = "Failed to get device info: '${e.message}'.";
        _ramInfo = "Failed to get RAM info: '${e.message}'.";
        _cameraInfo = "Failed to get camera info: '${e.message}'.";
        _privateIP = "Failed to get private IP address: '${e.message}'.";
        _publicIP = "Failed to get public IP address: '${e.message}'.";
        _manufacturerInfo = {'Error': '${e.message}'};
        _versionInfo = {'Error': '${e.message}'};
        _batteryInfo = {'Error': '${e.message}'};
        _deviceSettingModeInfo = {'Error': '${e.message}'};
        _systemInfo = {'Error': '${e.message}'};
        _installedAppsList = [];
      });
    }
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
                Text(_cameraInfo),
                Text('Private IP Address: $_privateIP'),
                Text('Public IP Address: $_publicIP'),

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

                // %%%%%%%%%%%%%%%%%%%%%%%
                const SizedBox(height: 10,),
                const Text('Mobile System Info:- ', style: TextStyle(fontWeight: FontWeight.bold),),
                ..._systemInfo.entries.map((entry) => Text('${entry.key}: ${entry.value}')).toList(),

                // %%%%%%%%%%%%%%%%%%%%%%%
                const SizedBox(height: 10,),
                const Text('Installed Apps List:- ', style: TextStyle(fontWeight: FontWeight.bold),),
                ListView.builder(
                  shrinkWrap: true,
                  physics: NeverScrollableScrollPhysics(),
                  itemCount: _installedAppsList.length,
                  itemBuilder: (context, index) {
                    return Text(_installedAppsList[index]);
                  },
                ),

              ],
            ),
          ),
        ),
      ),
    );
  }
}
