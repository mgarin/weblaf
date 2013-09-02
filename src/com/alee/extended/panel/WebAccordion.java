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

import com.alee.extended.layout.AccordionLayout;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.panel.WebPanelStyle;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.utils.CollectionUtils;
import com.alee.utils.swing.DataProvider;

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
     * Whether animate transition between states or not.
     */
    private boolean animate = WebAccordionStyle.animate;

    /**
     * Accordion style.
     */
    private AccordionStyle accordionStyle = WebAccordionStyle.accordionStyle;

    /**
     * Accordion orientation.
     */
    private int orientation = WebAccordionStyle.orientation;

    /**
     * Collapsed state icon.
     */
    private ImageIcon expandIcon = WebAccordionStyle.expandIcon;

    /**
     * Expanded state icon.
     */
    private ImageIcon collapseIcon = WebAccordionStyle.collapseIcon;

    /**
     * Whether accordion must fill all available space with expanded panes or not.
     */
    private boolean fillSpace = WebAccordionStyle.fillSpace;

    /**
     * Whether multiply expanded panes are allowed or not.
     */
    private boolean multiplySelectionAllowed = WebAccordionStyle.multiplySelectionAllowed;

    /**
     * Gap between panes for separated accordion style.
     */
    private int gap = WebAccordionStyle.gap;

    /**
     * Accordion collapsible pane listeners.
     */
    private List<AccordionListener> listeners = new ArrayList<AccordionListener> ( 1 );

    /**
     * Accordion collapsible panes.
     */
    private List<WebCollapsiblePane> panes = new ArrayList<WebCollapsiblePane> ();

    /**
     * Accordion collapsible pane state listeners.
     * These are private listeners required for some of accordion features.
     */
    private List<CollapsiblePaneListener> stateListeners = new ArrayList<CollapsiblePaneListener> ();

    /**
     * Constructs empty accordion with default style.
     */
    public WebAccordion ()
    {
        super ();
        initializeDefaultSettings ( WebAccordionStyle.accordionStyle );
    }

    /**
     * Constructs empty accordion with the specified style.
     *
     * @param accordionStyle
     */
    public WebAccordion ( AccordionStyle accordionStyle )
    {
        super ();
        initializeDefaultSettings ( accordionStyle );
    }

    /**
     * Initializes default accordion settings.
     */
    protected void initializeDefaultSettings ( AccordionStyle accordionStyle )
    {
        setDrawFocus ( true );
        setWebColored ( false );
        setLayout ( new AccordionLayout ( this ) );
        setAccordionStyle ( accordionStyle );
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
    public void setAnimate ( boolean animate )
    {
        this.animate = animate;
        updatePanesAnimation ();
    }

    /**
     * Updates collapsible panes animation property.
     */
    private void updatePanesAnimation ()
    {
        for ( WebCollapsiblePane pane : panes )
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
    public void setOrientation ( int orientation )
    {
        this.orientation = orientation;
        updatePanesBorderStyling ();
    }

    /**
     * Returns collapsed state icon.
     *
     * @return collapsed state icon
     */
    public ImageIcon getExpandIcon ()
    {
        return expandIcon;
    }

    /**
     * Sets collapsed state icon.
     *
     * @param expandIcon new collapsed state icon
     */
    public void setExpandIcon ( ImageIcon expandIcon )
    {
        this.expandIcon = expandIcon;
        updatePaneIcons ();
    }

    /**
     * Returns expanded state icon.
     *
     * @return expanded state icon
     */
    public ImageIcon getCollapseIcon ()
    {
        return collapseIcon;
    }

    /**
     * Sets expanded state icon.
     *
     * @param collapseIcon new expanded state icon
     */
    public void setCollapseIcon ( ImageIcon collapseIcon )
    {
        this.collapseIcon = collapseIcon;
        updatePaneIcons ();
    }

    /**
     * Updates collapsible pane icons.
     */
    private void updatePaneIcons ()
    {
        for ( WebCollapsiblePane pane : panes )
        {
            pane.setExpandIcon ( expandIcon );
            pane.setCollapseIcon ( collapseIcon );
        }
    }

    /**
     * Returns accordion style.
     *
     * @return accordion style
     */
    public AccordionStyle getAccordionStyle ()
    {
        return accordionStyle;
    }

    /**
     * Sets accordion style.
     *
     * @param accordionStyle new accordion style
     */
    public void setAccordionStyle ( AccordionStyle accordionStyle )
    {
        this.accordionStyle = accordionStyle;
        updatePanesBorderStyling ();
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
    public void setFillSpace ( boolean fillSpace )
    {
        this.fillSpace = fillSpace;
        revalidate ();
    }

    /**
     * Returns whether multiply expanded panes are allowed or not.
     *
     * @return whether multiply expanded panes are allowed or not
     */
    public boolean isMultiplySelectionAllowed ()
    {
        return multiplySelectionAllowed;
    }

    /**
     * Sets whether multiply expanded panes are allowed or not.
     *
     * @param multiplySelectionAllowed whether multiply expanded panes are allowed or not
     */
    public void setMultiplySelectionAllowed ( boolean multiplySelectionAllowed )
    {
        this.multiplySelectionAllowed = multiplySelectionAllowed;
        updateSelections ( -1, true );
    }

    /**
     * Updates panes selection states.
     *
     * @param index    index of the pane that will be left expanded in case multiply expanded panes are not allowed
     * @param collapse whether allow to collapse panes or not
     */
    private void updateSelections ( int index, boolean collapse )
    {
        boolean changed = false;
        if ( collapse && !multiplySelectionAllowed )
        {
            for ( int i = 0; i < panes.size (); i++ )
            {
                WebCollapsiblePane pane = panes.get ( i );
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
    public void setGap ( int gap )
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
    public WebCollapsiblePane addPane ( String title, Component content )
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
    public WebCollapsiblePane addPane ( int index, String title, Component content )
    {
        return addPane ( index, new WebCollapsiblePane ( title, content ) );
    }

    /**
     * Adds new collapsible pane into accordion with the specified icon, title and content.
     *
     * @param icon    collapsible pane icon
     * @param title   collapsible pane title
     * @param content collapsible pane content
     * @return new collapsible pane
     */
    public WebCollapsiblePane addPane ( Icon icon, String title, Component content )
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
    public WebCollapsiblePane addPane ( int index, Icon icon, String title, Component content )
    {
        return addPane ( index, new WebCollapsiblePane ( icon, title, content ) );
    }

    /**
     * Adds new collapsible pane into accordion with the specified title component and content.
     *
     * @param title   collapsible pane title component
     * @param content collapsible pane content
     * @return new collapsible pane
     */
    public WebCollapsiblePane addPane ( Component title, Component content )
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
    public WebCollapsiblePane addPane ( int index, Component title, Component content )
    {
        WebCollapsiblePane pane = new WebCollapsiblePane ( "", content );
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
    private WebCollapsiblePane addPane ( int index, final WebCollapsiblePane pane )
    {
        // Animation
        pane.setAnimate ( animate );

        // Proper icons
        pane.setExpandIcon ( expandIcon );
        pane.setCollapseIcon ( collapseIcon );

        // Collapsing new pane if needed
        if ( !multiplySelectionAllowed && isAnySelected () )
        {
            pane.setExpanded ( false, false );
        }

        // State change enabler
        pane.setStateChangeHandler ( new DataProvider<Boolean> ()
        {
            @Override
            public Boolean provide ()
            {
                return !fillSpace || !pane.isExpanded () || getSelectionCount () > 1;
            }
        } );

        // Adding new listener
        CollapsiblePaneListener cpl = new CollapsiblePaneAdapter ()
        {
            @Override
            public void expanding ( WebCollapsiblePane pane )
            {
                // Update selected panes
                updateSelections ( panes.indexOf ( pane ), true );
            }

            @Override
            public void collapsing ( WebCollapsiblePane pane )
            {
                // This hold additional events from firing when panes collapse due to panes selection mode
                if ( multiplySelectionAllowed || getSelectionCount () == 0 )
                {
                    // Update selected panes
                    updateSelections ( panes.indexOf ( pane ), false );
                }
            }
        };
        pane.addCollapsiblePaneListener ( cpl );
        stateListeners.add ( cpl );

        // Adding new pane
        add ( index, pane );
        panes.add ( index, pane );

        // Updating accordion
        updatePanesBorderStyling ();

        // Notify about selection change
        fireSelectionChanged ();

        return pane;
    }

    /**
     * Removes collapsible pane from the specified index from accordion.
     *
     * @param index collapsible pane index
     */
    public void removePane ( int index )
    {
        removePane ( panes.get ( index ) );
    }

    /**
     * Removes collapsible pane from accordion.
     *
     * @param pane collapsible pane to remove
     */
    private void removePane ( WebCollapsiblePane pane )
    {
        int index = panes.indexOf ( pane );
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

        // Updating accordion
        updatePanesBorderStyling ();
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
     * Returns actual list of available collapsible panes.
     * Be aware that accordion might be corrupted if you modify this list directly.
     *
     * @return actual list of available collapsible panes
     */
    public List<WebCollapsiblePane> getActualPanesList ()
    {
        return panes;
    }

    /**
     * Returns collapsible pane at the specified index.
     *
     * @param index collapsible pane index
     * @return collapsible pane at the specified index
     */
    public WebCollapsiblePane getPane ( int index )
    {
        return panes.get ( index );
    }

    /**
     * Updates panes styling according to accordion settings.
     */
    private void updatePanesBorderStyling ()
    {
        boolean united = accordionStyle.equals ( AccordionStyle.united );
        boolean separated = accordionStyle.equals ( AccordionStyle.separated );
        boolean hor = orientation == HORIZONTAL;

        // Accordion decoration
        setUndecorated ( !united );

        // Panes decoration
        for ( int i = 0; i < panes.size (); i++ )
        {
            WebCollapsiblePane pane = panes.get ( i );
            pane.setTitlePanePostion ( hor ? LEFT : TOP );
            if ( separated )
            {
                pane.setShadeWidth ( WebPanelStyle.shadeWidth );
                pane.setDrawSides ( separated, separated, separated, separated );
            }
            else
            {
                pane.setShadeWidth ( 0 );
                pane.setDrawSides ( !hor && i > 0, hor && i > 0, false, false );
            }
        }

        // Updating accordion
        revalidate ();
        repaint ();
    }

    /**
     * Returns whether any collapsible pane is expanded or not.
     *
     * @return true if any collapsible pane is expanded, false otherwise
     */
    public boolean isAnySelected ()
    {
        for ( WebCollapsiblePane pane : panes )
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
        for ( WebCollapsiblePane pane : panes )
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
        for ( WebCollapsiblePane pane : panes )
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
    public Icon getIconAt ( int index )
    {
        return panes.get ( index ).getIcon ();
    }

    /**
     * Sets collapsible pane icon at the specified index.
     *
     * @param index collapsible pane index
     * @param icon  new collapsible pane icon
     */
    public void setIconAt ( int index, Icon icon )
    {
        panes.get ( index ).setIcon ( icon );
    }

    /**
     * Returns collapsible pane title at the specified index.
     *
     * @param index collapsible pane index
     * @return collapsible pane title at the specified index
     */
    public String getTitleAt ( int index )
    {
        return panes.get ( index ).getTitle ();
    }

    /**
     * Sets  collapsible pane title at the specified index.
     *
     * @param index collapsible pane index
     * @param title new collapsible pane title
     */
    public void setTitleAt ( int index, String title )
    {
        panes.get ( index ).setTitle ( title );
    }

    /**
     * Returns collapsible pane title component at the specified index.
     *
     * @param index collapsible pane index
     * @return collapsible pane title component at the specified index
     */
    public Component getTitleComponentAt ( int index )
    {
        return panes.get ( index ).getTitleComponent ();
    }

    /**
     * Sets collapsible pane title component at the specified index.
     *
     * @param index          collapsible pane index
     * @param titleComponent new collapsible pane title component
     */
    public void setTitleComponentAt ( int index, Component titleComponent )
    {
        panes.get ( index ).setTitleComponent ( titleComponent );
    }

    /**
     * Returns collapsible pane content at the specified index.
     *
     * @param index collapsible pane index
     * @return collapsible pane content at the specified index
     */
    public Component getContentAt ( int index )
    {
        return panes.get ( index ).getContent ();
    }

    /**
     * Sets collapsible pane content at the specified index.
     *
     * @param index   collapsible pane index
     * @param content new collapsible pane content
     */
    public void setContentAt ( int index, Component content )
    {
        panes.get ( index ).setContent ( content );
    }

    /**
     * Returns collapsible pane margin at the specified index.
     *
     * @param index collapsible pane index
     * @return collapsible pane margin at the specified index
     */
    public Insets getContentMarginAt ( int index )
    {
        return panes.get ( index ).getContentMargin ();
    }

    /**
     * Sets collapsible pane margin at the specified index.
     *
     * @param index  collapsible pane index
     * @param margin new collapsible pane margin
     */
    public void setContentMarginAt ( int index, Insets margin )
    {
        panes.get ( index ).setContentMargin ( margin );
    }

    /**
     * Sets collapsible pane margin at the specified index.
     *
     * @param index  collapsible pane index
     * @param top    new collapsible pane top margin
     * @param left   new collapsible pane left margin
     * @param bottom new collapsible pane bottom margin
     * @param right  new collapsible pane right margin
     */
    public void setContentMarginAt ( int index, int top, int left, int bottom, int right )
    {
        setContentMarginAt ( index, new Insets ( top, left, bottom, right ) );
    }

    /**
     * Sets collapsible pane margin at the specified index.
     *
     * @param index  collapsible pane index
     * @param margin new collapsible pane margin
     */
    public void setContentMarginAt ( int index, int margin )
    {
        setContentMarginAt ( index, margin, margin, margin, margin );
    }

    /**
     * Returns selected collapsible panes.
     *
     * @return selected collapsible panes
     */
    public List<WebCollapsiblePane> getSelectedPanes ()
    {
        List<WebCollapsiblePane> selectedPanes = new ArrayList<WebCollapsiblePane> ();
        for ( WebCollapsiblePane pane : panes )
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
    public void setSelectedPanes ( List<WebCollapsiblePane> selectedPanes )
    {
        for ( WebCollapsiblePane pane : panes )
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
        List<Integer> selectedPanes = new ArrayList<Integer> ();
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
    public void setSelectedIndices ( List<Integer> indices )
    {
        for ( int i = 0; i < panes.size (); i++ )
        {
            panes.get ( i ).setExpanded ( indices != null && indices.contains ( i ) );
        }
    }

    /**
     * Returns accordion listeners.
     *
     * @return accordion listeners
     */
    public List<AccordionListener> getAccordionListeners ()
    {
        return CollectionUtils.copy ( listeners );
    }

    /**
     * Sets accordion listeners.
     *
     * @param listeners accordion listeners
     */
    public void setAccordionListeners ( List<AccordionListener> listeners )
    {
        this.listeners = listeners;
    }

    /**
     * Adds accordion listener.
     *
     * @param listener accordion listener to add
     */
    public void addAccordionListener ( AccordionListener listener )
    {
        listeners.add ( listener );
    }

    /**
     * Removes collapsible pane listener.
     *
     * @param listener collapsible pane listener to remove
     */
    public void removeAccordionListener ( AccordionListener listener )
    {
        listeners.remove ( listener );
    }

    /**
     * Notifies when collapsible pane starts to expand.
     */
    private void fireSelectionChanged ()
    {
        for ( AccordionListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.selectionChanged ();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String key )
    {
        SettingsManager.registerComponent ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( String key, Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String key, Object defaultValue )
    {
        SettingsManager.registerComponent ( this, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String group, String key )
    {
        SettingsManager.registerComponent ( this, group, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( String group, String key, Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String group, String key, Object defaultValue )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String key, boolean loadInitialSettings, boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( String key, Class<T> defaultValueClass, boolean loadInitialSettings,
                                                            boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String key, Object defaultValue, boolean loadInitialSettings, boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( String group, String key, Class<T> defaultValueClass,
                                                            boolean loadInitialSettings, boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String group, String key, Object defaultValue, boolean loadInitialSettings,
                                   boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( SettingsProcessor settingsProcessor )
    {
        SettingsManager.registerComponent ( this, settingsProcessor );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterSettings ()
    {
        SettingsManager.unregisterComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadSettings ()
    {
        SettingsManager.loadComponentSettings ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSettings ()
    {
        SettingsManager.saveComponentSettings ( this );
    }
}