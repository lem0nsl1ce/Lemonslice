package com.example.quicktorch

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast

class VolumeFlashlightService : AccessibilityService() {

    private var isTorchOn = false
    private var lastVolumeUpTime = 0L
    private val doubleTapThresholdMs = 400L // 400ms window to count as a double tap

    private val cameraManager by lazy {
        getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        // Detect Volume Up key press
        if (event.keyCode == KeyEvent.KEYCODE_VOLUME_UP && event.action == KeyEvent.ACTION_DOWN) {
            val currentTime = System.currentTimeMillis()

            if (currentTime - lastVolumeUpTime <= doubleTapThresholdMs) {
                // Double tap detected! Toggle flashlight
                toggleTorch()
                lastVolumeUpTime = 0L
                return true // Consume event so volume doesn't change
            } else {
                lastVolumeUpTime = currentTime
            }
        }
        return super.onKeyEvent(event)
    }

    private fun toggleTorch() {
        try {
            val cameraId = getTorchCameraId() ?: return
            isTorchOn = !isTorchOn
            cameraManager.setTorchMode(cameraId, isTorchOn)

            val status = if (isTorchOn) "LimeLight ON" else "LimeLight OFF"
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun getTorchCameraId(): String? {
        for (id in cameraManager.cameraIdList) {
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val hasFlash = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) ?: false
            val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
            if (hasFlash && facing == CameraCharacteristics.LENS_FACING_BACK) {
                return id
            }
        }
        return null
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}
}
