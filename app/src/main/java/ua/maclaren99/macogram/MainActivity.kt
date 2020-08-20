package ua.maclaren99.macogram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import ua.maclaren99.macogram.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader
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
        createDrawer()
    }

    private fun createDrawer() {
        mDrawer = DrawerBuilder()
            .withActivity(this)
            .withToolbar(mToolbar)
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
                PrimaryDrawerItem()
                    .withIdentifier(107)
                    .withName("Пригласить друзей")
                    .withIcon(R.drawable.ic_menu_invate)
                    .withIconTintingEnabled(true)
                    .withSelectable(false),
                DividerDrawerItem(),
                PrimaryDrawerItem()
                    .withIdentifier(108)
                    .withName("Вопросы о Macogram")
                    .withIcon(R.drawable.ic_menu_help)
                    .withIconTintingEnabled(true)
                    .withSelectable(false),
                PrimaryDrawerItem()
                    .withIdentifier(109)
                    .withName("Создать группу")
                    .withIcon(R.drawable.ic_menu_create_groups)
                    .withIconTintingEnabled(true)
                    .withSelectable(false)
            )
            .withOnDrawerItemClickListener(object: Drawer.OnDrawerItemClickListener{
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    Toast.makeText(applicationContext, position, Toast.LENGTH_SHORT).show()
                    return false
                }
            }).build()
    }

    private fun createHeader() {
        mHeader = AccountHeaderBuilder()
            .withActivity(this)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                ProfileDrawerItem().withName("Валик")
                    .withEmail("uhojuiskompanii@nah.suk")
            ).build()
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar

    }
}