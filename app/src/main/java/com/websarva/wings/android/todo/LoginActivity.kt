package com.websarva.wings.android.todo

import ValidateHelper
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val validate = ValidateHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val emailError = findViewById<TextInputLayout>(R.id.email)
        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordError = findViewById<TextInputLayout>(R.id.password)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)
        val signUpButton: Button = findViewById(R.id.SignUpButton)
        // FirebaseAuthのインスタンスを取得
        auth = FirebaseAuth.getInstance()

        emailEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // フォーカスが外れたときの処理
                val (result: Boolean, errorMsg: String) = validate.emailCheck(emailEditText)
                if (!result) {
                    emailError.error = errorMsg
                    return@OnFocusChangeListener
                }
                emailError.error = ""
            }
        }

        passwordEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // フォーカスが外れたときの処理
                val (result: Boolean, errorMsg: String) = validate.passwordCheck(passwordEditText)
                if (!result) {
                    passwordError.error = errorMsg
                    return@OnFocusChangeListener
                }
                passwordError.error = ""
            }
        }

        loginButton.setOnClickListener {
            clearBordFocus()
            //すべての入力項目のバリデーションチェック
            val (resutlEmail: Boolean, emailMsg: String) = validate.emailCheck(emailEditText)
            val (resultPassword: Boolean, passwordMsg) = validate.passwordCheck(passwordEditText)
            if (!(resutlEmail && resultPassword)) {
                emailError.error = emailMsg
                passwordError.error = passwordMsg
                return@setOnClickListener
            }

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            login(email, password)
        }

        signUpButton.setOnClickListener {
            clearBordFocus()
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish() // このアクティビティを終了して、戻れないようにする
        }


    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // ログイン成功
                    val user = auth.currentUser
                    Toast.makeText(this, "ログイン成功: ${user?.email}", Toast.LENGTH_SHORT).show()
                    // 次の画面に遷移する処理
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish() // このアクティビティを終了して、戻れないようにする
                } else {
                    // ログイン失敗
                    Toast.makeText(this, "ログイン失敗: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun clearBordFocus(){
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        // キーボードを閉じる処理
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(emailEditText.windowToken, 0)
        inputMethodManager.hideSoftInputFromWindow(passwordEditText.windowToken, 0)
        //フォーカスを外す処理
        emailEditText.clearFocus()
        passwordEditText.clearFocus()
    }
}