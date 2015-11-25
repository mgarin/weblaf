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

package com.alee.managers.style.skin.web;

import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.focus.FocusTracker;
import com.alee.managers.style.PainterShapeProvider;
import com.alee.painter.PartialDecoration;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Abstract web-style decoration painter implementation for usage within specific component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public abstract class WebDecorationPainter<E extends JComponent, U extends ComponentUI> extends AbstractDecorationPainter<E, U>
        implements PainterShapeProvider<E>, PartialDecoration
{
    /**
     * Listeners.
     */
    protected FocusTracker focusTracker;

    /**
     * Runtime variables.
     */
    protected boolean focused = false;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Installing FocusTracker to keep an eye on focused state
        focusTracker = new DefaultFocusTracker ()
        {
            @Override
            public boolean isTrackingEnabled ()
            {
                return !undecorated && paintFocus;
            }

            @Override
            public void focusChanged ( final boolean focused )
            {
                WebDecorationPainter.this.focused = focused;
                repaint ();
            }
        };
        FocusManager.addFocusTracker ( c, focusTracker );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        FocusManager.removeFocusTracker ( focusTracker );
        focusTracker = null;

        super.uninstall ( c, ui );
    }

    @Override
    public Insets getBorders ()
    {
        if ( undecorated )
        {
            // Empty borders
            return null;
        }
        else
        {
            // Decoration border
            // todo Return larger border if shade image is used?
            final int spacing = shadeWidth + 1;
            final int top = paintTop ? spacing : paintTopLine ? 1 : 0;
            final int left = paintLeft ? spacing : paintLeftLine ? 1 : 0;
            final int bottom = paintBottom ? spacing : paintBottomLine ? 1 : 0;
            final int right = paintRight ? spacing : paintRightLine ? 1 : 0;
            return new Insets ( top, left, bottom, right );
        }
    }

    @Override
    protected boolean isFocused ()
    {
        return focused;
    }
}