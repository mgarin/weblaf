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

package com.alee.extended.collapsible;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.managers.style.StyleId;

import java.awt.*;

/**
 * Custom {@link LayoutManager} for {@link AbstractHeaderPanel}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebCollapsiblePane">How to use WebCollapsiblePane</a>
 * @see WebCollapsiblePane
 */
public class HeaderLayout extends AbstractLayoutManager
{
    /**
     * {@link AbstractHeaderPanel} title {@link Component} constraints.
     */
    public static final String TITLE = "title";

    /**
     * {@link AbstractHeaderPanel} control {@link Component} constraints.
     */
    public static final String CONTROL = "control";

    /**
     * {@link AbstractHeaderPanel} title {@link Component}.
     */
    @Nullable
    protected Component title;

    /**
     * {@link AbstractHeaderPanel} control {@link Component}.
     */
    @Nullable
    protected Component control;

    @Override
    public void addComponent ( @NotNull final Component component, @Nullable final Object constraints )
    {
        final AbstractHeaderPanel headerPanel = ( AbstractHeaderPanel ) component.getParent ();
        if ( Objects.equals ( constraints, TITLE ) )
        {
            title = component;
            if ( title instanceof AbstractTitleLabel.UIResource )
            {
                StyleId.collapsiblepaneTitleLabel.at ( headerPanel ).set (
                        ( AbstractTitleLabel.UIResource ) title
                );
            }
        }
        else if ( Objects.equals ( constraints, CONTROL ) )
        {
            control = component;
            if ( control instanceof AbstractControlButton.UIResource )
            {
                StyleId.collapsiblepaneControlButton.at ( headerPanel ).set (
                        ( AbstractControlButton.UIResource ) control
                );
            }
        }
        else
        {
            throw new IllegalArgumentException ( "Unknown constraints: " + constraints );
        }
    }

    @Override
    public void removeComponent ( @NotNull final Component component )
    {
        if ( component == title )
        {
            if ( title instanceof AbstractTitleLabel.UIResource )
            {
                ( ( AbstractTitleLabel.UIResource ) title ).resetStyleId ();
            }
            title = null;
        }
        else if ( component == control )
        {
            if ( control instanceof AbstractControlButton.UIResource )
            {
                ( ( AbstractControlButton.UIResource ) control ).resetStyleId ();
            }
            control = null;
        }
        else
        {
            throw new IllegalArgumentException ( "Component was not added into header before: " + component );
        }
    }

    /**
     * Returns title {@link Component}.
     *
     * @return title {@link Component}
     */
    @Nullable
    public Component getTitle ()
    {
        return title;
    }

    /**
     * Returns control {@link Component}.
     *
     * @return control {@link Component}
     */
    @Nullable
    public Component getControl ()
    {
        return control;
    }

    @Override
    public void layoutContainer ( @NotNull final Container parent )
    {
        final AbstractHeaderPanel headerPanel = ( AbstractHeaderPanel ) parent;
        final Dimension size = headerPanel.getSize ();
        final Insets insets = headerPanel.getInsets ();
        final boolean ltr = headerPanel.getComponentOrientation ().isLeftToRight ();
        final int w = Math.max ( 0, size.width - insets.left - insets.right );
        final int h = Math.max ( 0, size.height - insets.top - insets.bottom );
        final Dimension cps = control != null ? control.getPreferredSize () : new Dimension ( 0, 0 );
        switch ( headerPanel.getHeaderPosition () )
        {
            case top:
            case bottom:
            {
                if ( title != null )
                {
                    title.setBounds (
                            ltr ? insets.left : insets.left + cps.width,
                            insets.top,
                            w > cps.width ? w - cps.width : 0,
                            h
                    );
                }
                if ( control != null )
                {
                    control.setBounds (
                            ltr ? insets.left + w - cps.width : insets.left,
                            insets.top,
                            cps.width < w ? cps.width : w,
                            h
                    );
                }
                break;
            }
            case left:
            {
                if ( title != null )
                {
                    final int th = h > cps.height ? h - cps.height : 0;
                    title.setBounds ( insets.left, insets.top + cps.height, w, th );
                }
                if ( control != null )
                {
                    final int ch = cps.height < h ? cps.height : h;
                    control.setBounds ( insets.left, insets.top, w, ch );
                }
                break;
            }
            case right:
            {
                if ( title != null )
                {
                    final int th = h > cps.height ? h - cps.height : 0;
                    title.setBounds ( insets.left, insets.top, w, th );
                }
                if ( control != null )
                {
                    final int ch = cps.height < h ? cps.height : h;
                    control.setBounds ( insets.left, insets.top + h - ch, w, ch );
                }
                break;
            }
        }
    }

    @NotNull
    @Override
    public Dimension preferredLayoutSize ( @NotNull final Container parent )
    {
        final AbstractHeaderPanel headerPanel = ( AbstractHeaderPanel ) parent;
        final Dimension ps = new Dimension ( 0, 0 );

        final Dimension tps = title != null ? title.getPreferredSize () : new Dimension ( 0, 0 );
        final Dimension cps = control != null ? control.getPreferredSize () : new Dimension ( 0, 0 );
        switch ( headerPanel.getHeaderPosition () )
        {
            case top:
            case bottom:
            {
                ps.width += tps.width + cps.width;
                ps.height += Math.max ( tps.height, cps.height );
                break;
            }
            case left:
            case right:
            {
                ps.width += Math.max ( tps.width, cps.width );
                ps.height += tps.height + cps.height;
                break;
            }
        }

        final Insets insets = headerPanel.getInsets ();
        ps.width += insets.left + insets.right;
        ps.height += insets.top + insets.bottom;

        return ps;
    }
}