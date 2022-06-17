package cloud.folium.usercrud.business.entity

enum class Permission(val permission: String) {
    CREATE_USER("create"), EDIT_USER("edit"), DELETE_USER("delete"), GET_USER("get_user");
}