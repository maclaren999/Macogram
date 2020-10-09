package ua.maclaren99.macogram.util

import android.net.Uri
import android.provider.ContactsContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ua.maclaren99.macogram.models.CommonModel
import ua.maclaren99.macogram.models.UserModel

lateinit var AUTH: FirebaseAuth
lateinit var UID: String
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var USER: UserModel

const val TYPE_TEXT = "text"

const val NODE_USERS = "users"
const val NODE_MESSAGES = "messages"
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
const val CHILD_TEXT = "text"
const val CHILD_FROM = "from"
const val CHILD_TYPE = "type"
const val CHILD_TIMESTAMP = "timeStamp"


fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
    USER = UserModel()
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
                USER = it.getValue(UserModel::class.java) ?: UserModel()
                if (USER.username.isEmpty()) USER.username = UID
                function()
            }
        )
}


/** Finds phones from [arrayContacts] in [NODE_PHONES] and adds their UID in users [NODE_PHONES] node*/
fun findContactsInDatabase(arrayContacts: ArrayList<CommonModel>) {
    if (AUTH.currentUser != null) {
        REF_DATABASE_ROOT.child(NODE_PHONES).addListenerForSingleValueEvent(AppValueEventListener {
            it.children.forEach { phoneKey ->
                arrayContacts.forEach { contact ->
                    if (phoneKey.key == contact.phone) {
                        REF_DATABASE_ROOT.child(NODE_PHONE_CONTACTS).child(UID)
                            .child(phoneKey.value.toString())
                            .child(CHILD_ID).setValue(phoneKey.value.toString())
                            .addOnFailureListener { APP_ACTIVITY.showToast(it.message.toString()) }

                        REF_DATABASE_ROOT.child(NODE_PHONE_CONTACTS).child(UID)
                            .child(phoneKey.value.toString())
                            .child(CHILD_FULLNAME).setValue(contact.fullname)
                            .addOnFailureListener { APP_ACTIVITY.showToast(it.message.toString()) }

                    }
                }

            }
        })
    }
}

fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

fun DataSnapshot.getuserModel(): UserModel =
    this.getValue(UserModel::class.java) ?: UserModel()

fun sendMessage(message: String, receivingUserID: String, typeText: String, onSendedFunction: () -> Unit) {

    var refDialogUser = "$NODE_MESSAGES/$UID/$receivingUserID"
    var refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserID/$UID"
    var messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key

    val messageMap = hashMapOf<String, Any>()
    messageMap[CHILD_FROM] = UID
    messageMap[CHILD_TEXT] = message
    messageMap[CHILD_TYPE] = typeText
    messageMap[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = messageMap
    mapDialog["$refDialogReceivingUser/$messageKey"] = messageMap
    REF_DATABASE_ROOT.updateChildren(mapDialog)
        .addOnSuccessListener { onSendedFunction() }
        .addOnFailureListener { APP_ACTIVITY.showToast(it.message.toString()) }
}