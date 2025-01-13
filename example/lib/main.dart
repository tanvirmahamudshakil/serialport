import 'dart:ui';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:serialport/serialport.dart';

void main() {
  DartPluginRegistrant.ensureInitialized();
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  Serialport flutterSerial = Serialport();
  List<String> serialList = [];
  var receivedData;
  String? selectSerialPath;
  int selectBundRate = 9600;

  getSerialList() async {
    serialList = await flutterSerial.getAvailablePorts() ?? [];
    setState(() {});
  }

  @override
  void dispose() {
    flutterSerial.closePort();
    super.dispose();
  }

  @override
  void initState() {
    flutterSerial.startSerial().listen(_updateConnectionStatus);
    super.initState();
  }

  void _updateConnectionStatus(String? result) async {
    setState(() {
      receivedData = result;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: [
            serialListBox(),
            SizedBox(height: 10),
            baundListBox(),
            Row(
              children: [
                MaterialButton(
                    onPressed: () {
                      getSerialList();
                    },
                    child: Text("Serial Get")),
                MaterialButton(
                    onPressed: () {
                      flutterSerial.openPort(serialPort: selectSerialPath ?? '', baudRate: selectBundRate, dataFormat: DataFormat.ASCII);
                    },
                    child: Text("Serial Read")),
                MaterialButton(
                    onPressed: () {
                      flutterSerial.closePort();
                    },
                    child: Text("Serial Close")),
              ],
            ),
            Text("${receivedData}")
          ],
        ),
      ),
    );
  }

  Widget serialListBox() {
    return DropdownButtonFormField(
        value: selectSerialPath,
        items: List.generate(serialList.length, (index) {
          var d = serialList[index];
          return DropdownMenuItem(child: Text("${d}"), value: d);
        }),
        onChanged: (value) {
          setState(() {
            selectSerialPath = value!;
          });
        });
  }

  Widget baundListBox() {
    return DropdownButtonFormField(
        value: selectBundRate,
        items: List.generate(flutterSerial.baudRateList.length, (index) {
          var d = flutterSerial.baudRateList[index];
          return DropdownMenuItem(child: Text("${d}"), value: d);
        }),
        onChanged: (value) {
          setState(() {
            selectBundRate = value!;
          });
        });
  }
}
