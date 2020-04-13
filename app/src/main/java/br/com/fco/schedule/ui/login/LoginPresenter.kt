package br.com.fco.schedule.ui.login

import br.com.fco.schedule.data.db.repositories.UserRepository
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

object LoginPresenter {

    var loginRequestFailed = {}
    var startButtonProgress = {}
    var stopButtonProgress = {}
    var redirectToMain = {}

    private val formHash: HashMap<String, Boolean> = hashMapOf(
        "email" to false,
        "password" to false
    )

    fun handleLogin(mail: String, password: String) {

        validatorMail(mail)
        validatorPassword(password)

        if (formIsValid()) requestLogin(mail, password)
        else loginRequestFailed
    }

    private fun requestLogin(mail: String, password: String) {
        startButtonProgress()
        if (UserRepository.authentication(
                mail,
                password
            )
        ) {
            redirectToMain()
        } else {
            stopButtonProgress()
            loginRequestFailed()
        }
    }

    private fun validatorMail(value: String) {
        value.trim().validator()
            .nonEmpty()
            .validEmail()
            .addSuccessCallback {
                formHash["email"] = true
            }.addErrorCallback {
                formHash["email"] = false
            }.check()
    }

    private fun validatorPassword(value: String) {
        value.trim().validator()
            .nonEmpty()
            .minLength(6)
            .addSuccessCallback {
                formHash["password"] = true
            }.addErrorCallback {
                formHash["password"] = false
            }.check()
    }

    /** Function that validate if all editText values are valid */
    private fun formIsValid(): Boolean {
        return !formHash.values.contains(false)
    }
}