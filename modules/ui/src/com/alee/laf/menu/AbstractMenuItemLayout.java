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

package com.alee.laf.menu;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.layout.AbstractContentLayout;
import com.alee.painter.decoration.layout.ContentLayoutData;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract implementation of simple menu item layout.
 * It only paints contents placed under {@link #ICON}, {@link #TEXT}, {@link #ACCELERATOR} and {@link #ARROW} constraints.
 * It doesn't use {@link JMenuItem} as base type to allow other custom menu items to use this layout if necessary.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> layout type
 * @author Mikle Garin
 */
public abstract class AbstractMenuItemLayout<C extends JComponent, D extends IDecoration<C, D>, I extends AbstractMenuItemLayout<C, D, I>>
        extends AbstractContentLayout<C, D, I>
{
    /**
     * Layout constraints.
     */
    protected static final String STATE_ICON = "state-icon";
    protected static final String ICON = "icon";
    protected static final String TEXT = "text";
    protected static final String ACCELERATOR = "accelerator";
    protected static final String ARROW = "arrow";

    /**
     * Gap between state icon and icon contents.
     */
    @Nullable
    @XStreamAsAttribute
    protected Integer stateIconGap;

    /**
     * Gap between icon and text contents.
     */
    @Nullable
    @XStreamAsAttribute
    protected Integer iconTextGap;

    /**
     * Gap between text and accelerator contents.
     */
    @Nullable
    @XStreamAsAttribute
    protected Integer textAcceleratorGap;

    /**
     * Gap between text and arrow contents.
     */
    @Nullable
    @XStreamAsAttribute
    protected Integer textArrowGap;

    /**
     * Returns whether or not menu items text should be aligned by maximum icon size.
     *
     * @param c {@link JComponent} that is being painted
     * @param d {@link IDecoration} state
     * @return true if menu items text should be aligned by maximum icon size, false otherwise
     */
    protected abstract boolean isAlignTextByIcons ( @NotNull C c, @NotNull D d );

    /**
     * Returns {@link PopupMenuIcons} information.
     *
     * @param c {@link JComponent} that is being painted
     * @param d {@link IDecoration} state
     * @return {@link PopupMenuIcons} information
     */
    @NotNull
    protected abstract PopupMenuIcons getPopupMenuIcons ( @NotNull C c, @NotNull D d );

    /**
     * Returns gap between state and icon contents.
     *
     * @param c {@link JComponent} that is being painted
     * @param d {@link IDecoration} state
     * @return gap between state and icon contents
     */
    protected int getStateIconGap ( @NotNull final C c, @NotNull final D d )
    {
        return stateIconGap != null ? stateIconGap : getIconTextGap ( c, d );
    }

    /**
     * Returns gap between icon and text contents.
     *
     * @param c {@link JComponent} that is being painted
     * @param d {@link IDecoration} state
     * @return gap between icon and text contents
     */
    protected int getIconTextGap ( @NotNull final C c, @NotNull final D d )
    {
        return iconTextGap != null ? iconTextGap : 0;
    }

    /**
     * Returns between text and accelerator contents.
     *
     * @param c {@link JComponent} that is being painted
     * @param d {@link IDecoration} state
     * @return between text and accelerator contents
     */
    protected int getTextAcceleratorGap ( @NotNull final C c, @NotNull final D d )
    {
        return textAcceleratorGap != null ? textAcceleratorGap : 0;
    }

    /**
     * Returns between text and arrow contents.
     *
     * @param c {@link JComponent} that is being painted
     * @param d {@link IDecoration} state
     * @return between text and arrow contents
     */
    protected int getTextArrowGap ( @NotNull final C c, @NotNull final D d )
    {
        return textArrowGap != null ? textArrowGap : 0;
    }

    @NotNull
    @Override
    public ContentLayoutData layoutContent ( @NotNull final C c, @NotNull final D d, @NotNull final Rectangle bounds )
    {
        final ContentLayoutData layoutData = new ContentLayoutData ( 4 );
        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        final Dimension available = new Dimension ( bounds.width, bounds.height );
        int x = ltr ? bounds.x : bounds.x + bounds.width;
        final boolean alignTextByIcons = isAlignTextByIcons ( c, d );
        final boolean hasStateIcon = !isEmpty ( c, d, STATE_ICON );
        final boolean hasIcon = !isEmpty ( c, d, ICON );
        final PopupMenuIcons popupMenuIcons = getPopupMenuIcons ( c, d );
        if ( hasStateIcon || alignTextByIcons && popupMenuIcons.hasBothIcons )
        {
            final Dimension ips = getPreferredSize ( c, d, available, STATE_ICON );
            if ( alignTextByIcons )
            {
                ips.width = Math.max ( ips.width, popupMenuIcons.maxStateIconWidth );
                if ( !popupMenuIcons.hasBothIcons )
                {
                    ips.width = Math.max ( ips.width, popupMenuIcons.maxIconWidth );
                }
            }
            x += ltr ? 0 : -ips.width;
            if ( hasStateIcon )
            {
                layoutData.put ( STATE_ICON, new Rectangle ( x, bounds.y, ips.width, bounds.height ) );
            }
            final int stateIconGap = getStateIconGap ( c, d );
            x += ltr ? ips.width + stateIconGap : -stateIconGap;
            available.width -= ips.width + stateIconGap;
        }
        if ( hasIcon || alignTextByIcons && popupMenuIcons.hasBothIcons )
        {
            final Dimension ips = getPreferredSize ( c, d, available, ICON );
            if ( alignTextByIcons )
            {
                ips.width = Math.max ( ips.width, popupMenuIcons.maxIconWidth );
                if ( !popupMenuIcons.hasBothIcons )
                {
                    ips.width = Math.max ( ips.width, popupMenuIcons.maxStateIconWidth );
                }
            }
            x += ltr ? 0 : -ips.width;
            if ( hasIcon )
            {
                layoutData.put ( ICON, new Rectangle ( x, bounds.y, ips.width, bounds.height ) );
            }
            final int iconTextGap = getIconTextGap ( c, d );
            x += ltr ? ips.width + iconTextGap : -iconTextGap;
            available.width -= ips.width + iconTextGap;
        }
        if ( !hasStateIcon && !hasIcon && alignTextByIcons && popupMenuIcons.hasAnyIcons && !popupMenuIcons.hasBothIcons )
        {
            final int iconTextGap = getIconTextGap ( c, d );
            final int maxIcon = Math.max ( popupMenuIcons.maxStateIconWidth, popupMenuIcons.maxIconWidth );
            x += ltr ? maxIcon + iconTextGap : -iconTextGap;
            available.width -= maxIcon + iconTextGap;
        }
        if ( !isEmpty ( c, d, ARROW ) )
        {
            final Dimension aps = getPreferredSize ( c, d, available, ARROW );
            final int ax = ltr ? x + available.width - aps.width : x - available.width;
            layoutData.put ( ARROW, new Rectangle ( ax, bounds.y, aps.width, bounds.height ) );
            available.width -= aps.width + getTextArrowGap ( c, d );
        }
        if ( !isEmpty ( c, d, ACCELERATOR ) )
        {
            final Dimension aps = getPreferredSize ( c, d, available, ACCELERATOR );
            final int ax = ltr ? x + available.width - aps.width : x - available.width;
            layoutData.put ( ACCELERATOR, new Rectangle ( ax, bounds.y, aps.width, bounds.height ) );
            available.width -= aps.width + getTextAcceleratorGap ( c, d );
        }
        if ( !isEmpty ( c, d, TEXT ) )
        {
            x += ltr ? 0 : -available.width;
            layoutData.put ( TEXT, new Rectangle ( x, bounds.y, available.width, bounds.height ) );
        }
        return layoutData;
    }

    @NotNull
    @Override
    protected Dimension getContentPreferredSize ( @NotNull final C c, @NotNull final D d, @NotNull final Dimension available )
    {
        final Dimension ps = new Dimension ();
        final boolean alignTextByIcons = isAlignTextByIcons ( c, d );
        final boolean hasStateIcon = !isEmpty ( c, d, STATE_ICON );
        final boolean hasIcon = !isEmpty ( c, d, ICON );
        final PopupMenuIcons popupMenuIcons = getPopupMenuIcons ( c, d );
        if ( hasStateIcon || alignTextByIcons && popupMenuIcons.hasBothIcons )
        {
            final Dimension ips = getPreferredSize ( c, d, available, STATE_ICON );
            if ( alignTextByIcons )
            {
                ips.width = Math.max ( ips.width, popupMenuIcons.maxStateIconWidth );
                if ( !popupMenuIcons.hasBothIcons )
                {
                    ips.width = Math.max ( ips.width, popupMenuIcons.maxIconWidth );
                }
            }
            ps.width += ips.width + getStateIconGap ( c, d );
            ps.height = Math.max ( ps.height, ips.height );
        }
        if ( hasIcon || alignTextByIcons && popupMenuIcons.hasBothIcons )
        {
            final Dimension ips = getPreferredSize ( c, d, available, ICON );
            if ( alignTextByIcons )
            {
                ips.width = Math.max ( ips.width, popupMenuIcons.maxIconWidth );
                if ( !popupMenuIcons.hasBothIcons )
                {
                    ips.width = Math.max ( ips.width, popupMenuIcons.maxStateIconWidth );
                }
            }
            ps.width += ips.width + getIconTextGap ( c, d );
            ps.height = Math.max ( ps.height, ips.height );
        }
        if ( !hasStateIcon && !hasIcon && alignTextByIcons && popupMenuIcons.hasAnyIcons && !popupMenuIcons.hasBothIcons )
        {
            final int maxIcon = Math.max ( popupMenuIcons.maxStateIconWidth, popupMenuIcons.maxIconWidth );
            ps.width += maxIcon + getIconTextGap ( c, d );
        }
        if ( !isEmpty ( c, d, TEXT ) )
        {
            final Dimension tps = getPreferredSize ( c, d, available, TEXT );
            ps.width += tps.width;
            ps.height = Math.max ( ps.height, tps.height );
        }
        if ( !isEmpty ( c, d, ACCELERATOR ) )
        {
            final Dimension aps = getPreferredSize ( c, d, available, ACCELERATOR );
            ps.width += aps.width + getTextAcceleratorGap ( c, d );
            ps.height = Math.max ( ps.height, aps.height );
        }
        if ( !isEmpty ( c, d, ARROW ) )
        {
            final Dimension aps = getPreferredSize ( c, d, available, ARROW );
            ps.width += aps.width + getTextArrowGap ( c, d );
            ps.height = Math.max ( ps.height, aps.height );
        }
        return ps;
    }

    /**
     * Information about state {@link Icon} and custom {@link Icon} of all menu items in {@link JPopupMenu}.
     */
    protected static class PopupMenuIcons
    {
        /**
         * Whether or not at least one menu item has a non-{@code null} state {@link Icon} or custom {@link Icon}.
         */
        public boolean hasAnyIcons;

        /**
         * Whether or not at least one menu item has both state {@link Icon} and custom {@link Icon}.
         */
        public boolean hasBothIcons;

        /**
         * Maximum width of state {@link Icon}s of all menu items in {@link JPopupMenu}.
         * Will simply be {@code 0} if no items in menu have state {@link Icon}.
         */
        public int maxStateIconWidth;

        /**
         * Maximum width of custom {@link Icon}s of all menu items in {@link JPopupMenu}.
         * Will simply be {@code 0} if no items in menu have custom {@link Icon}.
         */
        public int maxIconWidth;

        /**
         * Constructs new {@link PopupMenuIcons}.
         *
         * @param hasAnyIcons       whether or not at least one menu item has a non-{@code null} state {@link Icon} or custom {@link Icon}
         * @param hasBothIcons      whether or not at least one menu item has both state {@link Icon} and custom {@link Icon}
         * @param maxStateIconWidth maximum width of state {@link Icon}s of all menu items in {@link JPopupMenu}
         * @param maxIconWidth      maximum width of custom {@link Icon}s of all menu items in {@link JPopupMenu}
         */
        public PopupMenuIcons ( final boolean hasAnyIcons, final boolean hasBothIcons, final int maxStateIconWidth, final int maxIconWidth )
        {
            this.hasAnyIcons = hasAnyIcons;
            this.hasBothIcons = hasBothIcons;
            this.maxStateIconWidth = maxStateIconWidth;
            this.maxIconWidth = maxIconWidth;
        }
    }
}