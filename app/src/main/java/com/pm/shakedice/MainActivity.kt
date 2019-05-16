package com.pm.shakedice

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.R.attr.y
import android.R.attr.x
import android.annotation.SuppressLint
import android.widget.Toast
import kotlin.collections.fold as fold1


class MainActivity : AppCompatActivity(), SensorEventListener {
    private var mSensorManager: SensorManager? = null
    private var mAccelometer: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        mSensorManager!!.registerListener(
            this, mAccelometer,
            SensorManager.SENSOR_DELAY_GAME
        )

        rollDiceBtn.setOnClickListener {
            changeDice()
        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager?.registerListener(
            this,
            mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        mSensorManager?.unregisterListener(this)
    }

    private fun rollDice(event: SensorEvent) {
        val xVal = event.values[0]
        val yVal = event.values[1]
        val zVal = event.values[2]
        val accelerationSquareRoot =
            (xVal * xVal + yVal * yVal + zVal * zVal) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH)

        if (accelerationSquareRoot >= 4) {
            changeDice()
        }
    }

    private fun changeDice() {
        val randomNumber = Random().nextInt(6) + 1
        val diceRresources = when (randomNumber) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            6 -> R.drawable.dice_6
            else -> R.drawable.empty_dice;
        }

        diceImageView.setImageResource(diceRresources)
        diceNumberTextView.text = randomNumber.toString()
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            rollDice(event)
        }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}
