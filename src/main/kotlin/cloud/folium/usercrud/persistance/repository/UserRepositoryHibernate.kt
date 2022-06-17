package cloud.folium.usercrud.persistance.repository

import cloud.folium.usercrud.business.entity.User
import cloud.folium.usercrud.business.entity.annotations.Loggable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepositoryHibernate : JpaRepository<User, Long>, UserRepository