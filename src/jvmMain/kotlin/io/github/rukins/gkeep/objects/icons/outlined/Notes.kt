/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.rukins.gkeep.objects.icons.outlined

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Outlined.Notes: ImageVector
    get() {
        if (_notes != null) {
            return _notes!!
        }
        _notes = materialIcon(name = "Outlined.Notes") {
            materialPath {
                moveTo(21.0f, 11.01f)
                lineTo(3.0f, 11.0f)
                verticalLineToRelative(2.0f)
                horizontalLineToRelative(18.0f)
                close()
                moveTo(3.0f, 16.0f)
                horizontalLineToRelative(12.0f)
                verticalLineToRelative(2.0f)
                horizontalLineTo(3.0f)
                close()
                moveTo(21.0f, 6.0f)
                horizontalLineTo(3.0f)
                verticalLineToRelative(2.01f)
                lineTo(21.0f, 8.0f)
                close()
            }
        }
        return _notes!!
    }

private var _notes: ImageVector? = null
