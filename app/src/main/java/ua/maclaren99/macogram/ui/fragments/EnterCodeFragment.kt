package ua.maclaren99.macogram.ui.fragments

import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_code.*
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.activities.MainActivity
import ua.maclaren99.macogram.activities.RegisterActivity
import ua.maclaren99.macogram.util.*

class EnterCodeFragment(val mPhoneNumber: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {

    private val TAG = "Reg:EnterCodeFragment"

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

    @Suppress("UNREACHABLE_CODE")
    private fun verifyCode() {
        val code = register_edit_input_code.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                TODO("Вынести повторяющийся код из EnterPhoneNumber")
                Log.d(TAG, "verifyCode()")
                val uid = AUTH.uid.toString()
                val authDataMap = mutableMapOf<String, Any>(
                    Pair(CHILD_ID, uid),
                    Pair(CHILD_PHONE, mPhoneNumber),
                    Pair(CHILD_USERNAME, uid)
                )
                REF_DATABASE_ROOT.child(NODE_PHONES).child(mPhoneNumber).setValue(uid)
                    .addOnFailureListener { showToast(it.message.toString()) }
                    .addOnSuccessListener {
                        REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(authDataMap)
                            .addOnSuccessListener {
                                showToast("verifyCode - passed")
                                (activity as RegisterActivity).replaceActivity(MainActivity())
                            }
                            .addOnFailureListener { showToast(it.message.toString()) }

                    }
                showToast("by EnterCode!!")

            } else showToast(task.exception?.message.toString())
        }
    }


}