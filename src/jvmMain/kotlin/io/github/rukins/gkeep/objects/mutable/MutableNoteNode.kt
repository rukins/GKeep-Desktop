package io.github.rukins.gkeep.objects.mutable

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import io.github.rukins.gkeepapi.model.gkeep.node.nodeobject.ListItemNode
import io.github.rukins.gkeepapi.model.gkeep.node.nodeobject.NoteNode

class MutableNoteNode(node: NoteNode) : MutableAbstractNode(node) {
    val title = mutableStateOf(node.title)
    val isArchived = mutableStateOf(node.archived)
    val isPinned = mutableStateOf(node.pinned)
    val color = mutableStateOf(node.color)
    val labelIds = if (node.labelIds != null) mutableStateListOf(*node.labelIds.map { MutableLabelId(it) }.toTypedArray()) else mutableStateListOf()
    val listItemNode = if (node.listItemNode != null) MutableListItemNode(node.listItemNode) else MutableListItemNode(ListItemNode.builder().build())

    override fun toNode(): NoteNode {
        return NoteNode.builder()
            .id(id.value)
            .serverId(serverId.value)
            .parentId(parentId.value)
            .parentServerId(parentServerId.value)
            .timestamps(timestamps.toTimestamps())
            .sortValue(sortValue.value)
            .title(title.value)
            .archived(isArchived.value)
            .pinned(isPinned.value)
            .color(color.value)
            .labelIds(labelIds.map { it.toLabelId() })
            .listItemNode(listItemNode.toNode() as ListItemNode)
            .build()
    }
}