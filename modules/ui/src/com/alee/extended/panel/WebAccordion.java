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

package com.alee.extended.panel;

import com.alee.api.jdk.Supplier;
import com.alee.extended.layout.AccordionLayout;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.UISettingsManager;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Accordion groups separate collapsible panes into a single component.
 * It also has a few useful features like unified styling and selection mode.
 *
 * @author Mikle Garin
 */
public class WebAccordion extends WebPanel implements SwingConstants, SettingsMethods
{
    /**
     * todo 1. Separate UI and make use of new styling system
     */

    /**
     * Whether animate transition between states or not.
     */
    protected boolean animate = true;

    /**
     * Accordion orientation.
     */
    protected int orientation = SwingConstants.VERTICAL;

    /**
     * Whether accordion must fill all available space with expanded panes or not.
     */
    protected boolean fillSpace = true;

    /**
     * Whether multiple expanded panes are allowed or not.
     */
    protected boolean multipleSelectionAllowed = true;

    /**
     * Gap between panes for separated accordion style.
     */
    protected int gap = 0;

    /**
     * Accordion collapsible panes.
     */
    protected List<WebCollapsiblePane> panes = new ArrayList<WebCollapsiblePane> ();

    /**
     * Accordion collapsible pane state listeners.
     * These listeners required for some of accordion features.
     */
    protected transient List<CollapsiblePaneListener> stateListeners = new ArrayList<CollapsiblePaneListener> ();

    /**
     * Index of last expanded collapsible pane.
     */
    protected WebCollapsiblePane lastExpanded = null;

    /**
     * Constructs empty accordion with default style.
     */
    public WebAccordion ()
    {
        this ( StyleId.accordion );
    }

    /**
     * Constructs empty accordion with specified style.
     *
     * @param id style ID
     */
    public WebAccordion ( final StyleId id )
    {
        super ( id, new AccordionLayout () );
    }

    /**
     * Returns whether animate transition between states or not.
     *
     * @return true if transitions between states should be animated, false otherwise
     */
    public boolean isAnimate ()
    {
        return animate;
    }

    /**
     * Sets whether animate transition between states or not.
     *
     * @param animate whether animate transition between states or not
     */
    public void setAnimate ( final boolean animate )
    {
        this.animate = animate;
        updatePanesAnimation ();
    }

    /**
     * Updates collapsible panes animation property.
     */
    protected void updatePanesAnimation ()
    {
        for ( final WebCollapsiblePane pane : panes )
        {
            pane.setAnimate ( animate );
        }
    }

    /**
     * Returns accordion orientation.
     *
     * @return accordion orientation
     */
    public int getOrientation ()
    {
        return orientation;
    }

    /**
     * Sets accordion orientation.
     *
     * @param orientation new accordion orientation
     */
    public void setOrientation ( final int orientation )
    {
        this.orientation = orientation;
        revalidate ();
        repaint ();
    }

    /**
     * Returns whether accordion must fill all available space with expanded panes or not.
     *
     * @return whether accordion must fill all available space with expanded panes or not
     */
    public boolean isFillSpace ()
    {
        return fillSpace;
    }

    /**
     * Sets whether accordion must fill all available space with expanded panes or not.
     *
     * @param fillSpace whether accordion must fill all available space with expanded panes or not
     */
    public void setFillSpace ( final boolean fillSpace )
    {
        this.fillSpace = fillSpace;
        revalidate ();
    }

    /**
     * Returns whether multiple expanded panes are allowed or not.
     *
     * @return {@code true} if multiple expanded panes are allowed, {@code false} otherwise
     */
    public boolean isMultipleSelectionAllowed ()
    {
        return multipleSelectionAllowed;
    }

    /**
     * Sets whether multiple expanded panes are allowed or not.
     *
     * @param multipleSelectionAllowed whether multiple expanded panes are allowed or not
     */
    public void setMultipleSelectionAllowed ( final boolean multipleSelectionAllowed )
    {
        this.multipleSelectionAllowed = multipleSelectionAllowed;
        updateSelections ( -1, true );
    }

