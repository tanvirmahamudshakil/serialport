import 'package:flutter_test/flutter_test.dart';
import 'package:serialport/serialport.dart';
import 'package:serialport/serialport_platform_interface.dart';
import 'package:serialport/serialport_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockSerialportPlatform
    with MockPlatformInterfaceMixin
    implements SerialportPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final SerialportPlatform initialPlatform = SerialportPlatform.instance;

  test('$MethodChannelSerialport is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelSerialport>());
  });

  test('getPlatformVersion', () async {
    Serialport serialportPlugin = Serialport();
    MockSerialportPlatform fakePlatform = MockSerialportPlatform();
    SerialportPlatform.instance = fakePlatform;

    expect(await serialportPlugin.getPlatformVersion(), '42');
  });
}
