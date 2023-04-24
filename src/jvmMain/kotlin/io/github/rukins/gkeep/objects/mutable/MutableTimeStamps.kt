package io.github.rukins.gkeep.objects.mutable

import androidx.compose.runtime.mutableStateOf
import io.github.rukins.gkeepapi.model.gkeep.Timestamps

class MutableTimeStamps(timeStamps: Timestamps) {
    val created = mutableStateOf(timeStamps.created)
    val updated = mutableStateOf(timeStamps.updated)
    val trashed = mutableStateOf(timeStamps.trashed)
    val deleted = mutableStateOf(timeStamps.deleted)
    val userEdited = mutableStateOf(timeStamps.userEdited)
    val recentSharedChangesSeen = mutableStateOf(timeStamps.recentSharedChangesSeen)

    fun toTimestamps(): Timestamps {
        return Timestamps.builder()
            .created(created.value)
            .updated(updated.value)
            .trashed(trashed.value)
            .deleted(deleted.value)
            .userEdited(userEdited.value)
            .recentSharedChangesSeen(recentSharedChangesSeen.value)
            .build()
    }
}