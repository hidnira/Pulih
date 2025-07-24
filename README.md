# Pulih: Digital Physical Rehabilitation App 🔄

[](https://www.java.com)
[](https://openjfx.io/)
[](https://maven.apache.org/)

**Pulih** is a desktop application based on Java and JavaFX, designed to bridge communication between physiotherapists and patients in a remote physical rehabilitation program. The application facilitates an asynchronous workflow where a therapist can assign exercise programs, and a patient can report their progress by uploading videos for evaluation.

## Key Features 🤖
- **Dual User Roles**: Separate login systems for **Physiotherapists** and **Patients** with distinct access rights.
- **Patient Management (CRUD)**: Therapists can add, view details of, edit, and delete patient data.
- **Exercise Session Management (CRUD)**: Therapists can create, assign, edit, and delete exercise programs for each patient.
- **Patient Progress Reporting**: Patients can view their assigned tasks, upload report videos, and record their pain levels.
- **Asynchronous Evaluation**: Therapists can review report videos and provide written feedback.
- **Informative Dashboards**: A summary of statistics (total patients, reports needing review) for therapists and a progress summary for patients. [cite: 135, 249]
- **Data Visualization**: A line chart to monitor the patient's exercise adherence progress over time.
- **Local Data Storage**: All application data is stored portably in a single `database.xml` file.

## Technologies Used 💻
- **Java 11**: The primary programming language.
- **JavaFX**: The framework used for building the graphical user interface (GUI).
- **Maven**: A build automation tool for project and dependency management.
- **XStream**: A library for serializing/deserializing Java objects to XML.
- **Scene Builder**: A visual layout tool for designing FXML interfaces.

## Getting Started 📖

### Prerequisites
Ensure you have the following installed on your system:
1.  **Java Development Kit (JDK)**: Version **11** or newer.
2.  **Apache Maven**.

### Installation and Running
1.  **Open the Project**: Open the project folder in your IDE (IntelliJ IDEA or VS Code).
2.  **Open a Terminal**: Open a new terminal within your IDE.
3.  **Build the Project**: Run the following command to download all necessary libraries.
    ```bash
    mvn clean install
    ```
4.  **Run the Application**: After the build is successful, run the following command.
    ```bash
    mvn javafx:run
    ```

## Demo Accounts ▶️

You can use the following default accounts to test the application's features after running it for the first time.

**Physiotherapist Account:**
* **Username**: `windy.clore`
* **Password**: `terapis123`

**Patient Accounts:**
To test the patient workflow, please log in as the therapist first and create new patient accounts using the "+ Tambah Pasien Baru" feature with the following credentials:

* **Patient 1:**
    * **Email**: `baskara.aji@email.com`
    * **Password**: `baskara98`
* **Patient 2:**
    * **Email**: `anindita.p@email.com`
    * **Password**: `anindita01`
* **Patient 3:**
    * **Email**: `rian.setiawan@email.com`
    * **Password**: `rian95`
* **Patient 4:**
    * **Email**: `karina.s@email.com`
    * **Password**: `karina03`

## Project Structure 📂

```
Pulih/
├── data/                  # Contains database.xml
├── videos/                # Contains report videos uploaded by patients
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── bypepro/
│   │   │           └── pulih/
│   │   │               ├── Main.java
│   │   │               ├── controller/
│   │   │               └── model/
│   │   └── resources/
│   │       └── com/
│   │           └── bypepro/
│   │               └── pulih/
│   │                   ├── images/
│   │                   └── view/     # Contains all .fxml files
└── pom.xml                # Maven configuration file
```

## Acknowledgments 📌

This project was developed as a major assignment for a university course. 

**Pemula Produktif's Member:**
1. Aditya Muhammad (24523107)
2. Aufa Azahria Purba (24523026)
3. Hidayat Nur Hijrah (24523201)
4. Muhammad Farhan Haafidh Abror (24523115)
5. Rayyan Galih Indarto (24523224)


Also thank you to everyone who provided support.

## License 📄

Copyright © 2025 Pemula Produktif. All rights reserved.
