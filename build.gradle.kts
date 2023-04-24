import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = extra["project.group"] as String
version = extra["project.version"] as String

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.material3)
//                implementation(compose.materialIconsExtended)

                implementation("io.github.rukins:gkeepapi:${extra["gkeepapi.version"]}")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "io.github.rukins.gkeep.AppKt"
        nativeDistributions {
            packageName = "GKeep"
            packageVersion = "1.0.0"

            targetFormats(
                TargetFormat.Deb, TargetFormat.Rpm,
                TargetFormat.Msi,
                TargetFormat.Dmg,
                TargetFormat.AppImage
            )

            val iconsRoot = project.file("src/jvmMain/resources/logo")

            linux {
                iconFile.set(iconsRoot.resolve("linux.png"))
            }

            windows {
                iconFile.set(iconsRoot.resolve("windows.ico"))
            }

            macOS {
                iconFile.set(iconsRoot.resolve("macos.icns"))
            }

            modules("java.net.http")
            modules("jdk.unsupported")
        }
    }
}
