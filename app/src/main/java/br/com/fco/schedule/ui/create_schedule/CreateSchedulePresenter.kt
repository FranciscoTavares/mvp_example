package br.com.fco.schedule.ui.create_schedule

import br.com.fco.schedule.data.db.entities.Schedule
import br.com.fco.schedule.data.db.repositories.ScheduleRepository
import br.com.fco.schedule.data.db.repositories.UserRepository
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

object CreateSchedulePresenter {
    var startProgress = {}
    var stopProgress = {}
    var updateUi = {}
    var setError: (stateEmpty: Boolean) -> Unit = {}
    var enableSignupButton = {}
    var disableSignupButton = {}


    private val formHash: HashMap<String, Boolean> = hashMapOf(
        "title" to false,
        "description" to false,
        "details" to false
    )

    fun validatorTitle(value: String) {
        value.validator()
            .nonEmpty()
            .minLength(2)
            .addSuccessCallback {
                formHash["title"] = true
                if (formIsValid()) enableSignupButton()
            }.addErrorCallback {
                formHash["title"] = false
                disableSignupButton()
            }.check()
    }

    fun validatorDescription(value: String) {
        value.validator()
            .nonEmpty()
            .minLength(2)
            .addSuccessCallback {
                formHash["description"] = true
                if (formIsValid()) enableSignupButton()
            }.addErrorCallback {
                formHash["description"] = false
                disableSignupButton()
            }.check()
    }

    fun validatorDetails(value: String) {
        value.validator()
            .nonEmpty()
            .minLength(2)
            .addSuccessCallback {
                formHash["details"] = true
                if (formIsValid()) enableSignupButton()
            }.addErrorCallback {
                formHash["details"] = false
                disableSignupButton()
            }.check()

    }

    private fun formIsValid(): Boolean {
        return !formHash.values.contains(false)
    }

    fun getLoggedUser(callBack: (userName: String) -> Unit) {
        UserRepository.getLoggedUser()?.let {
            callBack(it.name)
        }
    }

    fun handlerCreateSchedule(schedule: Schedule) {
        startProgress()
        ScheduleRepository.initChangeListener {
            stopProgress()
            updateUi()
        }
        ScheduleRepository.insertOne(schedule)
    }
}