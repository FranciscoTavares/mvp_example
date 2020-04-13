package br.com.fco.schedule.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.fco.schedule.R
import br.com.fco.schedule.data.db.entities.User
import br.com.fco.schedule.ui.splash.SplashActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.toast

class ProfileFragment : Fragment() {

    private var presenter: ProfilePresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        presenter = ProfilePresenter

        presenter?.getUser {
            setUserData(it)
        }

        btnLogout.setOnClickListener {
            presenter?.setLogout()
        }

        presenter?.redirectToLogin = {
            activity?.let {
                it.finishAffinity()
                startActivity(Intent(it, SplashActivity::class.java))
            }
        }

        presenter?.showLogoutError = {
            context?.toast(getString(R.string.profile_fragment_msg_logout))
        }
    }

    private fun setUserData(user: User) {
        edtName.setText(user.name)
        edtEmail.setText(user.email)
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

}
