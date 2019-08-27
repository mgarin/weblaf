/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.extended.ninepatch.skin;

import com.alee.managers.style.ChildStyleId;
import com.alee.managers.style.StyleId;

/**
 * @author Mikle Garin
 */
public final class NinePatchEditorStyles
{
    /**
     * {@link com.alee.extended.ninepatch.NinePatchEditor} style identifiers.
     */
    public static final StyleId ninepatcheditor = StyleId.of ( "ninepatcheditor" );
    public static final ChildStyleId ninepatcheditorToolbar = ChildStyleId.of ( "toolbar" );
    public static final ChildStyleId ninepatcheditorZoomSlider = ChildStyleId.of ( "zoom" );
    public static final ChildStyleId ninepatcheditorPreviewField = ChildStyleId.of ( "preview" );
    public static final ChildStyleId ninepatcheditorPreviewBackground = ChildStyleId.of ( "preview-background" );
}