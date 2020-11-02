package ua.maclaren99.macogram.ui.fragments.register

import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_code.*
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.database.*
import ua.maclaren99.macogram.util.*

class EnterCodeFragment(val mPhoneNumber: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {

    private val TAG = "Reg:EnterCodeFragment"

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = mPhoneNumber
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
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                /**Вынести повторяющийся код из EnterPhoneNumber*/
                Log.d(TAG, "EnterCodeFragment - verifyCode()")
                login(mPhoneNumber)
            } else showToast(task.exception?.message.toString())
        }
    }


}