package br.com.caramelo.idwallteste.ui.auth

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.transition.TransitionManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.view.doOnLayout
import br.com.caramelo.idwallteste.R
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

        emailTextInput.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                emailTextInput.error?.let {
                    emailTextInput.error = null
                }
                nextButton.visibility = if (editable?.isEmpty() == true) View.GONE else View.VISIBLE
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        nextButton.setOnClickListener {
            val email = emailTextInput.editText?.text.toString()
            viewModel.auth(email)
        }

        if (savedInstanceState == null) {
            titleView.visibility = View.VISIBLE
            emailTextInput.visibility = View.GONE
            nextButton.visibility = View.GONE

            startAnimation()
        } else {
            constraintLayout.doOnLayout {
                val email = savedInstanceState.getString(EXTRA_EMAIL)
                titleView.y = titleY
                emailTextInput.editText?.setText(email)
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
            AuthStep.SUCCESS -> starDogPager()
            AuthStep.FAIL -> {
                hideLoading()
                showError(R.string.auth_unsuccessful_message)
            }
        }
    }

    private fun showError(@StringRes resId: Int) {
        emailTextInput.error = getString(resId)
    }

    private fun starDogPager() {
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
