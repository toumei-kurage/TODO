import android.content.Context
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.websarva.wings.android.todo.R

class ValidateHelper(private val context: Context) {
    //未入力チェック(空白の時false)
    private fun emptyCheck(text:String):Boolean{
        return text != ""
    }

    //電子メールの形式チェック(@が含まれていればtrue)
    private fun emailFormatCheck(email:String):Boolean{
        return email.contains("@")
    }

    /**
     * 桁数チェック
     * @param[text] 桁数チェックをしたい文字列
     * @param[digit] 対象文字列の文字数の下限値
     */
    private fun lengthCheck(text:String,digit:Int):Boolean{
        return text.length >= digit
    }

    /**
     * 電子メールのバリデーションチェック
     * 未入力チェックと電子メールの形式チェック
     */
    fun emailCheck(editTextEmail:EditText):Pair<Boolean,String>{
        val email = editTextEmail.text.toString()
        if(!emptyCheck(email)){
            return Pair(false,context.getString(R.string.error_empty))
        }
        if(!emailFormatCheck(email)){
            return Pair(false,context.getString(R.string.error_email))
        }
        return Pair(true,"OK")
    }

    /**
     * パスワードのバリデーションチェック
     */
    fun passwordCheck(editTextPassword:EditText):Pair<Boolean,String>{
        val password = editTextPassword.text.toString()
        if(!emptyCheck(password)){
            return Pair(false,context.getString(R.string.error_empty))
        }
        if(!lengthCheck(password,6)){
            return Pair(false,context.getString(R.string.error_digit_6))
        }
        return Pair(true,"OK")
    }

}