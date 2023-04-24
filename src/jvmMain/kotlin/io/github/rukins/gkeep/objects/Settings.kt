package io.github.rukins.gkeep.objects

class Settings() {
    var email: String = ""
    var currentVersion: String = ""
    var masterToken: String = ""

    var themeOrdinal: Int = 1
    var enableDarkMode: Boolean = false
    constructor(
        email: String, currentVersion: String, masterToken: String, themeOrdinal: Int, enableDarkMode: Boolean
    ) : this() {
        this.email = email
        this.currentVersion = currentVersion
        this.masterToken = masterToken
        this.themeOrdinal = themeOrdinal
        this.enableDarkMode = enableDarkMode
    }
}
