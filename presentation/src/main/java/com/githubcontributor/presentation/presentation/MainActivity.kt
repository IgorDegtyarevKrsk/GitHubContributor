package com.githubcontributor.presentation.presentation

import android.animation.ValueAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.githubcontributor.domain.RequestData
import com.githubcontributor.domain.Variant
import com.githubcontributor.presentation.App
import com.githubcontributor.presentation.R
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private lateinit var accessTokenQuestionTextView: TextView
    private lateinit var userNameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var organizationEditText: EditText
    private lateinit var variantSpinner: Spinner
    private lateinit var loadContributorButton: Button
    private lateinit var cancelButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingStatusTextView: TextView

    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewMembers()
        initViewModel()
        initView()
    }

    private fun initViewMembers() {
        accessTokenQuestionTextView = findViewById(R.id.accessTokenQuestionTextView)
        userNameEditText = findViewById(R.id.githubUsernameEditText)
        passwordEditText = findViewById(R.id.passwordTokenEditText)
        organizationEditText = findViewById(R.id.organizationEditText)
        variantSpinner = findViewById(R.id.variantSpinner)
        loadContributorButton = findViewById(R.id.loadContributorsButton)
        cancelButton = findViewById(R.id.cancelButton)
        progressBar = findViewById(R.id.progressBar)
        loadingStatusTextView = findViewById(R.id.loadingStatusTextView)
    }

    private fun initViewModel() {
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this, factory)
            .get(MainViewModel::class.java)
    }

    private fun initView() {
        accessTokenQuestionTextView.isVisible = viewModel.accessTokenQuestionEnabledInit
        viewModel.accessTokenQuestionEnabled.observe(this) { nextVisible ->
            val isTextVisible = accessTokenQuestionTextView.isVisible
            if (isTextVisible && !nextVisible) {
                startHideAccessTokenAnimation()
            } else accessTokenQuestionTextView.isVisible = nextVisible
        }
        accessTokenQuestionTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://github.com/settings/tokens/new")
            startActivity(intent)
        }
        viewModel.savedVariant.apply {
            userNameEditText.setText(username)
            passwordEditText.setText(password)
            organizationEditText.setText(org)
        }

        val variants = Variant.values().map { it.toString() }
        variantSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, variants)
        variantSpinner.setSelection(Variant.values().indexOf(viewModel.savedVariant.variant))

        loadContributorButton.setOnClickListener {
            val req = RequestData(getUserName(), getPassword(), getOrganization())
            viewModel.chooseVariant(req, getSelectedVariant())
        }
        viewModel.loadButtonEnabled.observe(this) { isLoadEnabled ->
            loadContributorButton.isEnabled = isLoadEnabled
        }
        cancelButton.setOnClickListener {
            viewModel.cancelClicked()
        }
        viewModel.cancelButtonEnabled.observe(this) { isCancelEnabled ->
            cancelButton.isEnabled = isCancelEnabled
        }
        viewModel.iconRunning.observe(this) { isProgress ->
            progressBar.visibility = if (isProgress) View.VISIBLE else View.INVISIBLE
        }
        viewModel.loadingStatusText.observe(this) { statusText ->
            loadingStatusTextView.text = statusText
        }

        val userAdapter = UsersRecyclerViewAdapter()
        findViewById<RecyclerView>(R.id.recyclerView).adapter = userAdapter
        viewModel.users.observe(this) { users ->
            userAdapter.users = users
        }
    }

    private fun startHideAccessTokenAnimation() {
        val startHeight = accessTokenQuestionTextView.height
        val endHeight = 0

        val anim = ValueAnimator.ofInt(startHeight, endHeight)
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams = accessTokenQuestionTextView.layoutParams
            layoutParams.height = value
            accessTokenQuestionTextView.layoutParams = layoutParams
        }
        anim.addListener(onEnd = {
            accessTokenQuestionTextView.isVisible = false
        })
        anim.duration = 800
        anim.start()
    }

    private fun getOrganization() = organizationEditText.text.toString()
    private fun getUserName() = userNameEditText.text.toString()
    private fun getPassword() = passwordEditText.text.toString()

    private fun getSelectedVariant(): Variant {
        val selectedVariantName = variantSpinner.selectedItem as String
        return Variant.valueOf(selectedVariantName)
    }

}