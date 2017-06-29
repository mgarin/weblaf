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

package com.alee.extended.canvas;

import com.alee.extended.WebComponent;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.ArrayUtils;
import com.alee.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple canvas implementation.
 * Unlike {@link java.awt.Canvas} it is based on {@link javax.swing.JComponent} and supports customizable UI and painter.
 * <p>
 * Component itself doesn't contain any customizable data so any custom painter can be easily provided for it.
 * It exists to prevent creation of multiple helper components for various small UI elements performing simple tasks.
 * <p/>
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application L&amp;F as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see WebComponent
 * @see WebCanvasUI
 * @see CanvasPainter
 */

public class WebCanvas extends WebComponent<WebCanvas, WCanvasUI> implements Stateful
{
    /**
     * Custom canvas states.
     */
    protected List<String> states;

    /**
     * Constructs new canvas.
     */
    public WebCanvas ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new canvas.
     *
     * @param states custom canvas states
     */
    public WebCanvas ( final String... states )
    {
        this ( StyleId.auto, states );
    }

    /**
     * Constructs new canvas.
     *
     * @param id     style ID
     * @param states custom canvas states
     */
    public WebCanvas ( final StyleId id, final String... states )
    {
        super ();
        addStates ( states );
        updateUI ();
        setStyleId ( id );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.canvas;
    }

    /**
     * Returns custom canvas states.
     *
     * @return custom canvas states
     */
    @Override
    public List<String> getStates ()
    {
        return states;
    }

    /**
     * Sets custom canvas states.
     *
     * @param states custom canvas states
     */
    public void setStates ( final List<String> states )
    {
        final List<String> old = CollectionUtils.copy ( this.states );
        this.states = states;
        fireStatesChanged ( old, this.states );
    }

    /**
     * Adds custom canvas states.
     *
     * @param states custom canvas states to add
     */
    public void addStates ( final String... states )
    {
        if ( ArrayUtils.notEmpty ( states ) )
        {
            final List<String> old = CollectionUtils.copy ( this.states );
            if ( this.states == null )
            {
                this.states = new ArrayList<String> ( 1 );
            }
            CollectionUtils.addAllNonNull ( this.states, states );
            fireStatesChanged ( old, this.states );
        }
    }

    /**
     * Removes custom canvas states.
     *
     * @param states custom canvas states to remove
     */
    public void removeStates ( final String... states )
    {
        if ( ArrayUtils.notEmpty ( states ) )
        {
            final List<String> old = CollectionUtils.copy ( this.states );
            if ( this.states != null )
            {
                CollectionUtils.removeAll ( this.states, states );
            }
            fireStatesChanged ( old, this.states );
        }
    }

    /**
     * Updates decoration states.
     *
     * @param oldStates previous custom states
     * @param newStates current custom states
     */
    public void fireStatesChanged ( final List<String> oldStates, final List<String> newStates )
    {
        firePropertyChange ( AbstractDecorationPainter.DECORATION_STATES_PROPERTY, oldStates, newStates );
    }

    /**
     * Returns the look and feel (L&amp;F) object that renders this component.
     *
     * @return the {@link WCanvasUI} object that renders this component
     */
    public WCanvasUI getUI ()
    {
        return ( WCanvasUI ) ui;
    }

    /**
     * Sets the L&amp;F object that renders this component.
     *
     * @param ui {@link WCanvasUI}
     */
    public void setUI ( final WCanvasUI ui )
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