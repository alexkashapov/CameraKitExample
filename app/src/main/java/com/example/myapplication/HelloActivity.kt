package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_hello.*
import java.io.File

class HelloActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = PhotoAdapter()
        openCamera.setOnClickListener {
            startActivityForResult(Intent(this@HelloActivity, MainActivity::class.java), 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_SUCCESS)
            data?.let {
                val list = it.getStringArrayListExtra("filenames")
                list?.forEach {
                    (recyclerView.adapter as PhotoAdapter).addItem(File(it))
                }
            }
    }
}
