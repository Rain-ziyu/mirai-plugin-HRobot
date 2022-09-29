import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.5.30"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
//升级mirai-console版本
    id("net.mamoe.mirai-console") version "2.12.3"
}

group = "com.happysnaker"
version = "3.2.1-SNAPSHOT"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    maven("https://repo.mirai.mamoe.net/snapshots")
    mavenCentral()
}

dependencies{

    implementation("com.alibaba:fastjson:1.2.76")
    implementation(kotlin("stdlib-jdk8"))
    // https://mvnrepository.com/artifact/org.jfree/jfreechart
    implementation("org.jfree:jfreechart:1.0.19")
    // https://mvnrepository.com/artifact/org.reflections/reflections
    implementation("org.reflections:reflections:0.9.11")
    implementation("org.yaml:snakeyaml:1.25")
    implementation("org.jsoup:jsoup:1.11.2")
    // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
    implementation("org.apache.commons:commons-lang3:3.12.0")
    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
//    compile group: 'org.yaml', name: 'snakeyaml', version: '1.25'
    testImplementation(kotlin("script-runtime"))
// https://mvnrepository.com/artifact/cn.hutool/hutool-all
    implementation("cn.hutool:hutool-all:5.8.7")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}