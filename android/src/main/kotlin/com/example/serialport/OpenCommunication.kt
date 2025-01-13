package com.example.serialport

import android.R.attr.port
import cn.lalaki.SerialPort
import java.io.File


class OpenCommunication {
    private var serialPort: SerialPort? = null
    fun getSerialPort(): List<String>? {
        return File("/dev/").listFiles { _, s -> s.contains("ttys", ignoreCase = true) }
            ?.sortedBy { it.name }?.map { it.absolutePath }
    }


    fun open(name: String?, isAscii : Boolean?,  baudRate : Int?) {
        if(serialPort != null) {
            serialPort?.close()
            serialPort = null;
        }
        if(name != null && baudRate != null && isAscii != null) {
            serialPort = SerialPort(name, baudRate, object : SerialPort.DataCallback {
                override fun onData(data: ByteArray) {
                    val value = (StringBuffer()
                        .append(port).append("/").append(if (isAscii) "ascii" else "hex")
                        .append(" read：").append(data).append("\n").toString())
                    CustomEventHandler.sendEvent(value)
                }
            })
        }

    }

    fun close() {
        serialPort?.close()
        serialPort = null
    }
}