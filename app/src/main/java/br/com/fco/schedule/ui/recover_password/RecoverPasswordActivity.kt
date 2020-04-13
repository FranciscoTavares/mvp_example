package br.com.fco.schedule.ui.recover_password

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import br.com.andersonsoares.utils.hideKeyboard
import br.com.fco.schedule.BuildConfig
import br.com.fco.schedule.R
import br.com.fco.schedule.utils.startButtonProgress
import br.com.fco.schedule.utils.stopButtonProgress
import kotlinx.android.synthetic.main.activity_recover_password.*
import org.jetbrains.anko.toast

class RecoverPasswordActivity : AppCompatActivity() {

    private var presenter: RecoverPasswordPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)
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

        presenter = RecoverPasswordPresenter

        edtEmail.doOnTextChanged { _, _, _, _ ->
            inputEmail.error = ""
        }

        presenter?.setFieldError = {
            inputEmail.error = getString(R.string.activity_recover_email_invalid)
        }

        presenter?.bodyBuilderMail = {
            val body = getString(R.string.activity_recover_email_body)
                .replace("{1}", it.name).replace("{2}", it.password)

            presenter?.sendMail(
                BuildConfig.EMAIL_ACCOUNT,
                BuildConfig.EMAIL_ACCOUNT_PASSWORD,
                getString(R.string.activity_recover_email_title),
                BuildConfig.SENDER,
                body,
                it.email
            )
        }

        presenter?.startButtonProgress = {
            startButtonProgress(loading, btnSend)
        }

        presenter?.sendMailFail = {
            stopButtonProgress(
                loading,
                btnSend,
                getString(R.string.activity_recover_password_btn_send)
            )
            toast(getString(R.string.activity_recover_email_fail))
        }

        presenter?.sendMailSuccess = {
            stopButtonProgress(
                loading,
                btnSend,
                getString(R.string.activity_recover_password_btn_send)
            )
            toast(getString(R.string.activity_recover_email_success))
            onBackPressed()
        }

        presenter?.getUserFail = {
            toast(getString(R.string.activity_recover_email_invalid))
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnSend.setOnClickListener {
            hideKeyboard()
            presenter?.handlerSendMail(edtEmail.text.toString())
        }
    }
}