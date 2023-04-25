package io.github.rukins.gkeep.repository

import io.github.rukins.gkeep.objects.UserData
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

object UserDataRepository : BasicRepository<UserData> {
    override val storageFilePath: Path = Path.of("${BasicRepository.STORAGE_FOLDER_PATH}/userData.json")

    override fun get(): UserData {
        if (!fileExists()) {
            return UserData()
        }

        val fileData = String(Files.readAllBytes(storageFilePath), StandardCharsets.UTF_8).ifEmpty { null }

        return if (fileData == null) UserData() else BasicRepository.fromJson(fileData, UserData::class.java)
    }

    override fun save(obj: UserData) {
        val userData = get()
        userData.merge(obj)

        Files.newBufferedWriter(storageFilePath).use {
            it.write(BasicRepository.toJson(userData))
        }
    }
}