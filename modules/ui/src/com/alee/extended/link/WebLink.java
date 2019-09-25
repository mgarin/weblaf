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

package com.alee.extended.link;

import com.alee.api.annotations.NotNull;
import com.alee.extended.label.WebStyledLabel;
import com.alee.managers.language.UILanguageManager;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Hyperlink-like component based on {@link WebStyledLabel}.
 * Custom {@link LinkAction} listeners can be used to customize link behavior.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebLink">How to use WebLink</a>
 * @see LinkDescriptor
 * @see WLinkUI
 * @see WebLinkUI
 * @see ILinkPainter
 * @see LinkPainter
 * @see WebStyledLabel
 * @see LinkAction
 */
public class WebLink extends WebStyledLabel
{
    /**
     * Component properties.
     */
    public static final String ACTION_PROPERTY = "action";
    public static final String VISITABLE_PROPERTY = "visitable";
    public static final String VISITED_PROPERTY = "visited";

    /**
     * Whether or not link can become {@link #visited}.
     */
    protected boolean visitable;

    /**
     * Whether or not link have already been visited at least once.
     */
    protected boolean visited;

    /**
     * Constructs new unconfigured link.
     */
    public WebLink ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new link with custom icon.
     *
     * @param icon link icon
     */
    public WebLink ( final Icon icon )
    {
        this ( StyleId.auto, icon );
    }

    /**
     * Constructs new link with custom text.
     *
     * @param text link text
     */
    public WebLink ( final String text )
    {
        this ( StyleId.auto, text );
    }

    /**
     * Constructs new link with custom icon and text.
     *
     * @param icon link icon
     * @param text link text
     */
    public WebLink ( final Icon icon, final String text )
    {
        this ( StyleId.auto, icon, text );
    }

    /**
     * Constructs new link with custom action.
     *
     * @param action link action
     */
    public WebLink ( final LinkAction action )
    {
        this ( StyleId.auto, action );
    }

    /**
     * Constructs new link with custom icon and action.
     *
     * @param icon   link icon
     * @param action link action
     */
    public WebLink ( final Icon icon, final LinkAction action )
    {
        this ( StyleId.auto, icon, action );
    }

    /**
     * Constructs new link with custom text and action.
     *
     * @param text   link text
     * @param action link action
     */
    public WebLink ( final String text, final LinkAction action )
    {
        this ( StyleId.auto, text, action );
    }

    /**
     * Constructs new link with custom icon, text and action.
     *
     * @param icon   link icon
     * @param text   link text
     * @param action link action
     */
    public WebLink ( final Icon icon, final String text, final LinkAction action )
    {
        this ( StyleId.auto, icon, text, action );
    }

    /**
     * Constructs new unconfigured link.
     *
     * @param id style ID
     */
    public WebLink ( final StyleId id )
    {
        this ( id, null, null, null );
    }

    /**
     * Constructs new link with custom icon.
     *
     * @param id   style ID
     * @param icon link icon
     */
    public WebLink ( final StyleId id, final Icon icon )
    {
        this ( id, icon, null, null );
    }

    /**
     * Constructs new link with custom text.
     *
     * @param id   style ID
     * @param text link text
     */
    public WebLink ( final StyleId id, final String text )
    {
        this ( id, null, text, null );
    }

    /**
     * Constructs new link with custom icon and text.
     *
     * @param id   style ID
     * @param icon link icon
     * @param text link text
     */
    public WebLink ( final StyleId id, final Icon icon, final String text )
    {
        this ( id, icon, text, null );
    }

    /**
     * Constructs new link with custom action.
     *
     * @param id     style ID
     * @param action link action
     */
    public WebLink ( final StyleId id, final LinkAction action )
    {
        this ( id, null, null, action );
    }

    /**
     * Constructs new link with custom icon and action.
     *
     * @param id     style ID
     * @param icon   link icon
     * @param action link action
     */
    public WebLink ( final StyleId id, final Icon icon, final LinkAction action )
    {
        this ( id, icon, null, action );
    }

    /**
     * Constructs new link with custom text and action.
     *
     * @param id     style ID
     * @param text   link text
     * @param action link action
     */
    public WebLink ( final StyleId id, final String text, final LinkAction action )
    {
        this ( id, null, text, action );
    }

