package io.github.rukins.gkeep.repository

import io.github.rukins.gkeep.objects.Settings
import io.github.rukins.gkeep.repository.BasicRepository.Companion.gson
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

class SettingsRepository : BasicRepository<Settings> {
    override val storageFilePath: Path = Path.of("${BasicRepository.STORAGE_FOLDER_PATH}/settings.json")

    override fun get(): Settings {
        val fileData = String(Files.readAllBytes(storageFilePath), StandardCharsets.UTF_8).ifEmpty { "{}" }

        return gson.fromJson(fileData, Settings::class.java)
    }

    override fun save(obj: Settings) {
        val writer = Files.newBufferedWriter(storageFilePath);
        writer.write(gson.toJson(obj))

        writer.close()
    }
}