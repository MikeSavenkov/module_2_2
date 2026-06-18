plugins {
    id("java")
    id("application")
    id("org.liquibase.gradle") version("2.2.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

application {
    mainClass.set("com.msavenkov.crudgradleproject.App")
}

group = "com.msavenkov.crudgradleproject"
version = "1.0.0"

val lombokVersion by extra("1.18.42")
val liquibaseVersion by extra("4.31.1")
val mysqlVersion by extra("8.0.33")

repositories {
    mavenCentral()
}

dependencies {
    //implementation("org.liquibase:liquibase-core:$liquibaseVersion")
    implementation("com.mysql:mysql-connector-j:$mysqlVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    liquibaseRuntime("org.liquibase:liquibase-core:$liquibaseVersion")
    //liquibaseRuntime("info.picocli:picocli:4.7.5")
    liquibaseRuntime("com.mysql:mysql-connector-j:$mysqlVersion")
}

//liquibase {
//    activities.register("main") {
//        arguments = mapOf(
//            "changeLogFile" to "src/main/resources/db/changelog/main-changelog.xml",
//            "url" to "jdbc:mysql://localhost:3306/test_db",
//            "username" to "root",
//            "password" to "123"
//        )
//    }
//}

tasks.register<JavaExec>("updateDatabase") {
    group = "liquibase"
    description = "Applies Liquibase changelog to the database"
    classpath = configurations.liquibaseRuntime.get()
    mainClass.set("liquibase.integration.commandline.Main")
    args(
        "--changeLogFile=src/main/resources/db/changelog/main-changelog.xml",
        "--url=jdbc:mysql://localhost:3306/test_db",
        "--username=root",
        "--password=123",
        "update"
    )
}

tasks.test {
    useJUnitPlatform()
}