    /**
     * Constructs new link with custom icon, text and action.
     *
     * @param id     style ID
     * @param icon   link icon
     * @param text   link text
     * @param action link action
     */
    public WebLink ( final StyleId id, final Icon icon, final String text, final LinkAction action )
    {
        super ( id, text, icon );
        addAction ( action );
    }

    /**
     * Returns whether or not link can become {@link #visited}.
     *
     * @return {@code true} if link can become {@link #visited}, {@code false} otherwise
     */
    public boolean isVisitable ()
    {
        return visitable;
    }

    /**
     * Sets whether or not link can become {@link #visited}.
     *
     * @param visitable whether or not link can become {@link #visited}
     */
    public void setVisitable ( final boolean visitable )
    {
        final boolean old = this.visitable;
        this.visitable = visitable;
        firePropertyChange ( VISITABLE_PROPERTY, old, visitable );
    }

    /**
     * Returns whether or not link have already been visited at least once.
     *
     * @return {@code true} if link have already been visited at least once, {@code false} otherwise
     */
    public boolean isVisited ()
    {
        return visited;
    }

    /**
     * Sets whether or not link have already been visited at least once.
     *
     * @param visited whether or not link have already been visited at least once
     */
    public void setVisited ( final boolean visited )
    {
        final boolean old = this.visited;
        this.visited = visited;
        firePropertyChange ( VISITED_PROPERTY, old, visited );
    }

    /**
     * Adds new link action.
     *
     * @param action link action
     */
    public void addAction ( final LinkAction action )
    {
        if ( action != null )
        {
            if ( getIcon () == null && getText () == null )
            {
                final Icon icon = action.getIcon ();
                if ( icon != null )
                {
                    setIcon ( icon );
                }
                final String text = action.getText ();
                if ( text != null )
                {
                    setText ( UILanguageManager.getInitialText ( text ) );
                    UILanguageManager.registerInitialLanguage ( this, text );
                }
            }
            listenerList.add ( LinkAction.class, action );
            firePropertyChange ( ACTION_PROPERTY, null, action );
        }
    }

    /**
     * Removes link action.
     *
     * @param action link action
     */
    public void removeAction ( final LinkAction action )
    {
        if ( action != null )
        {
            listenerList.remove ( LinkAction.class, action );
            firePropertyChange ( ACTION_PROPERTY, action, null );
        }
    }

    /**
     * Returns link actions.
     *
     * @return link actions
     */
    public List<LinkAction> getActions ()
    {
        return CollectionUtils.asList ( listenerList.getListeners ( LinkAction.class ) );
    }

    /**
     * Fires link action is performed.
     */
    public void fireLinkExecuted ()
    {
        // Firing listeners
        final ActionEvent event = new ActionEvent ( WebLink.this, 0, "Link clicked", System.currentTimeMillis (), 0 );
        for ( final LinkAction action : listenerList.getListeners ( LinkAction.class ) )
        {
            action.linkExecuted ( event );
        }

        // Firing action event
        fireActionPerformed ( event );

        // Marking link as visited
        setVisited ( true );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.link;
    }

    /**
     * Adds {@link ActionListener}.
     *
     * @param listener {@link ActionListener} to add
     */
    public void addActionListener ( final ActionListener listener )
    {
        listenerList.add ( ActionListener.class, listener );
    }

    /**
     * Removes {@link ActionListener}.
     *
     * @param listener {@link ActionListener} to remove
     */
    public void removeActionListener ( final ActionListener listener )
    {
        listenerList.remove ( ActionListener.class, listener );
    }

    /**
     * Fires action performed event on all added {@link ActionListener}s.
     *
     * @param event {@link ActionEvent}
     */
    public void fireActionPerformed ( final ActionEvent event )
    {
        for ( final ActionListener listener : listenerList.getListeners ( ActionListener.class ) )
        {
            listener.actionPerformed ( event );
        }
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WLinkUI} object that renders this component
     */
    @Override
    public WLinkUI getUI ()
    {
        return ( WLinkUI ) ui;
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WLinkUI}
     */
    public void setUI ( final WLinkUI ui )
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