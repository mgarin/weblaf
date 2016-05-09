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

import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents basic data for single document opened in WebDocumentPane.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see com.alee.extended.tab.WebDocumentPane
 * @see com.alee.extended.tab.PaneData
 */

public class DocumentData<C extends Component>
{
    /**
     * Document data listeners.
     * Used to properly update WebDocumentPane view on document changes.
     */
    protected transient List<DocumentDataListener> listeners = new ArrayList<DocumentDataListener> ( 1 );

    /**
     * Document ID.
     * This ID have to be unique only within single WebDocumentPane instance.
     */
    protected String id;

    /**
     * Document icon.
     * Used to display icon on the document tab.
     */
    protected Icon icon;

    /**
     * Document title.
     * This can be a plain text or a language key.
     * Whether this is a plain text or a language key will be determined automatically, you only have to provide it.
     */
    protected String title;

    /**
     * Document title foreground color.
     * Used to color tab title text.
     */
    protected Color foreground;

    /**
     * Document tab background color.
     * Used to color tab and tab content background.
     */
    protected Color background;

    /**
     * Whether this document is closeable or not.
     * All documents are closeable by default, but you may change that.
     */
    protected boolean closeable;

    /**
     * Whether this document is draggable or not.
     * All documents are draggable by default, but you may change that.
     */
    protected boolean draggable;

    /**
     * Document content.
     * A component that represents document tab content.
     */
    protected C component;

    /**
     * Constructs new document.
     *
     * @param id        document ID
     * @param title     document title
     * @param component document content
     */
    public DocumentData ( final String id, final String title, final C component )
    {
        this ( id, null, title, null, component );
    }

    /**
     * Constructs new document.
     *
     * @param id        document ID
     * @param icon      document icon
     * @param component document content
     */
    public DocumentData ( final String id, final Icon icon, final C component )
    {
        this ( id, icon, null, null, component );
    }

    /**
     * Constructs new document.
     *
     * @param id        document ID
     * @param icon      document icon
     * @param title     document title
     * @param component document content
     */
    public DocumentData ( final String id, final Icon icon, final String title, final C component )
    {
        this ( id, icon, title, null, component );
    }

    /**
     * Constructs new document.
     *
     * @param id         document ID
     * @param icon       document icon
     * @param title      document title
     * @param background document tab background color
     * @param component  document content
     */
    public DocumentData ( final String id, final Icon icon, final String title, final Color background, final C component )
    {
        this ( id, icon, title, background, true, component );
    }

    /**
     * Constructs new document.
     *
     * @param id         document ID
     * @param icon       document icon
     * @param title      document title
     * @param background document tab background color
     * @param closeable  whether document is closeable or not
     * @param component  document content
     */
    public DocumentData ( final String id, final Icon icon, final String title, final Color background, final boolean closeable,
                          final C component )
    {
        this ( id, icon, title, background, closeable, true, component );
    }

    /**
     * Constructs new document.
     *
     * @param id         document ID
     * @param icon       document icon
     * @param title      document title
     * @param background document tab background color
     * @param closeable  whether document is closeable or not
     * @param draggable  whether document is draggable or not
     * @param component  document content
     */
    public DocumentData ( final String id, final Icon icon, final String title, final Color background, final boolean closeable,
                          final boolean draggable, final C component )
    {
        this ( id, icon, title, Color.BLACK, background, closeable, draggable, component );
    }

    /**
     * Constructs new document.
     *
     * @param id         document ID
     * @param icon       document icon
     * @param title      document title
     * @param foreground document title foreground color
     * @param background document tab background color
     * @param closeable  whether document is closeable or not
     * @param draggable  whether document is draggable or not
     * @param component  document content
     */
    public DocumentData ( final String id, final Icon icon, final String title, final Color foreground, final Color background,
                          final boolean closeable, final boolean draggable, final C component )
    {
        super ();
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.foreground = foreground;
        this.background = background;
        this.closeable = closeable;
        this.draggable = draggable;
        this.component = component;
    }

    /**
     * Returns document ID.
     *
     * @return document ID
     */
    public String getId ()
    {
        return id;
    }

    /**
     * Sets document ID.
     *
     * @param id new document ID
     */
    public void setId ( final String id )
    {
        this.id = id;
    }

    /**
     * Returns document icon.
     *
     * @return document icon
     */
    public Icon getIcon ()
    {
        return icon;
    }

    /**
     * Sets document icon.
     *
     * @param icon new document icon
     */
    public void setIcon ( final Icon icon )
    {
        this.icon = icon;
        fireTitleChanged ();
    }

    /**
     * Returns document title.
     *
     * @return document title
     */
    public String getTitle ()
    {
        return title;
    }

    /**
     * Sets document title.
     *
     * @param title new document title
     */
    public void setTitle ( final String title )
    {
        this.title = title;
        fireTitleChanged ();
    }

    /**
     * Returns document title text foreground color.
     *
     * @return document title text foreground color
     */
    public Color getForeground ()
    {
        return foreground;
    }

    /**
     * Sets document title text foreground color
     *
     * @param foreground document title text foreground color
     */
    public void setForeground ( final Color foreground )
    {
        this.foreground = foreground;
        fireTitleChanged ();
    }

    /**
     * Returns document tab background color.
     *
     * @return document tab background color
     */
    public Color getBackground ()
    {
        return background;
    }

    /**
     * Sets document tab background color.
     *
     * @param background document tab background color
     */
    public void setBackground ( final Color background )
    {
        final Color old = this.background;
        this.background = background;
        fireBackgroundChanged ( old, background );
    }

    /**
     * Returns whether document is closeable or not.
     *
     * @return true if document is closeable, false otherwise
     */
    public boolean isCloseable ()
    {
        return closeable;
    }

    /**
     * Sets whether document is closeable or not.
     *
     * @param closeable whether document is closeable or not
     */
    public void setCloseable ( final boolean closeable )
    {
        this.closeable = closeable;
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
     * Returns document content.
     *
     * @return document content
     */
    public C getComponent ()
    {
        return component;
    }

    /**
     * Sets document content.
     *
     * @param component new document content
     */
    public void setComponent ( final C component )
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
    public List<DocumentDataListener> getListeners ()
    {
        return CollectionUtils.copy ( listeners );
    }

    /**
     * Adds document data listener.
     *
     * @param listener document data listener to add
     */
    public void addListener ( final DocumentDataListener listener )
    {
        listeners.add ( listener );
    }

    /**
     * Removes document data listener.
     *
     * @param listener document data listener to remove
     */
    public void removeListener ( final DocumentDataListener listener )
    {
        listeners.remove ( listener );
    }

    /**
     * Informs about data changes which affects document tab view.
     */
    public void fireTitleChanged ()
    {
        for ( final DocumentDataListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.titleChanged ( this );
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
        for ( final DocumentDataListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.backgroundChanged ( this, oldBackground, newBackground );
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
        for ( final DocumentDataListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.contentChanged ( this, oldComponent, newComponent );
        }
    }
}