package com.example.prave.sensorexample

import android.os.Bundle
import android.hardware.SensorManager
import android.widget.Toast
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.app.Activity
import android.hardware.Sensor
import android.speech.tts.TextToSpeech

/**
 * Created by Praveen John on 23-01-2019.
 */

class MainActivity : Activity(), SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var lastUpdate: Long = 0
    private var textToSpeech: TextToSpeech? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        lastUpdate = System.currentTimeMillis()
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event)
        }

    }

    private fun getAccelerometer(event: SensorEvent) {
        val values = event.values
        val x = values[0]
        val y = values[1]
        val z = values[2]

        val accelationCalculation = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH)
        val actualTime = event.timestamp
        if (accelationCalculation >= 2) {
            if (actualTime - lastUpdate < 200) {
                return
            }
            lastUpdate = actualTime
            Toast.makeText(this, "Device Shaked", Toast.LENGTH_SHORT)
                    .show()
            textToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener {  textToSpeech!!.speak("Device Shaked", TextToSpeech.QUEUE_FLUSH, null)})

        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this,
                sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }
}
