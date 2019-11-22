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

package com.alee.painter.common;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.style.Bounds;
import com.alee.painter.AbstractPainter;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * {@link NinePatchIcon} painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 * @see com.alee.utils.ninepatch.NinePatchIcon
 * @see com.alee.painter.AbstractPainter
 * @see com.alee.painter.Painter
 */
public class NinePatchIconPainter<C extends JComponent, U extends ComponentUI> extends AbstractPainter<C, U>
{
    /**
     * {@link NinePatchIcon} to paint.
     */
    @Nullable
    protected NinePatchIcon icon;

    /**
     * Constructs new {@link NinePatchIconPainter}.
     */
    public NinePatchIconPainter ()
    {
        this ( null );
    }

    /**
     * Constructs new {@link NinePatchIconPainter}.
     *
     * @param icon {@link NinePatchIcon} to paint
     */
    public NinePatchIconPainter ( @Nullable final NinePatchIcon icon )
    {
        setNinePatchIcon ( icon );
    }

    /**
     * Returns painted {@link NinePatchIcon}.
     *
     * @return painted {@link NinePatchIcon}
     */
    @Nullable
    public NinePatchIcon getNinePatchIcon ()
    {
        return icon;
    }

    /**
     * Sets painted {@link NinePatchIcon}.
     *
     * @param icon painted {@link NinePatchIcon}
     */
    public void setNinePatchIcon ( @Nullable final NinePatchIcon icon )
    {
        if ( this.icon != null && isInstalled () )
        {
            this.icon.setComponent ( null );
        }
        this.icon = icon;
        if ( this.icon != null && isInstalled () )
        {
            this.icon.setComponent ( component );
        }
        updateAll ();
    }

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();
        installIconComponent ();
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallIconComponent ();
        super.uninstallPropertiesAndListeners ();
    }

    /**
     * Attaches {@link NinePatchIcon} to {@link JComponent} used by this UI.
     */
    protected void installIconComponent ()
    {
        if ( icon != null )
        {
            icon.setComponent ( component );
        }
    }

    /**
     * Detaches {@link NinePatchIcon} from {@link JComponent} used by this UI.
     */
    protected void uninstallIconComponent ()
    {
        if ( icon != null )
        {
            icon.setComponent ( null );
        }
    }

    @Nullable
    @Override
    protected Insets getBorder ()
    {
        return icon != null ? icon.getMargin () : null;
    }

    @Override
    public void paint ( @NotNull final Graphics2D g2d, @NotNull final C c, @NotNull final U ui, @NotNull final Bounds bounds )
    {
        if ( icon != null )
        {
            icon.paintIcon ( g2d, bounds.get () );
        }
    }

    @NotNull
    @Override
    public Dimension getPreferredSize ()
    {
        return icon != null ? icon.getPreferredSize () : super.getPreferredSize ();
    }
}