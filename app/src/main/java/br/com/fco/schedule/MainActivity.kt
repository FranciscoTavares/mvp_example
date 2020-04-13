package br.com.fco.schedule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.fco.schedule.ui.closed_schedule.ClosedFragment
import br.com.fco.schedule.ui.open_schedule.OpenFragment
import br.com.fco.schedule.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(OpenFragment())

        navigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_open -> {
                    loadFragment(OpenFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_closed -> {
                    loadFragment(ClosedFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_profile -> {
                    loadFragment(ProfileFragment())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    override fun onResume() {
        super.onResume()
        navigationView.selectedItemId = R.id.navigation_open
    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.navHost, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
