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

import com.alee.extended.icon.OrientedIcon;
import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.style.ChildStyleId;
import com.alee.managers.style.ShapeProvider;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;
import com.alee.managers.style.Bounds;
import com.alee.painter.decoration.states.BoxOrientation;
import com.alee.utils.CollectionUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.swing.DataProvider;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This extended components allows you to quickly create and manipulate a collapsible pane.
 * Pane title, content and style can be modified in any way you like.
 *
 * @author Mikle Garin
 */

public class WebCollapsiblePane extends WebPanel implements SwingConstants, ShapeProvider, LanguageMethods, SettingsMethods
{
    /**
     * todo 1. Create CollapsiblePaneUI and its implementation
     * todo 2. Move implementation into UI and painter
     */

    /**
     * Collapsed state icon.
     */
    public static ImageIcon EXPAND_ICON = new ImageIcon ( WebCollapsiblePane.class.getResource ( "icons/arrow.png" ) );

    /**
     * Expanded state icon.
     */
    public static ImageIcon COLLAPSE_ICON = ImageUtils.rotateImage180 ( EXPAND_ICON );

    /**
     * Whether animate transition between states or not.
     */
    protected Boolean animate;

    /**
     * Collapsed state icon.
     */
    protected ImageIcon expandIcon;

    /**
     * Expanded state icon.
     */
    protected ImageIcon collapseIcon;

    /**
     * Whether rotate state icon according to title pane position or not.
     */
    protected Boolean rotateStateIcon;

    /**
     * Whether display state icon in title pane or not.
     */
    protected Boolean showStateIcon;

    /**
     * State icon position in title pane.
     */
    protected int stateIconPosition;

    /**
     * Title pane position in collapsible pane.
     */
    protected int titlePanePosition;

    /**
     * Collapsible pane listeners.
     */
    protected List<CollapsiblePaneListener> listeners = new ArrayList<CollapsiblePaneListener> ( 1 );

    /**
     * Cached collapsed state icon.
     */
    protected ImageIcon cachedExpandIcon = null;

    /**
     * Cached disabled collapsed state icon.
     */
    protected ImageIcon cachedDisabledExpandIcon = null;

    /**
     * Cached expanded state icon.
     */
    protected ImageIcon cachedCollapseIcon = null;

    /**
     * Cached disabled expanded state icon.
     */
    protected ImageIcon cachedDisabledCollapseIcon = null;

    /**
     * Handler that dynamically enable and disable collapsible pane state changes by providing according boolean value.
     */
    protected DataProvider<Boolean> stateChangeHandler = null;

    /**
     * Whether collapsible pane is expanded or not.
     */
    protected boolean expanded = true;

    /**
     * Current collapsible pane transition progress.
     * When pane is fully expanded this variable becomes 1f.
     * When pane is fully collapsed this variable becomes 0f.
     */
    protected float transitionProgress = 1f;

    /**
     * Collapsible pane expand and collapse speed.
     */
    protected float expandSpeed = 0.1f;

    /**
     * State change animation timer.
     */
    protected WebTimer animator = null;

    /**
     * Whether custom title component is set or not.
     */
    protected boolean customTitle = false;

    /**
     * Header panel.
     */
    protected HeaderPanel headerPanel;

    /**
     * Title component.
     */
    protected Component titleComponent;

    /**
     * State change button.
     */
    protected WebButton expandButton;

    /**
     * Content panel.
     */
    protected WebPanel contentPanel;

    /**
     * Collapsible pane content.
     */
    protected Component content = null;

    /**
     * Constructs empty collapsible pane.
     */
    public WebCollapsiblePane ()
    {
        this ( StyleId.collapsiblepane, "" );
    }

    /**
     * Constructs empty collapsible pane with specified title text.
     *
     * @param title collapsible pane title text
     */
    public WebCollapsiblePane ( final String title )
    {
        this ( StyleId.collapsiblepane, null, title );
    }

    /**
     * Constructs empty collapsible pane with specified title icon and text.
     *
     * @param icon  collapsible pane title icon
     * @param title collapsible pane title text
     */
    public WebCollapsiblePane ( final ImageIcon icon, final String title )
    {
        this ( StyleId.collapsiblepane, icon, title, null );
    }

    /**
     * Constructs collapsible pane with specified content.
     *
     * @param content collapsible pane content
     */
    public WebCollapsiblePane ( final Component content )
    {
        this ( StyleId.collapsiblepane, null, "", content );
    }