    /**
     * Updates panes selection states.
     *
     * @param index    index of the pane that will be left expanded in case multiple expanded panes are not allowed
     * @param collapse whether allow to collapse panes or not
     */
    protected void updateSelections ( int index, final boolean collapse )
    {
        boolean changed = false;
        if ( collapse )
        {
            if ( !multipleSelectionAllowed )
            {
                for ( int i = 0; i < panes.size (); i++ )
                {
                    final WebCollapsiblePane pane = panes.get ( i );
                    if ( index == -1 && pane.isExpanded () )
                    {
                        index = i;
                    }
                    if ( index != -1 && i != index && pane.isExpanded () )
                    {
                        changed = true;
                        pane.setExpanded ( false );
                    }
                }
            }
        }
        else
        {
            if ( getSelectionCount () == 0 )
            {
                lastExpanded.setExpanded ( true );
            }
        }

        // Notify about selection change
        if ( index != -1 || changed )
        {
            fireSelectionChanged ();
        }
    }

    /**
     * Returns gap between panes for separated accordion style.
     *
     * @return gap between panes for separated accordion style
     */
    public int getGap ()
    {
        return gap;
    }

    /**
     * Sets gap between panes for separated accordion style.
     *
     * @param gap new gap between panes for separated accordion style
     */
    public void setGap ( final int gap )
    {
        this.gap = gap;
        revalidate ();
    }

    /**
     * Adds new collapsible pane into accordion with the specified title and content.
     *
     * @param title   collapsible pane title
     * @param content collapsible pane content
     * @return new collapsible pane
     */
    public WebCollapsiblePane addPane ( final String title, final Component content )
    {
        return addPane ( panes.size (), title, content );
    }

    /**
     * Adds new collapsible pane into accordion with the specified title and content at the specified index.
     *
     * @param index   collapsible pane index
     * @param title   collapsible pane title
     * @param content collapsible pane content
     * @return new collapsible pane
     */
    public WebCollapsiblePane addPane ( final int index, final String title, final Component content )
    {
        return addPane ( index, new WebCollapsiblePane ( StyleId.accordionPane.at ( this ), title, content ) );
    }

    /**
     * Adds new collapsible pane into accordion with the specified icon, title and content.
     *
     * @param icon    collapsible pane icon
     * @param title   collapsible pane title
     * @param content collapsible pane content
     * @return new collapsible pane
     */
    public WebCollapsiblePane addPane ( final Icon icon, final String title, final Component content )
    {
        return addPane ( panes.size (), icon, title, content );
    }

    /**
     * Adds new collapsible pane into accordion with the specified icon, title and content at the specified index.
     *
     * @param index   collapsible pane index
     * @param icon    collapsible pane icon
     * @param title   collapsible pane title
     * @param content collapsible pane content
     * @return new collapsible pane
     */
    public WebCollapsiblePane addPane ( final int index, final Icon icon, final String title, final Component content )
    {
        return addPane ( index, new WebCollapsiblePane ( StyleId.accordionPane.at ( this ), icon, title, content ) );
    }

    /**
     * Adds new collapsible pane into accordion with the specified title component and content.
     *
     * @param title   collapsible pane title component
     * @param content collapsible pane content
     * @return new collapsible pane
     */
    public WebCollapsiblePane addPane ( final Component title, final Component content )
    {
        return addPane ( panes.size (), title, content );
    }

    /**
     * Adds new collapsible pane into accordion with the specified title component and content at the specified index.
     *
     * @param index   collapsible pane index
     * @param title   collapsible pane title component
     * @param content collapsible pane content
     * @return new collapsible pane
     */
    public WebCollapsiblePane addPane ( final int index, final Component title, final Component content )
    {
        final WebCollapsiblePane pane = new WebCollapsiblePane ( StyleId.accordionPane.at ( this ), content );
        pane.setTitleComponent ( title );
        return addPane ( index, pane );
    }

