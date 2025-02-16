package br.com.fiap.lanchonete.pagamento.adapters.config

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConfigDocumentDB(

) {

    @Bean
    fun mongoClient(): MongoClient {
        return com.mongodb.client.MongoClients.create(mongoClientSettings())
    }


    @Bean
    fun mongoClientSettings(): MongoClientSettings {
        var JAVA_HOME = System.getenv("JAVA_HOME")
        System.setProperty("javax.net.ssl.trustStore", "$JAVA_HOME/lib/security/cacerts")
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit")
        return MongoClientSettings.builder()
            .applyToSslSettings{ builder ->
                builder.enabled(true)
                builder.invalidHostNameAllowed(true)
            }
            .build()
    }

}