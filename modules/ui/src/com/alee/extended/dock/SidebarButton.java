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

package com.alee.extended.dock;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.laf.button.WebToggleButton;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.extensions.MouseEventRunnable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom sidebar {@link WebToggleButton} providing additional decoration states.
 *
 * @author Mikle Garin
 */
public class SidebarButton extends WebToggleButton implements Stateful
{
    /**
     * {@link WebDockableFrame} this {@link SidebarButton} is created for.
     */
    protected final WebDockableFrame frame;

    /**
     * Constructs new sidebar button.
     *
     * @param frame {@link WebDockableFrame} this {@link SidebarButton} is created for
     */
    public SidebarButton ( @NotNull final WebDockableFrame frame )
    {
        super ( StyleId.dockableframeSidebarButton.at ( frame ), frame.getTitle (), frame.getIcon () );
        this.frame = frame;
        setFocusable ( false );
        setSelected ( getSelectionState () );
        onMousePress ( MouseButton.right, new MouseEventRunnable ()
        {
            @Override
            public void run ( @NotNull final MouseEvent e )
            {
                final WebDockablePane dockablePane = frame.getDockablePane ();
                if ( dockablePane != null )
                {
                    if ( dockablePane.getSidebarButtonAction () == SidebarButtonAction.preview )
                    {
                        if ( frame.isMinimized () )
                        {
                            frame.dock ();
                        }
                        else
                        {
                            frame.minimize ();
                        }
                    }
                    else
                    {
                        if ( frame.isMinimized () )
                        {
                            frame.preview ();
                        }
                        else if ( frame.isPreview () )
                        {
                            frame.minimize ();
                        }
                    }
                }
            }
        } );
        addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( @NotNull final ActionEvent e )
            {
                final WebDockablePane dockablePane = frame.getDockablePane ();
                if ( dockablePane != null )
                {
                    if ( isSelected () )
                    {
                        switch ( dockablePane.getSidebarButtonAction () )
                        {
                            case restore:
                                frame.restore ();
                                break;

                            case preview:
                                frame.preview ();
                                break;

                            case dock:
                                frame.dock ();
                                break;

                            case detach:
                                frame.detach ();
                                break;
                        }
                    }
                    else
                    {
                        frame.minimize ();
                    }
                }
            }
        } );
    }

    /**
     * Returns {@link WebDockableFrame} this {@link SidebarButton} is created for.
     *
     * @return {@link WebDockableFrame} this {@link SidebarButton} is created for
     */
    public WebDockableFrame getFrame ()
    {
        return frame;
    }

    @Nullable
    @Override
    public List<String> getStates ()
    {
        final List<String> states = new ArrayList<String> ();

        // Frame states
        if ( frame != null )
        {
            states.add ( frame.getState ().name () );
            states.add ( frame.getPosition ().name () );
        }

        return states;
    }

    /**
     * Updates decoration states.
     */
    public void updateStates ()
    {
        final boolean selected = getSelectionState ();
        if ( selected != isSelected () )
        {
            setSelected ( selected );
        }
        DecorationUtils.fireStatesChanged ( this );
    }

    /**
     * Returns sidebar button selection state.
     *
     * @return {@code true} if sidebar button should be selected, {@code false} otherwise
     */
    protected boolean getSelectionState ()
    {
        return frame.isDocked () || frame.isFloating () || frame.isPreview () && frame.getDockablePane () != null &&
                frame.getDockablePane ().getSidebarButtonAction () == SidebarButtonAction.preview;
    }
}