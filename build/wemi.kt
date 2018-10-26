import wemi.Keys.runDirectory


val rgn by project {

    projectName set { "rgn" }
    projectGroup set { "rgn" }
    projectVersion set { "0.1-SNAPSHOT" }

    repositories add { repository("jitpack", "https://jitpack.io") }

    libraryDependencies add { dependency("org.slf4j:slf4j-api:1.7.22") } // Logging backend
    libraryDependencies add { dependency("com.github.Darkyenus:tproll:v1.2.4") } // Logging frontend

    libraryDependencies add { dependency("org.json:json:20180130") } // JSON Parser and generator

    libraryDependencies add { dependency("org.java-websocket:Java-WebSocket:1.3.8") } // Websockets
    libraryDependencies add { dependency("com.github.Darkyenus:DaveWebb:v1.2") } // Rest API

    libraryDependencies add { dependency("org.eclipse.persistence:org.eclipse.persistence.jpa:2.7.1")} // Eclipse JPA
    libraryDependencies add { dependency("mysql:mysql-connector-java:8.0.13")} // MySQL Connector

    extend(testing) {
        libraryDependencies add { JUnitAPI }
        libraryDependencies add { JUnitEngine }
    }

    mainClass set { "ksu.rgn.Main" }
    runDirectory set { projectRoot.get() }

}