package br.com.caramelo.idwallteste.ui.auth

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.transition.TransitionManager
import android.view.View
import androidx.view.doOnLayout
import br.com.caramelo.idwallteste.R
import br.com.caramelo.idwallteste.ext.onTextChangeListener
import br.com.caramelo.idwallteste.ui.base.BaseActivity
import br.com.caramelo.idwallteste.ui.feed.list.FeedActivity
import kotlinx.android.synthetic.main.activity_auth.*

private const val EXTRA_EMAIL = "extra_email"

class AuthActivity : BaseActivity() {

    private val viewModel by lazy {
        providesOf(AuthViewModel::class)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
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
                    emailTextInput.editText?.setSelection(email.length)
            }

            emailTextInput.editText?.onTextChangeListener {
                emailTextInput.error?.let {
                    emailTextInput.error = null
                }
                nextButton.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            }

            nextButton.setOnClickListener {
                val email = emailTextInput.editText?.text.toString()
                viewModel.auth(email)
            }

        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.stepLiveData.observe(this, stepObserver)
    }

    private val stepObserver = Observer<AuthStep> {
        when (it) {
            AuthStep.INVALID_EMAIL_ADDRESS -> {
                showError(R.string.email_invalid_message)
            }
            AuthStep.IN_PROGRESS -> showLoading()
            AuthStep.SUCCESS -> {
                starFeed()
            }
            AuthStep.FAIL -> {
                hideLoading()
                showError(R.string.auth_unsuccessful_message)
            }
        }
    }

    private fun showError(@StringRes resId: Int) {
        emailTextInput.error = getString(resId)
    }

    private fun starFeed() {
        val intent = Intent(this, FeedActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startAnimation() {
        constraintLayout.postDelayed( {
            titleView.animate()
                    .y(titleY)
                    .setDuration(500)
                    .start()
            TransitionManager.beginDelayedTransition(constraintLayout)
            emailTextInput.visibility = View.VISIBLE
        }, 2000)
    }

    private val titleY by lazy { resources.getDimension(R.dimen.title_margin_top) }

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
