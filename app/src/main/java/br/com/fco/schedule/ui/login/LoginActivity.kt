package br.com.fco.schedule.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import br.com.andersonsoares.utils.hideKeyboard
import br.com.fco.schedule.MainActivity
import br.com.fco.schedule.R
import br.com.fco.schedule.ui.recover_password.RecoverPasswordActivity
import br.com.fco.schedule.ui.signup.SignupActivity
import br.com.fco.schedule.utils.startButtonProgress
import br.com.fco.schedule.utils.stopButtonProgress
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private var presenter: LoginPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val translationY =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65F, resources.displayMetrics)
        imgBackground.translationY = translationY
        imgBackground.scaleX = 1.8F
        imgBackground.scaleY = 2.5F
        imgBackground.scaleType = ImageView.ScaleType.CENTER_CROP
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
        presenter = LoginPresenter

        btnLogin.setOnClickListener {
            hideKeyboard()
            presenter?.handleLogin(edtEmail.text.toString(), edtPassword.text.toString())
        }

        edtEmail.doOnTextChanged { _, _, _, _ ->
            inputEmail.error = ""
        }

        edtPassword.doOnTextChanged { _, _, _, _ ->
            inputPassword.error = ""
        }

        presenter?.loginRequestFailed = {
            inputEmail.error = getString(R.string.activity_login_email_or_password_invalid)
            inputPassword.error = getString(R.string.activity_login_email_or_password_invalid)
        }

        presenter?.redirectToMain = {
            startActivity(
                Intent(
                    this@LoginActivity,
                    MainActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            )
        }

        presenter?.startButtonProgress = {
            startButtonProgress(loading, btnLogin)
        }

        presenter?.stopButtonProgress = {
            stopButtonProgress(loading, btnLogin, getString(R.string.activity_login_btn_login))
        }

        btnRecoverPassword.setOnClickListener {
            startActivity(
                Intent(this@LoginActivity, RecoverPasswordActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            )
        }

        btnSignup.setOnClickListener {
            startActivity(
                Intent(this@LoginActivity, SignupActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            )
        }
    }
}