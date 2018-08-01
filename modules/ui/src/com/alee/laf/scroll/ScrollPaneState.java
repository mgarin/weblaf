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

package com.alee.laf.scroll;

import com.alee.api.merge.Mergeable;
import com.alee.utils.CoreSwingUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.io.Serializable;

/**
 * {@link JScrollPane} state holder.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see ScrollPaneSettingsProcessor
 * @see ScrollPaneState
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 * @see ScrollBarState
 */
@XStreamAlias ( "ScrollPaneState" )
public class ScrollPaneState implements Mergeable, Cloneable, Serializable
{
    /**
     * {@link ScrollBarState} for horizontal {@link JScrollBar}.
     */
    @XStreamAlias ( "HorizontalBar" )
    protected final ScrollBarState horizontal;

    /**
     * {@link ScrollBarState} for vertical {@link JScrollBar}.
     */
    @XStreamAlias ( "VerticalBar" )
    protected final ScrollBarState vertical;

    /**
     * Constructs default {@link ScrollPaneState}.
     */
    public ScrollPaneState ()
    {
        this ( null, null );
    }

    /**
     * Constructs new {@link ScrollPaneState} with settings from {@link JScrollPane}.
     *
     * @param scrollPane {@link JScrollPane} to retrieve settings from
     */
    public ScrollPaneState ( final JScrollPane scrollPane )
    {
        this (
                new ScrollBarState ( scrollPane.getHorizontalScrollBar () ),
                new ScrollBarState ( scrollPane.getVerticalScrollBar () )
        );
    }

    /**
     * Constructs new {@link ScrollPaneState} with specified settings.
     *
     * @param horizontal {@link ScrollBarState} for horizontal {@link JScrollBar}
     * @param vertical   {@link ScrollBarState} for vertical {@link JScrollBar}
     */
    public ScrollPaneState ( final ScrollBarState horizontal, final ScrollBarState vertical )
    {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    /**
     * Returns horizontal {@link JScrollBar} state.
     *
     * @return horizontal {@link JScrollBar} state
     */
    public ScrollBarState horizontal ()
    {
        return horizontal;
    }

    /**
     * Returns vertical {@link JScrollBar} state.
     *
     * @return vertical {@link JScrollBar} state
     */
    public ScrollBarState vertical ()
    {
        return vertical;
    }

    /**
     * Applies this {@link ScrollPaneState} to the specified {@link JScrollPane}.
     * Actual value application is performed later on EDT since {@link JScrollPane} size needs to be initialized first.
     *
     * @param scrollPane {@link JScrollPane} to apply this {@link ScrollPaneState} to
     */
    public void apply ( final JScrollPane scrollPane )
    {
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                if ( horizontal != null )
                {
                    horizontal.apply ( scrollPane.getHorizontalScrollBar () );
                }
                if ( vertical != null )
                {
                    vertical.apply ( scrollPane.getVerticalScrollBar () );
                }
            }
        } );
    }
}