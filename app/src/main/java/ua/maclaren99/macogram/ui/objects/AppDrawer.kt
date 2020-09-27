package ua.maclaren99.macogram.ui.objects

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.ui.fragments.SettingsFragment
import ua.maclaren99.macogram.util.USER
import ua.maclaren99.macogram.util.downloadAndSetImage
import ua.maclaren99.macogram.util.replaceFragment
import kotlin.coroutines.coroutineContext

class AppDrawer(val mainActivity: AppCompatActivity, val toolbar: Toolbar) {
    private lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mCurrentProfile: ProfileDrawerItem

    fun create() {
        initLoader()
        createHeader()
        createDrawer()
        mDrawerLayout = mDrawer.drawerLayout
    }

    fun disableDrawer() {
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        toolbar.setNavigationOnClickListener {
            mainActivity.supportFragmentManager.popBackStack()
        }
    }

    fun enableDrawer() {
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toolbar.setNavigationOnClickListener {
            mDrawer.openDrawer()
        }
    }

    private fun createDrawer() {
        mDrawer = DrawerBuilder()
            .withActivity(mainActivity)
            .withToolbar(toolbar)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(-1)
            .withAccountHeader(mHeader)
            .addDrawerItems(
                PrimaryDrawerItem()
                    .withIdentifier(100)
                    .withName("Создать группу")
                    .withIcon(R.drawable.ic_menu_create_groups)
                    .withIconTintingEnabled(true)
                    .withSelectable(false),
                PrimaryDrawerItem()
                    .withIdentifier(101)
                    .withName("Создать секретный чат")
                    .withIcon(R.drawable.ic_menu_secret_chat)
                    .withIconTintingEnabled(true)
                    .withSelectable(false),
                PrimaryDrawerItem()
                    .withIdentifier(102)
                    .withName("Создать канал")
                    .withIcon(R.drawable.ic_menu_create_channel)
                    .withIconTintingEnabled(true)
                    .withSelectable(false),
                PrimaryDrawerItem()
                    .withIdentifier(103)
                    .withName("Контакты")
                    .withIcon(R.drawable.ic_menu_contacts)
                    .withIconTintingEnabled(true)
                    .withSelectable(false),
                PrimaryDrawerItem()
                    .withIdentifier(104)
                    .withName("Звонки")
                    .withIcon(R.drawable.ic_menu_phone)
                    .withIconTintingEnabled(true)
                    .withSelectable(false),
                PrimaryDrawerItem()
                    .withIdentifier(105)
                    .withName("Избранное")
                    .withIcon(R.drawable.ic_menu_favorites)
                    .withIconTintingEnabled(true)
                    .withSelectable(false),
                PrimaryDrawerItem()
                    .withIdentifier(106)
                    .withName("Настройки")
                    .withIcon(R.drawable.ic_menu_settings)
                    .withIconTintingEnabled(true)
                    .withSelectable(false),
                DividerDrawerItem(),
                PrimaryDrawerItem()
                    .withIdentifier(107)
                    .withName("Пригласить друзей")
                    .withIcon(R.drawable.ic_menu_invate)
                    .withIconTintingEnabled(true)
                    .withSelectable(false),
                PrimaryDrawerItem()
                    .withIdentifier(108)
                    .withName("Вопросы о Macogram")
                    .withIcon(R.drawable.ic_menu_help)
                    .withIconTintingEnabled(true)
                    .withSelectable(false)
            )
            .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    when (position) {
                        7 -> mainActivity.replaceFragment(SettingsFragment())
                    }
                    return false
                }
            }).build()
    }

    private fun createHeader() {
        mCurrentProfile = ProfileDrawerItem()
            .withName(USER.fullname)
            .withEmail(USER.phone)
            .withIcon(USER.photoUrl)
            .withIdentifier(200)
        mHeader = AccountHeaderBuilder()
            .withActivity(mainActivity)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                mCurrentProfile
            ).build()
    }

    fun updateHeader() {
        mCurrentProfile
            .withName(USER.fullname)
            .withEmail(USER.phone)
            .withIcon(USER.photoUrl)
        mHeader.updateProfile(mCurrentProfile)
    }

    private fun initLoader() {
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable) {
                imageView.downloadAndSetImage(uri.toString())
            }
        })
    }
}