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

public val Icons.Outlined.More: ImageVector
    get() {
        if (_more != null) {
            return _more!!
        }
        _more = materialIcon(name = "Outlined.More") {
            materialPath {
                moveTo(22.0f, 3.0f)
                lineTo(7.0f, 3.0f)
                curveToRelative(-0.69f, 0.0f, -1.23f, 0.35f, -1.59f, 0.88f)
                lineTo(0.0f, 12.0f)
                lineToRelative(5.41f, 8.11f)
                curveToRelative(0.36f, 0.53f, 0.97f, 0.89f, 1.66f, 0.89f)
                lineTo(22.0f, 21.0f)
                curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
                lineTo(24.0f, 5.0f)
                curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
                close()
                moveTo(22.0f, 19.0f)
                lineTo(7.07f, 19.0f)
                lineTo(2.4f, 12.0f)
                lineToRelative(4.66f, -7.0f)
                lineTo(22.0f, 5.0f)
                verticalLineToRelative(14.0f)
                close()
            }
            materialPath {
                moveTo(9.0f, 12.0f)
                moveToRelative(-1.5f, 0.0f)
                arcToRelative(1.5f, 1.5f, 0.0f, true, true, 3.0f, 0.0f)
                arcToRelative(1.5f, 1.5f, 0.0f, true, true, -3.0f, 0.0f)
            }
            materialPath {
                moveTo(14.0f, 12.0f)
                moveToRelative(-1.5f, 0.0f)
                arcToRelative(1.5f, 1.5f, 0.0f, true, true, 3.0f, 0.0f)
                arcToRelative(1.5f, 1.5f, 0.0f, true, true, -3.0f, 0.0f)
            }
            materialPath {
                moveTo(19.0f, 12.0f)
                moveToRelative(-1.5f, 0.0f)
                arcToRelative(1.5f, 1.5f, 0.0f, true, true, 3.0f, 0.0f)
                arcToRelative(1.5f, 1.5f, 0.0f, true, true, -3.0f, 0.0f)
            }
        }
        return _more!!
    }

private var _more: ImageVector? = null
