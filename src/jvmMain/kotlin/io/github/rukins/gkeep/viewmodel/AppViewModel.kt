package io.github.rukins.gkeep.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import io.github.rukins.gkeep.objects.UserData
import io.github.rukins.gkeep.objects.mutable.*
import io.github.rukins.gkeep.repository.BasicRepository
import io.github.rukins.gkeep.repository.SettingsRepository
import io.github.rukins.gkeep.repository.UserDataRepository
import io.github.rukins.gkeep.service.GKeepService
import io.github.rukins.gkeep.ui.NavigationElement
import io.github.rukins.gkeepapi.model.gkeep.node.NodeType
import io.github.rukins.gkeepapi.model.gkeep.node.nodeobject.AbstractNode
import io.github.rukins.gkeepapi.model.gkeep.node.nodeobject.ListNode
import io.github.rukins.gkeepapi.model.gkeep.node.nodeobject.NoteNode
import io.github.rukins.gkeepapi.utils.NodeUtils
import io.github.rukins.gpsoauth.exception.AuthError
import java.net.URL

class AppViewModel {
    var settings: MutableSettings = MutableSettings(SettingsRepository.get())

    private val gKeepService: GKeepService = GKeepService(settings.masterToken, settings.currentVersion)

    var notes = mutableStateMapOf<String, MutableAbstractNode?>()

    var unsyncNotes = mutableStateMapOf<String, MutableAbstractNode?>()

    var currentEditableNote: MutableAbstractNode = MutableNoteNode(gKeepService.newDefaultNoteNode())

    val lastSortValue = mutableStateOf("0")

    val selectedNavigationElement = mutableStateOf(NavigationElement.NOTES)

    val showNavigationBar = mutableStateOf(true)

    val showNoteEditingBox = mutableStateOf(false)

    val showNoteActions = mutableStateOf(false)

    val isUserLoggedIn = mutableStateOf(true)

    val showProgressIndicatorOnRefresh = mutableStateOf(false)

    fun onRefresh() {
        showProgressIndicatorOnRefresh.value = true
        synchronized(this) {
            val nodeResponse = gKeepService.syncDataWithServer()

            val notesFromServer = NodeUtils.mergeListsOfAbstractNodes(
                NodeUtils.getAbstractNodeListFromAssembled(notes.values.map { it?.toNode()!! }),
                nodeResponse.nodes
            )

            unsyncNotes.clear()

            notes.putAll(getMutableMapOfIdAndMutableNode(NodeUtils.getAssembledAbstractNodeList(notesFromServer)))
            notes.forEach { idAndNode ->
                if (MutableAbstractNode.isNoteDeleted(idAndNode.value)) notes.remove(idAndNode.key)

                when (idAndNode.value?.type) {
                    NodeType.NOTE -> {
//                        idAndNode.value as MutableNoteNode
                        // TODO - ADD DELETING BLOBS IF IT WAS DELETED
                    }
                    NodeType.LIST -> {
                        (idAndNode.value as MutableListNode).listItemNodes.removeIf { MutableAbstractNode.isNoteDeleted(it) }
                    }
                    else -> {}
                }
            }
        }
        showProgressIndicatorOnRefresh.value = false
    }

    fun onCreateOrUpdateNote() {
        if (currentEditableNote.id.value == null) {
            currentEditableNote.sortValue.value = lastSortValue.value.toLong().plus(1).toString()
        }

        hideNoteEditingBoxAndNoteActions()
        saveNote(
            when (currentEditableNote.type) {
                NodeType.NOTE -> gKeepService.createOrUpdateNoteNode((currentEditableNote as MutableNoteNode).toNode())
                NodeType.LIST -> gKeepService.createOrUpdateListNode((currentEditableNote as MutableListNode).toNode())
                else -> null
            }
        )
    }

    fun onTrashOrRestoreNote() {
        hideNoteEditingBoxAndNoteActions()
        saveNote(
            if (MutableAbstractNode.isNoteTrashed(currentEditableNote)) {
                gKeepService.restoreNode(currentEditableNote.toNode())
            } else {
                gKeepService.trashNode(currentEditableNote.toNode())
            }
        )
    }

    fun onPinOrUnpinNote() {
        hideNoteEditingBoxAndNoteActions()
        saveNote(
            when (currentEditableNote.type) {
                NodeType.NOTE ->
                    gKeepService.pinOrUnpinNoteNode((currentEditableNote as MutableNoteNode).toNode())
                NodeType.LIST ->
                    gKeepService.pinOrUnpinListNode((currentEditableNote as MutableListNode).toNode())
                else ->
                    null
            }
        )
    }

