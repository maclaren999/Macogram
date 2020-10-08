package ua.maclaren99.macogram.models

data class UserModel(
    var id: String = "default_id",
    var username: String = "default_username",
    var bio: String = "default_bio",
    var fullname: String = "default_fullname",
    var status: String = "default_status",
    var phone: String = "default_phone",
    var photoUrl: String = "default_photoUrl"
) {

}