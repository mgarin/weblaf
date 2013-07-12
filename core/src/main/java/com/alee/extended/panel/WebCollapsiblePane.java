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
import com.alee.extended.label.WebVerticalLabel;
import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.utils.CollectionUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.DataProvider;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This extended components allows you to quickly create and manipulate a collapsible pane.
 * Pane title, content and style can be modified in any way you like.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class WebCollapsiblePane extends WebPanel implements SwingConstants, ShapeProvider, SettingsMethods
{
    /**
     * Whether animate transition between states or not.
     */
    private boolean animate = WebCollapsiblePaneStyle.animate;

    /**
     * Collapsed state icon.
     */
    private ImageIcon expandIcon = WebCollapsiblePaneStyle.expandIcon;

    /**
     * Expanded state icon.
     */
    private ImageIcon collapseIcon = WebCollapsiblePaneStyle.collapseIcon;

    /**
     * State icon margin.
     */
    private Insets stateIconMargin = WebCollapsiblePaneStyle.stateIconMargin;

    /**
     * Whether rotate state icon according to title pane position or not.
     */
    private boolean rotateStateIcon = WebCollapsiblePaneStyle.rotateStateIcon;

    /**
     * Whether display state icon in title pane or not.
     */
    private boolean showStateIcon = WebCollapsiblePaneStyle.showStateIcon;

    /**
     * State icon position in title pane.
     */
    private int stateIconPostion = WebCollapsiblePaneStyle.stateIconPostion;

    /**
     * Title pane position in collapsible pane.
     */
    private int titlePanePostion = WebCollapsiblePaneStyle.titlePanePostion;

    /**
     * Content margin.
     */
    private Insets contentMargin = WebCollapsiblePaneStyle.contentMargin;

    /**
     * Collapsible pane listeners.
     */
    private List<CollapsiblePaneListener> listeners = new ArrayList<CollapsiblePaneListener> ();

    /**
     * Cached collapsed state icon.
     */
    private ImageIcon cachedExpandIcon = null;

    /**
     * Cached disabled collapsed state icon.
     */
    private ImageIcon cachedDisabledExpandIcon = null;

    /**
     * Cached expanded state icon.
     */
    private ImageIcon cachedCollapseIcon = null;

    /**
     * Cached disabled expanded state icon.
     */
    private ImageIcon cachedDisabledCollapseIcon = null;

    /**
     * Handler that dynamically enable and disable collapsible pane state changes by providing according boolean value.
     */
    private DataProvider<Boolean> stateChangeHandler = null;

    /**
     * Whether collapsible pane is expanded or not.
     */
    private boolean expanded = true;

    /**
     * Current collapsible pane transition progress.
     * When pane is fully expanded this variable becomes 1f.
     * When pane is fully collapsed this variable becomes 0f.
     */
    private float transitionProgress = 1f;

    /**
     * Collapsible pane expand and collapse speed.
     */
    private float expandSpeed = 0.1f;

    /**
     * State change animation timer.
     */
    private WebTimer animator = null;

    /**
     * Whether custom title component is set or not.
     */
    private boolean customTitle = false;

    /**
     * Header panel.
     */
    private WebPanel headerPanel;

    /**
     * Title component.
     */
    private Component titleComponent;

    /**
     * State change button.
     */
    private WebButton expandButton;

    /**
     * Content panel.
     */
    private WebPanel contentPanel;

    /**
     * Collapsible pane content.
     */
    private Component content = null;

    /**
     * Constructs empty collapsible pane.
     */
    public WebCollapsiblePane ()
    {
        this ( "" );
    }

    /**
     * Constructs empty collapsible pane with specified title text.
     *
     * @param title collapsible pane title text
     */
    public WebCollapsiblePane ( String title )
    {
        this ( null, title );
    }

    /**
     * Constructs empty collapsible pane with specified title icon and text.
     *
     * @param icon  collapsible pane title icon
     * @param title collapsible pane title text
     */
    public WebCollapsiblePane ( ImageIcon icon, String title )
    {
        this ( icon, title, null );
    }

    /**
     * Constructs collapsible pane with specified title text and content.
     *
     * @param title   collapsible pane title text
     * @param content collapsible pane content
     */
    public WebCollapsiblePane ( String title, Component content )
    {
        this ( null, title, content );
    }

    /**
     * Constructs collapsible pane with specified title icon, text and content.
     *
     * @param icon    collapsible pane title icon
     * @param title   collapsible pane title text
     * @param content collapsible pane content
     */
    public WebCollapsiblePane ( Icon icon, String title, Component content )
    {
        super ();

        // todo Handle enable/disable
        // putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, true );

        this.content = content;

        setDrawFocus ( true );
        setUndecorated ( false );
        setWebColored ( false );
        setRound ( StyleConstants.smallRound );
        setLayout ( new BorderLayout ( 0, 0 ) );

        // Header

        headerPanel = new WebPanel ();
        headerPanel.setOpaque ( true );
        headerPanel.setUndecorated ( false );
        headerPanel.setShadeWidth ( 0 );
        headerPanel.setLayout ( new BorderLayout () );
        headerPanel.addMouseListener ( new MouseAdapter ()
        {
            public void mousePressed ( MouseEvent e )
            {
                if ( isAllowAction ( e ) )
                {
                    takeFocus ();
                }
            }

            public void mouseReleased ( MouseEvent e )
            {
                if ( isAllowAction ( e ) )
                {
                    invertExpandState ();
                }
            }

            private boolean isAllowAction ( MouseEvent e )
            {
                return SwingUtilities.isLeftMouseButton ( e ) && SwingUtils.size ( WebCollapsiblePane.this ).contains ( e.getPoint () );
            }
        } );
        updateHeaderPosition ();

        updateDefaultTitleComponent ( icon, title );
        updateDefaultTitleBorder ();

        expandButton = new WebButton ( collapseIcon );
        expandButton.setUndecorated ( true );
        expandButton.setDrawFocus ( false );
        expandButton.setMoveIconOnPress ( false );
        expandButton.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                invertExpandState ();
                takeFocus ();
            }
        } );
        setStateIcons ();
        updateStateIconMargin ();
        updateStateIconPosition ();

        // Content

        contentPanel = new WebPanel ()
        {
            public Dimension getPreferredSize ()
            {
                Dimension ps = super.getPreferredSize ();
                if ( titlePanePostion == TOP || titlePanePostion == BOTTOM )
                {
                    if ( WebCollapsiblePane.this.content != null )
                    {
                        Insets insets = getInsets ();
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
                        Insets insets = getInsets ();
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
        contentPanel.setOpaque ( false );
        contentPanel.setLayout ( new BorderLayout ( 0, 0 ) );
        contentPanel.setMargin ( contentMargin );
        add ( contentPanel, BorderLayout.CENTER );

        if ( this.content != null )
        {
            contentPanel.add ( this.content, BorderLayout.CENTER );
        }

        addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, new PropertyChangeListener ()
        {
            public void propertyChange ( PropertyChangeEvent evt )
            {
                updateStateIcons ();
            }
        } );
    }

    /**
     * Transfers application focus to this collapsible pane.
     */
    private void takeFocus ()
    {
        if ( isShowing () && isEnabled () )
        {
            expandButton.requestFocusInWindow ();
        }
    }

    /**
     * Updates default title component.
     */
    private void updateDefaultTitleComponent ()
    {
        updateDefaultTitleComponent ( getIcon (), getTitle () );
    }

    /**
     * Updates default title component with the specified title icon and text.
     *
     * @param icon  collapsible pane title icon
     * @param title collapsible pane title text
     */
    private void updateDefaultTitleComponent ( Icon icon, String title )
    {
        if ( !customTitle )
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
     * Updates default title component border.
     */
    private void updateDefaultTitleBorder ()
    {
        if ( titleComponent != null && !customTitle )
        {
            // Updating title margin according to title pane position
            boolean ltr = getComponentOrientation ().isLeftToRight ();
            Insets margin = getIcon () != null || titlePanePostion != LEFT || titlePanePostion == RIGHT ? new Insets ( 2, 2, 2, 2 ) :
                    new Insets ( 2, 4, 2, 2 );
            if ( titlePanePostion == LEFT )
            {
                margin = new Insets ( margin.right, margin.top, margin.left, margin.bottom );
            }
            else if ( titlePanePostion == RIGHT )
            {
                margin = new Insets ( margin.left, margin.bottom, margin.right, margin.top );
            }
            ( ( WebLabel ) titleComponent ).setMargin ( margin );
        }
    }

    /**
     * Updates header panel position.
     */
    private void updateHeaderPosition ()
    {
        updateHeaderSides ();
        if ( titlePanePostion == TOP )
        {
            add ( headerPanel, BorderLayout.NORTH );
        }
        else if ( titlePanePostion == BOTTOM )
        {
            add ( headerPanel, BorderLayout.SOUTH );
        }
        else if ( titlePanePostion == LEFT )
        {
            add ( headerPanel, BorderLayout.LINE_START );
        }
        else if ( titlePanePostion == RIGHT )
        {
            add ( headerPanel, BorderLayout.LINE_END );
        }
        revalidate ();
    }

    /**
     * Updates header panel sides style.
     */
    private void updateHeaderSides ()
    {
        headerPanel.setDrawSides ( expanded && titlePanePostion == BOTTOM, expanded && titlePanePostion == RIGHT,
                expanded && titlePanePostion == TOP, expanded && titlePanePostion == LEFT );
    }

    /**
     * Updates state icon position.
     */
    private void updateStateIconPosition ()
    {
        if ( showStateIcon )
        {
            if ( titlePanePostion == TOP || titlePanePostion == BOTTOM )
            {
                headerPanel.add ( expandButton, stateIconPostion == RIGHT ? BorderLayout.LINE_END : BorderLayout.LINE_START );
            }
            else if ( titlePanePostion == LEFT )
            {
                headerPanel.add ( expandButton, stateIconPostion == RIGHT ? BorderLayout.PAGE_START : BorderLayout.PAGE_END );
            }
            else if ( titlePanePostion == RIGHT )
            {
                headerPanel.add ( expandButton, stateIconPostion == RIGHT ? BorderLayout.PAGE_END : BorderLayout.PAGE_START );
            }
        }
        else
        {
            headerPanel.remove ( expandButton );
        }
        headerPanel.revalidate ();
    }

    /**
     * Updates state icon margin.
     */
    private void updateStateIconMargin ()
    {
        expandButton.setMargin ( stateIconMargin );
    }

    /**
     * Returns new default title component with specified icon and text.
     *
     * @param title collapsible pane title text
     * @param icon  collapsible pane title icon
     * @return new default title component with specified icon and text
     */
    private JComponent createDefaultTitleComponent ( Icon icon, String title )
    {
        // todo RTL orientation support
        WebLabel defaultTitle;
        if ( titlePanePostion == LEFT )
        {
            defaultTitle = new WebVerticalLabel ( title, icon, WebLabel.LEADING, false );
        }
        else if ( titlePanePostion == RIGHT )
        {
            defaultTitle = new WebVerticalLabel ( title, icon, WebLabel.LEADING, true );
        }
        else
        {
            defaultTitle = new WebLabel ( title, icon, WebLabel.LEADING );
        }
        defaultTitle.setDrawShade ( true );
        return defaultTitle;
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
     * Sets handler that dynamically enable and disable collapsible pane state changes by providing according boolean value
     *
     * @param stateChangeHandler new state change handler
     */
    public void setStateChangeHandler ( DataProvider<Boolean> stateChangeHandler )
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
    public boolean invertExpandState ( boolean animate )
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
     * @return true if operation succeed, false otherwise
     */
    public boolean setExpanded ( boolean expanded )
    {
        return setExpanded ( expanded, isShowing () && animate );
    }

    /**
     * Changes expanded state to specified one and returns whether operation succeed or not.
     * Operation might fail in case state change is disabled for some reason.
     *
     * @param animate whether animate state change transition or not
     * @return true if operation succeed, false otherwise
     */
    public boolean setExpanded ( boolean expanded, boolean animate )
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
    public boolean collapse ( boolean animate )
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
            animator = new WebTimer ( "WebCollapsiblePane.collapseTimer", StyleConstants.fastAnimationDelay, new ActionListener ()
            {
                public void actionPerformed ( ActionEvent e )
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
    private void finishCollapseAction ()
    {
        // Hide title border
        updateHeaderSides ();

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
    public boolean expand ( boolean animate )
    {
        if ( expanded || !isStateChangeEnabled () )
        {
            return false;
        }

        stopAnimation ();

        expanded = true;
        setStateIcons ();

        if ( content != null )
        {
            content.setVisible ( true );
        }

        // Show title border
        updateHeaderSides ();

        fireExpanding ();

        if ( animate && isShowing () )
        {
            animator = new WebTimer ( "WebCollapsiblePane.expandTimer", StyleConstants.fastAnimationDelay, new ActionListener ()
            {
                public void actionPerformed ( ActionEvent e )
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
    private void finishExpandAction ()
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
    private void stopAnimation ()
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
    public int getTitlePanePostion ()
    {
        return titlePanePostion;
    }

    /**
     * Sets title pane position in collapsible pane.
     *
     * @param titlePanePostion new title pane position in collapsible pane
     */
    public void setTitlePanePostion ( int titlePanePostion )
    {
        this.titlePanePostion = titlePanePostion;
        updateDefaultTitleComponent ();
        updateDefaultTitleBorder ();
        updateHeaderPosition ();
        updateStateIcons ();
        updateStateIconPosition ();
    }

    /**
     * Returns content margin.
     *
     * @return content margin
     */
    public Insets getContentMargin ()
    {
        return contentMargin;
    }

    /**
     * Sets content margin.
     *
     * @param margin content margin
     */
    public void setContentMargin ( Insets margin )
    {
        this.contentMargin = margin;
        contentPanel.setMargin ( margin );
        revalidate ();
    }

    /**
     * Sets content margin.
     *
     * @param top    top content margin
     * @param left   left content margin
     * @param bottom bottom content margin
     * @param right  right content margin
     */
    public void setContentMargin ( int top, int left, int bottom, int right )
    {
        setContentMargin ( new Insets ( top, left, bottom, right ) );
    }

    /**
     * Sets content margin.
     *
     * @param margin content margin
     */
    public void setContentMargin ( int margin )
    {
        setContentMargin ( margin, margin, margin, margin );
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
    public void setAnimate ( boolean animate )
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
        return customTitle ? null : ( ( WebLabel ) titleComponent ).getIcon ();
    }

    /**
     * Sets default title component icon.
     *
     * @param icon new default title component icon
     */
    public void setIcon ( Icon icon )
    {
        if ( !customTitle )
        {
            ( ( WebLabel ) titleComponent ).setIcon ( icon );
            updateDefaultTitleBorder ();
        }
    }

    /**
     * Returns default title component text.
     *
     * @return default title component text
     */
    public String getTitle ()
    {
        return customTitle ? null : ( ( WebLabel ) titleComponent ).getText ();
    }

    /**
     * Sets default title component text.
     *
     * @param title new default title component text
     */
    public void setTitle ( String title )
    {
        if ( !customTitle )
        {
            ( ( WebLabel ) titleComponent ).setText ( title );
        }
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
        clearCachedExpandIcons ();
        setStateIcons ();
    }

    /**
     * Returns state icon margin.
     *
     * @return state icon margin
     */
    public Insets getStateIconMargin ()
    {
        return stateIconMargin;
    }

    /**
     * Sets state icon margin.
     *
     * @param margin new state icon margin
     */
    public void setStateIconMargin ( Insets margin )
    {
        this.stateIconMargin = margin;
        updateStateIconMargin ();
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
    public void setRotateStateIcon ( boolean rotateStateIcon )
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
    public void setShowStateIcon ( boolean showStateIcon )
    {
        this.showStateIcon = showStateIcon;
        updateStateIconPosition ();
    }

    /**
     * Returns state icon position in title pane.
     *
     * @return state icon position in title pane
     */
    public int getStateIconPostion ()
    {
        return stateIconPostion;
    }

    /**
     * Sets state icon position in title pane.
     *
     * @param stateIconPostion new state icon position in title pane
     */
    public void setStateIconPostion ( int stateIconPostion )
    {
        this.stateIconPostion = stateIconPostion;
        updateStateIconPosition ();
    }

    /**
     * Updates state icons.
     */
    private void updateStateIcons ()
    {
        clearCachedCollapseIcons ();
        clearCachedExpandIcons ();
        setStateIcons ();
    }

    /**
     * Installs state icons into state change button.
     */
    private void setStateIcons ()
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

    /**
     * Clears cached expanded state icons.
     */
    private void clearCachedCollapseIcons ()
    {
        cachedCollapseIcon = null;
        cachedDisabledCollapseIcon = null;
    }

    /**
     * Returns cached expanded state icon.
     *
     * @return cached expanded state icon
     */
    private ImageIcon getCachedCollapseIcon ()
    {
        if ( cachedCollapseIcon == null )
        {
            boolean ltr = getComponentOrientation ().isLeftToRight ();
            if ( !rotateStateIcon || titlePanePostion == TOP || titlePanePostion == BOTTOM )
            {
                cachedCollapseIcon = new OrientedIcon ( collapseIcon );
            }
            else if ( titlePanePostion == LEFT )
            {
                cachedCollapseIcon = ImageUtils.rotateImage90CCW ( collapseIcon );
            }
            else if ( titlePanePostion == RIGHT )
            {
                cachedCollapseIcon = ImageUtils.rotateImage90CW ( collapseIcon );
            }
        }
        return cachedCollapseIcon;
    }

    /**
     * Returns cached disabled expanded state icon.
     *
     * @return cached disabled expanded state icon
     */
    private ImageIcon getCachedDisabledCollapseIcon ()
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
    private void clearCachedExpandIcons ()
    {
        cachedExpandIcon = null;
        cachedDisabledExpandIcon = null;
    }

    /**
     * Returns cached collapsed state icon.
     *
     * @return cached collapsed state icon
     */
    private ImageIcon getCachedExpandIcon ()
    {
        if ( cachedExpandIcon == null )
        {
            boolean ltr = getComponentOrientation ().isLeftToRight ();
            if ( !rotateStateIcon || titlePanePostion == TOP || titlePanePostion == BOTTOM )
            {
                cachedExpandIcon = expandIcon;
            }
            else if ( ltr ? titlePanePostion == LEFT : titlePanePostion == RIGHT )
            {
                cachedExpandIcon = ImageUtils.rotateImage90CCW ( expandIcon );
            }
            else if ( ltr ? titlePanePostion == RIGHT : titlePanePostion == LEFT )
            {
                cachedExpandIcon = ImageUtils.rotateImage90CW ( expandIcon );
            }
        }
        return cachedExpandIcon;
    }

    /**
     * Returns cached disabled collapsed state icon.
     *
     * @return cached disabled collapsed state icon
     */
    private ImageIcon getCachedDisabledExpandIcon ()
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
    public void setTitleComponent ( Component titleComponent )
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
    public void setContent ( Component content )
    {
        if ( this.content != null )
        {
            contentPanel.remove ( content );
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
    public void setCollapsiblePaneListeners ( List<CollapsiblePaneListener> listeners )
    {
        this.listeners = listeners;
    }

    /**
     * Adds collapsible pane listener.
     *
     * @param listener collapsible pane listener to add
     */
    public void addCollapsiblePaneListener ( CollapsiblePaneListener listener )
    {
        listeners.add ( listener );
    }

    /**
     * Removes collapsible pane listener.
     *
     * @param listener collapsible pane listener to remove
     */
    public void removeCollapsiblePaneListener ( CollapsiblePaneListener listener )
    {
        listeners.remove ( listener );
    }

    /**
     * Notifies when collapsible pane starts to expand.
     */
    private void fireExpanding ()
    {
        for ( CollapsiblePaneListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.expanding ( this );
        }
    }

    /**
     * Notifies when collapsible pane finished expanding.
     */
    private void fireExpanded ()
    {
        for ( CollapsiblePaneListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.expanded ( this );
        }
    }

    /**
     * Notifies when collapsible pane starts to collapse.
     */
    private void fireCollapsing ()
    {
        for ( CollapsiblePaneListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.collapsing ( this );
        }
    }

    /**
     * Notifies when collapsible pane finished collapsing.
     */
    private void fireCollapsed ()
    {
        for ( CollapsiblePaneListener listener : CollectionUtils.copy ( listeners ) )
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
        Dimension ps = getPreferredSize ();
        if ( content == null || transitionProgress <= 0 )
        {
            return ps;
        }
        else
        {
            Dimension cps = content.getPreferredSize ();
            if ( titlePanePostion == TOP || titlePanePostion == BOTTOM )
            {
                return new Dimension ( ps.width, ps.height - Math.round ( cps.height * transitionProgress ) );
            }
            else
            {
                return new Dimension ( ps.width - Math.round ( cps.width * transitionProgress ), ps.height );
            }
        }
    }

    /**
     * Sets the focusable state of this Component to the specified value.
     * This value overrides the Component's default focusability.
     *
     * @param focusable indicates whether this Component is focusable
     */
    public void setFocusable ( boolean focusable )
    {
        expandButton.setFocusable ( focusable );
    }

    /**
     * Returns whether this Component can be focused.
     *
     * @return true if this Component is focusable, false otherwise
     */
    public boolean isFocusable ()
    {
        return expandButton.isFocusable ();
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String key )
    {
        SettingsManager.registerComponent ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    public <T extends DefaultValue> void registerSettings ( String key, Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String key, Object defaultValue )
    {
        SettingsManager.registerComponent ( this, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String group, String key )
    {
        SettingsManager.registerComponent ( this, group, key );
    }

    /**
     * {@inheritDoc}
     */
    public <T extends DefaultValue> void registerSettings ( String group, String key, Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String group, String key, Object defaultValue )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String key, boolean loadInitialSettings, boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    public <T extends DefaultValue> void registerSettings ( String key, Class<T> defaultValueClass, boolean loadInitialSettings,
                                                            boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String key, Object defaultValue, boolean loadInitialSettings, boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    public <T extends DefaultValue> void registerSettings ( String group, String key, Class<T> defaultValueClass,
                                                            boolean loadInitialSettings, boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String group, String key, Object defaultValue, boolean loadInitialSettings,
                                   boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( SettingsProcessor settingsProcessor )
    {
        SettingsManager.registerComponent ( this, settingsProcessor );
    }

    /**
     * {@inheritDoc}
     */
    public void unregisterSettings ()
    {
        SettingsManager.unregisterComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    public void loadSettings ()
    {
        SettingsManager.loadComponentSettings ( this );
    }

    /**
     * {@inheritDoc}
     */
    public void saveSettings ()
    {
        SettingsManager.saveComponentSettings ( this );
    }
}