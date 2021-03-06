package pl.elpassion.eltc.login

import android.os.Bundle
import android.util.Base64
import com.elpassion.android.view.hide
import com.elpassion.android.view.show
import kotlinx.android.synthetic.main.login_activity.*
import pl.elpassion.eltc.*
import pl.elpassion.eltc.util.snack

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        initModel()
        loginButton.setOnClickListener {
            val credentials = getCredentials(user.text.toString(), password.text.toString())
            model.perform(SubmitCredentials(address.text.toString(), credentials))
        }
        model.perform(StartApp)
    }

    override fun showState(state: AppState?) {
        when (state) {
            is LoginState -> showLogin(state)
            is LoadingBuildsState -> {
                loginButton.isClickable = false
                loader.show()
            }
            is BuildsState -> openBuildsScreen()
        }
    }

    private fun showLogin(state: LoginState) {
        loginForm.show()
        loginButton.isClickable = true
        if (state.error != null) {
            showLoginError(state.error)
        }
    }

    private fun showLoginError(error: LoginState.Error) {
        loader.hide()
        coordinator.snack(error.message)
        model.perform(AcceptLoginError)
    }

    private fun getCredentials(user: String, password: String): String {
        val data = "$user:$password".toByteArray()
        return Base64.encodeToString(data, Base64.NO_WRAP)
    }
}