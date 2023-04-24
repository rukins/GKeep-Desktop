package io.github.rukins.gkeep.service

import io.github.rukins.gkeep.repository.LabelRepository
import io.github.rukins.gkeep.repository.NoteRepository
import io.github.rukins.gkeepapi.GKeepAPI
import io.github.rukins.gkeepapi.NodeRequestBuilder
import io.github.rukins.gkeepapi.model.gkeep.NodeResponse
import io.github.rukins.gkeepapi.model.gkeep.node.Color
import io.github.rukins.gkeepapi.model.gkeep.node.nodeobject.AbstractNode
import io.github.rukins.gkeepapi.model.gkeep.node.nodeobject.ListItemNode
import io.github.rukins.gkeepapi.model.gkeep.node.nodeobject.ListNode
import io.github.rukins.gkeepapi.model.gkeep.node.nodeobject.NoteNode
import io.github.rukins.gkeepapi.utils.IdUtils
import io.github.rukins.gkeepapi.utils.NodeUtils
import java.time.LocalDateTime

class GKeepService(masterToken: String, currentVersion: String) {
    private val gkeepAPI: GKeepAPI

    init {
        gkeepAPI = GKeepAPI(masterToken, currentVersion)
    }

    private val nodeRequestBuilder = NodeRequestBuilder.builder()

    private val noteRepository = NoteRepository()

    private val labelRepository = LabelRepository()

    fun getAllNodesFromStorageFile(): List<AbstractNode> {
        return NodeUtils.getAssembledAbstractNodeList(noteRepository.getAll())
    }

    fun saveAllNotes(notes: List<AbstractNode>) {
        noteRepository.saveAll(notes)
    }

    fun createOrUpdateNoteNode(noteNode: NoteNode): NoteNode {
        return nodeRequestBuilder.createOrUpdateNoteNode(noteNode)
    }

    fun pinOrUnpinNoteNode(node: NoteNode): NoteNode {
        if (node.pinned) {
            return nodeRequestBuilder.unpinNoteNode(node)
        }
        return nodeRequestBuilder.pinNoteNode(node)
    }

    fun archiveOrUnarchiveNoteNode(node: NoteNode): NoteNode {
        if (node.archived) {
            return nodeRequestBuilder.archiveNoteNode(node)
        }
        return nodeRequestBuilder.unarchiveNoteNode(node)
    }

    fun createOrUpdateListNode(listNode: ListNode): ListNode {
        return nodeRequestBuilder.createOrUpdateListNode(listNode)
    }

    fun pinOrUnpinListNode(node: ListNode): ListNode {
        if (node.pinned) {
            return nodeRequestBuilder.pinListNode(node)
        }
        return nodeRequestBuilder.unpinListNode(node)
    }

    fun archiveOrUnarchiveListNode(node: ListNode): ListNode {
        if (node.archived) {
            return nodeRequestBuilder.archiveListNode(node)
        }
        return nodeRequestBuilder.unarchiveListNode(node)
    }

    fun addListItemNodeToListNode(listNode: ListNode, listItemNode: ListItemNode): ListItemNode {
        listItemNode.sortValue =
            if (listNode.listItemNodes.isNotEmpty())
                (listNode.listItemNodes.sortedByDescending { it.sortValue }.first().sortValue.toLong() + 1).toString()
            else
                "0"
        // TODO - REMOVE LATER
        listItemNode.id = IdUtils.generateId()
        listItemNode.timestamps.updated = LocalDateTime.now()

        listNode.listItemNodes.add(listItemNode)

        nodeRequestBuilder.createOrUpdateListNode(listNode)

        return listItemNode
    }

    fun deleteListItemFromList(listNode: ListNode, listItemNode: ListItemNode): ListItemNode {
        listItemNode.timestamps.deleted = LocalDateTime.now()
        listNode.listItemNodes.first { it.id == listItemNode.id }.let { it.timestamps.deleted = listItemNode.timestamps.deleted }

        nodeRequestBuilder.createOrUpdateListNode(listNode)

        return listItemNode
    }

    fun trashNode(node: AbstractNode): AbstractNode {
        return nodeRequestBuilder.trashNode(node)
    }

    fun restoreNode(node: AbstractNode): AbstractNode {
        return nodeRequestBuilder.restoreNode(node)
    }

    fun deleteNode(node: AbstractNode): AbstractNode {
        return nodeRequestBuilder.deleteNode(node)
    }

    fun syncDataWithServer(): NodeResponse {
        val nodeResponse = gkeepAPI.changes(nodeRequestBuilder.build())

        nodeRequestBuilder.empty()

        return nodeResponse
    }

    fun getCurrentVersion(): String {
        return gkeepAPI.currentVersion
    }

    fun newDefaultNoteNode(): NoteNode {
        return NoteNode.builder()
            .title("")
            .archived(false)
            .pinned(false)
            .color(Color.DEFAULT)
            .labelIds(listOf())
            .listItemNode(newDefaultListItemNode())
            .build()
    }

    fun newDefaultListNode(): ListNode {
        return ListNode.builder()
            .title("")
            .archived(false)
            .pinned(false)
            .color(Color.DEFAULT)
            .labelIds(listOf())
            .listItemNodes(listOf())
            .build()
    }

    fun newDefaultListItemNode(): ListItemNode {
        return ListItemNode.builder()
            .text("")
            .checked(false)
            .superListItemId("")
            .superListItemServerId("")
            .build()
    }
}