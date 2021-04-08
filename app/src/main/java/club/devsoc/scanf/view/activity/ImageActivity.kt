package club.devsoc.scanf.view.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import club.devsoc.scanf.R
import club.devsoc.scanf.model.ImageModel
import club.devsoc.scanf.view.adapter.ImageViewAdapter
import club.devsoc.scanf.viewmodel.ImageActivityViewModel
import com.priyankvasa.android.cameraviewex.CameraView
import com.scanlibrary.ScanActivity
import com.scanlibrary.ScanConstants
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ImageActivity : AppCompatActivity() {


    private lateinit var addImageBtn:ImageView
    private lateinit var okBtn:ImageView
    private lateinit var recycler_view:RecyclerView
    private var adapterList:ArrayList<ImageModel> = ArrayList()
    private lateinit var adapter:ImageViewAdapter
    private lateinit var imageView: ImageView
    private lateinit var saveButton:Button
    private lateinit var numImagesTV:TextView
    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var imageFileName:String
    lateinit var currentPhotoPath: String
    var persistentImageName: String = "scanned.jpg"
    private val IMAGE_CAPTURE = 10
    private lateinit var camera:CameraView
    private val DOCUMENT_SCAN = 20
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 7
    private var uriList:ArrayList<Bitmap> = ArrayList()
    private lateinit var viewModel: ImageActivityViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        initActivity()

        setupViewModel()

        setupUI()

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

//        addImageBtn.setOnClickListener(View.OnClickListener {
//            val cameraImgIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//
//            cameraImgIntent.putExtra(
//                MediaStore.EXTRA_OUTPUT,
//                FileProvider.getUriForFile(
//                    applicationContext, BuildConfig.APPLICATION_ID + ".fileprovider",
//                    File(applicationContext.filesDir, persistentImageName)
//                )
//            )
//            startActivityForResult(cameraImgIntent, IMAGE_CAPTURE)
//        })


            saveButton.visibility=View.INVISIBLE

        saveButton.setOnClickListener(View.OnClickListener {
            //TAKE INPUT FROM USER
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle("Set Document Name")
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_with_edittext, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
            var timestamp:String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            editText.setText("PDF_" + timestamp)
            imageFileName = "PDF_"+timestamp
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i ->
                Toast.makeText(applicationContext, "Name set: " + editText.text.toString(), Toast.LENGTH_SHORT).show()
                imageFileName = editText.text.toString()
                createPDFWithMultipleImage()
            }
            builder.show()

        })
    }

    private fun createPDFWithMultipleImage()
    {
        var file = getOutputFile()
        Log.i("TAG", ">>>>>>>>>>>>>>>>>>>>>>createPDFWithMultipleImage: " + file!!.absolutePath)
        if (file != null) {
            try {
                var fileOutputStream = FileOutputStream(file)
                var pdfDocument = PdfDocument()
                for (i in 0 until uriList.size) {
                    Log.i("TAG", ">>>>>>>>>>>>>>createPDFWithMultipleImage: " + uriList[0])
                    Log.i(
                        "TAG",
                        ">>>>>>>>>>>>>>createPDFWithMultipleImage: " + uriList.size.toString()
                    )

//                    var bitmap = BitmapFactory.decodeFile(uriList[i])
                    var bitmap: Bitmap? = null
                    bitmap = uriList[i]
//                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uriList[i])
//                    contentResolver.delete(uriList[i], null, null)
                    var pageInfo: PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(
                        bitmap.width,
                        bitmap.height,
                        i + 1
                    ).create()
                    var page: PdfDocument.Page = pdfDocument.startPage(pageInfo)
                    var canvas: Canvas = page.canvas
                    var paint = Paint()
                    paint.color = Color.BLUE
                    canvas.drawPaint(paint)
                    canvas.drawBitmap(bitmap, 0f, 0f, null)
                    pdfDocument.finishPage(page)

                    bitmap.recycle()
                }
                pdfDocument.writeTo(fileOutputStream)
                pdfDocument.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getOutputFile(): File?
    {
        var root = File(getExternalFilesDir(null), "My PDF Folder")
        var isFolderCreated = true

        if (!root.exists()){
            isFolderCreated = root.mkdir()
        }

        if (isFolderCreated)
        {
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
//            String imageFileName = "PDF_" + timeStamp;




            return File(root, imageFileName + ".pdf")
        }
        else
        {
            Toast.makeText(this, "Folder is not created", Toast.LENGTH_SHORT).show();
            return null;
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }
        saveButton.visibility=View.VISIBLE
        when(requestCode) {
            IMAGE_CAPTURE -> {
                imageView.setImageBitmap(BitmapFactory.decodeFile("${applicationContext.filesDir}/${persistentImageName}"))
            }

            DOCUMENT_SCAN -> {
                val uri: Uri = data?.extras?.getParcelable(ScanConstants.SCANNED_RESULT)!!
//                numImagesTV.setText(String.format("Number of images: %f",uriList.size))
                var bitmap: Bitmap? = null
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    contentResolver.delete(uri, null, null)
                    uriList.add(bitmap)
                    adapterList.add(ImageModel(bitmap))
                    adapter.addData(adapterList)
                    adapter.notifyDataSetChanged()
//                    val sharedPref = this.getSharedPreferences("Prefs",Context.MODE_PRIVATE) ?: return
//                    val defaultValue = resources.getString(R.string.image_path)
//                    val image_path = sharedPref.getString(getString(R.string.image_path), defaultValue)
//                    with (sharedPref.edit()) {
//                        clear()
//                    }
//                    if (image_path != null) {
//                        uriList.add(image_path)
//                    }

                    Log.i(
                        "TAG",
                        ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>onActivityResult: " + uri.toString()
                    )

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

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(ImageActivityViewModel::class.java)
    }

    private fun setupUI() {
        recycler_view = findViewById(R.id.image_ativity_rv);
        recycler_view!!.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        adapter = ImageViewAdapter(arrayListOf())
        recycler_view!!.adapter = adapter;
    }


    private fun initActivity()
    {
        okBtn=findViewById(R.id.image_activity_okbtn)
//        addImageBtn=findViewById(R.id.image_activity_addimg)
//        imageView=findViewById(R.id.image_activity_imgvw)
        saveButton=findViewById(R.id.image_activity_save_btn);
        numImagesTV=findViewById(R.id.num_of_images_txt)
//        camera = findViewById(R.id.camera)
    }
}