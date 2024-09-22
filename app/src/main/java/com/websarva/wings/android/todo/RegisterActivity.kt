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

class RegisterActivity : AppCompatActivity() {

    // FirebaseAuthのインスタンスを準備
    private lateinit var auth: FirebaseAuth
    private val validate = ValidateHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // FirebaseAuthのインスタンスを取得
        auth = FirebaseAuth.getInstance()

        // UIの要素を取得
        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val usernameError = findViewById<TextInputLayout>(R.id.username)
        val emailError = findViewById<TextInputLayout>(R.id.email)
        val passwordError = findViewById<TextInputLayout>(R.id.password)

        editTextUsername.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // フォーカスが外れたときの処理
                val (result: Boolean, errorMsg: String) = validate.usernameCheck(editTextUsername)
                if (!result) {
                    usernameError.error = errorMsg
                    return@OnFocusChangeListener
                }
                usernameError.error = ""
            }
        }

        editTextEmail.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // フォーカスが外れたときの処理
                val (result: Boolean, errorMsg: String) = validate.emailCheck(editTextEmail)
                if (!result) {
                    emailError.error = errorMsg
                    return@OnFocusChangeListener
                }
                emailError.error = ""
            }
        }

        editTextPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // フォーカスが外れたときの処理
                val (result: Boolean, errorMsg: String) = validate.passwordCheck(editTextPassword)
                if (!result) {
                    passwordError.error = errorMsg
                    return@OnFocusChangeListener
                }
                passwordError.error = ""
            }
        }

        // ボタンが押された時の処理
        buttonRegister.setOnClickListener {
            clearBordFocus()
            //すべての入力項目のバリデーションチェック
            val (resultUsername:Boolean,usernameMsg:String) = validate.usernameCheck(editTextUsername)
            val (resultEmail: Boolean, emailMsg: String) = validate.emailCheck(editTextEmail)
            val (resultPassword: Boolean, passwordMsg) = validate.passwordCheck(editTextPassword)

            if(!(resultUsername && resultEmail && resultPassword)){
                usernameError.error = usernameMsg
                emailError.error = emailMsg
                passwordError.error = passwordMsg
                return@setOnClickListener
            }

            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            // Firebaseでユーザー登録
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // 登録成功
                        Toast.makeText(this, "登録成功", Toast.LENGTH_SHORT).show()
                        // 次の画面に進むなどの処理を書く
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish() // このアクティビティを終了して、戻れないようにする

                    } else {
                        // エラー処理
                        Toast.makeText(this, "登録失敗: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun clearBordFocus(){
        val usernameEditText = findViewById<EditText>(R.id.editTextUsername)
        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        // キーボードを閉じる処理
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(usernameEditText.windowToken,0)
        inputMethodManager.hideSoftInputFromWindow(emailEditText.windowToken, 0)
        inputMethodManager.hideSoftInputFromWindow(passwordEditText.windowToken, 0)
        //フォーカスを外す処理
        usernameEditText.clearFocus()
        emailEditText.clearFocus()
        passwordEditText.clearFocus()
    }
}