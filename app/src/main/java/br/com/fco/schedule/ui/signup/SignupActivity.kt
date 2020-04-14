package br.com.fco.schedule.ui.signup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import br.com.fco.schedule.R
import br.com.fco.schedule.data.db.entities.User
import br.com.fco.schedule.utils.startButtonProgress
import br.com.fco.schedule.utils.stopButtonProgress
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.toast

class SignupActivity : AppCompatActivity() {

    private var presenter: SignupPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
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

        presenter = SignupPresenter

        edtName.doOnTextChanged { _, _, _, _ ->
            presenter?.validatorName(edtName.text.toString())
        }

        edtEmail.doOnTextChanged { _, _, _, _ ->
            presenter?.validatorMail(edtEmail.text.toString())
        }

        edtPassword.doOnTextChanged { _, _, _, _ ->
            presenter?.validatorPassword(edtPassword.text.toString())
        }

        presenter?.enableSignupButton = {
            btnSignup.isEnabled = true
        }

        presenter?.disableSignupButton = {
            btnSignup.isEnabled = false
        }

        presenter?.startButtonProgress = {
            startButtonProgress(loading, btnSignup)
        }

        presenter?.registeredSuccessfully = {
            toast(getString(R.string.activity_signup_msg_success))
            onBackPressed()
        }

        presenter?.registrationFailed = {
            stopButtonProgress(
                loading,
                btnSignup,
                getString(R.string.activity_signup_btn_signup)
            )
        }

        btnSignup.setOnClickListener {
            handleSignup()
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    /** Function to handle signup request */
    private fun handleSignup() {
        val user = User()
        user.name = edtName.text.toString().trim()
        user.email = edtEmail.text.toString().trim()
        user.password = edtPassword.text.toString().trim()

        presenter?.handleSignup(user)
    }
}