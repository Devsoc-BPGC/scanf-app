package club.devsoc.scanf.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import club.devsoc.scanf.R
import club.devsoc.scanf.view.fragment.HomeFragment

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment.newInstance())
                .commitNow()
        }
    }
}