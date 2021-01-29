package club.devsoc.scanf.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import club.devsoc.scanf.R

class ImageActivity : AppCompatActivity() {

    private lateinit var addImageBtn:ImageView
    private lateinit var okBtn:ImageView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        initActivity()

    }

    private fun initActivity()
    {
        okBtn=findViewById(R.id.image_activity_okbtn)
        addImageBtn=findViewById(R.id.image_activity_addimg)
        imageView=findViewById(R.id.image_activity_imgvw)
    }
}