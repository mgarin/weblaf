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

package com.alee.painter;

/**
 * Painter listener interface.
 *
 * @author Mikle Garin
 */

public interface PainterListener
{
    /**
     * Called when painter visual representation changes.
     */
    public void repaint ();

    /**
     * Called when part of painter visual representation changes.
     *
     * @param x      part bounds X coordinate
     * @param y      part bounds Y coordinate
     * @param width  part bounds width
     * @param height part bounds height
     */
    public void repaint ( int x, int y, int width, int height );

    /**
     * Called when painter preferred size or margin changes.
     * This call will usually cause component border update.
     */
    public void revalidate ();

    /**
     * Called when painter requires component opacity to be updated.
     * Make sure you know what you are doing in case you are modifying opacity in runtime.
     */
    public void updateOpacity ();
}