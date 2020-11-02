package ua.maclaren99.macogram.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.MainActivity
import ua.maclaren99.macogram.database.findContactsInDatabase
import ua.maclaren99.macogram.models.CommonModel
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.showToast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}

fun showToast(message: String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

fun restartActivity() {
    val intent = Intent(APP_ACTIVITY, MainActivity::class.java)
    APP_ACTIVITY.startActivity(intent)
    APP_ACTIVITY.finish()
}

fun replaceFragment(fragment: Fragment, addToStack: Boolean = true) {
    if (addToStack) {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.data_container,
                fragment
            ).commit()
    } else {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .replace(
                R.id.data_container,
                fragment
            ).commit()
    }
}

fun Fragment.replaceFragment(fragment: Fragment) {
    this.fragmentManager?.beginTransaction()
        ?.addToBackStack(null)
        ?.replace(
            R.id.data_container,
            fragment
        )?.commit()
}

/** Reads contacts from phone book and put them in arrayContacts*/
fun initContacts() {
    if (checkPermissions(READ_CONTACTS)) {
        val arrayContacts = arrayListOf<CommonModel>()
        val cursor = APP_ACTIVITY.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            while (it.moveToNext()) {
                val fullname =
                    it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phone =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val newModel = CommonModel()
                newModel.fullname = fullname
                newModel.phone = phone.replace(" ", "").replace("-", "")
                arrayContacts.add(newModel)
            }
        }

        cursor?.close()
        findContactsInDatabase(arrayContacts)
    }
}

fun hideKeyboard() {
    val imm: InputMethodManager =
        APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken, 0)
}

fun ImageView.downloadAndSetImage(url: String){
    Picasso.get()
        .load(url)
        .fit()
        .placeholder(R.drawable.ic_default_user)
        .into(this)
}

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}