import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer

class UserCrudTests implements ApplicationContextInitializer<ConfigurableApplicationContext>{
    private MySqlConta

    @Override
    void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of("spring.datasource.url=${}")
    }
}
