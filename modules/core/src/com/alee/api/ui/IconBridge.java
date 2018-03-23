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

package com.alee.api.ui;

import javax.swing.*;

/**
 * Base interface for any classes that can return {@link Icon} for rendering operations.
 *
 * @param <P> {@link RenderingParameters} type
 * @author Mikle Garin
 */

public interface IconBridge<P extends RenderingParameters> extends RenderingBridge
{
    /**
     * Returns {@link Icon} based on provided {@link RenderingParameters}.
     * Returning {@code null} is valid, but will in most cases lead to empty icon as even {@code null} results are not ignored.
     *
     * @param parameters {@link RenderingParameters}
     * @return {@link Icon} based on provided {@link RenderingParameters}
     */
    public Icon getIcon ( P parameters );
}