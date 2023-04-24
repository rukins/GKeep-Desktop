package io.github.rukins.gkeep.objects.mutable

import androidx.compose.runtime.mutableStateOf
import io.github.rukins.gkeepapi.model.gkeep.node.LabelId

class MutableLabelId(labelId: LabelId) {
    val labelId = mutableStateOf(labelId.labelId)

    fun toLabelId(): LabelId {
        return LabelId(labelId.value)
    }
}