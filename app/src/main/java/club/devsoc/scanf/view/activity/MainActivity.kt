package club.devsoc.scanf.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import club.devsoc.scanf.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*

class MainActivity : AppCompatActivity(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var bottomSheet : ConstraintLayout
    private lateinit var bottomSheetBehavior :
            BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        defineLayouts()
        initActivity()
        attachOnClickListeners()

        nav_view.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
                nav_view
                .setCheckedItem(R.id.nav_recent_files)
        }
    }
    override fun onNavigationItemSelected
                (menuItem: MenuItem): Boolean {
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
        // defining the bottom sheet layout
        bottomSheet = bottom_sheet_layout
    }

    private fun initActivity() {
        // defining the bottom sheet behaviour
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        // setting initial state of bottom sheet to hidden
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    // attaching on click listeners
    private fun attachOnClickListeners() {
        document_scan_btn.setOnClickListener(this)
        qr_scan_btn.setOnClickListener(this)
        floating_action_button.setOnClickListener(this)
        main_activity_layout.setOnClickListener(this)
    }

    // toggling bottom sheet visibility when fab is pressed
    private fun onFabClicked() {
        if (bottomSheetBehavior.state == 2 ||
            bottomSheetBehavior.state == 5)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        else
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    // whenever the user clicks anywhere on the screen, the bottom sheet is hidden
    private fun onScreenClicked() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onClick(v: View?) {
        when (v) {
            document_scan_btn -> Toast.makeText(
                this,
                getString(R.string.scan_document),
                Toast.LENGTH_SHORT).show()
            qr_scan_btn -> Toast.makeText(
                this,
                getString(R.string.qr_scanner),
                Toast.LENGTH_SHORT).show()
            floating_action_button -> onFabClicked()
            main_activity_layout -> onScreenClicked()
        }
    }
}