    fun onArchiveOrUnArchiveNote() {
        hideNoteEditingBoxAndNoteActions()
        saveNote(
            when (currentEditableNote.type) {
                NodeType.NOTE ->
                    gKeepService.archiveOrUnarchiveNoteNode((currentEditableNote as MutableNoteNode).toNode())
                NodeType.LIST ->
                    gKeepService.archiveOrUnarchiveListNode((currentEditableNote as MutableListNode).toNode())
                else ->
                    null
            }
        )
    }

    fun onAddListItemToList(list: MutableListNode, text: String) {
        val newListItemNode = gKeepService.newDefaultListItemNode()
        newListItemNode.text = text

        list.listItemNodes.add(
            MutableListItemNode(gKeepService.addListItemNodeToListNode(list.toNode(), newListItemNode))
        )
    }

    fun onDeleteListItemFromList(list: MutableListNode, listItemNode: MutableListItemNode) {
        MutableListItemNode(gKeepService.deleteListItemFromList(list.toNode(), listItemNode.toNode()))
        list.listItemNodes.removeIf { it.id == listItemNode.id }
    }

    fun onPointerEventEnterOrExitNoteField(note: MutableAbstractNode? = null) {
        if (note != null) {
            currentEditableNote = note
            showNoteActions.value = true
        } else {
            showNoteActions.value = false
        }
    }

    fun onClickEditOrCreateNote(note: MutableAbstractNode = MutableNoteNode(gKeepService.newDefaultNoteNode())) {
        currentEditableNote = note
        showNoteEditingBox.value = true
    }

    fun onClickMenuButton() {
        showNavigationBar.value = !showNavigationBar.value
    }

    fun onChangeTheme() {
        settings.enableDarkMode.value = !settings.enableDarkMode.value
    }

    fun onCloseApp() {
        onRefresh()

        settings.currentVersion = gKeepService.getCurrentVersion()
        SettingsRepository.save(settings.toSettings())

        gKeepService.saveUserDataToStorageFile(
            UserData(
                notes.values.map { it?.toNode()!! },
                unsyncNotes.values.map { it?.toNode()!! }
            )
        )
    }

    @Throws(AuthError::class)
    fun onConfirmLogin(masterToken: String, authenticationToken: String) {
        gKeepService.updateMasterToken(masterToken, authenticationToken)
        isUserLoggedIn.value = true

        BasicRepository.createStorageFolder()
        SettingsRepository.createFile()
        UserDataRepository.createFile()
    }

    fun loadUserDataFromStorageFiles() {
        val userData = gKeepService.getUserDataFromStorageFile()

        notes = getMutableMapOfIdAndMutableNode(NodeUtils.getAssembledAbstractNodeList(userData.nodes))
        unsyncNotes = getMutableMapOfIdAndMutableNode(NodeUtils.getAssembledAbstractNodeList(userData.unsyncNodes))
    }

    private fun isInternetConnectionAvailable(): Boolean {
        return try {
            URL("https://www.google.com").openConnection().connect()
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun saveNote(node: AbstractNode?, refresh: Boolean = true) {
        if (node == null) {
            return
        }

        val noteToSave: MutableAbstractNode? = when (node.type) {
            NodeType.NOTE -> MutableNoteNode(node as NoteNode)
            NodeType.LIST -> MutableListNode(node as ListNode)
            else -> null
        }

        if (noteToSave != null) {
            if (notes.containsKey(node.id)) {
                notes.replace(noteToSave.id.value, noteToSave)
            } else if (unsyncNotes.containsKey(node.id)) {
                unsyncNotes.replace(noteToSave.id.value, noteToSave)
            } else {
                unsyncNotes[noteToSave.id.value] = noteToSave
            }
        }

        if (refresh) onRefresh()
    }

    private fun hideNoteEditingBoxAndNoteActions() {
        showNoteEditingBox.value = false
        showNoteActions.value = false
    }

    private fun getMutableMapOfIdAndMutableNode(notes: List<AbstractNode>): SnapshotStateMap<String, MutableAbstractNode?> {
        return mutableStateMapOf(
            *notes
                .map {
                    Pair(
                        it.id,
                        if (it.type == NodeType.NOTE)
                            MutableNoteNode(it as NoteNode)
                        else if ((it.type == NodeType.LIST))
                            MutableListNode(it as ListNode)
                        else
                            null
                    )
                }
                .toTypedArray()
        )
    }
}