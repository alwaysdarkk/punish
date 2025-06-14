dependencies {
    implementation(projects.punishApi)

    compileOnly(libs.spigot)

    implementation(libs.commands)
    implementation(libs.inventory.framework)
    implementation(libs.configurate)

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.h2)
    implementation(libs.mysql)
}