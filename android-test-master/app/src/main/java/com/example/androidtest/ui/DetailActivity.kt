package com.example.androidtest.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtest.R
import com.example.androidtest.models.Breed
import com.example.androidtest.utils.DATA_KEY
import com.example.androidtest.utils.loadImage
import com.example.androidtest.utils.verifyAvailableNetwork
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var breed: Breed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        if (intent.hasExtra(DATA_KEY) && verifyAvailableNetwork(this)) {
            breed = intent?.getParcelableExtra(DATA_KEY) as Breed
            setContentOnUI(breed)
        } else {
            Toast.makeText(this, getString(R.string.internet_warning), Toast.LENGTH_LONG).show()
        }
    }

    private fun setContentOnUI(breed: Breed) {
        tv_breed_name.text = breed.name
        tv_breed_age.text = breed.lifeSpan
        tv_breed_for.text = breed.bred_for
        tv_origin.text = breed.origin
        val imageUrl = "https://cdn2.thedogapi.com/images/${breed.referenceImageId}.jpg"
        iv_breed_image.loadImage(baseContext, imageUrl)
    }
}