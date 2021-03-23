 package com.example.speechtotext

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


 class MainActivity : AppCompatActivity() {


     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)
         if (savedInstanceState == null) {
             supportFragmentManager.beginTransaction()
                     .replace(R.id.container, MainFragment.newInstance())
                     .commitNow()
         }
     }
}

