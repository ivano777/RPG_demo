package dminis.rpg.game.configuration;

import java.awt.Desktop;
import java.net.URI;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BrowserLauncher {

    @Value("${rpg-demo.deploy-end.url:http://localhost:5173}")
    private String frontEndUrl;

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(frontEndUrl));
                } else {
                    log.error("Azione BROWSE non supportata");
                }
            } else {
                log.error("Desktop non supportato");
            }
        } catch (Exception e) {
            log.error("Errore apertura browser:",e);
        }
    }
}
