package com.example.swapcampus_zheng_li

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.swapcampus_zheng_li.ui.SwapCampusApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = (application as SwapCampusApplication).container
        setContent { SwapCampusApp(container) }
    }
}
