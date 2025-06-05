dependencies {
    implementation(projects.punishApi)

    compileOnly(libs.spigot)
    compileOnly(libs.inventory.framework)

    implementation(libs.commands)
    implementation(libs.configurate)

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.h2)
}