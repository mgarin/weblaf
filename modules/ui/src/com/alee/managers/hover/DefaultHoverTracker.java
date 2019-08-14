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

package com.alee.managers.hover;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Default {@link HoverTracker} implementation for {@link HoverTracker} usage convenience.
 * This implementation also offers customization of additional hoverable {@link Component}s attached to tracked {@link JComponent}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-HoverManager">How to use HoverManager</a>
 * @see com.alee.managers.hover.HoverManager
 */
public abstract class DefaultHoverTracker implements HoverTracker
{
    /**
     * Tracked {@link JComponent}.
     */
    @NotNull
    protected final JComponent component;

    /**
     * Whether or not tracking is currently enabled.
     */
    protected boolean enabled;

    /**
     * Whether or not tracked {@link JComponent} is currently hovered according to this tracker settings.
     */
    protected boolean hovered;

    /**
     * Constructs new {@link DefaultHoverTracker}.
     *
     * @param component tracked {@link JComponent}
     */
    public DefaultHoverTracker ( @NotNull final JComponent component )
    {
        this.component = component;
        this.enabled = true;
        this.hovered = isInvolved ( component, HoverManager.getHoverOwner () );
    }

    @Override
    public boolean isEnabled ()
    {
        return enabled && component.isShowing ();
    }

    @Override
    public void setEnabled ( final boolean enabled )
    {
        this.enabled = enabled;
    }

    @Override
    public boolean isHovered ()
    {
        return hovered;
    }

    @Override
    public void setHovered ( final boolean hovered )
    {
        this.hovered = hovered;
    }

    @Override
    public boolean isInvolved ( @NotNull final JComponent tracked, @Nullable final Component component )
    {
        return tracked == component || CoreSwingUtils.isAncestorOf ( tracked, component );
    }
}