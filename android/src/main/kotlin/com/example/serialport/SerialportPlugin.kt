package com.example.serialport

import android.content.Context
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** SerialportPlugin */
class SerialportPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private var methodChannel: MethodChannel? = null
  private var eventChannel: EventChannel? = null
  private var receiver: CustomEventHandler? = null
  var communication: OpenCommunication = OpenCommunication()
  private val methodChannelName = "dev.ak.flutter_serial/embedded_serial_method_channel"
  private val eventChannelName = "dev.ak.flutter_serial/embedded_serial_event_channel"

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    setupChannels(flutterPluginBinding.binaryMessenger)
    methodChannel = MethodChannel(flutterPluginBinding.binaryMessenger, methodChannelName)
    methodChannel!!.setMethodCallHandler(this)
  }

  private fun setupChannels(messenger: BinaryMessenger) {
    eventChannel = EventChannel(messenger, eventChannelName)
    receiver = CustomEventHandler()
    eventChannel?.setStreamHandler(receiver)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    val argments = (call.arguments() as Map<String, String>?)
    when (call.method) {
      "embeddedSerial/availablePorts" -> {
        val list = communication.getSerialPort() ?: emptyList()
        result.success(list)
      }

      "embeddedSerial/open" -> argments?.get("serialPort")?.let {
        argments["dataFormat"]?.toBoolean()?.let { it1 ->
          communication.open(
            it, it1, argments["baudRate"]!!
                .toInt()
          )
        }
      }

      "embeddedSerial/close" -> communication.close()

      "embeddedSerial/destroy" -> communication.close()
      else -> result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    methodChannel?.setMethodCallHandler(null);
    teardownChannels();
  }

  private fun teardownChannels() {
    methodChannel!!.setMethodCallHandler(null)
    eventChannel!!.setStreamHandler(null)
    receiver!!.onCancel("")
    eventChannel = null
    receiver = null
  }
}
