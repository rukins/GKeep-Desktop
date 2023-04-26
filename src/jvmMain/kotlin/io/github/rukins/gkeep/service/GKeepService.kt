package io.github.rukins.gkeep.service

import io.github.rukins.gkeep.objects.UserData
import io.github.rukins.gkeep.repository.UserDataRepository
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
import io.github.rukins.gpsoauth.Auth
import io.github.rukins.gpsoauth.model.AccessTokenRequestParams
import io.github.rukins.gpsoauth.model.MasterTokenRequestParams
import java.time.LocalDateTime


class GKeepService(masterToken: String, currentVersion: String) {
    private var gkeepAPI: GKeepAPI

    init {
        gkeepAPI = GKeepAPI(masterToken, currentVersion)
    }

    private val nodeRequestBuilder = NodeRequestBuilder.builder()

    fun getUserDataFromStorageFile(): UserData {
        return UserDataRepository.get()
    }

    fun saveUserDataToStorageFile(userData: UserData) {
        userData.nodes = NodeUtils.getAbstractNodeListFromAssembled(userData.nodes)
        userData.unsyncNodes = NodeUtils.getAbstractNodeListFromAssembled(userData.unsyncNodes)
        UserDataRepository.save(userData)
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
            return nodeRequestBuilder.unarchiveNoteNode(node)
        }
        return nodeRequestBuilder.archiveNoteNode(node)
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
            return nodeRequestBuilder.unarchiveListNode(node)
        }
        return nodeRequestBuilder.archiveListNode(node)
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

    fun updateMasterToken(masterToken: String, authenticationToken: String): String {
        val correctMasterToken =
            if (authenticationToken.isNotEmpty()) {
                getMasterToken(authenticationToken)
            } else {
                // check if master token is correct
                getAccessToken(masterToken)

                masterToken
            }

        gkeepAPI = GKeepAPI(correctMasterToken)

        return correctMasterToken
    }

    private fun getMasterToken(authenticationToken: String): String {
        val masterTokenRequestParams = MasterTokenRequestParams
            .withDefaultValues()
            .token(authenticationToken)
            .build()

        return Auth().getMasterToken(masterTokenRequestParams).masterToken
    }

    // only for checking if master token is correct. remove later
    private fun getAccessToken(masterToken: String): String {
        val accessTokenRequestParams = AccessTokenRequestParams
            .withDefaultValues()
            .masterToken(masterToken)
            .app("com.google.android.keep")
            .scopes("oauth2:https://www.googleapis.com/auth/memento https://www.googleapis.com/auth/reminders")
            .build()

        return Auth().getAccessToken(accessTokenRequestParams).accessToken
    }
}