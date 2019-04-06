package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var cameraControls: CameraControls
    val filesList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        camera.onStart()
    }

    override fun onPause() {
        super.onPause()
        camera.onPause()
    }

    override fun onResume() {
        super.onResume()
        camera.onResume()
    }

    override fun onStop() {
        super.onStop()
        camera.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        camera.onRequestPermissionsResult(requestCode, permissions, grantResults)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        blackCover.visibility= View.GONE
        data?.also {
            filesList.add(it.getStringExtra("filename"))
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        if (filesList.isEmpty()) {
            setResult(RESULT_CANCEL)
        } else {
            intent.putExtra("filenames", filesList)
            setResult(RESULT_SUCCESS, intent)
        }
        super.onBackPressed()
    }

    object process{
        var isProcessing = false
    }

}
