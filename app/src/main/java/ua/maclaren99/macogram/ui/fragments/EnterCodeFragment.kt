package ua.maclaren99.macogram.ui.fragments

import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_code.*
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.activities.MainActivity
import ua.maclaren99.macogram.activities.RegisterActivity
import ua.maclaren99.macogram.util.AUTH
import ua.maclaren99.macogram.util.AppTextWatcher
import ua.maclaren99.macogram.util.replaceActivity
import ua.maclaren99.macogram.util.showToast

class EnterCodeFragment(val mPhoneNumber: String, val id: String) : Fragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()
        (activity as RegisterActivity).title = mPhoneNumber
        register_edit_input_code.addTextChangedListener(
            AppTextWatcher {
                if (register_edit_input_code.text.length == 6) {
                    verifyCode()
                }
            }
        )
    }

    private fun verifyCode() {
        val code = register_edit_input_code.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
                showToast("Welcome")
                (activity as RegisterActivity).replaceActivity(MainActivity())
            } else showToast(it.exception?.message.toString())
        }
    }


}