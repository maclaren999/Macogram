package ua.maclaren99.macogram.models

data class CommonModel(
    var id: String = "",
    var username: String = "",
    var bio: String = "",
    var fullname: String = "",
    var status: String = "",
    var phone: String = "",
    var photoUrl: String = "default_photoUrl"
) {

}