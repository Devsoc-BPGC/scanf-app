package club.devsoc.scanf.view.activity

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import club.devsoc.scanf.BuildConfig
import club.devsoc.scanf.R
import club.devsoc.scanf.showDialogOK
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.priyankvasa.android.cameraviewex.CameraView
import com.priyankvasa.android.cameraviewex.ErrorLevel
import com.priyankvasa.android.cameraviewex.Image
import com.priyankvasa.android.cameraviewex.Modes
import com.scanlibrary.ScanActivity
import com.scanlibrary.ScanConstants
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ImageActivity : AppCompatActivity() {


    private lateinit var addImageBtn:ImageView
    private lateinit var okBtn:ImageView
    private lateinit var imageView: ImageView
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String
    var persistentImageName: String = "scanned.jpg"
    private val IMAGE_CAPTURE = 10
    private lateinit var camera:CameraView
    private val DOCUMENT_SCAN = 20
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 7
    private lateinit var uriList:ArrayList<Uri>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        initActivity()

        onClick()

        checkAndRequestPermissions()

    }

    private fun onClick() {
        okBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, ScanActivity::class.java)
            intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, ScanConstants.OPEN_CAMERA)
            startActivityForResult(intent, DOCUMENT_SCAN)
            Log.i("TAG", ">>>>>>>>>>>>>>>>>onClick: clicked btn")
        })

        addImageBtn.setOnClickListener(View.OnClickListener {
            val cameraImgIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            cameraImgIntent.putExtra(
                MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(
                    applicationContext, BuildConfig.APPLICATION_ID + ".fileprovider",
                    File(applicationContext.filesDir, persistentImageName)
                )
            )
            startActivityForResult(cameraImgIntent, IMAGE_CAPTURE)
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when(requestCode) {
            IMAGE_CAPTURE -> {
                imageView.setImageBitmap(BitmapFactory.decodeFile("${applicationContext.filesDir}/${persistentImageName}"))
            }

            DOCUMENT_SCAN -> {
                val uri: Uri = data?.extras?.getParcelable(ScanConstants.SCANNED_RESULT)!!
                uriList.add(uri)
                Log.i("TAG", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>onActivityResult: "+uri.toString())
                var bitmap: Bitmap? = null
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    contentResolver.delete(uri, null, null)
                    imageView.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val camera = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.CAMERA
        )
        val readExtStorage = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val writeExtStorage = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }

        if (readExtStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (writeExtStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                val perms: MutableMap<String, Int> = HashMap()
                // Initialize the map with both permissions
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED

                // Fill with actual results from user
                if (grantResults.isNotEmpty()) {
                    var i = 0
                    while (i < permissions.size) {
                        perms[permissions[i]] = grantResults[i]
                        i++
                    }

                    // Check for both permissions
                    if (perms[Manifest.permission.CAMERA] != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.CAMERA
                            )
                        ) {
//                            showDialogOK("Camera permission required for this app") { "_" , which ->
//                                when (which) {
//                                    DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
//                                    DialogInterface.BUTTON_NEGATIVE -> {
//                                    }
//                                }
//                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Go to settings and enable permissions",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }




    private fun initActivity()
    {
        okBtn=findViewById(R.id.image_activity_okbtn)
        addImageBtn=findViewById(R.id.image_activity_addimg)
        imageView=findViewById(R.id.image_activity_imgvw)
    }
}