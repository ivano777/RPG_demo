
# RPG Demo

Semplice demo di gioco di ruolo scritta in **Java 21**.

---

## Come generare l’eseguibile

```bash
mvn clean package -Pexe

* Produce **`RPG_demo.exe`** (nome esatto nel target dipende dalla tua configurazione Launch4j).
* Alla partenza l’eseguibile cerca **Java 21** in una cartella chiamata **`jre`** nella
  stessa directory.

    * Se trova la cartella: usa quella JRE locale.
    * Se non la trova: prova a usare la Java 21 installata sul sistema.

> ⚠️ **Avviso SmartScreen**
> L’EXE non è firmato digitalmente, quindi Windows potrebbe mostrare
> “Windows ha protetto il PC”. Clicca **“Ulteriori informazioni → Esegui comunque”**
> se il file proviene da una fonte affidabile (es. repository ufficiale del progetto).

---

## Esecuzione manuale (senza EXE)

1. Assicurati di avere **Java 21** nel `PATH` (o usa la JRE inclusa in `jre/`).
2. Avvia l’applicazione:

java -jar target/rpg-demo-1.0.jar
```

Oppure, con la JRE locale (dalla cartella principale):

```
##bash
jre/bin/java -Djava.awt.headless=false -jar RPG_demo/controller/target/controller-0.0.1-SNAPSHOT.jar
```

```
##powerShell
& .\jre\bin\java.exe '-Djava.awt.headless=false' -jar '.\RPG_demo\controller\target\controller-0.0.1-SNAPSHOT.jar'
```

```
##cmd
jre\bin\java.exe -Djava.awt.headless=false -jar RPG_demo\controller\target\controller-0.0.1-SNAPSHOT.jar
```

---

## Struttura del progetto

```
RPG_DEMO_project/
│
├─ RPG_demo.exe # avvio one-click dell’app
├─ jre/ # JRE 21 portatile (opzionale ma consigliata)
└─ RPG_demo/ # progetto Maven multi-modulo
    ├─ common/
    ├─ controller/ # qui nasce l’eseguibile
    ├─ domain/
    ├─ service/
    ├─ frontend/
    └─ README.md

```

Feel free to open an issue or pull request per segnalare bug o proporre miglioramenti!

```
    *add github link*
```
