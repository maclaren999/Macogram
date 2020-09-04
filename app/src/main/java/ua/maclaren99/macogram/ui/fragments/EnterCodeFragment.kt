package ua.maclaren99.macogram.ui.fragments

import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_enter_code.*
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.util.AppTextWatcher
import ua.maclaren99.macogram.util.showToast

class EnterCodeFragment : Fragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()
        register_edit_input_code.addTextChangedListener(
            AppTextWatcher {
                if (register_edit_input_code.text.length == 6) {
                    verifyCode()
                }
            }
        )
    }

    private fun verifyCode() {
        showToast("Verification started")
    }

    class CustomTextWatcher()

}