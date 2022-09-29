package com.example.assignmentdonee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val button: Button = findViewById(R.id.btnAddTest);

        bottomNavView.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.navrequests -> {
                    val intent = Intent(this, RequestManagement::class.java)
                    startActivity(intent)
                }
            }
        }

    }




    fun btnAddTest(view: View?) {
        val intent = Intent(this, AddRequest::class.java)
        startActivity(intent)
        finish()

    }

}