    /**
     * Adds collapsible pane into accordion at the specified index.
     *
     * @param index collapsible pane index
     * @param pane  collapsible pane to add
     * @return added collapsible pane
     */
    public WebCollapsiblePane addPane ( final int index, final WebCollapsiblePane pane )
    {
        // Title position
        if ( orientation == HORIZONTAL )
        {
            pane.setTitlePanePosition ( getComponentOrientation ().isLeftToRight () ? LEFT : RIGHT );
        }
        else
        {
            pane.setTitlePanePosition ( TOP );
        }

        // Animation
        pane.setAnimate ( animate );

        // Collapsing new pane if needed
        if ( !multipleSelectionAllowed && isAnySelected () )
        {
            pane.setExpanded ( false, false );
        }

        // State change enabler
        pane.setStateChangeHandler ( new Supplier<Boolean> ()
        {
            @Override
            public Boolean get ()
            {
                // Allow action if we are expanding pane
                if ( !pane.isExpanded () )
                {
                    return true;
                }

                // Allow collapse action if accordion does not fill space
                if ( !fillSpace )
                {
                    return true;
                }

                // Allow collapse action if there are other available expanded panes
                final int selectionCount = getSelectionCount ();
                if ( selectionCount > 1 )
                {
                    return true;
                }

                // Allow collapse action if we can expand previously collapsed pane instead of this one
                return selectionCount == 1 && lastExpanded != null && lastExpanded != pane;
            }
        } );

        // Adding new listener
        final CollapsiblePaneListener cpl = new CollapsiblePaneAdapter ()
        {
            @Override
            public void expanding ( final WebCollapsiblePane collapsiblePane )
            {
                // Update selected panes
                updateSelections ( panes.indexOf ( collapsiblePane ), true );
            }

            @Override
            public void collapsing ( final WebCollapsiblePane collapsiblePane )
            {
                // This hold additional events from firing when panes collapse due to panes selection mode
                if ( multipleSelectionAllowed || getSelectionCount () == 0 )
                {
                    // Update selected panes
                    updateSelections ( panes.indexOf ( collapsiblePane ), false );
                }

                // Update last selected
                lastExpanded = collapsiblePane;
            }
        };
        pane.addCollapsiblePaneListener ( cpl );
        stateListeners.add ( cpl );

        // Adding new pane
        add ( pane, index );
        panes.add ( index, pane );

        // Notify about selection change
        fireSelectionChanged ();

        return pane;
    }

    /**
     * Removes collapsible pane from the specified index from accordion.
     *
     * @param index collapsible pane index
     */
    public void removePane ( final int index )
    {
        removePane ( panes.get ( index ) );
    }

    /**
     * Removes collapsible pane from accordion.
     *
     * @param pane collapsible pane to remove
     */
    public void removePane ( final WebCollapsiblePane pane )
    {
        final int index = panes.indexOf ( pane );
        if ( index == -1 )
        {
            return;
        }

        // State change enabler
        pane.setStateChangeHandler ( null );

        // Removing pane listener
        pane.removeCollapsiblePaneListener ( stateListeners.get ( index ) );
        stateListeners.remove ( index );

        // Removing pane
        remove ( pane );
        panes.remove ( index );

        // Updating last expanded pane
        if ( pane == lastExpanded )
        {
            lastExpanded = null;
        }
    }

    /**
     * Returns list of available collapsible panes.
     *
     * @return list of available collapsible panes
     */
    public List<WebCollapsiblePane> getPanes ()
    {
        return CollectionUtils.copy ( panes );
    }

    /**
     * Returns collapsible pane at the specified index.
     *
     * @param index collapsible pane index
     * @return collapsible pane at the specified index
     */
    public WebCollapsiblePane getPane ( final int index )
    {
        return panes.get ( index );
    }

