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

public val Icons.Outlined.Unarchive: ImageVector
    get() {
        if (_unarchive != null) {
            return _unarchive!!
        }
        _unarchive = materialIcon(name = "Outlined.Unarchive") {
            materialPath {
                moveTo(20.54f, 5.23f)
                lineToRelative(-1.39f, -1.68f)
                curveTo(18.88f, 3.21f, 18.47f, 3.0f, 18.0f, 3.0f)
                lineTo(6.0f, 3.0f)
                curveToRelative(-0.47f, 0.0f, -0.88f, 0.21f, -1.16f, 0.55f)
                lineTo(3.46f, 5.23f)
                curveTo(3.17f, 5.57f, 3.0f, 6.02f, 3.0f, 6.5f)
                lineTo(3.0f, 19.0f)
                curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
                horizontalLineToRelative(14.0f)
                curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
                lineTo(21.0f, 6.5f)
                curveToRelative(0.0f, -0.48f, -0.17f, -0.93f, -0.46f, -1.27f)
                close()
                moveTo(6.24f, 5.0f)
                horizontalLineToRelative(11.52f)
                lineToRelative(0.83f, 1.0f)
                lineTo(5.42f, 6.0f)
                lineToRelative(0.82f, -1.0f)
                close()
                moveTo(5.0f, 19.0f)
                lineTo(5.0f, 8.0f)
                horizontalLineToRelative(14.0f)
                verticalLineToRelative(11.0f)
                lineTo(5.0f, 19.0f)
                close()
                moveTo(8.0f, 14.0f)
                horizontalLineToRelative(2.55f)
                verticalLineToRelative(3.0f)
                horizontalLineToRelative(2.9f)
                verticalLineToRelative(-3.0f)
                lineTo(16.0f, 14.0f)
                lineToRelative(-4.0f, -4.0f)
                close()
            }
        }
        return _unarchive!!
    }

private var _unarchive: ImageVector? = null
