package br.com.fco.schedule.ui.create_schedule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import br.com.andersonsoares.utils.hideKeyboard
import br.com.fco.schedule.R
import br.com.fco.schedule.data.db.entities.Schedule
import br.com.fco.schedule.utils.startButtonProgress
import br.com.fco.schedule.utils.stopButtonProgress
import kotlinx.android.synthetic.main.activity_create_schedule.*

class CreateScheduleActivity : AppCompatActivity() {

    private var presenter: CreateSchedulePresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_schedule)
    }

    override fun onResume() {
        super.onResume()
        initListeners()
    }

    override fun onStop() {
        stopListeners()
        super.onStop()
    }

    private fun stopListeners() {
        presenter = null
    }

    private fun initListeners() {

        presenter = CreateSchedulePresenter

        presenter?.updateUi = {
            onBackPressed()
        }

        presenter?.getLoggedUser { userName ->
            edtAuthor.setText(userName)
        }

        edtTitle.doOnTextChanged { _, _, _, _ ->
            presenter?.validatorTitle(edtTitle.text.toString())
        }

        edtDescription.doOnTextChanged { _, _, _, _ ->
            presenter?.validatorDescription(edtDescription.text.toString())
        }

        edtDetails.doOnTextChanged { _, _, _, _ ->
            presenter?.validatorDetails(edtDetails.text.toString())
        }

        presenter?.startProgress = {
            startButtonProgress(loading, btnConclude)
        }

        presenter?.stopProgress = {
            stopButtonProgress(
                loading,
                btnConclude,
                getString(R.string.activity_create_schedule_btn_conclude)
            )
        }

        presenter?.enableSignupButton = {
            btnConclude.isEnabled = true
        }

        presenter?.disableSignupButton = {
            btnConclude.isEnabled = false
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnConclude.setOnClickListener {
            createSchedule()
        }
    }

    private fun createSchedule() {
        hideKeyboard()
        val schedule = Schedule()
        schedule.title = edtTitle.text.toString().trim()
        schedule.description = edtDescription.text.toString().trim()
        schedule.details = edtDetails.text.toString()
        schedule.author = edtAuthor.text.toString().trim()

        presenter?.handlerCreateSchedule(schedule)
    }
}