    /**
     * Returns whether any collapsible pane is expanded or not.
     *
     * @return true if any collapsible pane is expanded, false otherwise
     */
    public boolean isAnySelected ()
    {
        for ( final WebCollapsiblePane pane : panes )
        {
            if ( pane.isExpanded () )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns index of the first expanded collapsible pane or -1 if none expanded.
     *
     * @return index of the first expanded collapsible pane or -1 if none expanded
     */
    public int getFirstSelectedIndex ()
    {
        for ( final WebCollapsiblePane pane : panes )
        {
            if ( pane.isExpanded () )
            {
                return panes.indexOf ( pane );
            }
        }
        return -1;
    }

    /**
     * Returns amount of expanded collapsible panes.
     *
     * @return amount of expanded collapsible panes
     */
    public int getSelectionCount ()
    {
        int count = 0;
        for ( final WebCollapsiblePane pane : panes )
        {
            if ( pane.isExpanded () )
            {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns collapsible pane icon at the specified index.
     *
     * @param index collapsible pane index
     * @return collapsible pane icon at the specified index
     */
    public Icon getIconAt ( final int index )
    {
        return panes.get ( index ).getIcon ();
    }

    /**
     * Sets collapsible pane icon at the specified index.
     *
     * @param index collapsible pane index
     * @param icon  new collapsible pane icon
     */
    public void setIconAt ( final int index, final Icon icon )
    {
        panes.get ( index ).setIcon ( icon );
    }

    /**
     * Returns collapsible pane title at the specified index.
     *
     * @param index collapsible pane index
     * @return collapsible pane title at the specified index
     */
    public String getTitleAt ( final int index )
    {
        return panes.get ( index ).getTitle ();
    }

    /**
     * Sets  collapsible pane title at the specified index.
     *
     * @param index collapsible pane index
     * @param title new collapsible pane title
     */
    public void setTitleAt ( final int index, final String title )
    {
        panes.get ( index ).setTitle ( title );
    }

    /**
     * Returns collapsible pane title component at the specified index.
     *
     * @param index collapsible pane index
     * @return collapsible pane title component at the specified index
     */
    public Component getTitleComponentAt ( final int index )
    {
        return panes.get ( index ).getTitleComponent ();
    }

    /**
     * Sets collapsible pane title component at the specified index.
     *
     * @param index          collapsible pane index
     * @param titleComponent new collapsible pane title component
     */
    public void setTitleComponentAt ( final int index, final Component titleComponent )
    {
        panes.get ( index ).setTitleComponent ( titleComponent );
    }

    /**
     * Returns collapsible pane content at the specified index.
     *
     * @param index collapsible pane index
     * @return collapsible pane content at the specified index
     */
    public Component getContentAt ( final int index )
    {
        return panes.get ( index ).getContent ();
    }

    /**
     * Sets collapsible pane content at the specified index.
     *
     * @param index   collapsible pane index
     * @param content new collapsible pane content
     */
    public void setContentAt ( final int index, final Component content )
    {
        panes.get ( index ).setContent ( content );
    }

    /**
     * Returns selected collapsible panes.
     *
     * @return selected collapsible panes
     */
    public List<WebCollapsiblePane> getSelectedPanes ()
    {
        final List<WebCollapsiblePane> selectedPanes = new ArrayList<WebCollapsiblePane> ();
        for ( final WebCollapsiblePane pane : panes )
        {
            if ( pane.isExpanded () )
            {
                selectedPanes.add ( pane );
            }
        }
        return selectedPanes;
    }

    /**
     * Sets selected collapsible panes.
     *
     * @param selectedPanes selected collapsible panes
     */
    public void setSelectedPanes ( final List<WebCollapsiblePane> selectedPanes )
    {
        for ( final WebCollapsiblePane pane : panes )
        {
            pane.setExpanded ( selectedPanes != null && selectedPanes.contains ( pane ) );
        }
    }

    /**
     * Returns selected collapsible pane indices.
     *
     * @return selected collapsible pane indices
     */
    public List<Integer> getSelectedIndices ()
    {
        final List<Integer> selectedPanes = new ArrayList<Integer> ();
        for ( int i = 0; i < panes.size (); i++ )
        {
            if ( panes.get ( i ).isExpanded () )
            {
                selectedPanes.add ( i );
            }
        }
        return selectedPanes;
    }

    /**
     * Sets selected collapsible pane indices.
     *
     * @param indices selected collapsible pane indices
     */
    public void setSelectedIndices ( final List<Integer> indices )
    {
        for ( int i = 0; i < panes.size (); i++ )
        {
            panes.get ( i ).setExpanded ( indices != null && indices.contains ( i ) );
        }
    }

    /**
     * Adds accordion listener.
     *
     * @param listener accordion listener to add
     */
    public void addAccordionListener ( final AccordionListener listener )
    {
        listenerList.add ( AccordionListener.class, listener );
    }

    /**
     * Removes collapsible pane listener.
     *
     * @param listener collapsible pane listener to remove
     */
    public void removeAccordionListener ( final AccordionListener listener )
    {
        listenerList.remove ( AccordionListener.class, listener );
    }

    /**
     * Notifies when collapsible pane starts to expand.
     */
    protected void fireSelectionChanged ()
    {
        for ( final AccordionListener listener : listenerList.getListeners ( AccordionListener.class ) )
        {
            listener.selectionChanged ();
        }
    }

    @Override
    public void registerSettings ( final Configuration configuration )
    {
        UISettingsManager.registerComponent ( this, configuration );
    }

    @Override
    public void registerSettings ( final SettingsProcessor processor )
    {
        UISettingsManager.registerComponent ( this, processor );
    }

    @Override
    public void unregisterSettings ()
    {
        UISettingsManager.unregisterComponent ( this );
    }

    @Override
    public void loadSettings ()
    {
        UISettingsManager.loadSettings ( this );
    }

    @Override
    public void saveSettings ()
    {
        UISettingsManager.saveSettings ( this );
    }
}