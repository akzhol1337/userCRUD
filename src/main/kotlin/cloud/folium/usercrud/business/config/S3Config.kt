package cloud.folium.usercrud.business.config

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.AmazonS3
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class S3Config {
    @Bean
    fun getAWSS3():  AmazonS3{
        val credentials: AWSCredentials = BasicAWSCredentials(
            "YCAJE6Wt80x6e9hpi6z71HTVJ",
            "YCOJMZZ3JS_uuicZibtB3X2A_6LFp5hW9VVawXaq"
        )
        return AmazonS3ClientBuilder.standard()
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .withEndpointConfiguration(
                EndpointConfiguration(
                    "storage.yandexcloud.net", "ru-central1"
                )
            )
            .build()
    }
}