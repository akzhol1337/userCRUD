package cloud.folium.usercrud.business.entity

import org.springframework.web.multipart.MultipartFile

class UserRequestDto(id: Long?, firstName: String?, lastName: String?, middleName: String?, country: String?, gender: Int?, email: String?, avatar: MultipartFile?) {
    var id: Long?
    var firstName: String?
    var lastName: String?
    var middleName: String?
    var country: String?
    var gender: Int?
    var email: String?
    var avatar: MultipartFile?

    init {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.middleName = middleName
        this.country = country
        this.gender = gender
        this.email = email
        this.avatar = avatar
    }
}