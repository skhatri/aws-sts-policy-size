tasks.wrapper {
    gradleVersion = "7.4.2"
}

plugins {
  idea
  id("java-library")
}

repositories {
    mavenCentral()
}


java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

configurations {
    implementation {
        resolutionStrategy.failOnVersionConflict()
    }
}

dependencies {
  listOf("aws-java-sdk-sts", "aws-java-sdk-s3", "aws-java-sdk-bom").forEach { mod ->
    implementation("com.amazonaws:${mod}:1.11.860")
  }
}


task("run-data", JavaExec::class) {
    main = "com.github.skhatri.aws.DataGen"
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs = listOf(
        "-Xms512m", "-Xmx512m"
    )
}

task("run-token", JavaExec::class) {
    main = "com.github.skhatri.aws.AwsStsGetToken"
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs = listOf(
        "-Xms512m", "-Xmx512m"
    )
}

