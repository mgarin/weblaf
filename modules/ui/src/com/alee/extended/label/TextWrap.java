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

package com.alee.extended.label;

/**
 * Enumeration representing {@link AbstractStyledTextContent} wrap types.
 *
 * @author Alexandr Zernov
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebStyledLabel">How to use WebStyledLabel</a>
 */
public enum TextWrap
{
    /**
     * Don't use wrapping.
     * Styled text will be displayed as it is.
     */
    none,

    /**
     * Character wrap.
     * Each character reaching width limit will be wrapped.
     */
    character,

    /**
     * Word wrap.
     * Each word reaching width limit will be wrapped.
     */
    word,

    /**
     * Word over character wrap.
     * Each word reaching width limit will be wrapped.
     * If a single word doesn't fit into width limit it will be wrapped on per-character basis.
     */
    mixed
}