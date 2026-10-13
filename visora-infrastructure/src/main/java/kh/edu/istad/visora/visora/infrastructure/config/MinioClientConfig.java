package kh.edu.istad.visora.visora.infrastructure.config;

import io.minio.MinioClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioClientConfig {

    @Bean
    public MinioClient minioClient(MinioProperties minioProperties){
        return MinioClient.builder()
                .endpoint(minioProperties.endpoint())
                .credentials(minioProperties.accessKey(), minioProperties.secretKey())
                .build();
    }

}
