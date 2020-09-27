package ua.maclaren99.macogram.util

enum class appStatus(val status: String) {
    ONLINE("online"),
    OFFLINE("offline"),
    TYPING("typing");

    companion object{
        fun updateStatus(appStatus: appStatus){
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_STATUS)
                .setValue(appStatus)
                .addOnFailureListener { APP_ACTIVITY.showToast(it.message.toString()) }
        }
    }

}