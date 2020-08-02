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
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var documentScanButton: ImageView
    private lateinit var qrScanButton: ImageView
    private lateinit var bottomSheet: ConstraintLayout
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        drawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_recent_files)
        }

        defineLayouts()
        initActivity()
        attachOnClickListeners()
    }
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_settings -> {
            }
            R.id.nav_recent_files -> {
            }
            R.id.nav_document_viewer -> {
            }
        }
        return true
    }

    private fun defineLayouts() {
        // defining the main activity data binding
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // defining the bottom sheet layout
        bottomSheet = findViewById(R.id.bottom_sheet_layout)
    }

    private fun initActivity() {
        // defining the bottom sheet behaviour
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        // defining the buttons inside the bottom sheet
        // DATA BINDING IS NOT USED HERE, AS THE LAYOUT NEEDS TO BE A COORDINATOR LAYOUT
        bottomSheet.apply {
            documentScanButton = findViewById(R.id.document_scan_btn)
            qrScanButton = findViewById(R.id.qr_scan_btn)
        }

        // setting initial state of bottom sheet to hidden
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    // attaching on click listeners
    private fun attachOnClickListeners() {
        documentScanButton.setOnClickListener(this)
        qrScanButton.setOnClickListener(this)
        mainBinding.floatingActionButton.setOnClickListener(this)
        mainBinding.mainActivityLayout.setOnClickListener(this)
    }

    // toggling bottom sheet visibility when fab is pressed
    private fun onFabClicked() {
        if (bottomSheetBehavior.state == 2 || bottomSheetBehavior.state == 5)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        else
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    // whenever the user clicks anywhere on the screen, the bottom sheet is hidden
    private fun onScreenClicked() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.document_scan_btn -> Toast.makeText(
                this,
                getString(R.string.scan_document),
                Toast.LENGTH_SHORT).show()
            R.id.qr_scan_btn -> Toast.makeText(
                this,
                getString(R.string.qr_scanner),
                Toast.LENGTH_SHORT).show()
            R.id.floating_action_button -> onFabClicked()
            R.id.main_activity_layout -> onScreenClicked()
        }
    }
}