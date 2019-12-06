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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.extended.WebComponent;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.ArrayUtils;
import com.alee.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simple canvas implementation.
 * Unlike {@link java.awt.Canvas} it is based on {@link javax.swing.JComponent} and supports customizable UI and painter.
 *
 * Component itself doesn't contain any customizable data so any custom painter can be easily provided for it.
 * It exists to prevent creation of multiple helper components for various small UI elements performing simple tasks.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see CanvasDescriptor
 * @see WCanvasUI
 * @see WebCanvasUI
 * @see ICanvasPainter
 * @see CanvasPainter
 * @see WebComponent
 */
public class WebCanvas extends WebComponent<WebCanvas, WCanvasUI> implements Stateful
{
    /**
     * Custom {@link WebCanvas} states.
     */
    @NotNull
    protected List<String> states;

    /**
     * Constructs new {@link WebCanvas}.
     */
    public WebCanvas ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new {@link WebCanvas}.
     *
     * @param states custom {@link WebCanvas} states
     */
    public WebCanvas ( @Nullable final String... states )
    {
        this ( StyleId.auto, states );
    }

    /**
     * Constructs new {@link WebCanvas}.
     *
     * @param id     {@link StyleId}
     * @param states custom {@link WebCanvas} states
     */
    public WebCanvas ( @NotNull final StyleId id, @Nullable final String... states )
    {
        this.states = new ArrayList<String> ( 1 );
        addStates ( states );
        updateUI ();
        setStyleId ( id );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.canvas;
    }

    /**
     * Returns custom {@link WebCanvas} states.
     *
     * @return custom {@link WebCanvas} states
     */
    @NotNull
    @Override
    public List<String> getStates ()
    {
        return new ArrayList<String> ( states );
    }

    /**
     * Returns whether or not {@link WebCanvas} has specified custom state.
     * Note that this doesn't include any states provided by underlying UI or painter implementations.
     *
     * @param state state to check
     * @return {@code true} if {@link WebCanvas} has specified custom state, {@code false} otherwise
     */
    public boolean hasState ( @NotNull final String state )
    {
        return states.contains ( state );
    }

    /**
     * Sets custom {@link WebCanvas} states.
     *
     * @param states custom {@link WebCanvas} states
     */
    public void setStates ( final List<String> states )
    {
        if ( !CollectionUtils.equals ( this.states, states, false ) )
        {
            final List<String> old = CollectionUtils.copy ( this.states );
            this.states = states;
            fireStatesChanged ( old, this.states );
        }
    }

    /**
     * Adds custom {@link WebCanvas} states.
     *
     * @param states custom {@link WebCanvas} states to add
     */
    public void addStates ( @Nullable final String... states )
    {
        if ( ArrayUtils.notEmpty ( states ) )
        {
            final List<String> old = CollectionUtils.copy ( this.states );
            CollectionUtils.addUniqueNonNull ( this.states, states );
            fireStatesChanged ( old, this.states );
        }
    }

    /**
     * Adds custom {@link WebCanvas} states.
     *
     * @param states custom {@link WebCanvas} states to add
     */
    public void addStates ( @Nullable final Collection<String> states )
    {
        if ( states != null && ArrayUtils.notEmpty ( states ) )
        {
            final List<String> old = CollectionUtils.copy ( this.states );
            CollectionUtils.addUniqueNonNull ( this.states, states );
            fireStatesChanged ( old, this.states );
        }
    }

    /**
     * Removes custom {@link WebCanvas} states.
     *
     * @param states custom {@link WebCanvas} states to remove
     */
    public void removeStates ( @Nullable final String... states )
    {
        if ( ArrayUtils.notEmpty ( states ) )
        {
            final List<String> old = CollectionUtils.copy ( this.states );
            CollectionUtils.removeAll ( this.states, states );
            fireStatesChanged ( old, this.states );
        }
    }

    /**
     * Removes custom {@link WebCanvas} states.
     *
     * @param states custom {@link WebCanvas} states to remove
     */
    public void removeStates ( @Nullable final Collection<String> states )
    {
        if ( states != null && ArrayUtils.notEmpty ( states ) )
        {
            final List<String> old = CollectionUtils.copy ( this.states );
            CollectionUtils.removeAll ( this.states, states );
            fireStatesChanged ( old, this.states );
        }
    }

    /**
     * Updates {@link WebCanvas} decoration states.
     *
     * @param oldStates previous custom {@link WebCanvas} states
     * @param newStates current custom {@link WebCanvas} states
     */
    public void fireStatesChanged ( @NotNull final List<String> oldStates, @NotNull final List<String> newStates )
    {
        firePropertyChange ( AbstractDecorationPainter.DECORATION_STATES_PROPERTY, oldStates, newStates );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WCanvasUI} object that renders this component
     */
    public WCanvasUI getUI ()
    {
        return ( WCanvasUI ) ui;
    }

    /**
     * Sets the LaF object that renders this component.
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

    @NotNull
    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }
}