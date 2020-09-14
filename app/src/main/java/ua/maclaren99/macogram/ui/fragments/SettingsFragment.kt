package ua.maclaren99.macogram.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_settings.*
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.activities.RegisterActivity
import ua.maclaren99.macogram.databinding.FragmentSettingsBinding
import ua.maclaren99.macogram.util.*

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
        settings_user_photo.downloadAndSetImage(USER.photoUrl)

        settings_btn_change_username.setOnClickListener { replaceFragment(ChangeUsernameFragment()) }
        settings_btn_change_bio.setOnClickListener { replaceFragment(ChangeBioFragment()) }
        settings_btn_change_photo.setOnClickListener { changeUserPhoto() }
    }

    private fun changeUserPhoto() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_exit -> {
                AUTH.signOut()
                APP_ACTIVITY.replaceActivity(RegisterActivity())
            }
            R.id.settings_menu_change_name -> replaceFragment(ChangeFullnameFragment())
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val uri = CropImage.getActivityResult(data).uri     //getting path of croped image
            val path = REF_STORAGE_ROOT.child(PROFILE_IMAGE_FOLDER).child(UID)

            uploadImageToStorage(uri, path) {
                getItemUrlFromStoarage(path) {
                    putUrlToDatabase(it) {
                        USER.photoUrl = it
                        settings_user_photo.downloadAndSetImage(it)
                        showToast(getString(R.string.data_updated))
                    }
                }
            }

        }
    }


}