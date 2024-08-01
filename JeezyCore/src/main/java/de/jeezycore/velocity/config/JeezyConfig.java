package de.jeezycore.velocity.config;

import com.moandjiezana.toml.Toml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;


public class JeezyConfig {

    private final File folder = new File("plugins/jeezycore");
    private final File theDir = new File(folder.toString());
    private static final File config = new File("plugins/jeezycore/config.toml");
    public static Toml toml = new Toml().read(config);

    public void createData() {
        if (!theDir.exists()) {
            theDir.mkdirs();
            System.out.println("Folder created: " + theDir.getName() + ".");
        }

        if(!config.exists()) {
            try {
                ClassLoader classLoader = getClass().getClassLoader();
                URL resource = classLoader.getResource("config.toml");

                Files.copy(resource.openStream(), config.toPath()); // Copy the file out of our jar into our plugins Data Folder
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}