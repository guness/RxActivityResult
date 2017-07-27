package com.petarmarijanovic.rxactivityresult.sample

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.petarmarijanovic.rxactivityresult.RxActivityResult
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
  
  private lateinit var rxActivityResult: RxActivityResult
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    rxActivityResult = RxActivityResult(this)
    
    bluetooth_button.setOnClickListener { enableBluetooth() }
    activity_button.setOnClickListener { startActivity() }
  }
  
  private fun enableBluetooth() {
    rxActivityResult.start(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        .subscribe({ updateTextView(bt_status, it.resultCode) },
                   { it.printStackTrace() })
    
  }
  
  private fun startActivity() {
    rxActivityResult.start(Intent(this, OtherActivity::class.java))
        .subscribe({ updateTextView(activity_status, it.resultCode) },
                   { it.printStackTrace() })
  }
  
  private fun updateTextView(view: TextView, resultCode: Int) {
    if (resultCode == Activity.RESULT_OK) {
      view.text = "OK"
      view.setTextColor(Color.parseColor("#00FF00"))
    } else if (resultCode == Activity.RESULT_CANCELED) {
      view.text = "CANCELED"
      view.setTextColor(Color.parseColor("#FF0000"))
    }
  }
}