plugins {
    id("java")
}

group = "com.msavenkov.crudgradleproject"
version = "1.0.0"

val lombokVersion by extra("1.18.42")

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}