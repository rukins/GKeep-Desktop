package io.github.rukins.gkeep.objects.mutable

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import io.github.rukins.gkeepapi.model.gkeep.node.nodeobject.ListItemNode
import io.github.rukins.gkeepapi.model.gkeep.node.nodeobject.ListNode

class MutableListNode(node: ListNode) : MutableAbstractNode(node) {
    val title = mutableStateOf(node.title)
    val isArchived = mutableStateOf(node.archived)
    val isPinned = mutableStateOf(node.pinned)
    val color = mutableStateOf(node.color)
    val labelIds = if (node.labelIds != null) mutableStateListOf(*node.labelIds.map { MutableLabelId(it) }.toTypedArray()) else mutableStateListOf()
    val listItemNodes = if (node.listItemNodes != null) mutableStateListOf(*node.listItemNodes.map { MutableListItemNode(it) }.toTypedArray()) else mutableStateListOf()

    override fun toNode(): ListNode {
        return ListNode.builder()
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
            .listItemNodes(listItemNodes.map { it.toNode() as ListItemNode })
            .build()
    }
}