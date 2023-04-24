package io.github.rukins.gkeep.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import io.github.rukins.gkeep.objects.Settings
import io.github.rukins.gkeep.objects.mutable.*
import io.github.rukins.gkeep.service.GKeepService
import io.github.rukins.gkeep.ui.NavigationElement
import io.github.rukins.gkeepapi.model.gkeep.node.NodeType
import io.github.rukins.gkeepapi.model.gkeep.node.nodeobject.AbstractNode
import io.github.rukins.gkeepapi.model.gkeep.node.nodeobject.ListNode
import io.github.rukins.gkeepapi.model.gkeep.node.nodeobject.NoteNode
import io.github.rukins.gkeepapi.utils.NodeUtils
import java.net.URL

class AppViewModel(
    settings: Settings, notes: List<AbstractNode>, unsyncNotes: List<AbstractNode> = listOf()
) {
    val gKeepService = GKeepService(settings.masterToken, settings.currentVersion)

    var settings = MutableSettings(settings)

    var notes = getMutableMapOfIdAndMutableNode(notes)

    var unsyncNotes = getMutableMapOfIdAndMutableNode(unsyncNotes)

    var currentEditableNote: MutableAbstractNode = MutableNoteNode(gKeepService.newDefaultNoteNode())

    val lastSortValue = mutableStateOf("0")

    val selectedNavigationElement = mutableStateOf(NavigationElement.NOTES)

    val showNavigationBar = mutableStateOf(true)

    val showNoteEditingBox = mutableStateOf(false)

    val showNoteActions = mutableStateOf(false)

    fun onRefresh() {
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

    fun onTrashNote() {
        hideNoteEditingBoxAndNoteActions()
        saveNote(gKeepService.trashNode(currentEditableNote.toNode()))
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
//        settings.currentVersion = gKeepService.getCurrentVersion()
//        SettingsRepository().save(settings.toSettings())
    }

    fun loadDataFromStorageFiles() {
        notes = getMutableMapOfIdAndMutableNode(gKeepService.getAllNodesFromStorageFile())
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

    private fun getMutableMapOfIdAndMutableNode(notes: List<AbstractNode>): MutableMap<String, MutableAbstractNode?> {
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