package com.websarva.wings.android.todo

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Toolbarを設定
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle(R.string.login_name)
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)

        // アイコンのクリックリスナーを設定
        toolbar.setNavigationIcon(null) // 戻るボタンを非表示にする
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_logout -> {
                    logout()
                    true
                }

                else -> false
            }
        }
    }
    // メニューをインフレートする
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    private fun logout() {
        // SharedPreferencesを使用してセッションをクリアする
        val sharedPreferences: SharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // セッション情報をクリア
        editor.apply()

        // LoginActivityへ遷移
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        // HomeActivityを終了して戻れないようにする
        finish()
    }
}