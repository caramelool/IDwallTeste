package br.com.caramelo.idwallteste.ui.auth

import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.transition.TransitionManager
import android.view.View
import br.com.caramelo.idwallteste.R
import br.com.caramelo.idwallteste.ext.doOnLayout
import br.com.caramelo.idwallteste.ext.onTextChangeListener
import br.com.caramelo.idwallteste.ui.base.BaseActivity
import br.com.caramelo.idwallteste.ui.base.BaseViewModel
import br.com.caramelo.idwallteste.ui.base.State
import br.com.caramelo.idwallteste.ui.feed.category.FeedCategoryActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

private const val EXTRA_EMAIL = "extra_email"

class AuthActivity : BaseActivity() {

    @Inject
    lateinit var factory: AuthViewModel.Factory

    private val authViewModel by lazy { viewModel<AuthViewModel>(factory).value }

    override val viewModels: Array<BaseViewModel>
        get() = arrayOf(authViewModel)

    private val titleY by lazy { resources.getDimension(R.dimen.title_margin_top) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        constraintLayout.doOnLayout {

            if (savedInstanceState == null) {
                titleView.visibility = View.VISIBLE
                emailTextInput.visibility = View.GONE
                nextButton.visibility = View.GONE
                startAnimation()
            } else {
                val email = savedInstanceState.getString(EXTRA_EMAIL)
                titleView.y = titleY
                emailTextInput.editText?.setText(email)
                emailTextInput.editText?.setSelection(email?.length ?: 0)
            }

        }

        emailTextInput.editText?.onTextChangeListener {
            authViewModel.setEmail(it)
        }

        nextButton.setOnClickListener {
            authViewModel.auth()
        }
    }

    override fun render(state: State) {
        when (state) {
            is AuthViewModelState.ButtonVisibility -> {
                buttonVisibility(state.visible)
            }
            is AuthViewModelState.InvalidEmailAddress -> {
                showError(R.string.email_invalid_message)
            }
            is AuthViewModelState.InProgressAddress -> {
                showLoading()
            }
            is AuthViewModelState.Success -> {
                starFeed()
            }
            is AuthViewModelState.Fail -> {
                hideLoading()
                showError(R.string.auth_unsuccessful_message)
            }
        }
    }

    private fun buttonVisibility(visible: Boolean) {
        emailTextInput.error?.let {
            emailTextInput.error = null
        }
        nextButton.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun showError(@StringRes resId: Int) {
        emailTextInput.error = getString(resId)
    }

    private fun starFeed() {
        val intent = Intent(this, FeedCategoryActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startAnimation() {
        constraintLayout.postDelayed({
            titleView.animate()
                .y(titleY)
                .setDuration(500)
                .start()
            TransitionManager.beginDelayedTransition(constraintLayout)
            emailTextInput.visibility = View.VISIBLE
        }, 2000)
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
        nextButton.visibility = View.GONE
        emailTextInput.isEnabled = false
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
        nextButton.visibility = View.VISIBLE
        emailTextInput.isEnabled = true
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(EXTRA_EMAIL, emailTextInput.editText?.text?.toString())
    }
}
