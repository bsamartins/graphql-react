import com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask

plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    kotlin("plugin.allopen") version libs.versions.kotlin.get()
    kotlin("plugin.noarg") version libs.versions.kotlin.get()
    kotlin("plugin.spring") version libs.versions.kotlin.get()
    id("com.netflix.dgs.codegen") version libs.versions.dgs.codegen.get()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:${libs.versions.dgs.dependencies.get()}"))
    implementation(platform("org.springframework.boot:spring-boot-dependencies:${libs.versions.spring.boot.get()}"))

    implementation("com.netflix.graphql.dgs:dgs-starter")
    implementation("com.netflix.graphql.dgs:graphql-dgs-pagination")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
    implementation("org.hibernate.search:hibernate-search-mapper-orm:${libs.versions.hibernate.search.get()}")
    implementation("org.hibernate.search:hibernate-search-backend-lucene:${libs.versions.hibernate.search.get()}")
    runtimeOnly("org.hibernate.orm:hibernate-community-dialects")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv")
    runtimeOnly("org.xerial:sqlite-jdbc:3.49.1.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

noArg {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
}

tasks.named<GenerateJavaTask>("generateJava") {
    packageName = "io.bsamartins.sandbox.graphql.codegen"
    typeMapping["MovieConnection"] = "graphql.relay.SimpleListConnection<Movie>"

}

tasks.withType<Test> {
    useJUnitPlatform()
}