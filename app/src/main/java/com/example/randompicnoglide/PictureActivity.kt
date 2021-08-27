package com.example.randompicnoglide

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.randompicnoglide.databinding.ActivityPictureBinding
import kotlinx.coroutines.*
import java.net.URL

class PictureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPictureBinding
    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPictureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        val imageUri = intent.getStringExtra(MainActivity.EXTRA_URI)

        if (imageUri!!.isEmpty()) {
            Toast.makeText(applicationContext, "Enter URL", Toast.LENGTH_SHORT).show()
        } else {
            val result: Deferred<Bitmap?> = coroutineScope.async(Dispatchers.IO) {
                BitmapFactory.decodeStream(URL(imageUri).openStream())
            }
            coroutineScope.launch(Dispatchers.Main) {
                val bitmap: Bitmap? = result.await()
                bitmap.apply {
                    binding.imageViewUri.setImageBitmap(bitmap)
                }
            }
        }
    }

}

