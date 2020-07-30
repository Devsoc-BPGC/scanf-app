package club.devsoc.scanf.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import club.devsoc.scanf.R
import club.devsoc.scanf.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var documentScanButton : ImageView
    private lateinit var qrScanButton : ImageView
    private lateinit var bottomSheet : ConstraintLayout
    private lateinit var bottomSheetBehavior : BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        defineLayouts()
        initActivity()
        attachOnClickListeners()
    }

    private fun defineLayouts(){
        //defining the main activity data binding
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //defining the bottom sheet layout
        bottomSheet  = findViewById(R.id.bottom_sheet_layout)
    }

    private fun initActivity(){
        //defining the bottom sheet behaviour
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        //defining the buttons inside the bottom sheet
        //DATA BINDING IS NOT USED HERE, AS THE LAYOUT NEEDS TO BE A COORDINATOR LAYOUT
        bottomSheet.apply {
            documentScanButton = findViewById(R.id.document_scan_btn)
            qrScanButton = findViewById(R.id.qr_scan_btn)
        }

        //setting initial state of bottom sheet to hidden
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    //attaching on click listeners
    private fun attachOnClickListeners(){
        documentScanButton.setOnClickListener(this)
        qrScanButton.setOnClickListener(this)
        mainBinding.floatingActionButton.setOnClickListener(this)
        mainBinding.mainActivityLayout.setOnClickListener(this)
    }

    //toggling bottom sheet visibility when fab is pressed
    private fun onFabClicked(){
        if (bottomSheetBehavior.state == 2 || bottomSheetBehavior.state == 5 )
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        else
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    //whenever the user clicks anywhere on the screen, the bottom sheet is hidden
    private fun onScreenClicked(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.document_scan_btn -> Toast.makeText(this, "Scan document", Toast.LENGTH_SHORT).show()
            R.id.qr_scan_btn -> Toast.makeText(this, "QR Scanner", Toast.LENGTH_SHORT).show()
            R.id.floating_action_button ->onFabClicked()
            R.id.main_activity_layout -> onScreenClicked()
        }
    }
}