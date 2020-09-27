package ua.maclaren99.macogram.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_change_fullname.*
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.activities.MainActivity
import ua.maclaren99.macogram.util.*


class ChangeFullnameFragment : BaseChangeFragment(R.layout.fragment_change_fullname) {

    override fun onResume() {
        super.onResume()
        val fullNameList = USER.fullname.split(" ".toRegex(), 2)
        if (fullNameList.size > 1) {
            settings_input_name.setText(fullNameList[0])
            settings_input_surname.setText(fullNameList[1])
        } else settings_input_name.setText(fullNameList[0])
    }

    override fun change() {
        val name: String = settings_input_name.text.toString()
        val surname: String = settings_input_surname.text.toString()
        if (name.isBlank()) {
            showToast(getString(R.string.settings_toast_name_is_empty))
        } else {
            val fullname = "$name $surname"
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_FULLNAME)
                .setValue(fullname).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast(getString(R.string.data_updated))
                        USER.fullname = fullname
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                        fragmentManager?.popBackStack()
                    }
                }
        }
    }

}