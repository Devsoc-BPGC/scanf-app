package club.devsoc.scanf.view.activity

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import club.devsoc.scanf.R
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

    private lateinit var codeScanner: CodeScanner;
    private val CAMERA_PERMISSION_REQUEST_CODE = 778;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_q_r_scan)
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(this,scannerView);

        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
                codeScanner.startPreview();
        } else {
            EasyPermissions.requestPermissions(this,
                getString(R.string.camera_permission_text),
                CAMERA_PERMISSION_REQUEST_CODE, Manifest.permission.CAMERA);
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
                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
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
            codeScanner.startPreview();
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
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}