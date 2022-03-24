package club.devsoc.scanf.view.activity

import android.content.ContentUris
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import club.devsoc.scanf.R
import club.devsoc.scanf.model.GalleryModel
import club.devsoc.scanf.view.adapter.GalleryAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.gallery_bottom_sheet.*

private lateinit var photoList : ArrayList<GalleryModel>

class GalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        getImages()

        setGallery()

    }

    private fun setGallery() {

        var galleryAdapter = GalleryAdapter(photoList)
        photo_list.layoutManager= GridLayoutManager(this, 2)
        photo_list.adapter = galleryAdapter
        //notify data set changed
        galleryAdapter.notifyDataSetChanged()
    }

    private fun getImages() {
        val imageProjection = arrayOf(
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media._ID
        )
        val imageSortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            null,
            null,
            null
        )

        Log.e("yeet", cursor?.count.toString())

        photoList = ArrayList(3)
        if(cursor != null && cursor.count != 0) {

            cursor.moveToNext()
            while(cursor.isAfterLast == false) {
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                photoList.add(GalleryModel(contentUri))
                cursor.moveToNext()


            }
            Log.e("yeet", photoList[3].toString())


//            Glide.with(binding.root).load(contentUri).into(selected_doc_iv)
//            Log.e("yeet", contentUri.toString())


        }
        else {
            Log.e("yeet", "Ohno")
        }


    }
}