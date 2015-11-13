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

package com.alee.managers.style;

import com.alee.extended.painter.Painter;
import com.alee.managers.style.skin.Skin;
import com.alee.managers.style.skin.StyleListener;
import com.alee.utils.CompareUtils;

import javax.swing.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This object contains style data for single component instance.
 * This is basically all {@link com.alee.managers.style.StyleManager} knows about the component styling.
 *
 * @author Mikle Garin
 */

public final class StyleData
{
    /**
     * Applied skin.
     */
    private Skin skin;

    /**
     * Style ID.
     */
    private StyleId styleId;

    /**
     * Custom painters.
     */
    private Map<String, Painter> painters;

    /**
     * Related style children.
     */
    private List<WeakReference<JComponent>> children;

    /**
     * Skin change listeners.
     */
    private List<StyleListener> listeners;

    /**
     * Constructs new empty style data object.
     */
    public StyleData ()
    {
        super ();
        this.skin = null;
        this.styleId = null;
        this.painters = null;
        this.children = null;
        this.listeners = null;
    }

    /**
     * Returns currently applied skin.
     *
     * @return currently applied skin
     */
    public Skin getSkin ()
    {
        return skin;
    }

    /**
     * Applies new component skin and returns previously applied skin.
     *
     * @param component       component to apply skin to
     * @param skin            skin to apply
     * @param applyToChildren whether or not should apply the same skin to style children
     * @return previously applied skin
     */
    public Skin applySkin ( final JComponent component, final Skin skin, final boolean applyToChildren )
    {
        // Checking that provided skin is actually different one
        final boolean newSkin = skin != getSkin ();

        // Saving reference to old skin
        final Skin oldSkin;
        if ( newSkin )
        {
            // Removing old skin
            oldSkin = removeSkin ( component );
        }
        else
        {
            // Simply providing current skin
            oldSkin = getSkin ();
        }

        // Applying new skin to specified component
        if ( newSkin && skin != null )
        {
            skin.applySkin ( component );
            this.skin = skin;
        }

        // Applying skin to component's style children
        if ( skin != null && applyToChildren )
        {
            applyChildrenSkin ( skin );
        }

        // Informing about skin changes
        if ( newSkin )
        {
            // Informing about skin changes
            fireSkinChanged ( component, oldSkin, skin );

            // Informing about skin visual update
            fireSkinUpdated ( component, getStyleId () );
        }

        return oldSkin;
    }

    /**
     * Applies skin to style children.
     *
     * @param skin skin to apply
     */
    private void applyChildrenSkin ( final Skin skin )
    {
        if ( children != null )
        {
            for ( final WeakReference<JComponent> reference : children )
            {
                final JComponent child = reference.get ();
                if ( child != null )
                {
                    StyleManager.setSkin ( child, skin );
                }
            }
        }
    }

    /**
     * Updates current skin in the skinnable component.
     * <p/>
     * This method is used only to properly update skin on various changes.
     * It is not recommended to use it outside of style manager behavior.
     *
     * @param component component to update skin for
     */
    public void updateSkin ( final JComponent component )
    {
        // Updating component skin
        getSkin ().updateSkin ( component );

        // Updating children skins
        if ( children != null )
        {
            for ( final WeakReference<JComponent> reference : children )
            {
                final JComponent child = reference.get ();
                if ( child != null )
                {
                    StyleManager.updateSkin ( child );
                }
            }
        }

        // Informing about skin visual update
        fireSkinUpdated ( component, getStyleId () );
    }

    /**
     * Removes skin currently applied to the specified component.
     *
     * @param component component to remove skin for
     * @return previously applied skin
     */
    public Skin removeSkin ( final JComponent component )
    {
        final Skin oldSkin = this.skin;
        if ( this.skin != null )
        {
            this.skin.removeSkin ( component );
            this.skin = null;
        }
        return oldSkin;
    }

    /**
     * Returns currently used style ID.
     *
     * @return currently used style ID
     */
    public StyleId getStyleId ()
    {
        return styleId;
    }

    /**
     * Sets currently used style ID.
     *
     * @param component component to change style ID for
     * @param styleId   new style ID
     */
    public void setStyleId ( final JComponent component, final StyleId styleId )
    {
        if ( !CompareUtils.equals ( this.styleId, styleId ) )
        {
            // Saving old style ID reference
            final StyleId oldStyleId = this.styleId;

            // Saving new style ID
            this.styleId = styleId;

            // Updating component skin
            updateSkin ( component );

            // Informing about style change
            fireStyleChanged ( component, oldStyleId, styleId );
        }
    }

    /**
     * Returns custom painters.
     *
     * @return custom painters
     */
    public Map<String, Painter> getPainters ()
    {
        return painters;
    }

    /**
     * Sets custom painters.
     *
     * @param painters custom painters
     */
    public void setPainters ( final Map<String, Painter> painters )
    {
        this.painters = painters;
    }

    /**
     * Returns related style children.
     *
     * @return related style children
     */
    public List<WeakReference<JComponent>> getChildren ()
    {
        return children;
    }

    /**
     * Adds related style child.
     *
     * @param child related style child
     */
    public void addChild ( final JComponent child )
    {
        if ( children == null )
        {
            children = new ArrayList<WeakReference<JComponent>> ( 1 );
        }
        children.add ( new WeakReference<JComponent> ( child ) );
    }

    /**
     * Removes related style child.
     *
     * @param child related style child
     */
    public void removeChild ( final JComponent child )
    {
        final Iterator<WeakReference<JComponent>> iterator = children.iterator ();
        while ( iterator.hasNext () )
        {
            final WeakReference<JComponent> next = iterator.next ();
            if ( next.get () == child )
            {
                iterator.remove ();
            }
        }
    }

    /**
     * Adds style change listener.
     *
     * @param listener style change listener to add
     */
    public void addStyleListener ( final StyleListener listener )
    {
        if ( listeners == null )
        {
            listeners = new ArrayList<StyleListener> ( 1 );
        }
        listeners.add ( listener );
    }

    /**
     * Removes style change listener.
     *
     * @param listener style change listener to remove
     */
    public void removeStyleListener ( final StyleListener listener )
    {
        if ( listeners != null )
        {
            listeners.remove ( listener );
        }
    }

    /**
     * Informs about component skin change.
     *
     * @param component component which style has changed
     * @param oldSkin   previously used skin
     * @param newSkin   currently used skin
     */
    private void fireSkinChanged ( final JComponent component, final Skin oldSkin, final Skin newSkin )
    {
        if ( listeners != null )
        {
            for ( final StyleListener listener : listeners )
            {
                listener.skinChanged ( component, oldSkin, newSkin );
            }
        }
    }

    /**
     * Informs about component style change.
     *
     * @param component  component which style has changed
     * @param oldStyleId previously used style ID
     * @param newStyleId currently used style ID
     */
    private void fireStyleChanged ( final JComponent component, final StyleId oldStyleId, final StyleId newStyleId )
    {
        if ( listeners != null )
        {
            for ( final StyleListener listener : listeners )
            {
                listener.styleChanged ( component, oldStyleId, newStyleId );
            }
        }
    }

    /**
     * Informs about component skin visual update.
     * Skin update might occur when component style ID changes or its parent style component style ID changes.
     *
     * @param component component which style have been updated
     * @param styleId   component style ID
     */
    private void fireSkinUpdated ( final JComponent component, final StyleId styleId )
    {
        if ( listeners != null )
        {
            for ( final StyleListener listener : listeners )
            {
                listener.skinUpdated ( component, styleId );
            }
        }
    }
}