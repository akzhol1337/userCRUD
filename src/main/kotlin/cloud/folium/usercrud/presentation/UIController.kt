package cloud.folium.usercrud.presentation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.stereotype.Controller

@Controller
class UIController {
    @GetMapping("/createuser")
    fun addUser(): String {
        return "create-user"
    }

    @GetMapping("/updateuser")
    fun updateUser(): String {
        return "update-user"
    }
}