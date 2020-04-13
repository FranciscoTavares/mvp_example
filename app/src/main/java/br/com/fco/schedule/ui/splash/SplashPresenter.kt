package br.com.fco.schedule.ui.splash

import br.com.fco.schedule.data.db.repositories.UserRepository

object SplashPresenter {

    var redirectToMain = {}
    var redirectToLogin = {}

    fun getUser() {
        UserRepository.getLoggedUser()?.let {
            redirectToMain()
        } ?: run {
            redirectToLogin()
        }
    }
}