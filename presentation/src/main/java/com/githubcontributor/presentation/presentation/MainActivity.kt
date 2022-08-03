package com.githubcontributor.presentation.presentation

import android.animation.ValueAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.githubcontributor.domain.RequestData
import com.githubcontributor.domain.Variant
import com.githubcontributor.presentation.App
import com.githubcontributor.presentation.databinding.ActivityMainBinding
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initViewModel()
        initView()
    }

    private fun initViewModel() {
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this, factory)
            .get(MainViewModel::class.java)
    }

    private fun initView() {
        initTokenQuestion()
        viewModel.savedVariant.apply {
            binding.githubUsernameEditText.setText(username)
            binding.passwordTokenEditText.setText(password)
            binding.organizationEditText.setText(org)
        }

        val variants = Variant.values().map { it.toString() }
        binding.variantSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, variants)
        binding.variantSpinner.setSelection(Variant.values().indexOf(viewModel.savedVariant.variant))

        binding.loadContributorsButton.setOnClickListener {
            val req = RequestData(getUserName(), getPassword(), getOrganization())
            viewModel.chooseVariant(req, getSelectedVariant())
        }
        viewModel.loadButtonEnabled.observe(this) { isLoadEnabled ->
            binding.loadContributorsButton.isEnabled = isLoadEnabled
        }
        binding.cancelButton.setOnClickListener {
            viewModel.cancelClicked()
        }
        viewModel.cancelButtonEnabled.observe(this) { isCancelEnabled ->
            binding.cancelButton.isEnabled = isCancelEnabled
        }
        viewModel.iconRunning.observe(this) { isProgress ->
            binding.progressBar.visibility = if (isProgress) View.VISIBLE else View.INVISIBLE
        }
        viewModel.loadingStatusText.observe(this) { statusText ->
            binding.loadingStatusTextView.text = statusText
        }

        val userAdapter = UsersRecyclerViewAdapter()
        binding.recyclerView.adapter = userAdapter
        viewModel.users.observe(this) { users ->
            userAdapter.users = users
        }
    }

    private fun initTokenQuestion() {
        binding.accessTokenQuestionTextView.isVisible =
            viewModel.accessTokenQuestionEnabled.value == true
        viewModel.accessTokenQuestionEnabled.observe(this) { nextVisible ->
            binding.accessTokenQuestionTextView.let { textView ->
                val isTextVisible = textView.isVisible
                if (isTextVisible && !nextVisible) {
                    startHideAccessTokenAnimation()
                } else textView.isVisible = nextVisible
            }
        }
        binding.accessTokenQuestionTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://github.com/settings/tokens/new")
            startActivity(intent)
        }
    }

    private fun startHideAccessTokenAnimation() {
        val startHeight = binding.accessTokenQuestionTextView.height
        val endHeight = 0

        val anim = ValueAnimator.ofInt(startHeight, endHeight)
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams = binding.accessTokenQuestionTextView.layoutParams
            layoutParams.height = value
            binding.accessTokenQuestionTextView.layoutParams = layoutParams
        }
        anim.addListener(onEnd = {
            binding.accessTokenQuestionTextView.isVisible = false
        })
        anim.duration = 800
        anim.start()
    }

    private fun getOrganization() = binding.organizationEditText.text.toString()
    private fun getUserName() = binding.githubUserNameTextView.text.toString()
    private fun getPassword() = binding.passwordTokenEditText.text.toString()

    private fun getSelectedVariant(): Variant {
        val selectedVariantName = binding.variantSpinner.selectedItem as String
        return Variant.valueOf(selectedVariantName)
    }

}