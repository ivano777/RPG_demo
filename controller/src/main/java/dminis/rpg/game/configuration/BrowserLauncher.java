package dminis.rpg.game.configuration;

import java.awt.Desktop;
import java.net.URI;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BrowserLauncher {

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        try {
            Desktop.getDesktop().browse(new URI("http://localhost:8080"));
        } catch (Exception e) {
            // se fallisce non muore l'app, logga e basta
            System.err.println("Impossibile aprire il browser: " + e.getMessage());
        }
    }
}
