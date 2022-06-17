package cloud.folium.usercrud.business.entity

import org.springframework.security.core.authority.SimpleGrantedAuthority
import cloud.folium.usercrud.business.entity.UserRequestDto
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import org.springframework.web.multipart.MultipartFile
import java.util.function.Function
import java.util.stream.Collectors

enum class Role(val permissions: Set<Permission>) {
    USER(setOf(Permission.CREATE_USER)),
    ADMIN(setOf(Permission.CREATE_USER, Permission.EDIT_USER, Permission.GET_USER, Permission.DELETE_USER));

    fun getAuthorities(): Set<SimpleGrantedAuthority> {
        return permissions.stream().map{ permission -> SimpleGrantedAuthority(permission.permission)}
        .collect(Collectors.toSet())
    }
}