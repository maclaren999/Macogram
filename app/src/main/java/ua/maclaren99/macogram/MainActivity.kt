package ua.maclaren99.macogram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.mikepenz.materialdrawer.util.DrawerItemViewHelper
import com.mikepenz.materialdrawer.widget.AccountHeaderView
import ua.maclaren99.macogram.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mDrawer: DrawerItemViewHelper
    private lateinit var mHeader: AccountHeaderView
    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


    }

    override fun onStart() {
        super.onStart()
        initFields()
        initFunc()
    }

    private fun initFunc() {
        setSupportActionBar(mToolbar)
        createHeader()
    }

    private fun createHeader() {
        mHeader = AccountHeaderView(applicationContext).backg
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar

    }
}