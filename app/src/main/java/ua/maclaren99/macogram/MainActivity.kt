package ua.maclaren99.macogram

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.maclaren99.macogram.database.AUTH
import ua.maclaren99.macogram.database.initFirebase
import ua.maclaren99.macogram.database.initUser
import ua.maclaren99.macogram.databinding.ActivityMainBinding
import ua.maclaren99.macogram.ui.fragments.MainFragment
import ua.maclaren99.macogram.ui.fragments.register.EnterPhoneNumberFragment
import ua.maclaren99.macogram.ui.objects.AppDrawer
import ua.maclaren99.macogram.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    lateinit var mToolbar: Toolbar
    lateinit var mAppDrawer: AppDrawer
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        initFirebase()
        initUser {
            CoroutineScope(Dispatchers.IO).launch {
                initContacts()
            }
            initFields()
            initFunc()
        }
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer()

    }

    private fun initFunc() {
        setSupportActionBar(mToolbar)
        if (AUTH.currentUser != null) {
            Log.d(TAG, AUTH.currentUser!!.uid.toString())
            mAppDrawer.create()
            replaceFragment(MainFragment(), false)
        } else {
            replaceFragment(EnterPhoneNumberFragment())
        }
    }

    override fun onResume() {
        super.onResume()
        AppStatus.updateStatus(AppStatus.ONLINE)
    }

    override fun onPause() {
        super.onPause()
        AppStatus.updateStatus(AppStatus.SEEN_RECANTLY)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(
                APP_ACTIVITY,
                READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initContacts()
        }
    }
}