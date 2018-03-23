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

package com.alee.extended.split;

import com.alee.api.data.Orientation;
import com.alee.extended.WebContainer;
import com.alee.laf.button.WebButton;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Divider component used by {@link WebMultiSplitPane}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see MultiSplitPaneDividerDescriptor
 * @see WMultiSplitPaneDividerUI
 * @see WebMultiSplitPaneDividerUI
 * @see IMultiSplitPaneDividerPainter
 * @see MultiSplitPaneDividerPainter
 * @see WebMultiSplitPane
 * @see WebMultiSplitPaneModel
 */

public class WebMultiSplitPaneDivider extends WebContainer<WebMultiSplitPaneDivider, WMultiSplitPaneDividerUI>
{
    /**
     * {@link WebMultiSplitPane} this divider is used for.
     * It is final as there is no practical need to reuse dividers for different panes.
     */
    protected final WebMultiSplitPane multiSplitPane;

    /**
     * Constructs new {@link WebMultiSplitPaneDivider}.
     *
     * @param multiSplitPane {@link WebMultiSplitPane} this divider is attached to
     */
    public WebMultiSplitPaneDivider ( final WebMultiSplitPane multiSplitPane )
    {
        this ( StyleId.auto, multiSplitPane );
    }

    /**
     * Constructs new {@link WebMultiSplitPaneDivider}.
     *
     * @param id             {@link StyleId}
     * @param multiSplitPane {@link WebMultiSplitPane} this divider is attached to
     */
    public WebMultiSplitPaneDivider ( final StyleId id, final WebMultiSplitPane multiSplitPane )
    {
        super ();
        this.multiSplitPane = multiSplitPane;
        updateUI ();
        setStyleId ( id );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.multisplitpanedivider;
    }

    /**
     * Returns {@link WebMultiSplitPane} this divider is used for.
     *
     * @return {@link WebMultiSplitPane} this divider is used for
     */
    public WebMultiSplitPane getMultiSplitPane ()
    {
        return multiSplitPane;
    }

    /**
     * Returns {@link Orientation} of this {@link WebMultiSplitPaneDivider}.
     * It is always opposite to {@link Orientation} of the {@link WebMultiSplitPane}.
     *
     * @return {@link Orientation} of this {@link WebMultiSplitPaneDivider}
     */
    public Orientation getOrientation ()
    {
        return getMultiSplitPane ().getOrientation ().opposite ();
    }

    /**
     * Returns whether or not drag for this {@link WebMultiSplitPaneDivider} is available.
     *
     * @return {@code true} if drag for this {@link WebMultiSplitPaneDivider} is available, {@code false} otherwise
     */
    public boolean isDragAvailable ()
    {
        return getMultiSplitPane ().getModel ().isDragAvailable ( this );
    }

    /**
     * Returns {@link WebButton} that can be used to expand component to the right of this {@link WebMultiSplitPaneDivider}.
     *
     * @return {@link WebButton} that can be used to expand component to the right of this {@link WebMultiSplitPaneDivider}
     */
    public WebButton getLeftOneTouchButton ()
    {
        return getUI ().getLeftOneTouchButton ();
    }

    /**
     * Creates and returns new {@link WebButton} that can be used to expand component to the right of this {@link WebMultiSplitPaneDivider}.
     *
     * @return new {@link WebButton} that can be used to expand component to the right of this {@link WebMultiSplitPaneDivider}
     */
    protected WebButton createLeftOneTouchButton ()
    {
        final OneTouchButton button = new OneTouchButton ( StyleId.multisplitpanedividerOneTouchLeftButton.at ( this ), this );
        button.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                multiSplitPane.getModel ().toggleViewToRight ( WebMultiSplitPaneDivider.this );
            }
        } );
        return button;
    }

    /**
     * Returns {@link WebButton} that can be used to expand component to the left of this {@link WebMultiSplitPaneDivider}.
     *
     * @return {@link WebButton} that can be used to expand component to the left of this {@link WebMultiSplitPaneDivider}
     */
    public WebButton getRightOneTouchButton ()
    {
        return getUI ().getRightOneTouchButton ();
    }

    /**
     * Creates and returns new {@link WebButton} that can be used to expand component to the left of this {@link WebMultiSplitPaneDivider}.
     *
     * @return new {@link WebButton} that can be used to expand component to the left of this {@link WebMultiSplitPaneDivider}
     */
    protected WebButton createRightOneTouchButton ()
    {
        final OneTouchButton button = new OneTouchButton ( StyleId.multisplitpanedividerOneTouchRightButton.at ( this ), this );
        button.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                multiSplitPane.getModel ().toggleViewToLeft ( WebMultiSplitPaneDivider.this );
            }
        } );
        return button;
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WMultiSplitPaneDividerUI} object that renders this component
     */
    public WMultiSplitPaneDividerUI getUI ()
    {
        return ( WMultiSplitPaneDividerUI ) ui;
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WMultiSplitPaneDividerUI}
     */
    public void setUI ( final WMultiSplitPaneDividerUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public void updateUI ()
    {
        StyleManager.getDescriptor ( this ).updateUI ( this );
    }

    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }
}