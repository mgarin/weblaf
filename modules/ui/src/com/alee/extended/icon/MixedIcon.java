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

package com.alee.extended.icon;

import com.alee.api.annotations.NotNull;
import com.alee.api.ui.DisabledCopySupplier;
import com.alee.api.ui.TransparentCopySupplier;
import com.alee.managers.icon.IconException;
import com.alee.utils.ImageUtils;

import javax.swing.*;

/**
 * Abstract class that can be used as a bae for any {@link Icon} that mixes other {@link Icon}s together.
 * It provides a few utility methods and convenient constructors
 *
 * @param <I> {@link MixedIcon} type
 * @author Mikle Garin
 */
public abstract class MixedIcon<I extends MixedIcon<I>> implements Icon, DisabledCopySupplier<I>, TransparentCopySupplier<I>
{
    /**
     * Mixed {@link Icon}s.
     */
    @NotNull
    protected final Icon[] icons;

    /**
     * Constructs new {@link MixedIcon}.
     *
     * @param icons {@link Icon}s to mix
     */
    public MixedIcon ( @NotNull final Icon... icons )
    {
        if ( icons.length <= 1 )
        {
            throw new IconException ( "At least 2 icons or more should be provided" );
        }
        this.icons = icons;
    }

    /**
     * Returns new {@link MixedIcon} instance.
     *
     * @param icons {@link Icon}s to mix
     * @return new {@link MixedIcon} instance
     */
    protected abstract I newInstance ( @NotNull Icon... icons );

    /**
     * Returns maximum width of the specified {@link Icon}s.
     *
     * @param icons {@link Icon} to process
     * @return maximum width of the specified {@link Icon}s
     */
    protected int maxWidth ( @NotNull final Icon... icons )
    {
        int maxWidth = 0;
        for ( final Icon icon : icons )
        {
            maxWidth = Math.max ( maxWidth, icon.getIconWidth () );
        }
        return maxWidth;
    }

    /**
     * Returns maximum height of the specified {@link Icon}s.
     *
     * @param icons {@link Icon} to process
     * @return maximum height of the specified {@link Icon}s
     */
    protected int maxHeight ( @NotNull final Icon... icons )
    {
        int maxHeight = 0;
        for ( final Icon icon : icons )
        {
            maxHeight = Math.max ( maxHeight, icon.getIconHeight () );
        }
        return maxHeight;
    }

    /**
     * Returns new {@link MixedIcon} with disabled copies of originally mixed {@link Icon}s.
     *
     * @return new {@link MixedIcon} with disabled copies of originally mixed {@link Icon}s
     */
    @Override
    @NotNull
    public I createDisabledCopy ()
    {
        final Icon[] disabled = new Icon[ icons.length ];
        for ( int i = 0; i < icons.length; i++ )
        {
            disabled[ i ] = ImageUtils.getDisabledCopy ( icons[ i ] );
        }
        return newInstance ( disabled );
    }

    /**
     * Returns new {@link MixedIcon} with semi-transparent copies of originally mixed {@link Icon}s.
     *
     * @return new {@link MixedIcon} with semi-transparent copies of originally mixed {@link Icon}s
     */
    @Override
    @NotNull
    public I createTransparentCopy ( final float opacity )
    {
        final Icon[] transparent = new Icon[ icons.length ];
        for ( int i = 0; i < icons.length; i++ )
        {
            transparent[ i ] = ImageUtils.getTransparentCopy ( icons[ i ], opacity );
        }
        return newInstance ( transparent );
    }
}