package ua.maclaren99.macogram.ui.fragments

import android.view.*
import kotlinx.android.synthetic.main.fragment_change_username.*
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.activities.MainActivity
import ua.maclaren99.macogram.util.*
import java.util.*

class ChangeUsernameFragment : BaseChangeFragment(R.layout.fragment_change_username) {

    lateinit var mNewUsername: String

    override fun onResume() {
        super.onResume()
    }



    override fun change() {
        mNewUsername = settings_input_username.text.toString().toLowerCase(Locale.getDefault())
        if (mNewUsername.isBlank()) {
            showToast(getString(R.string.empty_username_toast))
        } else {
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(
                    AppValueEventListener {
                        if (it.hasChild(mNewUsername)) {
                            showToast("Such username already exists.")
                        } else if (mNewUsername.contains(' ')) {
                            showToast("Username can't contain spaces.")
                        } else {
                            changeUsername()
                        }
                    }
                )
        }
    }

    private fun changeUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mNewUsername).setValue(UID)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUsername()
                } else showToast(it.exception?.message.toString())
            }
    }

    private fun updateCurrentUsername() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_USERNAME).setValue(mNewUsername)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    deleteOldUsername()
                } else showToast(it.exception?.message.toString())
            }
    }

    private fun deleteOldUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.username).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(getString(R.string.data_updated))
                    USER.username = mNewUsername
                    fragmentManager?.popBackStack()

                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

}