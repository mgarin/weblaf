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

package com.alee.extended.overlay;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.extended.WebContainer;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.utils.CollectionUtils;
import com.alee.utils.ProprietaryUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link WebOverlay} is a custom container that allows overlaying it's content {@link JComponent} with other {@link JComponent}s.
 * There can only be one content {@link JComponent}, but amount of overlaying {@link JComponent}s is not limited.
 *
 * @author Mikle Garin
 * @see #setContent(JComponent)
 * @see #addOverlay(Overlay)
 * @see #removeOverlay(Overlay)
 */
public class WebOverlay extends WebContainer<WebOverlay, WOverlayUI>
{
    /**
     * Overlays property.
     */
    public static final String CONTENT_PROPERTY = "content";

    /**
     * Content {@link JComponent}.
     */
    @Nullable
    protected JComponent content;

    /**
     * {@link List} of {@link Overlay}s.
     */
    @NotNull
    protected List<Overlay> overlays;

    /**
     * Constructs new {@link WebOverlay}.
     */
    public WebOverlay ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new {@link WebOverlay}.
     *
     * @param content {@link JComponent} content
     */
    public WebOverlay ( @Nullable final JComponent content )
    {
        this ( StyleId.auto, content );
    }

    /**
     * Constructs new {@link WebOverlay}.
     *
     * @param id {@link StyleId}
     */
    public WebOverlay ( @NotNull final StyleId id )
    {
        this ( id, null );
    }

    /**
     * Constructs new {@link WebOverlay}.
     *
     * @param id      {@link StyleId}
     * @param content {@link JComponent} content
     */
    public WebOverlay ( @NotNull final StyleId id, @Nullable final JComponent content )
    {
        super ();
        this.overlays = new ArrayList<Overlay> ();
        ProprietaryUtils.enableMixingCutoutShape ( this );
        updateUI ();
        setStyleId ( id );
        setContent ( content );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.overlay;
    }

    @Nullable
    @Override
    public OverlayLayout getLayout ()
    {
        return ( OverlayLayout ) super.getLayout ();
    }

    @Override
    public void setLayout ( @Nullable final LayoutManager layout )
    {
        if ( layout == null || layout instanceof OverlayLayout )
        {
            super.setLayout ( layout );
        }
        else
        {
            throw new IllegalArgumentException ( "Only OverlayLayout instances are supported" );
        }
    }

    @NotNull
    @Override
    public JComponent getComponent ( final int index )
    {
        return ( JComponent ) super.getComponent ( index );
    }

    /**
     * Returns content {@link JComponent}.
     *
     * @return content {@link JComponent}
     */
    @Nullable
    public JComponent getContent ()
    {
        return content;
    }

    /**
     * Changes content to the specified {@link JComponent}.
     *
     * @param content content {@link JComponent}
     */
    public void setContent ( @Nullable final JComponent content )
    {
        add ( content );
    }

    @Override
    protected void addImpl ( @Nullable final Component component, @Nullable final Object constraints, final int index )
    {
        if ( component == null || component instanceof JComponent )
        {
            // Check if new content is being added
            final boolean contentBeingAdded = component == null || findOverlay ( ( JComponent ) component ) == null;

            // Removing other content if it exists
            final JComponent oldContent = this.content;
            if ( contentBeingAdded && oldContent != null )
            {
                final int contentIndex = getComponentZOrder ( oldContent );
                super.remove ( contentIndex );
            }

            // Addding content or overlay component
            if ( component != null )
            {
                super.addImpl ( component, constraints, index );
            }

            // Updating content information
            if ( contentBeingAdded )
            {
                // Updating content reference
                this.content = ( JComponent ) component;

                // Firing content change event
                firePropertyChange ( CONTENT_PROPERTY, oldContent, component );
            }
        }
        else
        {
            throw new RuntimeException ( "WebOverlay doesn't support AWT components" );
        }
    }

    @Override
    public void remove ( final int index )
    {
        final JComponent component = getComponent ( index );

        // Removing overlay information
        final Overlay overlay = findOverlay ( component );
        if ( overlay != null )
        {
            overlays.remove ( overlay );
        }

        // Adding content or overlay component
        super.remove ( index );

        // Updating content information
        if ( overlay == null )
        {
            // Updating content reference
            this.content = null;

            // Firing content change event
            firePropertyChange ( CONTENT_PROPERTY, component, null );
        }
    }

    /**
     * Returns amount of added {@link Overlay}s.
     *
     * @return amount of added {@link Overlay}s
     */
    public int getOverlayCount ()
    {
        return overlays.size ();
    }

    /**
     * Returns {@link List} of added {@link Overlay}s.
     *
     * @return {@link List} of added {@link Overlay}s
     */
    @NotNull
    public List<Overlay> getOverlays ()
    {
        return CollectionUtils.copy ( overlays );
    }

    /**
     * Adds specified {@link Overlay}.
     *
     * @param overlay {@link Overlay} to add
     */
    public void addOverlay ( @NotNull final Overlay overlay )
    {
        removeOverlay ( overlay );

        overlays.add ( overlay );
        add ( overlay.component () );

        revalidate ();
        repaint ();
    }

    /**
     * Removes {@link Overlay} with the specified {@link JComponent}.
     *
     * @param component overlay {@link JComponent}
     */
    public void removeOverlay ( @NotNull final JComponent component )
    {
        remove ( component );

        revalidate ();
        repaint ();
    }

    /**
     * Removes specified {@link Overlay}.
     *
     * @param overlay {@link Overlay} to remove
     */
    public void removeOverlay ( @NotNull final Overlay overlay )
    {
        remove ( overlay.component () );

        revalidate ();
        repaint ();
    }

    /**
     * Returns {@link Overlay} for the specified overlay {@link JComponent}.
     *
     * @param component overlay {@link JComponent}
     * @return {@link Overlay} for the specified overlay {@link JComponent}
     */
    @Nullable
    public Overlay findOverlay ( @NotNull final JComponent component )
    {
        Overlay result = null;
        for ( final Overlay overlay : overlays )
        {
            if ( overlay.component () == component )
            {
                result = overlay;
                break;
            }
        }
        return result;
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WOverlayUI} object that renders this component
     */
    public WOverlayUI getUI ()
    {
        return ( WOverlayUI ) ui;
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WOverlayUI}
     */
    public void setUI ( final WOverlayUI ui )
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