package com.dicoding.storyapp.ui.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.databinding.ActivityRegisterBinding
import com.dicoding.storyapp.ui.customview.EmailEditText
import com.dicoding.storyapp.ui.customview.LoginButton
import com.dicoding.storyapp.ui.customview.PasswordEditText
import com.dicoding.storyapp.ui.customview.RegisterButton

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerButton: RegisterButton
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerButton = binding.registerButton
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText
        var nameEditText = binding.nameEditText

        registerButton.isEnabled = false

        emailEditText.addTextChangedListener(registerTextWatcher)
        passwordEditText.addTextChangedListener(registerTextWatcher)

        registerButton.setOnClickListener {
            Toast.makeText(this, "Email: ${emailEditText.text} ,Password: ${passwordEditText.text}, Name: ${nameEditText.text} ", Toast.LENGTH_SHORT).show()
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
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        val isEmailValid = email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 8

        registerButton.isEnabled = isEmailValid && isPasswordValid && (binding.nameEditTextLayout.toString().isNotEmpty())
    }
}