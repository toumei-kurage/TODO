package com.websarva.wings.android.todo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    // FirebaseAuthのインスタンスを準備
    private lateinit var auth: FirebaseAuth

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

        // ボタンが押された時の処理
        buttonRegister.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            // 入力が空でないか確認
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "メールアドレスとパスワードを入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

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
}