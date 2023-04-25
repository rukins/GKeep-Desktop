package io.github.rukins.gkeep.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import io.github.rukins.gkeep.objects.icons.filled.PushPin
import io.github.rukins.gkeep.objects.icons.outlined.*
import io.github.rukins.gkeep.objects.mutable.MutableAbstractNode
import io.github.rukins.gkeep.viewmodel.AppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteActions(modifier: Modifier, viewModel: AppViewModel) {
    val scope = rememberCoroutineScope()

    val backgroundModifier = Modifier
        .background(
            MutableAbstractNode.getColorByType(
                viewModel.currentEditableNote,
                viewModel.settings.enableDarkMode.value,
                MaterialTheme.colorScheme.secondaryContainer
            ),
            RoundedCornerShape(50.dp)
        )

    Row(
        modifier = modifier
            .onClick {  }
            .padding(0.dp, 0.dp, 10.dp, 0.dp)
            .then(backgroundModifier)
    ) {
        NoteActionIconButton(
            backgroundModifier,
            if (MutableAbstractNode.isNotePinned(viewModel.currentEditableNote)) Icons.Filled.PushPin else Icons.Outlined.PushPin,
            if (MutableAbstractNode.isNotePinned(viewModel.currentEditableNote)) "Unpin" else "Pin"
        ) {
            scope.launch(Dispatchers.IO) { viewModel.onPinOrUnpinNote() }
        }
        NoteActionIconButton(
            backgroundModifier,
            if (MutableAbstractNode.isNoteArchived(viewModel.currentEditableNote)) Icons.Outlined.Unarchive else Icons.Outlined.Archive,
            if (MutableAbstractNode.isNoteArchived(viewModel.currentEditableNote)) "Unarchive" else "Archive"
        ) {
            scope.launch(Dispatchers.IO) { viewModel.onArchiveOrUnArchiveNote() }
        }
        NoteActionIconButton(
            backgroundModifier,
            if (MutableAbstractNode.isNoteTrashed(viewModel.currentEditableNote)) Icons.Outlined.Restore else Icons.Outlined.Delete,
            if (MutableAbstractNode.isNoteTrashed(viewModel.currentEditableNote)) "Restore" else "Move to Trash"
        ) {
            scope.launch(Dispatchers.IO) { viewModel.onTrashOrRestoreNote() }
        }
        NoteActionIconButton(backgroundModifier, Icons.Outlined.CheckBox, "CheckBox") {

        }
        NoteActionIconButton(backgroundModifier, Icons.Outlined.More, "More") {

        }
//        NoteActionIconButton(backgroundModifier, Icons.Outlined.Image, "Add image") {
//
//        }
//        NoteActionIconButton(backgroundModifier, Icons.Outlined.Colorize, "Colorize") {
//
//        }
//        NoteActionIconButton(backgroundModifier, Icons.Outlined.ContentCopy, "ContentCopy") {
//
//        }
//        NoteActionIconButton(backgroundModifier, Icons.Outlined.ContentCopy, "ContentCopy") {
//
//        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NoteActionIconButton(backgroundModifier: Modifier, imageVector: ImageVector, tooltipText: String, onClick: () -> Unit) {
    TooltipArea(
        tooltip = {
            Box(
                modifier = Modifier
                    .onClick {  }
                    .then(backgroundModifier)
            ) {
                Text(
                    tooltipText,
                    modifier = Modifier.padding(5.dp, 0.dp),
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        tooltipPlacement = TooltipPlacement.CursorPoint(
            alignment = Alignment.TopCenter,
            offset = DpOffset(0.dp, (-10).dp)
        )
    ) {
        IconButton(
            onClick = onClick,
        ) {
            Icon(imageVector, imageVector.name, tint = MaterialTheme.colorScheme.outline)
        }
    }
}