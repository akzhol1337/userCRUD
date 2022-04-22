import com.example.usercrud.business.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserControllerTestGroovy extends Specification{
    @Autowired
    private UserServuce userServiceTest
    @Autowired
    private MockMvc mock

    // todo

    def "it should return 200 for adding correct user to db"() {
        given:
            User user = new User(null, "firstName", "secondName", "middleName", "country", 0, "email@emaill.email")
        when:
            mock.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user))).andDo(print()).andExpect(status().isOk());
    }

    def "it should return 400 for adding incorrect user to db (empty firstname)"() {
        given:
            User user = new User(null, null, "secondname", "middlename", "country", 0, "email@email.email");
        when:
            mock.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user))).andDo(print()).andExpect(status().isBadRequest());
    }

}