    /**
     * Constructs collapsible pane with specified title text and content.
     *
     * @param title   collapsible pane title text
     * @param content collapsible pane content
     */
    public WebCollapsiblePane ( final String title, final Component content )
    {
        this ( StyleId.collapsiblepane, null, title, content );
    }

    /**
     * Constructs collapsible pane with specified title icon, text and content.
     *
     * @param icon    collapsible pane title icon
     * @param title   collapsible pane title text
     * @param content collapsible pane content
     */
    public WebCollapsiblePane ( final Icon icon, final String title, final Component content )
    {
        this ( StyleId.collapsiblepane, icon, title, content );
    }

    /**
     * Constructs empty collapsible pane.
     *
     * @param id style ID
     */
    public WebCollapsiblePane ( final StyleId id )
    {
        this ( id, "" );
    }

    /**
     * Constructs empty collapsible pane with specified title text.
     *
     * @param id    style ID
     * @param title collapsible pane title text
     */
    public WebCollapsiblePane ( final StyleId id, final String title )
    {
        this ( id, null, title );
    }

    /**
     * Constructs empty collapsible pane with specified title icon and text.
     *
     * @param id    style ID
     * @param icon  collapsible pane title icon
     * @param title collapsible pane title text
     */
    public WebCollapsiblePane ( final StyleId id, final ImageIcon icon, final String title )
    {
        this ( id, icon, title, null );
    }

    /**
     * Constructs collapsible pane with specified content.
     *
     * @param id      style ID
     * @param content collapsible pane content
     */
    public WebCollapsiblePane ( final StyleId id, final Component content )
    {
        this ( id, null, "", content );
    }

    /**
     * Constructs collapsible pane with specified title text and content.
     *
     * @param id      style ID
     * @param title   collapsible pane title text
     * @param content collapsible pane content
     */
    public WebCollapsiblePane ( final StyleId id, final String title, final Component content )
    {
        this ( id, null, title, content );
    }

