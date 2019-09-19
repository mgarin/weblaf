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

package com.alee.extended.tab;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents basic data for single document opened in {@link WebDocumentPane}.
 * todo Change this to interface so it could be implemented in any custom data class
 *
 * @param <C> {@link Component} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see WebDocumentPane
 * @see PaneData
 */
public class DocumentData<C extends Component>
{
    /**
     * {@link DocumentDataListener}s used to properly update {@link WebDocumentPane} on document changes.
     */
    @Nullable
    protected EventListenerList listeners;

    /**
     * Document identifier unique within {@link WebDocumentPane}.
     */
    @NotNull
    protected String id;

    /**
     * Document {@link Icon} used for the document tab.
     */
    @Nullable
    protected Icon icon;

    /**
     * Plain text or a language key used as document title.
     * Whether this is a plain text or a language key will be determined automatically, you only have to provide it.
     */
    @NotNull
    protected String title;

    /**
     * Document foreground {@link Color} used for the document tab title.
     */
    @Nullable
    protected Color foreground;

    /**
     * Document background {@link Color} used for the document tab and content.
     */
    @Nullable
    protected Color background;

    /**
     * Whether this document is closable or not.
     * All documents are closable by default, but you may change that.
     */
    protected boolean closable;

    /**
     * Whether this document is draggable or not.
     * All documents are draggable by default, but you may change that.
     */
    protected boolean draggable;

    /**
     * {@link Component} used as document tab content.
     */
    @NotNull
    protected C component;

    /**
     * Constructs new {@link DocumentData}.
     *
     * @param id        document identifier unique within {@link WebDocumentPane}
     * @param title     plain text or a language key used as document title
     * @param component {@link Component} used as document tab content
     */
    public DocumentData ( @NotNull final String id, @NotNull final String title, @NotNull final C component )
    {
        this ( id, null, title, null, null, true, true, component );
    }

    /**
     * Constructs new {@link DocumentData}.
     *
     * @param id        document identifier unique within {@link WebDocumentPane}
     * @param icon      document {@link Icon} used for the document tab
     * @param title     plain text or a language key used as document title
     * @param component {@link Component} used as document tab content
     */
    public DocumentData ( @NotNull final String id, @Nullable final Icon icon, @NotNull final String title, @NotNull final C component )
    {
        this ( id, icon, title, null, null, true, true, component );
    }

    /**
     * Constructs new {@link DocumentData}.
     *
     * @param id         document identifier unique within {@link WebDocumentPane}
     * @param icon       document {@link Icon} used for the document tab
     * @param title      plain text or a language key used as document title
     * @param background document background {@link Color} used for the document tab and content
     * @param component  {@link Component} used as document tab content
     */
    public DocumentData ( @NotNull final String id, @Nullable final Icon icon, @NotNull final String title,
                          @Nullable final Color background, @NotNull final C component )
    {
        this ( id, icon, title, null, background, true, true, component );
    }

    /**
     * Constructs new {@link DocumentData}.
     *
     * @param id         document identifier unique within {@link WebDocumentPane}
     * @param icon       document {@link Icon} used for the document tab
     * @param title      plain text or a language key used as document title
     * @param background document background {@link Color} used for the document tab and content
     * @param closable   whether document is closable or not
     * @param component  {@link Component} used as document tab content
     */
    public DocumentData ( @NotNull final String id, @Nullable final Icon icon, @NotNull final String title,
                          @Nullable final Color background, final boolean closable, @NotNull final C component )
    {
        this ( id, icon, title, null, background, closable, true, component );
    }

    /**
     * Constructs new {@link DocumentData}.
     *
     * @param id         document identifier unique within {@link WebDocumentPane}
     * @param icon       document {@link Icon} used for the document tab
     * @param title      plain text or a language key used as document title
     * @param background document background {@link Color} used for the document tab and content
     * @param closable   whether document is closable or not
     * @param draggable  whether document is draggable or not
     * @param component  {@link Component} used as document tab content
     */
    public DocumentData ( @NotNull final String id, @Nullable final Icon icon, @NotNull final String title,
                          @Nullable final Color background, final boolean closable, final boolean draggable, @NotNull final C component )
    {
        this ( id, icon, title, null, background, closable, draggable, component );
    }

    /**
     * Constructs new {@link DocumentData}.
     *
     * @param id         document identifier unique within {@link WebDocumentPane}
     * @param icon       document {@link Icon} used for the document tab
     * @param title      plain text or a language key used as document title
     * @param foreground document foreground {@link Color} used for the document tab title
     * @param background document background {@link Color} used for the document tab and content
     * @param closable   whether document is closable or not
     * @param draggable  whether document is draggable or not
     * @param component  {@link Component} used as document tab content
     */
    public DocumentData ( @NotNull final String id, @Nullable final Icon icon, @NotNull final String title,
                          @Nullable final Color foreground, @Nullable final Color background,
                          final boolean closable, final boolean draggable, @NotNull final C component )
    {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.foreground = foreground;
        this.background = background;
        this.closable = closable;
        this.draggable = draggable;
        this.component = component;
    }

    /**
     * Returns document identifier unique within {@link WebDocumentPane}.
     *
     * @return document identifier unique within {@link WebDocumentPane}
     */
    @NotNull
    public String getId ()
    {
        return id;
    }

