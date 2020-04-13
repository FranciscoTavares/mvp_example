package br.com.fco.schedule.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.fco.schedule.MainActivity
import br.com.fco.schedule.R
import br.com.fco.schedule.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    private var presenter: SplashPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        initListeners()
    }

    override fun onPause() {
        super.onPause()
        stopListeners()
    }

    private fun stopListeners() {
        presenter = null
    }

    private fun initListeners() {
        presenter = SplashPresenter

        presenter?.redirectToMain = {
            finishAffinity()
            startActivity(
                Intent(this@SplashActivity, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            )
        }

        presenter?.redirectToLogin = {
            finishAffinity()
            startActivity(
                Intent(this@SplashActivity, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            )
        }

        presenter?.getUser()
    }
}