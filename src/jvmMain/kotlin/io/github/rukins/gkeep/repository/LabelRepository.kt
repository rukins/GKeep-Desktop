package io.github.rukins.gkeep.repository

import io.github.rukins.gkeepapi.model.gkeep.userinfo.Label
import java.nio.file.Path

object LabelRepository : BasicRepository<Label> {
    override val storageFilePath: Path = Path.of("${BasicRepository.STORAGE_FOLDER_PATH}/labels.json")
}