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

public val Icons.Outlined.Label: ImageVector
    get() {
        if (_label != null) {
            return _label!!
        }
        _label = materialIcon(name = "Outlined.Label") {
            materialPath {
                moveTo(17.63f, 5.84f)
                curveTo(17.27f, 5.33f, 16.67f, 5.0f, 16.0f, 5.0f)
                lineTo(5.0f, 5.01f)
                curveTo(3.9f, 5.01f, 3.0f, 5.9f, 3.0f, 7.0f)
                verticalLineToRelative(10.0f)
                curveToRelative(0.0f, 1.1f, 0.9f, 1.99f, 2.0f, 1.99f)
                lineTo(16.0f, 19.0f)
                curveToRelative(0.67f, 0.0f, 1.27f, -0.33f, 1.63f, -0.84f)
                lineTo(22.0f, 12.0f)
                lineToRelative(-4.37f, -6.16f)
                close()
                moveTo(16.0f, 17.0f)
                horizontalLineTo(5.0f)
                verticalLineTo(7.0f)
                horizontalLineToRelative(11.0f)
                lineToRelative(3.55f, 5.0f)
                lineTo(16.0f, 17.0f)
                close()
            }
        }
        return _label!!
    }

private var _label: ImageVector? = null
