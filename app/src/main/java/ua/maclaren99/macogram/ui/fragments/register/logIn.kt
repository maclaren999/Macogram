package ua.maclaren99.macogram.ui.fragments.register

import android.util.Log
import ua.maclaren99.macogram.database.*
import ua.maclaren99.macogram.util.AppValueEventListener
import ua.maclaren99.macogram.util.restartActivity
import ua.maclaren99.macogram.util.showToast

internal fun login(phoneNumber: String){
    val uid = AUTH.uid.toString()
    val authDataMap = mutableMapOf<String, Any>(
        Pair(CHILD_ID, uid),
        Pair(CHILD_PHONE, phoneNumber)
    )

    REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
        .addListenerForSingleValueEvent(AppValueEventListener {
            if (!it.hasChild(CHILD_USERNAME)) {
                authDataMap[CHILD_USERNAME] = uid
            }

            REF_DATABASE_ROOT.child(
                NODE_PHONES
            ).child(phoneNumber).setValue(uid)
                .addOnFailureListener { showToast(it.message.toString()) }
                .addOnSuccessListener {
                    REF_DATABASE_ROOT.child(
                        NODE_USERS
                    ).child(uid).updateChildren(authDataMap)
                        .addOnSuccessListener {
                            showToast("verifyCode - passed")
                            restartActivity()
                        }
                        .addOnFailureListener { showToast(it.message.toString()) }

                }
            showToast("EnterCodeFragment - verifyCode()")
        })


}