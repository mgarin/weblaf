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

package com.alee.painter.decoration;

/**
 * Base decoration states used by WebLaF decoration painters.
 *
 * @author Mikle Garin
 * @see AbstractDecorationPainter
 * @see AbstractDecorationPainter#getDecorationStates()
 * @see IDecoration
 */

public interface DecorationState
{
    public static final String enabled = "enabled";
    public static final String disabled = "disabled";
    public static final String focused = "focused";
    public static final String hover = "hover";
    public static final String pressed = "pressed";
    public static final String selected = "selected";
    public static final String empty = "empty";
    public static final String collapsed = "collapsed";
    public static final String expanded = "expanded";
    public static final String dragged = "dragged";
    public static final String checked = "checked";
    public static final String mixed = "mixed";
    public static final String dropOn = "dropOn";
    public static final String dropBetween = "dropBetween";

    /**
     * Used to provide toolbar floating state for toolbar painter.
     *
     * @see com.alee.laf.toolbar.ToolBarPainter#getDecorationStates()
     */
    public static final String floating = "floating";

    /**
     * Used to provide component horizontal orientation state.
     *
     * @see com.alee.laf.progressbar.ProgressBarPainter#getDecorationStates()
     */
    public static final String horizontal = "horizontal";

    /**
     * Used to provide component vertical orientation state.
     *
     * @see com.alee.laf.progressbar.ProgressBarPainter#getDecorationStates()
     */
    public static final String vertical = "vertical";

    /**
     * Used to provide iconified state for root pane painter.
     * This state identifies that window is in iconified mode right now.
     *
     * @see com.alee.laf.rootpane.RootPanePainter#getDecorationStates()
     */
    public static final String iconified = "iconified";

    /**
     * Used to provide maximized state for root pane and internal frame painter.
     * This state identifies that window is in maximized mode right now.
     *
     * @see com.alee.laf.rootpane.RootPanePainter#getDecorationStates()
     * @see com.alee.laf.desktoppane.InternalFramePainter#getDecorationStates()
     */
    public static final String maximized = "maximized";

    /**
     * Used to provide fullscreen state for root pane painter.
     * This state identifies that window is in fullscreen mode right now.
     *
     * @see com.alee.laf.rootpane.RootPanePainter#getDecorationStates()
     */
    public static final String fullscreen = "fullscreen";

    /**
     * Used to provide progress bar type for progress bar painter.
     *
     * @see com.alee.laf.progressbar.ProgressBarPainter#getDecorationStates()
     */
    public static final String indeterminate = "indeterminate";
}