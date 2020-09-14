package ua.maclaren99.macogram.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.theartofdev.edmodo.cropper.CropImage
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.databinding.ActivityMainBinding
import ua.maclaren99.macogram.models.User
import ua.maclaren99.macogram.ui.fragments.ChatsFragment
import ua.maclaren99.macogram.ui.objects.AppDrawer
import ua.maclaren99.macogram.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mToolbar: Toolbar
    lateinit var mAppDrawer: AppDrawer
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        initFields()
        initFunc()
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(this, mToolbar)
        initFirebase()
        initUser()
    }


    private fun initFunc() {
        if (AUTH.currentUser != null) {
            Log.d(TAG, AUTH.currentUser!!.uid.toString())
            setSupportActionBar(mToolbar)
            mAppDrawer.create()
            replaceFragment(ChatsFragment(), false)
        } else {
            replaceActivity(RegisterActivity())
        }
    }

    private fun initUser() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
            .addListenerForSingleValueEvent(
                AppValueEventListener {
                    USER = it.getValue(User::class.java) ?: User()

                }
            )
    }


}