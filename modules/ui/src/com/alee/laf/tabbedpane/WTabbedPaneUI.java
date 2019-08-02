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

package com.alee.laf.tabbedpane;

import com.alee.painter.SectionPainter;

import javax.swing.text.View;
import java.awt.*;
import java.util.Vector;

/**
 * Pluggable look and feel class for {@link WebTabbedPane} component.
 *
 * @author Mikle Garin
 */
public abstract class WTabbedPaneUI extends WebBasicTabbedPaneUI
{
    public abstract TabbedPaneStyle getTabbedPaneStyle ();

    public abstract void setTabbedPaneStyle ( TabbedPaneStyle tabbedPaneStyle );

    public abstract TabStretchType getTabStretchType ();

    public abstract void setTabStretchType ( TabStretchType tabStretchType );

    //

    public abstract Vector<View> getHtmlViews ();

    public abstract ScrollableTabSupport getTabScroller ();

    public abstract int[] getTabRuns ();

    public abstract Rectangle[] getRects ();

    public abstract int getMaxTabHeight ();

    public abstract int getMaxTabWidth ();

    public abstract SectionPainter getBackgroundPainterAt ( int tabIndex );
}