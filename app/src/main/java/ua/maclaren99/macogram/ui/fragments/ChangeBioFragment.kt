package ua.maclaren99.macogram.ui.fragments

import kotlinx.android.synthetic.main.fragment_change_bio.*
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.database.*
import ua.maclaren99.macogram.util.*

class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {

    override fun onResume() {
        super.onResume()
        settings_input_bio.setText(USER.bio)

    }

    override fun change() {
        super.change()
        val newBio = settings_input_bio.text.toString()

        setBioToDatabase(newBio)


    }

}