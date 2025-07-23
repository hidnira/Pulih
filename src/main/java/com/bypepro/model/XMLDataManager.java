/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.model;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The primary data persistence manager, implemented using the Singleton design pattern.
 * This class is responsible for all data persistence operations, which involve converting
 * Java objects to XML format (serialization) and back (deserialization) using the XStream library.
 */

public class XMLDataManager {

    // Singleton Implementation
    private static XMLDataManager instance;
    private final XStream xstream;
    private final String DATABASE_FILE = "data/database.xml";


    /**
     * Private constructor to prevent instantiation from other classes.
     * This is where XStream is initialized, its security is configured, and aliases are set
     * to produce a cleaner and more readable XML output.
     */

    private XMLDataManager() {
        xstream = new XStream();
        XStream.setupDefaultSecurity(xstream);
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.alias("pulih-data", PulihData.class);
        xstream.alias("fisioterapis", Fisioterapis.class);
        xstream.alias("pasien", Pasien.class);
        xstream.alias("sesiLatihan", SesiLatihan.class);
        xstream.alias("gerakan", Gerakan.class);
        xstream.alias("laporan", Laporan.class);
        xstream.alias("evaluasi", Evaluasi.class);
    }


    /**
     * Provides the single, global instance of the XMLDataManager.
     * This ensures that only one object manages the database file throughout the application.
     * @return The singleton instance of XMLDataManager.
     */

    public static synchronized XMLDataManager getInstance() {
        if (instance == null) {
            instance = new XMLDataManager();
        }
        return instance;
    }


    // Public Data Save & Load Methods

    /**
     * Serializes a PulihData object into an XML string and saves it to the database file.
     * @param data The PulihData object containing all application data (patients, therapists, etc.).
     */

    public void saveData(PulihData data) {
        try {
            Files.createDirectories(Paths.get("data"));
            String xml = xstream.toXML(data);
            try (FileWriter writer = new FileWriter(DATABASE_FILE)) {
                writer.write(xml);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Loads data from the database.xml file and deserializes it back into a PulihData object.
     * If the file does not exist (e.g., on the first run), it creates default data.
     * @return A PulihData object populated with data, or a new, empty PulihData object if the file is not found.
     */

    public PulihData loadData() {
        try {
            File file = new File(DATABASE_FILE);
            if (file.exists() && file.length() > 0) {
                String xml = new String(Files.readAllBytes(Paths.get(DATABASE_FILE)));
                return (PulihData) xstream.fromXML(xml);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Database tidak ditemukan, membuat data default...");
        return createDefaultData();
    }


    /**
     * Creates initial data with one default Physiotherapist.
     * This is called when the application is run for the first time.
     * @return A PulihData object containing the default data.
     */

    private PulihData createDefaultData() {
        PulihData dataBaru = new PulihData();

        String defaultPassword = "terapis123";
        String hashedPassword = PasswordHasher.hashPassword(defaultPassword);

        Fisioterapis terapisDefault = new Fisioterapis(
                "windy.clore",
                hashedPassword,
                "Windy Clore, S.Ft",
                "windy.clore@klinikpulih.com",
                "081120250719"
        );

        dataBaru.getListFisioterapis().add(terapisDefault);

        saveData(dataBaru);
        System.out.println("Terapis default 'dian.lestari' dengan password '" + defaultPassword + "' telah dibuat.");

        return dataBaru;
    }
}