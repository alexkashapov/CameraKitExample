package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.support.annotation.DrawableRes
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import com.camerakit.CameraKit
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.camera_controls.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class CameraControls @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(getContext()).inflate(R.layout.camera_controls, this)


    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        flashButton.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                when (rootView.camera.flash) {
                    CameraKit.FLASH_ON -> {
                        rootView.camera.flash = CameraKit.FLASH_AUTO
                        changeViewImageResource(view as ImageView, R.drawable.ic_flash_auto)
                    }
                    CameraKit.FLASH_AUTO -> {
                        rootView.camera.flash = CameraKit.FLASH_OFF
                        changeViewImageResource(view as ImageView, R.drawable.ic_flash_off)
                    }
                    CameraKit.FLASH_OFF -> {
                        rootView.camera.flash = CameraKit.FLASH_ON
                        changeViewImageResource(view as ImageView, R.drawable.ic_flash_on)
                    }
                }
            }
            true

        }
        captureButton.setOnClickListener {
            val fileDir = File("${Environment.getExternalStorageDirectory().path}/myapp")
            if (!fileDir.exists()) fileDir.mkdir()
            val previewIntent = Intent(context, PreviewActivity::class.java)
            val sFile = File("${fileDir.path}/selphie_${UUID.randomUUID()}.jpg")
            val filename = File("${fileDir.path}/photo_${UUID.randomUUID()}.jpg")
            rootView.blackCover.visibility = View.VISIBLE
            makeFoto(CameraKit.FACING_FRONT, sFile) {
                makeFoto(CameraKit.FACING_BACK, filename) {
                    previewIntent.putExtra("selphie", sFile.path)
                    previewIntent.putExtra("filepath", filename.path)
                    (context as AppCompatActivity).startActivityForResult(previewIntent, 2)
                }
            }
        }
    }

    fun handleViewTouchFeedback(view: View, motionEvent: MotionEvent) {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                touchDownAnimation(view)
            }
            MotionEvent.ACTION_UP -> {
                touchUpAnimation(view)
            }
        }
    }

    private fun touchDownAnimation(view: View) {
        view.animate()
            .scaleX(0.88f)
            .scaleY(0.88f)
            .setDuration(300)
            .setInterpolator(OvershootInterpolator())
            .start()
    }

    private fun touchUpAnimation(view: View) {
        view.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(300)
            .setInterpolator(OvershootInterpolator())
            .start()
    }

    private fun changeViewImageResource(imageView: ImageView, @DrawableRes resId: Int): Boolean {
        imageView.rotation = 0f
        imageView.animate()
            .rotationBy(360f)
            .setDuration(400)
            .setInterpolator(OvershootInterpolator())
            .start()

        imageView.postDelayed({ imageView.setImageResource(resId) }, 120)
        return true
    }

    private fun makeFoto(facing: Int, filename: File, onComplete: () -> Unit) {
        rootView.camera.facing = facing
        rootView.camera.captureImage { view, photo ->
            val stream = FileOutputStream(filename)
//                    stream.use { it.write(photo) }
            try {
                stream.write(photo)
                stream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            onComplete()
        }
    }
}