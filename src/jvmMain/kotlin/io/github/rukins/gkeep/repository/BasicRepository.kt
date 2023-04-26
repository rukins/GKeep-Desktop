package io.github.rukins.gkeep.repository

import com.google.gson.Gson
import io.github.rukins.gkeepapi.config.GsonConfig
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

interface BasicRepository<T> {
    val storageFilePath: Path

    companion object {
        private val gson: Gson = GsonConfig.gson()

        fun toJson(obj: Any): String {
            return gson.toJson(obj)
        }

        fun <T> fromJson(json: String, classOfT: Class<T>): T {
            return gson.fromJson(json, classOfT)
        }

        fun createStorageFolder() {
            Files.createDirectory(Path.of(STORAGE_FOLDER_PATH))
        }

        val STORAGE_FOLDER_PATH = getRootPath() + "/.gkeep"

        private fun getRootPath(): String {
            val osName = System.getProperty("os.name").lowercase(Locale.getDefault())

            return if (osName.contains("windows"))
                System.getenv("APPDATA")
            else if (osName.contains("mac"))
                System.getenv("?")
            else
                System.getProperty("user.home")
        }
    }

    fun get(): T {
        throw NotImplementedError()
    }

    fun save(obj: T) {
        throw NotImplementedError()
    }

    fun createFile() {
        Files.createFile(storageFilePath)
    }

    fun fileExists(): Boolean {
        return Files.exists(storageFilePath)
    }
}