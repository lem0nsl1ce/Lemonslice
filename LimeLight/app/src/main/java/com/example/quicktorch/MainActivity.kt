package com.example.quicktorch

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(64, 64, 64, 64)
        }

        val titleView = TextView(this).apply {
            text = "🍋 LimeLight"
            textSize = 28f
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 32)
        }

        val descriptionView = TextView(this).apply {
            text = "Double-tap your Volume Up button from anywhere to quickly toggle the flashlight on or off.\n\nTo enable this feature, turn on the accessibility service below."
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 64)
        }

        val enableButton = Button(this).apply {
            text = "Enable in Accessibility Settings"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }

        rootLayout.addView(titleView)
        rootLayout.addView(descriptionView)
        rootLayout.addView(enableButton)

        setContentView(rootLayout)
    }
}
