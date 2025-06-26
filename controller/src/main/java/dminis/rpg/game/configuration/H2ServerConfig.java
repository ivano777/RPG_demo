package dminis.rpg.game.configuration;


import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.SQLException;

@Configuration
//@Profile("dev")   // togli se lo vuoi sempre
public class H2ServerConfig {

    // Avvia H2 in modalità TCP sulla porta 9092
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2TcpServer() throws SQLException {
        return Server.createTcpServer(
                "-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }

    // (opzionale) Avvia anche la console Web in standalone su 8082
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2WebServer() throws SQLException {
        return Server.createWebServer(
                "-web", "-webAllowOthers", "-webPort", "8082");
    }
}