package com.example.serialport

import android.serialport.SerialPortFinder
import android.serialport.port.BaseReader
import android.serialport.port.SerialApiManager
import android.text.TextUtils
import android.util.Log


class OpenCommunication {
    private var spManager: SerialApiManager? = null
    private var baseReader: BaseReader? = null
    private var currentPort: String? = null
    var entries: List<String> = ArrayList()
    var entryValues: List<String> = ArrayList()
    var readChannel: String = ""
    var mSerialPortFinder: SerialPortFinder = SerialPortFinder()
    fun getSerialPort(): List<String> {
        return mSerialPortFinder.allDevices.toList()
    }

    fun initData() {
        spManager = SerialApiManager.getInstances().setLogInterceptor { type, port, isAscii, log ->
            val dataMap: MutableMap<String, String> =
                HashMap()
            dataMap["readChannel"] = readChannel
            CustomEventHandler.sendEvent(dataMap.toString())
        }
        baseReader = object : BaseReader() {
            override fun onParse(port: String, isAscii: Boolean, read: String) {
                Log.d(
                    "SerialPortRead", StringBuffer()
                        .append(port).append("/").append(if (isAscii) "ascii" else "hex")
                        .append(" read：").append(read).append("\n").toString()
                )
                readChannel += "\n" + (StringBuffer()
                    .append(port).append("/").append(if (isAscii) "ascii" else "hex")
                    .append(" read：").append(read).append("\n").toString())
                val dataMap: MutableMap<String, String> = HashMap()

                dataMap["readChannel"] = readChannel
                CustomEventHandler.sendEvent(dataMap.toString())
            }
        }
    }


    fun open(name: String, isAscii: Boolean, baudRate: Int) {
        initData()
        var checkPort = name
        if (TextUtils.isEmpty(checkPort)) {
            return
        } else if (TextUtils.equals(checkPort, "other")) {
            checkPort = name
            if (TextUtils.isEmpty(checkPort)) {
                return
            }
        }

        if (TextUtils.equals(currentPort, checkPort)) {
            return
        }

        if (!TextUtils.isEmpty(currentPort)) {
            // Close the CurrentPort serial port
            spManager!!.stopSerialPort(currentPort)
        }

        if (entryValues.contains(checkPort)) {
            currentPort = checkPort
            spManager!!.startSerialPort(checkPort, isAscii, baseReader, baudRate)

        }
    }


    fun close() {
        if (!TextUtils.isEmpty(currentPort)) {
            // currentPort
            spManager!!.stopSerialPort(currentPort)
            currentPort = ""
        }
    }
}