package ru.netology.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.R

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
    }
}