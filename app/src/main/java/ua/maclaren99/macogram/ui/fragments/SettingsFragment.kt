package ua.maclaren99.macogram.ui.fragments

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import kotlinx.android.synthetic.main.fragment_settings.*
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.activities.MainActivity
import ua.maclaren99.macogram.activities.RegisterActivity
import ua.maclaren99.macogram.databinding.FragmentSettingsBinding
import ua.maclaren99.macogram.util.AUTH
import ua.maclaren99.macogram.util.USER
import ua.maclaren99.macogram.util.replaceActivity
import ua.maclaren99.macogram.util.replaceFragment

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private lateinit var mBinding: FragmentSettingsBinding

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() {
        settings_full_name_view.text = USER.fullname
        settings_status_view.text = USER.status
        settings_username.text = USER.username
        settings_phone_number.text = USER.phone
        settings_bio.text = USER.bio

        settings_btn_change_username.setOnClickListener { replaceFragment(ChangeUsernameFragment()) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_exit -> {
                AUTH.signOut()
                (activity as MainActivity).replaceActivity(RegisterActivity())
            }
            R.id.settings_menu_change_name -> replaceFragment(ChangeFullnameFragment())
        }
        return true
    }
}