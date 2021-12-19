plugins {
    `java-library`
    `maven-publish`
}

group = "tk.booky"
version = "1.5.2"

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")

    maven("https://mvn.intellectualsites.com/content/repositories/snapshots/")
    maven("https://mvn.intellectualsites.com/content/groups/public/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://jitpack.io/")
}

dependencies {
    api("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")
    api("dev.jorel.commandapi:commandapi-core:6.5.3")

    api("com.plotsquared:PlotSquared-Core:6.2.1")
    api("com.plotsquared:PlotSquared-Bukkit:6.2.1") { isTransitive = false }
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications.create<MavenPublication>("maven") {
        artifactId = project.name.toLowerCase()
        from(components["java"])
    }
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}
