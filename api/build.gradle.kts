plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    kotlin("plugin.allopen") version libs.versions.kotlin.get()
    kotlin("plugin.spring") version libs.versions.kotlin.get()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:${libs.versions.dgs.dependencies.get()}"))
    implementation(platform("org.springframework.boot:spring-boot-dependencies:${libs.versions.spring.boot.get()}"))

    implementation("com.netflix.graphql.dgs:dgs-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
}