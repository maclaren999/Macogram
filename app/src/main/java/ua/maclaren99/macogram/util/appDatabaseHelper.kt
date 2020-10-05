package ua.maclaren99.macogram.util

import android.app.Activity
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ua.maclaren99.macogram.activities.MainActivity
import ua.maclaren99.macogram.activities.RegisterActivity
import ua.maclaren99.macogram.models.CommonModel
import ua.maclaren99.macogram.models.User
import ua.maclaren99.macogram.ui.fragments.EnterPhoneNumberFragment

lateinit var AUTH: FirebaseAuth
lateinit var UID: String
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var USER: User

const val NODE_USERS = "users"
const val NODE_USERNAMES = "usernames"
const val NODE_PHONES = "phones"
const val NODE_PHONE_CONTACTS = "phone_contacts"


const val PROFILE_IMAGE_FOLDER = "profile_image"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHILD_BIO = "bio"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_STATUS = "status"

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
    USER = User()
    UID = AUTH.currentUser?.uid.toString()
}

inline fun putUrlToDatabase(url: String, crossinline function: (url: String) -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_PHOTO_URL)
        .setValue(url)
        .addOnSuccessListener { function(url) }
        .addOnFailureListener { APP_ACTIVITY.showToast(it.message.toString()) }
}

inline fun getItemUrlFromStoarage(
    path: StorageReference,
    crossinline function: (url: String) -> Unit
) {
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener { APP_ACTIVITY.showToast(it.message.toString()) }
}

inline fun uploadImageToStorage(
    uri: Uri,
    path: StorageReference,
    crossinline function: () -> Unit
) {
    path.putFile(uri)
        .addOnSuccessListener { function() }
        .addOnFailureListener { APP_ACTIVITY.showToast(it.message.toString()) }
}

inline fun initUser(crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
        .addListenerForSingleValueEvent(
            AppValueEventListener {
                USER = it.getValue(User::class.java) ?: User()
                if (USER.username.isEmpty()) USER.username = UID
                function()
            }
        )
}

/** Reads contacts from phone book and put them in [arrayContacts]*/
fun initContacts() {
    if (checkPermissions(READ_CONTACTS)) {
        var arrayContacts = arrayListOf<CommonModel>()
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

/** Find people from your phone book in App database & sync*/
fun findContactsInDatabase(arrayContacts: ArrayList<CommonModel>) {
    REF_DATABASE_ROOT.child(NODE_PHONES).addListenerForSingleValueEvent(AppValueEventListener {
        it.children.forEach { phoneKey ->
            arrayContacts.forEach { contact ->
                if (phoneKey.key == contact.phone) {
                    REF_DATABASE_ROOT.child(NODE_PHONE_CONTACTS).child(UID)
                        .child(phoneKey.value.toString())
                        .child(CHILD_ID).setValue(phoneKey.value.toString())
                }
            }

        }
    })
}

fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()