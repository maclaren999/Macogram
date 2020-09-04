package ua.maclaren99.macogram.ui.fragments

import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.util.replaceFragment
import ua.maclaren99.macogram.util.showToast

class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

    override fun onStart() {
        super.onStart()
        register_btn_next.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        if (register_edit_input_phone_number.text.isEmpty()){
            showToast("Incorrect number")
        }else{
            replaceFragment(EnterCodeFragment())
        }
    }

}