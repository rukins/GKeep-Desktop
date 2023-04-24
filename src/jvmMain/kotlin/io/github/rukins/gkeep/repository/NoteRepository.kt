package io.github.rukins.gkeep.repository

import com.google.gson.reflect.TypeToken
import io.github.rukins.gkeep.repository.BasicRepository.Companion.STORAGE_FOLDER_PATH
import io.github.rukins.gkeep.repository.BasicRepository.Companion.gson
import io.github.rukins.gkeepapi.model.gkeep.node.nodeobject.AbstractNode
import io.github.rukins.gkeepapi.utils.NodeUtils
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

class NoteRepository : BasicRepository<AbstractNode> {
    override val storageFilePath: Path = Path.of("$STORAGE_FOLDER_PATH/nodes.json")

    override fun getAll(): List<AbstractNode> {
        val fileData = String(Files.readAllBytes(storageFilePath), StandardCharsets.UTF_8).ifEmpty { "[]" }

        return gson.fromJson(fileData, object : TypeToken<List<AbstractNode>>(){}.type)
    }

    override fun saveAll(list: List<AbstractNode>) {
        val writer = Files.newBufferedWriter(storageFilePath);
        writer.write(gson.toJson(NodeUtils.mergeListsOfAbstractNodes(getAll(), list)))

        writer.close()
    }

    override fun createFile() {
        Files.createFile(storageFilePath)
    }

    override fun fileExists(): Boolean {
        return Files.exists(storageFilePath)
    }
}