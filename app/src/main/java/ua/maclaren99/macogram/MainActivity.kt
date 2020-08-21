package ua.maclaren99.macogram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import ua.maclaren99.macogram.databinding.ActivityMainBinding
import ua.maclaren99.macogram.ui.fragments.ChatsFragment
import ua.maclaren99.macogram.ui.objects.AppDrawer

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mToolbar: Toolbar
    private lateinit var mAppDrawer: AppDrawer

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

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(this, mToolbar)

    }

    private fun initFunc() {
        mAppDrawer.create()
        setSupportActionBar(mToolbar)
        supportFragmentManager.beginTransaction()
            .replace(R.id.dataContainer,
                ChatsFragment()
            ).commit()

    }
}