package ua.maclaren99.macogram.util

import ua.maclaren99.macogram.database.*

enum class AppStatus(val status: String) {
    ONLINE("online"),
    OFFLINE("offline"),
    TYPING("typing"),
    SEEN_RECANTLY("seen recently");

    companion object {
        //Func receives status and updates it in database
        fun updateStatus(appStatus: AppStatus) {
            if (AUTH.currentUser != null) {
                REF_DATABASE_ROOT.child(
                    NODE_USERS
                ).child(UID).child(CHILD_STATUS)
                    .setValue(appStatus)
                    .addOnFailureListener { showToast(it.message.toString()) }
            }
        }
    }

}