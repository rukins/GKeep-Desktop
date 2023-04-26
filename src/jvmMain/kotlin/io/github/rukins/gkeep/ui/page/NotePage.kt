package io.github.rukins.gkeep.ui.page

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.rukins.gkeep.objects.mutable.MutableAbstractNode
import io.github.rukins.gkeep.objects.mutable.MutableAbstractNode.Companion.getColorByType
import io.github.rukins.gkeep.objects.mutable.MutableAbstractNode.Companion.isNoteArchived
import io.github.rukins.gkeep.objects.mutable.MutableAbstractNode.Companion.isNotePinned
import io.github.rukins.gkeep.objects.mutable.MutableAbstractNode.Companion.isNoteTrashed
import io.github.rukins.gkeep.objects.mutable.MutableListNode
import io.github.rukins.gkeep.objects.mutable.MutableNoteNode
import io.github.rukins.gkeep.ui.NavigationElement
import io.github.rukins.gkeep.ui.NoteActions
import io.github.rukins.gkeep.viewmodel.AppViewModel
import io.github.rukins.gkeepapi.model.gkeep.node.NodeType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NotePage(viewModel: AppViewModel) {
    val scope = rememberCoroutineScope()
    val lazyGridState = rememberLazyGridState()

    val allNotes = viewModel.notes.values + viewModel.unsyncNotes.values

    val notes = when (viewModel.selectedNavigationElement.value) {
        NavigationElement.NOTES -> allNotes.filter { !isNoteTrashed(it) && !isNoteArchived(it) }
        NavigationElement.ARCHIVE -> allNotes.filter { !isNoteTrashed(it) && isNoteArchived(it) }
        NavigationElement.TRASH -> allNotes.filter { isNoteTrashed(it) }
        else -> emptyList<MutableAbstractNode>()
    }.sortedByDescending { it?.sortValue?.value }

    if (notes.isNotEmpty())
        viewModel.lastSortValue.value = notes.first()?.sortValue?.value.toString()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .absolutePadding(if (viewModel.showNavigationBar.value) 80.dp else 20.dp, 70.dp, 5.dp, 20.dp)
    ) {
        LazyVerticalGrid(
            modifier = Modifier.padding(0.dp, 0.dp, 15.dp, 0.dp),
            columns = GridCells.Adaptive(300.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            state = lazyGridState
        ) {
            items(notes.filter { isNotePinned(it) }) { note ->
                when (note?.type) {
                    NodeType.NOTE -> NoteCard(note as MutableNoteNode, viewModel, scope)
                    NodeType.LIST -> ListCard(note as MutableListNode, viewModel, scope)
                    else -> {}
                }
            }

            items(notes.filterNot { isNotePinned(it) }) {note ->
                when (note?.type) {
                    NodeType.NOTE -> NoteCard(note as MutableNoteNode, viewModel, scope)
                    NodeType.LIST -> ListCard(note as MutableListNode, viewModel, scope)
                    else -> {}
                }
            }
        }

        VerticalScrollbar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = lazyGridState
            ),
            style = ScrollbarStyle(
                minimalHeight = 16.dp,
                thickness = 8.dp,
                shape = RoundedCornerShape(5.dp),
                hoverDurationMillis = 300,
                unhoverColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                hoverColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun NoteCard(note: MutableNoteNode, viewModel: AppViewModel, scope: CoroutineScope) {
    Card(
        modifier = Modifier
            .sizeIn(0.dp, 50.dp)
            .combinedClickable {
                scope.launch { viewModel.onClickEditOrCreateNote(note) }
            }
            .onPointerEvent(PointerEventType.Enter) {
                scope.launch { viewModel.onPointerEventEnterOrExitNoteField(note) }
            }
            .onPointerEvent(PointerEventType.Exit) {
                scope.launch { viewModel.onPointerEventEnterOrExitNoteField() }
            },
        border = BorderStroke(if (note.isPinned.value) 5.dp else 1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(
            getColorByType(note.color.value, viewModel.settings.enableDarkMode.value, MaterialTheme.colorScheme.secondaryContainer),
            MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Text(
            note.title.value,
            modifier = Modifier.padding(10.dp),
            fontWeight = FontWeight.Bold
        )

        val text = note.listItemNode.text.value
        Text(
            if (text.length < 200) text else text.substring(0, 200) + "\n...",
            modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp)
        )

        if (viewModel.showNoteActions.value && note.id == viewModel.currentEditableNote.id) {
            NoteActions(Modifier.align(Alignment.CenterHorizontally), viewModel)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ListCard(list: MutableListNode, viewModel: AppViewModel, scope: CoroutineScope) {
    Card(
        modifier = Modifier
            .sizeIn(0.dp, 50.dp)
            .combinedClickable {
                scope.launch { viewModel.onClickEditOrCreateNote(list) }
            }
            .onPointerEvent(PointerEventType.Enter) {
                scope.launch { viewModel.onPointerEventEnterOrExitNoteField(list) }
            }
            .onPointerEvent(PointerEventType.Exit) {
                scope.launch { viewModel.onPointerEventEnterOrExitNoteField() }
            },
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(
            getColorByType(list.color.value, viewModel.settings.enableDarkMode.value, MaterialTheme.colorScheme.secondaryContainer),
            MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Text(
            list.title.value,
            modifier = Modifier.padding(10.dp),
            fontWeight = FontWeight.Bold
        )

        list.listItemNodes.sortedByDescending { it.sortValue.value }.sortedBy { it.checked.value }

        val uncheckedListItems = list.listItemNodes
            .filter { !it.checked.value }
            .sortedByDescending { it.sortValue.value }
            .sortedBy { it.checked.value }
            .toList()

        val checkedListItemsCount = list.listItemNodes.size - uncheckedListItems.size

        uncheckedListItems.forEach { n ->
            Text(
                n.text.value,
                modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 5.dp)
            )
        }

        if (checkedListItemsCount != 0) {
            Text(
                "+ $checkedListItemsCount checked item" + if (checkedListItemsCount > 1) "s" else "",
                modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 5.dp).alpha(0.7f)
            )
        }

        if (viewModel.showNoteActions.value && list.id == viewModel.currentEditableNote.id) {
            NoteActions(Modifier.align(Alignment.CenterHorizontally), viewModel)
        }
    }
}
