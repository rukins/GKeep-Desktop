package io.github.rukins.gkeep.objects.mutable

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import io.github.rukins.gkeepapi.model.gkeep.Timestamps
import io.github.rukins.gkeepapi.model.gkeep.node.Color.*
import io.github.rukins.gkeepapi.model.gkeep.node.NodeType
import io.github.rukins.gkeepapi.model.gkeep.node.nodeobject.AbstractNode

abstract class MutableAbstractNode(node: AbstractNode) {
    val id = mutableStateOf(node.id)
    val serverId = mutableStateOf(node.serverId)
    val parentId = mutableStateOf(node.parentId)
    val parentServerId = mutableStateOf(node.parentServerId)
    val timestamps = MutableTimeStamps(node.timestamps)
    val sortValue = mutableStateOf(node.sortValue)
    val type: NodeType = node.type

    abstract fun toNode(): AbstractNode

    companion object {
        fun getColorByType(color: io.github.rukins.gkeepapi.model.gkeep.node.Color?, enableDarkMode: Boolean, default: Color): Color {
            if (color == null) {
                return default
            }

            return when(color) {
                DEFAULT -> default
                RED -> if(enableDarkMode) Color(0xFF77172e) else Color(0xFFfaafa8)
                ORANGE -> if(enableDarkMode) Color(0xFF692b16) else Color(0xFFf39f76)
                YELLOW -> if(enableDarkMode) Color(0xFF7c4a03) else Color(0xFFfff8b8)
                GREEN -> if(enableDarkMode) Color(0xFF274d3b) else Color(0xFFe2f6d3)
                TEAL -> if(enableDarkMode) Color(0xFF0e615e) else Color(0xFFb4ddd3)
                BLUE -> if(enableDarkMode) Color(0xFF246377) else Color(0xFFd4e3ed)
                CERULEAN -> if(enableDarkMode) Color(0xFF284255) else Color(0xFFaeccdc)
                PURPLE -> if(enableDarkMode) Color(0xFF472e5b) else Color(0xFFd4bfdb)
                PINK -> if(enableDarkMode) Color(0xFF6c3a4f) else Color(0xFFf6e2dd)
                BROWN -> if(enableDarkMode) Color(0xFF4b443a) else Color(0xFFe9e3d5)
                GRAY -> if(enableDarkMode) Color(0xFF232427) else Color(0xFFefeff1)
            }
        }

        fun getColorByType(mutableAbstractNode: MutableAbstractNode, enableDarkMode: Boolean, default: Color): Color {
            val color = when (mutableAbstractNode.type) {
                NodeType.NOTE -> (mutableAbstractNode as MutableNoteNode).color.value
                NodeType.LIST -> (mutableAbstractNode as MutableListNode).color.value
                else -> null
            } ?: return default

            return when(color) {
                DEFAULT -> default
                RED -> if(enableDarkMode) Color(0xFF77172e) else Color(0xFFfaafa8)
                ORANGE -> if(enableDarkMode) Color(0xFF692b16) else Color(0xFFf39f76)
                YELLOW -> if(enableDarkMode) Color(0xFF7c4a03) else Color(0xFFfff8b8)
                GREEN -> if(enableDarkMode) Color(0xFF274d3b) else Color(0xFFe2f6d3)
                TEAL -> if(enableDarkMode) Color(0xFF0e615e) else Color(0xFFb4ddd3)
                BLUE -> if(enableDarkMode) Color(0xFF246377) else Color(0xFFd4e3ed)
                CERULEAN -> if(enableDarkMode) Color(0xFF284255) else Color(0xFFaeccdc)
                PURPLE -> if(enableDarkMode) Color(0xFF472e5b) else Color(0xFFd4bfdb)
                PINK -> if(enableDarkMode) Color(0xFF6c3a4f) else Color(0xFFf6e2dd)
                BROWN -> if(enableDarkMode) Color(0xFF4b443a) else Color(0xFFe9e3d5)
                GRAY -> if(enableDarkMode) Color(0xFF232427) else Color(0xFFefeff1)
            }
        }

        fun isNotePinned(note: MutableAbstractNode?): Boolean {
            if (note == null) return false
            return when (note.type) {
                NodeType.NOTE -> (note as MutableNoteNode).isPinned.value
                NodeType.LIST -> (note as MutableListNode).isPinned.value
                else -> false
            }
        }

        fun isNoteArchived(note: MutableAbstractNode?): Boolean {
            if (note == null) return false
            return when (note.type) {
                NodeType.NOTE -> (note as MutableNoteNode).isArchived.value
                NodeType.LIST -> (note as MutableListNode).isArchived.value
                else -> false
            }
        }

        fun isNoteTrashed(note: MutableAbstractNode?): Boolean {
            if (note == null || note.timestamps.trashed.value == null) return false
            return note.timestamps.trashed.value != Timestamps.DEFAULT_LOCALDATETIME
        }

        fun isNoteDeleted(note: MutableAbstractNode?): Boolean {
            if (note == null || note.timestamps.deleted.value == null) return false
            return note.timestamps.deleted.value != Timestamps.DEFAULT_LOCALDATETIME
        }
    }
}