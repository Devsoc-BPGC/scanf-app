package club.devsoc.scanf.view.activity

import android.Manifest
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.budiyev.android.codescanner.*
import club.devsoc.scanf.R
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import kotlinx.android.synthetic.main.activity_q_r_scan.*
import pub.devrel.easypermissions.EasyPermissions

class QRScanActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private val CAMERA_PERMISSION_REQUEST_CODE = 778

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_q_r_scan)
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(this,scannerView)

        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
                codeScanner.startPreview()
        } else {
            EasyPermissions.requestPermissions(this,
                getString(R.string.camera_permission_text),
                CAMERA_PERMISSION_REQUEST_CODE, Manifest.permission.CAMERA)
        }

        // Parameters (default values) can be changed according to needs
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks for qr scanner
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                //scanned info is stored in default variable 'it'.
                //alert dialog with copy, open link (only urls) and done button which shows the text of the qr code
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.apply {

                    setTitle("Result")
                    setMessage(it.text)

                    setPositiveButton("Done") { _, _ ->

                    }
                    //checks if text is url, if yes then shows a button to open in browser
                    if(Patterns.WEB_URL.matcher(it.text).matches()) {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.text))
                        setNegativeButton("Open in Browser") { _, _ ->
                            startActivity(browserIntent)
                        }
                    }
                    //copies text to clipboard
                    setNeutralButton("Copy") { _, _ ->
                            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("label",it.text)
                            clipboard.setPrimaryClip(clip)
                    }
                }.create().show()
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
//                error message stored in it.message
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        this.scanner_view.setOnClickListener{
            codeScanner.startPreview()
        }


    }
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray)
    {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}