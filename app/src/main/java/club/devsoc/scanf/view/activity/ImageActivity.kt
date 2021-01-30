package club.devsoc.scanf.view.activity

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import club.devsoc.scanf.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.priyankvasa.android.cameraviewex.CameraView
import com.priyankvasa.android.cameraviewex.ErrorLevel
import com.priyankvasa.android.cameraviewex.Image
import com.priyankvasa.android.cameraviewex.Modes
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImageActivity : AppCompatActivity() {


    private lateinit var addImageBtn:ImageView
    private lateinit var okBtn:ImageView
    private lateinit var imageView: ImageView
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String

    private lateinit var camera:CameraView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        initActivity()

        camera.addCameraOpenedListener { /* Camera opened. */ }
            .addCameraErrorListener { t: Throwable, errorLevel: ErrorLevel -> /* Camera error! */ }
            .addCameraClosedListener { /* Camera closed. */ }

        // enable only single capture mode
        camera.setCameraMode(Modes.CameraMode.SINGLE_CAPTURE)

        // OR keep other modes as is and enable single capture mode
        camera.enableCameraMode(Modes.CameraMode.SINGLE_CAPTURE)

        // Output format is whatever set for [app:outputFormat] parameter
        // Callback on UI thread
        camera.addPictureTakenListener { image: Image -> /* Picture taken. */ }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        camera.capture()

        // Disable single capture mode
        camera.disableCameraMode(Modes.CameraMode.SINGLE_CAPTURE)

        //addImageBtn.setOnClickListener(View.OnClickListener { dispatchTakePictureIntent() })


        /*on clicking photobutton
        dispatchTakePictureIntent()
        */


    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        camera.start()
    }

    override fun onPause() {
        camera.stop()
        super.onPause()
    }

//    override fun onDestroyView() {
//        camera.destroy()
//        super.onDestroyView()
//    }

//    private fun dispatchTakePictureIntent() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            // Ensure that there's a camera activity to handle the intent
//            takePictureIntent.resolveActivity(packageManager)?.also {
//                // Create the File where the photo should go
//                val photoFile: File? = try {
//                    createImageFile()
//                } catch (ex: IOException) {
//                    // Error occurred while creating the File
//
//                    null
//                }
//                // Continue only if the File was successfully created
//                photoFile?.also {
//                    val photoURI: Uri = FileProvider.getUriForFile(
//                        this,
//                        "com.example.android.fileprovider",
//                        it
//                    )
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//                }
//            }
//        }
//    }


//    @Throws(IOException::class)
//    private fun createImageFile(): File {
//        // Create an image file name
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
//        return File.createTempFile(
//            "JPEG_${timeStamp}_", /* prefix */
//            ".jpg", /* suffix */
//            storageDir /* directory */
//        ).apply {
//            // Save a file: path for use with ACTION_VIEW intents
//            currentPhotoPath = absolutePath
//        }
//    }




    private fun initActivity()
    {
        okBtn=findViewById(R.id.image_activity_okbtn)
        addImageBtn=findViewById(R.id.image_activity_addimg)
//        imageView=findViewById(R.id.image_activity_imgvw)
        camera = findViewById(R.id.camera)
    }
}