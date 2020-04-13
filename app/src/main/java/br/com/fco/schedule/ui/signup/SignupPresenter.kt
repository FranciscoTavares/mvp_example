package br.com.fco.schedule.ui.signup

import br.com.fco.schedule.data.db.entities.User
import br.com.fco.schedule.data.db.repositories.UserRepository
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

object SignupPresenter {

    private val formHash: HashMap<String, Boolean> = hashMapOf(
        "name" to false,
        "email" to false,
        "password" to false
    )

    var enableSignupButton = {}
    var disableSignupButton = {}
    var startButtonProgress = {}
    var registeredSuccessfully = {}
    var registrationFailed = {}

    fun validatorName(value: String) {
        value.validator()
            .nonEmpty()
            .minLength(2)
            .addSuccessCallback {
                formHash["name"] = true
                if (formIsValid()) enableSignupButton()
            }.addErrorCallback {
                formHash["name"] = false
                disableSignupButton()
            }.check()
    }

    fun validatorMail(value: String) {
        value.validator()
            .nonEmpty()
            .validEmail()
            .addSuccessCallback {
                formHash["email"] = true
                if (formIsValid()) enableSignupButton()
            }.addErrorCallback {
                formHash["email"] = false
                disableSignupButton()
            }.check()
    }

    fun validatorPassword(value: String) {
        value.validator()
            .nonEmpty()
            .minLength(6)
            .addSuccessCallback {
                formHash["password"] = true
                if (formIsValid()) enableSignupButton()
            }.addErrorCallback {
                formHash["password"] = false
                disableSignupButton()
            }.check()
    }

    /** Function that validate if all editText values are valid */
    private fun formIsValid(): Boolean {
        return !formHash.values.contains(false)
    }

    fun handleSignup(user: User) {
        if (formIsValid()) {
            startButtonProgress
            UserRepository.insertOne(user) { success ->
                if (success) {
                    registeredSuccessfully()
                } else {
                    registrationFailed()
                }
            }
        }
    }
}