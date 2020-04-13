package br.com.fco.schedule.ui.recover_password

import br.com.fco.schedule.data.db.entities.User
import br.com.fco.schedule.data.db.repositories.UserRepository
import com.github.tntkhang.gmailsenderlibrary.GMailSender
import com.github.tntkhang.gmailsenderlibrary.GmailListener
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

object RecoverPasswordPresenter {

    var setFieldError = {}
    var getUserFail = {}
    var sendMailFail = {}
    var sendMailSuccess = {}
    var bodyBuilderMail: (user: User) -> Unit = {}
    var startButtonProgress = {}

    fun handlerSendMail(mail: String) {
        if (validatorEmail(mail)) getUserByEmail(mail)
        else setFieldError()

    }

    private fun validatorEmail(value: String): Boolean {
        var isValid = false
        value.trim().validator()
            .nonEmpty()
            .validEmail()
            .addSuccessCallback {
                isValid = true
            }.addErrorCallback {
                isValid = false
            }.check()
        return isValid
    }

    private fun getUserByEmail(userMail: String) {
        UserRepository.getUserByEmail(userMail)?.let {
            bodyBuilderMail(it)
        } ?: run {
            getUserFail()
        }
    }

    fun sendMail(
        senderEmail: String,
        senderPassword: String,
        title: String,
        sender: String,
        body: String,
        to: String
    ) {

        startButtonProgress()

        GMailSender.withAccount(senderEmail, senderPassword)
            .withTitle(title)
            .withBody(body)
            .withSender(sender)
            .toEmailAddress(to)
            .withListenner(object : GmailListener {
                override fun sendFail(err: String?) {
                    sendMailFail()
                }

                override fun sendSuccess() {
                    sendMailSuccess()
                }
            }).send()
    }
}