    /**
     * Sets document identifier unique within {@link WebDocumentPane}.
     *
     * @param id new document identifier unique within {@link WebDocumentPane}
     */
    public void setId ( @NotNull final String id )
    {
        this.id = id;
    }

    /**
     * Returns document {@link Icon} used for the document tab.
     *
     * @return document {@link Icon} used for the document tab
     */
    @Nullable
    public Icon getIcon ()
    {
        return icon;
    }

    /**
     * Sets document {@link Icon} used for the document tab.
     *
     * @param icon new document {@link Icon} used for the document tab
     */
    public void setIcon ( @Nullable final Icon icon )
    {
        this.icon = icon;
        fireTitleChanged ();
    }

    /**
     * Returns plain text or a language key used as document title.
     *
     * @return plain text or a language key used as document title
     */
    @NotNull
    public String getTitle ()
    {
        return title;
    }

    /**
     * Sets plain text or a language key used as document title.
     *
     * @param title new plain text or a language key used as document title
     */
    public void setTitle ( @NotNull final String title )
    {
        this.title = title;
        fireTitleChanged ();
    }

    /**
     * Returns document foreground {@link Color} used for the document tab title.
     *
     * @return document foreground {@link Color} used for the document tab title
     */
    @Nullable
    public Color getForeground ()
    {
        return foreground;
    }

    /**
     * Sets document foreground {@link Color} used for the document tab title
     *
     * @param foreground document foreground {@link Color} used for the document tab title
     */
    public void setForeground ( @Nullable final Color foreground )
    {
        this.foreground = foreground;
        fireTitleChanged ();
    }

    /**
     * Returns document background {@link Color} used for the document tab and content.
     *
     * @return document background {@link Color} used for the document tab and content
     */
    @Nullable
    public Color getBackground ()
    {
        return background;
    }

    /**
     * Sets document background {@link Color} used for the document tab and content.
     *
     * @param background document background {@link Color} used for the document tab and content
     */
    public void setBackground ( @Nullable final Color background )
    {
        final Color old = this.background;
        this.background = background;
        fireBackgroundChanged ( old, background );
    }

    /**
     * Returns whether document is closable or not.
     *
     * @return true if document is closable, false otherwise
     */
    public boolean isClosable ()
    {
        return closable;
    }

    /**
     * Sets whether document is closable or not.
     *
     * @param closable whether document is closable or not
     */
    public void setClosable ( final boolean closable )
    {
        this.closable = closable;
        fireTitleChanged ();
    }

    /**
     * Returns whether document is draggable or not.
     *
     * @return true if document is draggable, false otherwise
     */
    public boolean isDraggable ()
    {
        return draggable;
    }

    /**
     * Sets whether document is draggable or not.
     *
     * @param draggable whether document is draggable or not
     */
    public void setDraggable ( final boolean draggable )
    {
        this.draggable = draggable;
    }

    /**
     * Returns {@link Component} used as document tab content.
     *
     * @return {@link Component} used as document tab content
     */
    @NotNull
    public C getComponent ()
    {
        return component;
    }

    /**
     * Sets {@link Component} used as document tab content.
     *
     * @param component new {@link Component} used as document tab content
     */
    public void setComponent ( @NotNull final C component )
    {
        final Component old = this.component;
        this.component = component;
        fireContentChanged ( old, component );
    }

    /**
     * Returns available document data listeners.
     *
     * @return available document data listeners
     */
    @NotNull
    public List<DocumentDataListener> getListeners ()
    {
        return listeners != null ?
                CollectionUtils.asList ( listeners.getListeners ( DocumentDataListener.class ) ) :
                new ArrayList<DocumentDataListener> ();
    }

    /**
     * Adds document data listener.
     *
     * @param listener document data listener to add
     */
    public void addListener ( @NotNull final DocumentDataListener listener )
    {
        if ( listeners == null )
        {
            listeners = new EventListenerList ();
        }
        listeners.add ( DocumentDataListener.class, listener );
    }

    /**
     * Removes document data listener.
     *
     * @param listener document data listener to remove
     */
    public void removeListener ( @NotNull final DocumentDataListener listener )
    {
        if ( listeners != null )
        {
            listeners.remove ( DocumentDataListener.class, listener );
            if ( listeners.getListenerCount () == 0 )
            {
                listeners = null;
            }
        }
    }

    /**
     * Informs about data changes which affects document tab view.
     */
    public void fireTitleChanged ()
    {
        if ( listeners != null )
        {
            for ( final DocumentDataListener listener : listeners.getListeners ( DocumentDataListener.class ) )
            {
                listener.titleChanged ( this );
            }
        }
    }

    /**
     * Inform about tab background changes.
     *
     * @param oldBackground previous background color
     * @param newBackground new background color
     */
    public void fireBackgroundChanged ( final Color oldBackground, final Color newBackground )
    {
        if ( listeners != null )
        {
            for ( final DocumentDataListener listener : listeners.getListeners ( DocumentDataListener.class ) )
            {
                listener.backgroundChanged ( this, oldBackground, newBackground );
            }
        }
    }

    /**
     * Informs about tab component changes.
     *
     * @param oldComponent previous tab content
     * @param newComponent new tab content
     */
    public void fireContentChanged ( final Component oldComponent, final Component newComponent )
    {
        if ( listeners != null )
        {
            for ( final DocumentDataListener listener : listeners.getListeners ( DocumentDataListener.class ) )
            {
                listener.contentChanged ( this, oldComponent, newComponent );
            }
        }
    }
}