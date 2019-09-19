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

package com.alee.extended.behavior;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.laf.WebLookAndFeel;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;

/**
 * Custom {@link Behavior} that performs {@link Document} changes tracking.
 * You need to specify {@link JTextComponent} which {@link Document} changes will be tracked.
 * Use {@link #install()} and {@link #uninstall()} methods to setup and remove this behavior.
 *
 * @param <C> {@link JTextComponent} type
 * @author Mikle Garin
 */
public abstract class DocumentChangeBehavior<C extends JTextComponent> implements DocumentListener, PropertyChangeListener, Behavior
{
    /**
     * Weak reference to {@link JTextComponent} to track {@link Document} changes for.
     */
    @NotNull
    protected final WeakReference<C> textComponent;

    /**
     * Constructs new {@link DocumentChangeBehavior} for the specified {@link JTextComponent}.
     *
     * @param textComponent {@link JTextComponent} to track {@link Document} changes for
     */
    public DocumentChangeBehavior ( @NotNull final C textComponent )
    {
        this.textComponent = new WeakReference<C> ( textComponent );
    }

    /**
     * Installs behavior into component.
     *
     * @return this behavior
     */
    public DocumentChangeBehavior<C> install ()
    {
        final JTextComponent jTextComponent = textComponent.get ();
        if ( jTextComponent != null )
        {
            jTextComponent.getDocument ().addDocumentListener ( this );
            jTextComponent.addPropertyChangeListener ( WebLookAndFeel.DOCUMENT_PROPERTY, this );
        }
        return this;
    }

    /**
     * Uninstalls behavior from the component.
     *
     * @return this behavior
     */
    public DocumentChangeBehavior<C> uninstall ()
    {
        final C jTextComponent = textComponent.get ();
        if ( jTextComponent != null )
        {
            jTextComponent.removePropertyChangeListener ( WebLookAndFeel.DOCUMENT_PROPERTY, this );
            jTextComponent.getDocument ().removeDocumentListener ( this );
        }
        return this;
    }

    @Override
    public void propertyChange ( @NotNull final PropertyChangeEvent e )
    {
        final Object oldDocument = e.getOldValue ();
        if ( oldDocument != null && oldDocument instanceof Document )
        {
            ( ( Document ) oldDocument ).removeDocumentListener ( this );
        }
        final Object newDocument = e.getNewValue ();
        if ( newDocument != null && newDocument instanceof Document )
        {
            ( ( Document ) newDocument ).addDocumentListener ( this );
        }
        fireDocumentChanged ( null );
    }

    @Override
    public void insertUpdate ( @NotNull final DocumentEvent e )
    {
        fireDocumentChanged ( e );
    }

    @Override
    public void removeUpdate ( @NotNull final DocumentEvent e )
    {
        fireDocumentChanged ( e );
    }

    @Override
    public void changedUpdate ( @NotNull final DocumentEvent e )
    {
        fireDocumentChanged ( e );
    }

    /**
     * Fires {@link Document} change event if {@link JTextComponent} is still available.
     *
     * @param event occured document event, {@code null} if the whole {@link Document} was replaced
     */
    protected void fireDocumentChanged ( @Nullable final DocumentEvent event )
    {
        final C component = textComponent.get ();
        if ( component != null )
        {
            documentChanged ( component, event );
        }
    }

    /**
     * Informs that {@link Document} has changed in some way.
     *
     * @param component {@link JTextComponent} containing changed document
     * @param event     occured document event, {@code null} if the whole {@link Document} was replaced
     */
    public abstract void documentChanged ( @NotNull C component, @Nullable DocumentEvent event );
}