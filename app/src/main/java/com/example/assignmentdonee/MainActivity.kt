package com.example.assignmentdonee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val button1 : Button = findViewById(R.id.requestBtn)
       val button2 : Button = findViewById(R.id.editBtn)

        button1.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RequestItem::class.java)
            startActivity(intent)
        })

        button2.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, EditItem::class.java)
            startActivity(intent)
        })



    }

}
