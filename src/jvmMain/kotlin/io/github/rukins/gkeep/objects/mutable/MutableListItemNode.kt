package io.github.rukins.gkeep.objects.mutable

import androidx.compose.runtime.mutableStateOf
import io.github.rukins.gkeepapi.model.gkeep.node.nodeobject.ListItemNode

class MutableListItemNode(node: ListItemNode) : MutableAbstractNode(node) {
    val text = mutableStateOf(node.text)
    val checked = mutableStateOf(node.checked)
    val superListItemId = mutableStateOf(node.superListItemId)
    val superListItemServerId = mutableStateOf(node.superListItemServerId)

    override fun toNode(): ListItemNode {
        return ListItemNode.builder()
            .id(id.value)
            .serverId(serverId.value)
            .parentId(parentId.value)
            .parentServerId(parentServerId.value)
            .timestamps(timestamps.toTimestamps())
            .sortValue(sortValue.value)
            .text(text.value)
            .checked(checked.value)
            .superListItemId(superListItemId.value)
            .superListItemServerId(superListItemServerId.value)
            .build()
    }
}