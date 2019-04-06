package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_preview.*
import java.io.File

class PreviewActivity : AppCompatActivity() {
    var filePath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        val pathSelphie = File(intent.getStringExtra("selphie"))
        val filePath = File(intent.getStringExtra("filepath"))
        Picasso.get().load(filePath).into(previewImage)
        Picasso.get().load(pathSelphie).into(selphieView)
        okButton.setOnClickListener {
            val resultIntent = Intent()
//            resultIntent.putExtra("filename", filePath.path)
            setResult(RESULT_SUCCESS, resultIntent)
            finish()
        }
        continueButton.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("filename", filePath)
            setResult(RESULT_CONTINUE, resultIntent)
            finish()
        }
        cancelButton.setOnClickListener {
            setResult(RESULT_CANCEL)
            finish()
        }
    }
}