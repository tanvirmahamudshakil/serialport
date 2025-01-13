import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'serialport.dart';
import 'serialport_method_channel.dart';

abstract class SerialportPlatform extends PlatformInterface {
  /// Constructs a SerialportPlatform.
  SerialportPlatform() : super(token: _token);

  static final Object _token = Object();

  static SerialportPlatform _instance = MethodChannelSerialport();

  /// The default instance of [SerialportPlatform] to use.
  ///
  /// Defaults to [MethodChannelSerialport].
  static SerialportPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [SerialportPlatform] when
  /// they register themselves.
  static set instance(SerialportPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

Stream<String?> startSerial() {
    throw UnimplementedError('startSerial() has not been implemented.');
  }

  /// Thrown by operations that have not been implemented yet.
  /// a [UnsupportedError] all things considered. This mistake is just planned for
  /// use during improvement.
  Future<String?> openPort(
      {required DataFormat dataFormat,
      required String serialPort,
      required int baudRate}) {
    throw UnimplementedError('openSerial() has not been implemented.');
  }

  /// Thrown by operations that have not been implemented yet.
  /// a [UnsupportedError] all things considered. This mistake is just planned for
  /// use during improvement.
  Future<List<String>?> getAvailablePorts() {
    throw UnimplementedError('getAvailablePorts() has not been implemented.');
  }

  /// Thrown by operations that have not been implemented yet.
  /// a [UnsupportedError] all things considered. This mistake is just planned for
  /// use during improvement.
  Future<String?> closePort() {
    throw UnimplementedError('closeSerial() has not been implemented.');
  }

}
