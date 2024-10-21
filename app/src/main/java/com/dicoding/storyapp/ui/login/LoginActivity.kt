package com.dicoding.storyapp.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.ui.customview.EmailEditText
import com.dicoding.storyapp.ui.customview.LoginButton
import com.dicoding.storyapp.ui.customview.PasswordEditText

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginButton: LoginButton
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi elemen-elemen UI
        loginButton = binding.loginButton
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText

        // Awalnya tombol disabled
        loginButton.isEnabled = false

        // Tambahkan TextWatcher pada email dan password
        emailEditText.addTextChangedListener(loginTextWatcher)
        passwordEditText.addTextChangedListener(loginTextWatcher)

        // Ketika tombol login diklik
        loginButton.setOnClickListener {
            Toast.makeText(this, "Email: ${emailEditText.text} dan Password: ${passwordEditText.text}", Toast.LENGTH_SHORT).show()
        }
    }

    // TextWatcher untuk memantau perubahan teks
    private val loginTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Do nothing
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Panggil fungsi untuk validasi setiap ada perubahan
            validateFields()
        }

        override fun afterTextChanged(s: Editable?) {
            // Do nothing
        }
    }

    // Fungsi validasi untuk memeriksa email dan password
    private fun validateFields() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        // Validasi email dan password
        val isEmailValid = email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 8

        // Aktifkan tombol login hanya jika email dan password valid
        loginButton.isEnabled = isEmailValid && isPasswordValid
    }
}
