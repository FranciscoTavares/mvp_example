package br.com.fco.schedule.ui.profile

import br.com.fco.schedule.data.db.entities.User
import br.com.fco.schedule.data.db.repositories.UserRepository

object ProfilePresenter {

    var redirectToLogin = {}
    var showLogoutError = {}

    fun getUser(user: (user: User) -> Unit) {
        UserRepository.getLoggedUser()?.let {
            user(it)
        }
    }

    fun setLogout() {
        UserRepository.setLogout { success ->
            if (success) {
                redirectToLogin()
            } else {
                showLogoutError()
            }
        }
    }
}