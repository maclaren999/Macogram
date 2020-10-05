package ua.maclaren99.macogram.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.databinding.ActivityRegisterBinding
import ua.maclaren99.macogram.ui.fragments.EnterPhoneNumberFragment
import ua.maclaren99.macogram.util.initFirebase
import ua.maclaren99.macogram.util.replaceFragment

class RegisterActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initFirebase()
    }

    override fun onStart() {
        super.onStart()
        mToolbar = mBinding.regiserToolbar
        setSupportActionBar(mToolbar)
        title = getString(R.string.register_toolbar_title)
        replaceFragment(EnterPhoneNumberFragment())
    }
}