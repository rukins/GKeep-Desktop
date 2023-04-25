package io.github.rukins.gkeep.repository

import io.github.rukins.gkeep.objects.Settings
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

object SettingsRepository : BasicRepository<Settings> {
    override val storageFilePath: Path = Path.of("${BasicRepository.STORAGE_FOLDER_PATH}/settings.json")

    override fun get(): Settings {
        if (!fileExists()) {
            return Settings()
        }

        val fileData = String(Files.readAllBytes(storageFilePath), StandardCharsets.UTF_8).ifEmpty { null }

        return if (fileData == null) Settings() else BasicRepository.fromJson(fileData, Settings::class.java)
    }

    override fun save(obj: Settings) {
        val writer = Files.newBufferedWriter(storageFilePath);
        writer.write(BasicRepository.toJson(obj))

        writer.close()
    }
}