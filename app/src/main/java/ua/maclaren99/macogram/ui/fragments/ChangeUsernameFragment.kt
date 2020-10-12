package ua.maclaren99.macogram.ui.fragments

import kotlinx.android.synthetic.main.fragment_change_username.*
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.database.*
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
            REF_DATABASE_ROOT.child(
                NODE_USERNAMES
            )
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
        REF_DATABASE_ROOT.child(
            NODE_USERNAMES
        ).child(mNewUsername).setValue(UID)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUsername(mNewUsername)
                } else showToast(it.exception?.message.toString())
            }
    }
}