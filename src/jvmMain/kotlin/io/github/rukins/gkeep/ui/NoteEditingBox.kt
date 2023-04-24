package io.github.rukins.gkeep.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.rukins.gkeep.objects.icons.outlined.Remove
import io.github.rukins.gkeep.objects.mutable.MutableAbstractNode
import io.github.rukins.gkeep.objects.mutable.MutableListNode
import io.github.rukins.gkeep.objects.mutable.MutableNoteNode
import io.github.rukins.gkeep.viewmodel.AppViewModel
import io.github.rukins.gkeepapi.model.gkeep.node.NodeType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteEditingBox(modifier: Modifier, viewModel: AppViewModel) {
    Box(
        modifier = modifier
            .onClick {  }
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp))
            .background(
                MutableAbstractNode.getColorByType(
                    viewModel.currentEditableNote,
                    viewModel.settings.enableDarkMode.value,
                    MaterialTheme.colorScheme.secondaryContainer
                ),
                RoundedCornerShape(10.dp)
            )
    ) {
        when (viewModel.currentEditableNote.type) {
            NodeType.NOTE -> NoteNodeEditingBox(viewModel.currentEditableNote as MutableNoteNode)
            NodeType.LIST -> ListNodeEditingBox(viewModel.currentEditableNote as MutableListNode, viewModel)
            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteNodeEditingBox(noteNode: MutableNoteNode) {
    Column {
        OutlinedTextField(
            value = noteNode.title.value,
            onValueChange = { noteNode.title.value = it },
            label = { Text("Title") },
            colors = getTransparentTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outline))

        OutlinedTextField(
            value = noteNode.listItemNode.text.value,
            onValueChange = { noteNode.listItemNode.text.value = it },
            label = { Text("Text") },
            colors = getTransparentTextFieldColors(),
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp, 10.dp, 10.dp, 10.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListNodeEditingBox(listNode: MutableListNode, viewModel: AppViewModel) {
    Column {
        OutlinedTextField(
            value = listNode.title.value,
            onValueChange = { listNode.title.value = it },
            label = { Text("Title") },
            colors = getTransparentTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outline))

        Box {
            val stateVertical = rememberScrollState(0)

            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(stateVertical)
            ) {
                val newListItemText = remember { mutableStateOf("") }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        modifier = Modifier
                            .weight(0.1f),
                        onClick = {
                            viewModel.onAddListItemToList(listNode, newListItemText.value)
                            newListItemText.value = ""
                        }
                    ) {
                        Icon(
                            Icons.Outlined.Add, "Add",
                            tint = MaterialTheme.colorScheme.outline,
                        )
                    }

                    OutlinedTextField(
                        value = newListItemText.value,
                        onValueChange = { newListItemText.value = it },
                        colors = getTransparentTextFieldColors(),
                        modifier = Modifier
                            .weight(0.9f)
                            .fillMaxWidth()
                            .padding(10.dp, 10.dp, 10.dp, 10.dp)
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp))
                            .onFocusChanged {
//                                if (!it.isFocused) {
//                                    viewModel.onAddListItemToList(listNode, newListItemText.value)
//                                    newListItemText.value = ""
//                                }
                            }
                    )
                }

                listNode.listItemNodes
                    .sortedByDescending { it.sortValue.value }
                    .sortedBy { it.checked.value }
                    .forEach { listItemNode ->
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = listItemNode.checked.value,
                                onCheckedChange = { listItemNode.checked.value = !listItemNode.checked.value },
                                modifier = Modifier.weight(0.1f)
                            )

                            OutlinedTextField(
                                value = listItemNode.text.value,
                                onValueChange = { listItemNode.text.value = it },
                                colors = getTransparentTextFieldColors(),
                                modifier = Modifier
                                    .weight(0.90f)
                                    .fillMaxWidth()
                                    .padding(10.dp, 10.dp, 10.dp, 10.dp)
                                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp)),
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            viewModel.onDeleteListItemFromList(listNode, listItemNode)
                                        }
                                    ) {
                                        Icon(
                                            Icons.Outlined.Remove, "Remove",
                                            tint = MaterialTheme.colorScheme.outline,
                                        )
                                    }
                                },
                            )
                        }
                }
            }
            VerticalScrollbar(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                adapter = rememberScrollbarAdapter(stateVertical)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun getTransparentTextFieldColors() = TextFieldDefaults.textFieldColors(
    containerColor = Color.Transparent,
    disabledTextColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent
)