    /**
     * Constructs collapsible pane with specified title icon, text and content.
     *
     * @param id      style ID
     * @param icon    collapsible pane title icon
     * @param title   collapsible pane title text
     * @param content collapsible pane content
     */
    public WebCollapsiblePane ( final StyleId id, final Icon icon, final String title, final Component content )
    {
        super ( id, new BorderLayout ( 0, 0 ) );

        this.content = content;

        // todo Handle enable/disable
        // putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, true );

        // Header

        headerPanel = new HeaderPanel ();
        headerPanel.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mouseReleased ( final MouseEvent e )
            {
                if ( SwingUtilities.isLeftMouseButton ( e ) && Bounds.margin.of ( WebCollapsiblePane.this ).contains ( e.getPoint () ) )
                {
                    invertExpandState ();
                    takeFocus ();
                }
            }
        } );
        headerPanel.addKeyListener ( new KeyAdapter ()
        {
            @Override
            public void keyReleased ( final KeyEvent e )
            {
                if ( Hotkey.ENTER.isTriggered ( e ) || Hotkey.SPACE.isTriggered ( e ) )
                {
                    invertExpandState ();
                }
            }
        } );
        updateHeaderPosition ();

        updateDefaultTitleComponent ( icon, title );

        expandButton = new WebButton ( StyleId.collapsiblepaneExpandButton.at ( this ), getCollapseIcon () );
        expandButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                invertExpandState ();
                takeFocus ();
            }
        } );
        setStateIcons ();
        updateStateIconPosition ();

        // Content

        contentPanel = new WebPanel ( StyleId.collapsiblepaneContentPanel.at ( this ), new BorderLayout ( 0, 0 ) )
        {
            @Override
            public Dimension getPreferredSize ()
            {
                final Dimension ps = super.getPreferredSize ();
                if ( titlePanePosition == TOP || titlePanePosition == BOTTOM )
                {
                    if ( WebCollapsiblePane.this.content != null )
                    {
                        final Insets insets = getInsets ();
                        ps.width = insets.left + WebCollapsiblePane.this.content.getPreferredSize ().width + insets.right;
                    }
                    if ( transitionProgress < 1f )
                    {
                        ps.height = Math.round ( ps.height * transitionProgress );
                    }
                }
                else
                {
                    if ( WebCollapsiblePane.this.content != null )
                    {
                        final Insets insets = getInsets ();
                        ps.height = insets.top + WebCollapsiblePane.this.content.getPreferredSize ().height + insets.bottom;
                    }
                    if ( transitionProgress < 1f )
                    {
                        ps.width = Math.round ( ps.width * transitionProgress );
                    }
                }
                return ps;
            }
        };
        add ( contentPanel, BorderLayout.CENTER );

        if ( this.content != null )
        {
            contentPanel.add ( this.content, BorderLayout.CENTER );
        }

        addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateStateIcons ();
            }
        } );
    }

    /**
     * Transfers application focus to this collapsible pane.
     */
    protected void takeFocus ()
    {
        if ( isShowing () && isEnabled () )
        {
            if ( isFocusable () )
            {
                requestFocusInWindow ();
            }
            else
            {
                transferFocus ();
            }
        }
    }

    /**
     * Updates default title component.
     */
    protected void updateDefaultTitleComponent ()
    {
        updateDefaultTitleComponent ( getIcon (), getTitle () );
    }

    /**
     * Updates default title component with the specified title icon and text.
     *
     * @param icon  collapsible pane title icon
     * @param title collapsible pane title text
     */
    protected void updateDefaultTitleComponent ( final Icon icon, final String title )
    {
        if ( headerPanel != null && !customTitle )
        {
            if ( titleComponent != null )
            {
                headerPanel.remove ( titleComponent );
            }
            titleComponent = createDefaultTitleComponent ( icon, title );
            headerPanel.add ( titleComponent, BorderLayout.CENTER );
        }
    }

    /**
     * Updates header panel position.
     */
    protected void updateHeaderPosition ()
    {
        if ( headerPanel != null )
        {
            if ( titlePanePosition == TOP )
            {
                add ( headerPanel, BorderLayout.NORTH );
            }
            else if ( titlePanePosition == BOTTOM )
            {
                add ( headerPanel, BorderLayout.SOUTH );
            }
            else if ( titlePanePosition == LEFT )
            {
                add ( headerPanel, BorderLayout.LINE_START );
            }
            else if ( titlePanePosition == RIGHT )
            {
                add ( headerPanel, BorderLayout.LINE_END );
            }
            headerPanel.updateDecoration ();
            revalidate ();
        }
    }

    /**
     * Updates state icon position.
     */
    protected void updateStateIconPosition ()
    {
        if ( headerPanel != null && expandButton != null )
        {
            if ( showStateIcon )
            {
                if ( titlePanePosition == TOP || titlePanePosition == BOTTOM )
                {
                    headerPanel.add ( expandButton, stateIconPosition == RIGHT ? BorderLayout.LINE_END : BorderLayout.LINE_START );
                }
                else if ( titlePanePosition == LEFT )
                {
                    headerPanel.add ( expandButton, stateIconPosition == RIGHT ? BorderLayout.PAGE_START : BorderLayout.PAGE_END );
                }
                else if ( titlePanePosition == RIGHT )
                {
                    headerPanel.add ( expandButton, stateIconPosition == RIGHT ? BorderLayout.PAGE_END : BorderLayout.PAGE_START );
                }
            }
            else
            {
                headerPanel.remove ( expandButton );
            }
            headerPanel.revalidate ();
        }
    }

    /**
     * Returns new default title component with specified icon and text.
     *
     * @param title collapsible pane title text
     * @param icon  collapsible pane title icon
     * @return new default title component with specified icon and text
     */
    protected JComponent createDefaultTitleComponent ( final Icon icon, final String title )
    {
        // todo RTL orientation support
        final ChildStyleId id;
        switch ( titlePanePosition )
        {
            case TOP:
                id = StyleId.collapsiblepaneTopTitleLabel;
                break;
            case LEFT:
                id = StyleId.collapsiblepaneLeftTitleLabel;
                break;
            case BOTTOM:
                id = StyleId.collapsiblepaneBottomTitleLabel;
                break;
            case RIGHT:
                id = StyleId.collapsiblepaneRightTitleLabel;
                break;
            default:
                throw new IllegalArgumentException ( "Unknown title pane position specified" );
        }
        return new WebLabel ( id.at ( this ), title, icon, WebLabel.LEADING );
    }

    /**
     * Returns handler that dynamically enable and disable collapsible pane state changes by providing according boolean value.
     *
     * @return state change handler
     */
    public DataProvider<Boolean> getStateChangeHandler ()
    {
        return stateChangeHandler;
    }

    /**
     * Sets handler that dynamically enable and disable collapsible pane state changes by providing according boolean value.
     *
     * @param stateChangeHandler new state change handler
     */
    public void setStateChangeHandler ( final DataProvider<Boolean> stateChangeHandler )
    {
        this.stateChangeHandler = stateChangeHandler;
    }

    /**
     * Returns whether collapsible pane state change is enabled or not.
     *
     * @return true if collapsible pane state change is enabled, false otherwise
     */
    public boolean isStateChangeEnabled ()
    {
        return stateChangeHandler == null || stateChangeHandler.provide ();
    }

    /**
     * Returns whether collapsible pane is performing animated transition at the moment or not.
     *
     * @return true if collapsible pane is performing animated transition at the moment, false otherwise
     */
    public boolean isAnimating ()
    {
        return animator != null && animator.isRunning ();
    }

    /**
     * Changes expanded state to opposite and returns whether operation succeed or not.
     * Operation might fail in case state change is disabled for some reason.
     *
     * @return true if operation succeed, false otherwise
     */
    public boolean invertExpandState ()
    {
        return invertExpandState ( animate );
    }

    /**
     * Changes expanded state to opposite and returns whether operation succeed or not.
     * Operation might fail in case state change is disabled for some reason.
     *
     * @param animate whether animate state change transition or not
     * @return true if operation succeed, false otherwise
     */
    public boolean invertExpandState ( final boolean animate )
    {
        return setExpanded ( !isExpanded (), animate );
    }

    /**
     * Returns whether this collapsible pane is expanded or not.
     *
     * @return true if this collapsible pane is expanded, false otherwise
     */
    public boolean isExpanded ()
    {
        return expanded;
    }

    /**
     * Changes expanded state to specified one and returns whether operation succeed or not.
     * Operation might fail in case state change is disabled for some reason.
     *
     * @param expanded whether collapsible pane should be expanded or collapsed
     * @return true if operation succeed, false otherwise
     */
    public boolean setExpanded ( final boolean expanded )
    {
        return setExpanded ( expanded, isShowing () && animate );
    }

    /**
     * Changes expanded state to specified one and returns whether operation succeed or not.
     * Operation might fail in case state change is disabled for some reason.
     *
     * @param expanded whether collapsible pane should be expanded or collapsed
     * @param animate  whether animate state change transition or not
     * @return true if operation succeed, false otherwise
     */
    public boolean setExpanded ( final boolean expanded, final boolean animate )
    {
        if ( isEnabled () )
        {
            if ( expanded )
            {
                return expand ( animate );
            }
            else
            {
                return collapse ( animate );
            }
        }
        return false;
    }

    /**
     * Changes expanded state to collapsed and returns whether operation succeed or not.
     * Operation might fail in case state change is disabled for some reason.
     *
     * @return true if operation succeed, false otherwise
     */
    public boolean collapse ()
    {
        return collapse ( animate );
    }

    /**
     * Changes expanded state to collapsed and returns whether operation succeed or not.
     * Operation might fail in case state change is disabled for some reason.
     *
     * @param animate whether animate state change transition or not
     * @return true if operation succeed, false otherwise
     */
    public boolean collapse ( final boolean animate )
    {
        if ( !expanded || !isStateChangeEnabled () )
        {
            return false;
        }

        stopAnimation ();

        expanded = false;
        setStateIcons ();
        fireCollapsing ();

        if ( animate && isShowing () )
        {
            animator = new WebTimer ( "WebCollapsiblePane.collapseTimer", StyleConstants.fps48, new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    if ( transitionProgress > 0f )
                    {
                        transitionProgress = Math.max ( 0f, transitionProgress - expandSpeed );
                        revalidate ();
                    }
                    else
                    {
                        transitionProgress = 0f;
                        finishCollapseAction ();
                        animator.stop ();
                    }
                }
            } );
            animator.start ();
        }
        else
        {
            transitionProgress = 0f;
            finishCollapseAction ();
        }
        return true;
    }

    /**
     * Finishes collapse action.
     */
    protected void finishCollapseAction ()
    {
        // Updating header style
        if ( headerPanel != null )
        {
            headerPanel.updateDecoration ();
        }

        // Hide content
        if ( content != null )
        {
            content.setVisible ( false );
        }

        // Update collapsible pane
        revalidate ();
        repaint ();

        // Inform about event
        fireCollapsed ();
    }

    /**
     * Changes expanded state to expanded and returns whether operation succeed or not.
     * Operation might fail in case state change is disabled for some reason.
     *
     * @return true if operation succeed, false otherwise
     */
    public boolean expand ()
    {
        return expand ( animate );
    }

    /**
     * Changes expanded state to expanded and returns whether operation succeed or not.
     * Operation might fail in case state change is disabled for some reason.
     *
     * @param animate whether animate state change transition or not
     * @return true if operation succeed, false otherwise
     */
    public boolean expand ( final boolean animate )
    {
        if ( expanded || !isStateChangeEnabled () )
        {
            return false;
        }

        stopAnimation ();

        expanded = true;
        setStateIcons ();

        // Updating header style
        if ( headerPanel != null )
        {
            headerPanel.updateDecoration ();
        }

        // Show content
        if ( content != null )
        {
            content.setVisible ( true );
        }

        fireExpanding ();

        if ( animate && isShowing () )
        {
            animator = new WebTimer ( "WebCollapsiblePane.expandTimer", StyleConstants.fps48, new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    if ( transitionProgress < 1f )
                    {
                        transitionProgress = Math.min ( 1f, transitionProgress + expandSpeed );
                        revalidate ();
                    }
                    else
                    {
                        transitionProgress = 1f;
                        finishExpandAction ();
                        animator.stop ();
                    }
                }
            } );
            animator.start ();
        }
        else
        {
            transitionProgress = 1f;
            finishExpandAction ();
        }
        return true;
    }

    /**
     * Finishes expand action.
     */
    protected void finishExpandAction ()
    {
        // Update collapsible pane
        revalidate ();
        repaint ();

        // Inform about event
        fireExpanded ();
    }

    /**
     * Stops state transition animation.
     */
    protected void stopAnimation ()
    {
        if ( animator != null && animator.isRunning () )
        {
            animator.stop ();
        }
    }

    /**
     * Returns title pane position in collapsible pane.
     *
     * @return title pane position in collapsible pane
     */
    public int getTitlePanePosition ()
    {
        return titlePanePosition;
    }

    /**
     * Sets title pane position in collapsible pane.
     *
     * @param titlePanePosition new title pane position in collapsible pane
     */
    public void setTitlePanePosition ( final int titlePanePosition )
    {
        this.titlePanePosition = titlePanePosition;
        updateDefaultTitleComponent ();
        updateHeaderPosition ();
        updateStateIcons ();
        updateStateIconPosition ();
    }

    /**
     * Returns whether animate transition between states or not.
     *
     * @return true if animate transition between states, false otherwise
     */
    public boolean isAnimate ()
    {
        return animate;
    }

    /**
     * Sets whether animate transition between states or not
     *
     * @param animate whether animate transition between states or not
     */
    public void setAnimate ( final boolean animate )
    {
        this.animate = animate;
    }

    /**
     * Returns default title component icon.
     *
     * @return default title component icon
     */
    public Icon getIcon ()
    {
        return customTitle ? null : titleComponent != null ? ( ( WebLabel ) titleComponent ).getIcon () : null;
    }

    /**
     * Sets default title component icon.
     *
     * @param icon new default title component icon
     */
    public void setIcon ( final Icon icon )
    {
        if ( !customTitle )
        {
            ( ( WebLabel ) titleComponent ).setIcon ( icon );
        }
    }

    /**
     * Returns default title component text.
     *
     * @return default title component text
     */
    public String getTitle ()
    {
        return customTitle ? null : titleComponent != null ? ( ( WebLabel ) titleComponent ).getText () : null;
    }

    /**
     * Sets default title component text.
     *
     * @param title new default title component text
     */
    public void setTitle ( final String title )
    {
        if ( !customTitle )
        {
            ( ( WebLabel ) titleComponent ).setText ( title );
        }
    }

    /**
     * Sets default title component text alignment.
     *
     * @param alignment new default title component text alignment
     */
    public void setTitleAlignment ( final int alignment )
    {
        if ( !customTitle )
        {
            ( ( WebLabel ) titleComponent ).setHorizontalAlignment ( alignment );
        }
    }

    /**
     * Returns expanded state icon.
     *
     * @return expanded state icon
     */
    public ImageIcon getCollapseIcon ()
    {
        return collapseIcon != null ? collapseIcon : COLLAPSE_ICON;
    }

    /**
     * Sets expanded state icon.
     *
     * @param collapseIcon new expanded state icon
     */
    public void setCollapseIcon ( final ImageIcon collapseIcon )
    {
        this.collapseIcon = collapseIcon;
        clearCachedCollapseIcons ();
        setStateIcons ();
    }

    /**
     * Returns collapsed state icon.
     *
     * @return collapsed state icon
     */
    public ImageIcon getExpandIcon ()
    {
        return expandIcon != null ? expandIcon : EXPAND_ICON;
    }

    /**
     * Sets collapsed state icon.
     *
     * @param expandIcon new collapsed state icon
     */
    public void setExpandIcon ( final ImageIcon expandIcon )
    {
        this.expandIcon = expandIcon;
        clearCachedExpandIcons ();
        setStateIcons ();
    }

    /**
     * Returns whether rotate state icon according to title pane position or not.
     *
     * @return true if stat icon must be rotated according to title pane position, false otherwise
     */
    public boolean isRotateStateIcon ()
    {
        return rotateStateIcon;
    }

    /**
     * Sets whether rotate state icon according to title pane position or not.
     *
     * @param rotateStateIcon whether rotate state icon according to title pane position or not
     */
    public void setRotateStateIcon ( final boolean rotateStateIcon )
    {
        this.rotateStateIcon = rotateStateIcon;
        updateStateIcons ();
    }

    /**
     * Returns whether display state icon in title pane or not.
     *
     * @return true if state icon must be displayed in title pane, false otherwise
     */
    public boolean isShowStateIcon ()
    {
        return showStateIcon;
    }

    /**
     * Sets whether display state icon in title pane or not.
     *
     * @param showStateIcon whether display state icon in title pane or not
     */
    public void setShowStateIcon ( final boolean showStateIcon )
    {
        this.showStateIcon = showStateIcon;
        updateStateIconPosition ();
    }

    /**
     * Returns state icon position in title pane.
     *
     * @return state icon position in title pane
     */
    public int getStateIconPosition ()
    {
        return stateIconPosition;
    }

    /**
     * Sets state icon position in title pane.
     *
     * @param stateIconPosition new state icon position in title pane
     */
    public void setStateIconPosition ( final int stateIconPosition )
    {
        this.stateIconPosition = stateIconPosition;
        updateStateIconPosition ();
    }

    /**
     * Updates state icons.
     */
    protected void updateStateIcons ()
    {
        clearCachedCollapseIcons ();
        clearCachedExpandIcons ();
        setStateIcons ();
    }

    /**
     * Installs state icons into state change button.
     */
    protected void setStateIcons ()
    {
        if ( expandButton != null )
        {
            if ( expanded )
            {
                expandButton.setIcon ( getCachedCollapseIcon () );
                expandButton.setDisabledIcon ( getCachedDisabledCollapseIcon () );
            }
            else
            {
                expandButton.setIcon ( getCachedExpandIcon () );
                expandButton.setDisabledIcon ( getCachedDisabledExpandIcon () );
            }
        }
    }

    /**
     * Clears cached expanded state icons.
     */
    protected void clearCachedCollapseIcons ()
    {
        cachedCollapseIcon = null;
        cachedDisabledCollapseIcon = null;
    }

    /**
     * Returns cached expanded state icon.
     *
     * @return cached expanded state icon
     */
    protected ImageIcon getCachedCollapseIcon ()
    {
        if ( cachedCollapseIcon == null )
        {
            // todo Proper icon for RTL
            // boolean ltr = getComponentOrientation ().isLeftToRight ();
            if ( !rotateStateIcon || titlePanePosition == TOP || titlePanePosition == BOTTOM )
            {
                cachedCollapseIcon = new OrientedIcon ( getCollapseIcon () );
            }
            else if ( titlePanePosition == LEFT )
            {
                cachedCollapseIcon = ImageUtils.rotateImage90CCW ( getCollapseIcon () );
            }
            else if ( titlePanePosition == RIGHT )
            {
                cachedCollapseIcon = ImageUtils.rotateImage90CW ( getCollapseIcon () );
            }
        }
        return cachedCollapseIcon;
    }

    /**
     * Returns cached disabled expanded state icon.
     *
     * @return cached disabled expanded state icon
     */
    protected ImageIcon getCachedDisabledCollapseIcon ()
    {
        if ( cachedDisabledCollapseIcon == null )
        {
            cachedDisabledCollapseIcon = ImageUtils.createDisabledCopy ( getCachedCollapseIcon () );
        }
        return cachedDisabledCollapseIcon;
    }

    /**
     * Clears cached collapsed state icons.
     */
    protected void clearCachedExpandIcons ()
    {
        cachedExpandIcon = null;
        cachedDisabledExpandIcon = null;
    }

    /**
     * Returns cached collapsed state icon.
     *
     * @return cached collapsed state icon
     */
    protected ImageIcon getCachedExpandIcon ()
    {
        if ( cachedExpandIcon == null )
        {
            final boolean ltr = getComponentOrientation ().isLeftToRight ();
            if ( !rotateStateIcon || titlePanePosition == TOP || titlePanePosition == BOTTOM )
            {
                cachedExpandIcon = getExpandIcon ();
            }
            else if ( ltr ? titlePanePosition == LEFT : titlePanePosition == RIGHT )
            {
                cachedExpandIcon = ImageUtils.rotateImage90CCW ( getExpandIcon () );
            }
            else if ( ltr ? titlePanePosition == RIGHT : titlePanePosition == LEFT )
            {
                cachedExpandIcon = ImageUtils.rotateImage90CW ( getExpandIcon () );
            }
        }
        return cachedExpandIcon;
    }

    /**
     * Returns cached disabled collapsed state icon.
     *
     * @return cached disabled collapsed state icon
     */
    protected ImageIcon getCachedDisabledExpandIcon ()
    {
        if ( cachedDisabledExpandIcon == null )
        {
            cachedDisabledExpandIcon = ImageUtils.createDisabledCopy ( getCachedExpandIcon () );
        }
        return cachedDisabledExpandIcon;
    }

    /**
     * Returns header panel.
     *
     * @return header panel
     */
    public WebPanel getHeaderPanel ()
    {
        return headerPanel;
    }

    /**
     * Returns state change button.
     *
     * @return state change button
     */
    public WebButton getExpandButton ()
    {
        return expandButton;
    }

    /**
     * Returns title component.
     *
     * @return title component
     */
    public Component getTitleComponent ()
    {
        return titleComponent;
    }

    /**
     * Sets custom title component.
     *
     * @param titleComponent new custom title component
     */
    public void setTitleComponent ( final Component titleComponent )
    {
        if ( this.titleComponent != null )
        {
            headerPanel.remove ( this.titleComponent );
        }
        if ( titleComponent != null )
        {
            headerPanel.add ( titleComponent, BorderLayout.CENTER );
        }
        this.titleComponent = titleComponent;
        this.customTitle = true;
    }

    /**
     * Returns collapsible pane content.
     *
     * @return collapsible pane content
     */
    public Component getContent ()
    {
        return content;
    }

    /**
     * Sets collapsible pane content.
     *
     * @param content new collapsible pane content
     */
    public void setContent ( final Component content )
    {
        if ( this.content != null )
        {
            contentPanel.remove ( this.content );
        }

        this.content = content;
        content.setVisible ( transitionProgress > 0f );

        contentPanel.add ( content, BorderLayout.CENTER );
        revalidate ();
    }

    /**
     * Returns collapsible pane listeners.
     *
     * @return collapsible pane listeners
     */
    public List<CollapsiblePaneListener> getCollapsiblePaneListeners ()
    {
        return CollectionUtils.copy ( listeners );
    }

    /**
     * Sets collapsible pane listeners.
     *
     * @param listeners new collapsible pane listeners
     */
    public void setCollapsiblePaneListeners ( final List<CollapsiblePaneListener> listeners )
    {
        this.listeners = listeners;
    }

    /**
     * Adds collapsible pane listener.
     *
     * @param listener collapsible pane listener to add
     */
    public void addCollapsiblePaneListener ( final CollapsiblePaneListener listener )
    {
        listeners.add ( listener );
    }

    /**
     * Removes collapsible pane listener.
     *
     * @param listener collapsible pane listener to remove
     */
    public void removeCollapsiblePaneListener ( final CollapsiblePaneListener listener )
    {
        listeners.remove ( listener );
    }

    /**
     * Notifies when collapsible pane starts to expand.
     */
    public void fireExpanding ()
    {
        for ( final CollapsiblePaneListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.expanding ( this );
        }
    }

    /**
     * Notifies when collapsible pane finished expanding.
     */
    public void fireExpanded ()
    {
        for ( final CollapsiblePaneListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.expanded ( this );
        }
    }

    /**
     * Notifies when collapsible pane starts to collapse.
     */
    public void fireCollapsing ()
    {
        for ( final CollapsiblePaneListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.collapsing ( this );
        }
    }

    /**
     * Notifies when collapsible pane finished collapsing.
     */
    public void fireCollapsed ()
    {
        for ( final CollapsiblePaneListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.collapsed ( this );
        }
    }

    /**
     * Returns current collapsible pane transition progress.
     * Returns 1f when pane is fully expanded and 0f when pane is fully collapsed.
     *
     * @return current collapsible pane transition progress
     */
    public float getTransitionProgress ()
    {
        return transitionProgress;
    }

    /**
     * Returns preferred size without taking collapsible pane content into account.
     *
     * @return preferred size without taking collapsible pane content into account
     */
    public Dimension getBasePreferredSize ()
    {
        final Dimension ps = getPreferredSize ();
        if ( content == null || transitionProgress <= 0 )
        {
            return ps;
        }
        else
        {
            final Dimension cps = content.getPreferredSize ();
            if ( titlePanePosition == TOP || titlePanePosition == BOTTOM )
            {
                return new Dimension ( ps.width, ps.height - Math.round ( cps.height * transitionProgress ) );
            }
            else
            {
                return new Dimension ( ps.width - Math.round ( cps.width * transitionProgress ), ps.height );
            }
        }
    }

    @Override
    public void setLanguage ( final String key, final Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    @Override
    public void updateLanguage ( final Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    @Override
    public void updateLanguage ( final String key, final Object... data )
    {
        LanguageManager.updateComponent ( this, key, data );
    }

    @Override
    public void removeLanguage ()
    {
        LanguageManager.unregisterComponent ( this );
    }

    @Override
    public boolean isLanguageSet ()
    {
        return LanguageManager.isRegisteredComponent ( this );
    }

    @Override
    public void setLanguageUpdater ( final LanguageUpdater updater )
    {
        LanguageManager.registerLanguageUpdater ( this, updater );
    }

    @Override
    public void removeLanguageUpdater ()
    {
        LanguageManager.unregisterLanguageUpdater ( this );
    }

    @Override
    public void registerSettings ( final String key )
    {
        SettingsManager.registerComponent ( this, key );
    }

    @Override
    public <T extends DefaultValue> void registerSettings ( final String key, final Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass );
    }

    @Override
    public void registerSettings ( final String key, final Object defaultValue )
    {
        SettingsManager.registerComponent ( this, key, defaultValue );
    }

    @Override
    public void registerSettings ( final String group, final String key )
    {
        SettingsManager.registerComponent ( this, group, key );
    }

    @Override
    public <T extends DefaultValue> void registerSettings ( final String group, final String key, final Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass );
    }

    @Override
    public void registerSettings ( final String group, final String key, final Object defaultValue )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue );
    }

    @Override
    public void registerSettings ( final String key, final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public <T extends DefaultValue> void registerSettings ( final String key, final Class<T> defaultValueClass,
                                                            final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public void registerSettings ( final String key, final Object defaultValue, final boolean loadInitialSettings,
                                   final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public <T extends DefaultValue> void registerSettings ( final String group, final String key, final Class<T> defaultValueClass,
                                                            final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public void registerSettings ( final String group, final String key, final Object defaultValue, final boolean loadInitialSettings,
                                   final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public void registerSettings ( final SettingsProcessor settingsProcessor )
    {
        SettingsManager.registerComponent ( this, settingsProcessor );
    }

    @Override
    public void unregisterSettings ()
    {
        SettingsManager.unregisterComponent ( this );
    }

    @Override
    public void loadSettings ()
    {
        SettingsManager.loadComponentSettings ( this );
    }

    @Override
    public void saveSettings ()
    {
        SettingsManager.saveComponentSettings ( this );
    }

    /**
     * Custom header panel.
     */
    public class HeaderPanel extends WebPanel implements Stateful
    {
        /**
         * Constructs new header panel.
         */
        public HeaderPanel ()
        {
            super ( StyleId.collapsiblepaneHeaderPanel.at ( WebCollapsiblePane.this ), new BorderLayout () );
        }

        @Override
        public List<String> getStates ()
        {
            final String state = isExpanded () || getTransitionProgress () > 0f ? DecorationState.expanded : DecorationState.collapsed;
            final String position = BoxOrientation.get ( getTitlePanePosition () ).name ();
            return CollectionUtils.asList ( state, position );
        }

        /**
         * Force decoration updates.
         */
        public void updateDecoration ()
        {
            DecorationUtils.fireStatesChanged ( this );
        }
    }
}