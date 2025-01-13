import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'serialport.dart';
import 'serialport_platform_interface.dart';

/// An implementation of [SerialportPlatform] that uses method channels.
class MethodChannelSerialport extends SerialportPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel("dev.ak.flutter_serial/embedded_serial_method_channel");

  ///EventChannel for opening the inputStream and OutputStream
  final eventChannel = const EventChannel("dev.ak.flutter_serial/embedded_serial_event_channel");

  @override
  Stream<String?> startSerial() {
    Stream<String?> data = eventChannel.receiveBroadcastStream().map((event) => event.toString());

    return data;
  }

  @override
  Future<List<String>?> getAvailablePorts() async {
    List<String>? list = await methodChannel.invokeListMethod<String>('embeddedSerial/availablePorts');
    return list;
  }

  @override
  Future<String?> openPort({required DataFormat dataFormat, required String serialPort, required int baudRate}) async {
    final argument = {
      "dataFormat": dataFormat == DataFormat.ASCII ? "true" : "false",
      "serialPort": serialPort,
      "baudRate": baudRate.toString(),
    };
    final version = await methodChannel.invokeMethod<String>('embeddedSerial/open', argument);
    return version;
  }

  ///close the opened port which you have opened from [openPort] this method
  ///At a time you can't opened two ports
  ///Close the previous to open the new
  @override
  Future<String?> closePort() async {
    final version = await methodChannel.invokeMethod<String>('embeddedSerial/close');
    return version;
  }
}
