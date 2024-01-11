package com.sanjoo.sujanixassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sanjoo.sujanixassignment.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    lateinit var binding:ActivityHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_history)


    }
}