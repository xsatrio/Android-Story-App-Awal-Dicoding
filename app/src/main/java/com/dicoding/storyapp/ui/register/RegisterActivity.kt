package com.dicoding.storyapp.ui.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.storyapp.R
import com.dicoding.storyapp.ViewModelFactory
import com.dicoding.storyapp.data.Results
import com.dicoding.storyapp.databinding.ActivityRegisterBinding
import com.dicoding.storyapp.ui.customview.EmailEditText
import com.dicoding.storyapp.ui.customview.NameEditText
import com.dicoding.storyapp.ui.customview.PasswordEditText
import com.dicoding.storyapp.ui.customview.RegisterButton
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerButton: RegisterButton
    private lateinit var nameEditText: NameEditText
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText

    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerButton = binding.registerButton
        nameEditText = binding.nameEditText
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText

        registerButton.isEnabled = false

        nameEditText.addTextChangedListener(registerTextWatcher)
        emailEditText.addTextChangedListener(registerTextWatcher)
        passwordEditText.addTextChangedListener(registerTextWatcher)

        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            Log.d("RegisterActivity", "$name, $email, $password")
            registerViewModel.register(name, email, password)
            observeRegisterResult()
            Log.d("RegisterActivity", "$name, $email, $password")
        }
    }

    private val registerTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            validateFields()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun validateFields() {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        val isNameValid = name.isNotEmpty()
        val isEmailValid = email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 8

        registerButton.isEnabled = isNameValid && isEmailValid && isPasswordValid
    }

    private fun observeRegisterResult() {
        lifecycleScope.launch {
            registerViewModel.registerResult.collect { results ->
                when (results) {
                    is Results.Loading -> {
                        showLoading(true)
                    }
                    is Results.Success -> {
                        showLoading(false)
                        Log.d("RegisterActivity", "Register success: ${results.data.message}")
                        Toast.makeText(this@RegisterActivity, getString(R.string.register_success) + results.data.message, Toast.LENGTH_SHORT).show()
                    }
                    is Results.Error -> {
                        showLoading(false)
                        Log.e("RegisterActivity", "Error: ${results.error}")
                        Toast.makeText(this@RegisterActivity, getString(R.string.register_error) + results.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = android.view.View.VISIBLE
            binding.registerButton.isEnabled = false
        } else {
            binding.progressBar.visibility = android.view.View.GONE
            binding.registerButton.isEnabled = true
        }
    }
}