package ua.maclaren99.macogram.ui.fragments.register

import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.database.*
import ua.maclaren99.macogram.util.*
import java.util.concurrent.TimeUnit

class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

    private lateinit var mPhoneNumber: String
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private val TAG = "Reg:EnterPhoneNumber"

    override fun onStart() {
        super.onStart()

        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast("Welcome")
                        login(mPhoneNumber)
//                        restartActivity()
//qwe       Вынести
//                        Log.d(TAG, "EnterPhoneNumberFragment - verifyCode()")
//                        val uid = AUTH.uid.toString()
//                        val authDataMap = mutableMapOf<String, Any>(
//                            Pair(CHILD_ID, uid),
//                            Pair(CHILD_PHONE, mPhoneNumber)
//                        )
//                        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
//                            .addListenerForSingleValueEvent(AppValueEventListener {
//                                if (!it.hasChild(CHILD_USERNAME)) authDataMap[CHILD_USERNAME] = uid
//
//                                REF_DATABASE_ROOT.child(
//                                    NODE_PHONES
//                                ).child(mPhoneNumber).setValue(uid)
//                                    .addOnFailureListener { showToast(it.message.toString()) }
//                                    .addOnSuccessListener {
//                                        REF_DATABASE_ROOT.child(
//                                            NODE_USERS
//                                        ).child(uid).updateChildren(authDataMap)
//                                            .addOnSuccessListener {
//                                                showToast("verifyCode - passed")
//                                                restartActivity()
//                                            }
//                                            .addOnFailureListener { showToast(it.message.toString()) }
//
//                                    }
//                                showToast("EnterCodeFragment - verifyCode()")
//                            })
//qwe


                    } else showToast(it.exception?.message.toString())
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(
                    EnterCodeFragment(
                        mPhoneNumber,
                        id
                    )
                )
                Log.d(TAG, "onCodeSent()")
            }
        }
        register_btn_next.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        if (register_edit_input_phone_number.text.isEmpty()) {
            showToast("Incorrect number")
        } else {
            authUser()
        }
    }

    private fun authUser() {
        mPhoneNumber = register_edit_input_phone_number.text.toString()

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            mPhoneNumber,
            60,
            TimeUnit.SECONDS,
            APP_ACTIVITY,
            mCallback
        )
    }

}