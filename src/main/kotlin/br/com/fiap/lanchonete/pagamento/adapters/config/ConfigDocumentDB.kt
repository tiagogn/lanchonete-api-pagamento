package br.com.fiap.lanchonete.pagamento.adapters.config

import com.mongodb.MongoClientSettings
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConfigDocumentDB(

) {

    @Bean
    fun mongoClientSettings(): MongoClientSettings {
        var JAVA_HOME = System.getenv("JAVA_HOME")
        System.setProperty("javax.net.ssl.trustStore", "$JAVA_HOME/lib/security/cacerts")
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit")
        return MongoClientSettings.builder()
            .applyToSslSettings{ builder ->
                builder.enabled(true)
            }
            .build()
    }

    @Bean
    fun mongoClientSettingsBuilderCustomizer(): MongoClientSettingsBuilderCustomizer {
        return MongoClientSettingsBuilderCustomizer { builder ->
            builder.applyToSslSettings { ssl ->
                ssl.enabled(true)
            }
        }
    }
}