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

package com.alee.laf.rootpane;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.data.CompassDirection;
import com.alee.api.jdk.Consumer;
import com.alee.api.jdk.Function;
import com.alee.api.jdk.Objects;
import com.alee.extended.behavior.ComponentResizeBehavior;
import com.alee.extended.image.WebImage;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.grouping.GroupPane;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.window.WebWindow;
import com.alee.managers.language.LM;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.*;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Custom UI for {@link JRootPane} component.
 * This UI also includes custom frame and dialog decoration elements.
 *
 * @author Mikle Garin
 */
public class WebRootPaneUI extends WRootPaneUI implements ShapeSupport, MarginSupport, PaddingSupport, SwingConstants
{
    /**
     * todo 1. Probably track content pane change and update its style in future
     */

    /**
     * Root pane styling icons.
     */
    public static ImageIcon minimizeIcon = new ImageIcon ( WebRootPaneUI.class.getResource ( "icons/minimize.png" ) );
    public static ImageIcon minimizeActiveIcon = new ImageIcon ( WebRootPaneUI.class.getResource ( "icons/minimize_active.png" ) );
    public static ImageIcon maximizeIcon = new ImageIcon ( WebRootPaneUI.class.getResource ( "icons/maximize.png" ) );
    public static ImageIcon maximizeActiveIcon = new ImageIcon ( WebRootPaneUI.class.getResource ( "icons/maximize_active.png" ) );
    public static ImageIcon restoreIcon = new ImageIcon ( WebRootPaneUI.class.getResource ( "icons/restore.png" ) );
    public static ImageIcon restoreActiveIcon = new ImageIcon ( WebRootPaneUI.class.getResource ( "icons/restore_active.png" ) );
    public static ImageIcon closeIcon = new ImageIcon ( WebRootPaneUI.class.getResource ( "icons/close.png" ) );
    public static ImageIcon closeActiveIcon = new ImageIcon ( WebRootPaneUI.class.getResource ( "icons/close_active.png" ) );

    /**
     * Style settings.
     */
    protected boolean installComponents;
    protected int iconSize;
    protected String emptyTitleText;
    protected boolean setupButtonIcons;
    protected boolean displayTitleComponent;
    protected boolean displayWindowButtons;
    protected boolean displayMinimizeButton;
    protected boolean displayMaximizeButton;
    protected boolean displayCloseButton;
    protected boolean displayMenuBar;

    /**
     * Component painter.
     */
    @DefaultPainter ( RootPanePainter.class )
    protected IRootPanePainter painter;

    /**
     * Listeners.
     */
    protected transient PropertyChangeListener resizableChangeListener;
    protected transient ComponentResizeBehavior resizeBehavior;
    protected transient PropertyChangeListener windowTitleListener;

    /**
     * Additional components used be the UI.
     */
    protected transient JComponent titleComponent;
    protected transient WindowDecorationBehavior decorationBehavior;
    protected transient WebImage titleIcon;
    protected transient WebLabel titleLabel;
    protected transient GroupPane buttonsPanel;
    protected transient WebButton minimizeButton;
    protected transient WebButton maximizeButton;
    protected transient WebButton closeButton;

    /**
     * Runtime variables
     */
    protected transient JRootPane root;
    protected transient Window window;
    protected transient Frame frame;
    protected transient Dialog dialog;
    protected transient LayoutManager previousLayoutManager;
    protected transient LayoutManager layoutManager;

    /**
     * Returns an instance of the {@link WebRootPaneUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebRootPaneUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebRootPaneUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Saving root pane reference
        root = ( JRootPane ) c;

        // Applying skin
        StyleManager.installSkin ( root );

        // Installing content pane style ID
        // We are not listening for its changes since the only way is to track layered pane contents
        // In that case we would also need to track root pane contents for layered pane changes which is excessive
        // Content pane is usually not changed or provided by the root pane override and this style will be applied then
        final Container contentPane = root.getContentPane ();
        if ( contentPane instanceof JComponent )
        {
            final JComponent jContentPane = ( JComponent ) contentPane;
            if ( LafUtils.hasWebLafUI ( jContentPane ) )
            {
                StyleId.rootpaneContent.at ( root ).set ( jContentPane );
            }
        }
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( root );

        // Cleaning up runtime variables
        root = null;

        super.uninstallUI ( c );
    }

    @NotNull
    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( root, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( root, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( root, painter, enabled );
    }

    @Nullable
    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( root );
    }

    @Override
    public void setMargin ( @Nullable final Insets margin )
    {
        PainterSupport.setMargin ( root, margin );
    }

    @Nullable
    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( root );
    }

    @Override
    public void setPadding ( @Nullable final Insets padding )
    {
        PainterSupport.setPadding ( root, padding );
    }

    /**
     * Returns root pane painter.
     *
     * @return root pane painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets root pane painter.
     * Pass null to remove root pane painter.
     *
     * @param painter new root pane painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( root, this, new Consumer<IRootPanePainter> ()
        {
            @Override
            public void accept ( final IRootPanePainter newPainter )
            {
                WebRootPaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, IRootPanePainter.class, AdaptiveRootPanePainter.class );
    }

    @Override
    public boolean isDecorated ()
    {
        return painter != null && painter.isDecorated ();
    }

    @Override
    public void installWindowDecorations ()
    {
        if ( root.getWindowDecorationStyle () != JRootPane.NONE && isDecorated () )
        {
            window = getWindow ();
            frame = window instanceof Frame ? ( Frame ) window : null;
            dialog = window instanceof Dialog ? ( Dialog ) window : null;
            installSettings ();
            installListeners ();
            installLayout ();
            installDecorationComponents ();
        }
    }

    @Override
    public void uninstallWindowDecorations ()
    {
        if ( window != null )
        {
            uninstallDecorationComponents ();
            uninstallLayout ();
            uninstallListeners ();
            uninstallSettings ();
            dialog = null;
            frame = null;
            window = null;
        }
    }

    @Override
    public boolean isDisplayTitleComponent ()
    {
        return displayTitleComponent;
    }

    @Override
    public void setDisplayTitleComponent ( final boolean display )
    {
        this.displayTitleComponent = display;
        root.revalidate ();
    }

    @Override
    public JComponent getTitleComponent ()
    {
        return titleComponent;
    }

    @Override
    public void setTitleComponent ( final JComponent title )
    {
        final boolean decorated = titleComponent != null;
        if ( decorated )
        {
            root.remove ( titleComponent );
        }
        titleComponent = title;
        if ( decorated )
        {
            root.add ( titleComponent );
            root.revalidate ();
        }
    }

    @Override
    public boolean isDisplayWindowButtons ()
    {
        return displayWindowButtons;
    }

    @Override
    public void setDisplayWindowButtons ( final boolean display )
    {
        this.displayWindowButtons = display;
        root.revalidate ();
    }

    @Override
    public boolean isDisplayMinimizeButton ()
    {
        return displayMinimizeButton;
    }

    @Override
    public void setDisplayMinimizeButton ( final boolean display )
    {
        this.displayMinimizeButton = display;
        updateButtons ();
        root.revalidate ();
    }

    @Override
    public boolean isDisplayMaximizeButton ()
    {
        return displayMaximizeButton;
    }

    @Override
    public void setDisplayMaximizeButton ( final boolean display )
    {
        this.displayMaximizeButton = display;
        updateButtons ();
        root.revalidate ();
    }

    @Override
    public boolean isDisplayCloseButton ()
    {
        return displayCloseButton;
    }

    @Override
    public void setDisplayCloseButton ( final boolean display )
    {
        this.displayCloseButton = display;
        updateButtons ();
        root.revalidate ();
    }

    @Override
    public JComponent getButtonsPanel ()
    {
        return buttonsPanel;
    }

    @Override
    public boolean isDisplayMenuBar ()
    {
        return displayMenuBar;
    }

    @Override
    public void setDisplayMenuBar ( final boolean display )
    {
        this.displayMenuBar = display;
        root.revalidate ();
    }

    /**
     * Installs settings used in runtime.
     */
    protected void installSettings ()
    {
        if ( isFrame () )
        {
            // Installing maximized frame bounds
            frame.setMaximizedBounds ( SystemUtils.getMaximizedBounds ( frame ) );
        }
    }

    /**
     * Uninstalls settings used in runtime.
     */
    protected void uninstallSettings ()
    {
        if ( isFrame () )
        {
            // Uninstalling maximized frame bounds
            frame.setMaximizedBounds ( null );
        }
    }

    /**
     * Installs listeners.
     */
    protected void installListeners ()
    {
        // Listen to window resizeability changes
        resizableChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateButtons ();
            }
        };
        window.addPropertyChangeListener ( WebWindow.RESIZABLE_PROPERTY, resizableChangeListener );

        // Window resize behavior
        // todo Should this be tied to painter instead?
        resizeBehavior = new ComponentResizeBehavior ( root, new Function<Point, CompassDirection> ()
        {
            @Override
            public CompassDirection apply ( final Point p )
            {
                // Ensure dialog or frame is resizable
                final CompassDirection direction;
                if ( dialog != null && dialog.isResizable () || frame != null && frame.isResizable () )
                {
                    // Ensure that point is outside of inner bounds
                    final Rectangle bounds = BoundsType.padding.bounds ( root );
                    final Rectangle inner = GeometryUtils.expand ( bounds, -5, -5, -10, -10 );
                    if ( !inner.contains ( p ) )
                    {
                        // Ensure that point is inside the outer bounds
                        final Rectangle outer = GeometryUtils.expand ( inner, 10, 10, 20, 20 );
                        if ( outer.contains ( p ) )
                        {
                            // Return appropriate resize direction
                            if ( p.y < inner.y )
                            {
                                if ( p.x < inner.x )
                                {
                                    direction = CompassDirection.northWest;
                                }
                                else if ( p.x > inner.x + inner.width )
                                {
                                    direction = CompassDirection.northEast;
                                }
                                else
                                {
                                    direction = CompassDirection.north;
                                }
                            }
                            else if ( p.y > inner.y + inner.height )
                            {
                                if ( p.x < inner.x )
                                {
                                    direction = CompassDirection.southWest;
                                }
                                else if ( p.x > inner.x + inner.width )
                                {
                                    direction = CompassDirection.southEast;
                                }
                                else
                                {
                                    direction = CompassDirection.south;
                                }
                            }
                            else if ( p.x < inner.x )
                            {
                                direction = CompassDirection.west;
                            }
                            else if ( p.x > inner.x + inner.width )
                            {
                                direction = CompassDirection.east;
                            }
                            else
                            {
                                direction = null;
                            }
                        }
                        else
                        {
                            direction = null;
                        }
                    }
                    else
                    {
                        direction = null;
                    }
                }
                else
                {
                    direction = null;
                }
                return direction;
            }
        } );
        resizeBehavior.install ();
    }

    /**
     * Uninstalls listeners.
     */
    protected void uninstallListeners ()
    {
        resizeBehavior.uninstall ();
        resizeBehavior = null;
        window.removePropertyChangeListener ( WebWindow.RESIZABLE_PROPERTY, resizableChangeListener );
        resizableChangeListener = null;
    }

    /**
     * Installs appropriate layout manager.
     */
    protected void installLayout ()
    {
        if ( layoutManager == null )
        {
            layoutManager = new WebRootPaneLayout ();
        }
        previousLayoutManager = root.getLayout ();
        root.setLayout ( layoutManager );
    }

    /**
     * Uninstalls layout manager.
     */
    protected void uninstallLayout ()
    {
        if ( previousLayoutManager != null )
        {
            root.setLayout ( previousLayoutManager );
            previousLayoutManager = null;
            layoutManager = null;
        }
    }

    /**
     * Returns whether or not custom decoration components can be installed.
     *
     * @return true if custom decoration components can be installed, false otherwise
     */
    protected boolean isComponentInstallAllowed ()
    {
        return installComponents && ( isFrame () || isDialog () );
    }

    /**
     * Installs decoration components.
     */
    protected void installDecorationComponents ()
    {
        if ( isComponentInstallAllowed () )
        {
            // Title
            createTitleComponent ();

            // Buttons
            updateButtons ();
        }
    }

    /**
     * Uninstalls decoration components.
     */
    protected void uninstallDecorationComponents ()
    {
        if ( isComponentInstallAllowed () )
        {
            // Title
            destroyTitleComponent ();

            // Buttons
            destroyButtons ();
        }
    }

    /**
     * Creates window title component.
     */
    protected void createTitleComponent ()
    {
        if ( titleComponent == null )
        {
            // Title panel
            final StyleId titlePanelId = StyleId.rootpaneTitlePanel.at ( root );
            titleComponent = new WebPanel ( titlePanelId, new BorderLayout ( 0, 0 ) );

            // Window icon
            titleIcon = new WebImage ( StyleId.rootpaneTitleIcon.at ( titleComponent ), getWindowImage () );
            titleComponent.add ( titleIcon, BorderLayout.LINE_START );

            // Window title
            titleLabel = new WebLabel ( StyleId.rootpaneTitleLabel.at ( titleComponent ), getWindowTitle () );
            titleLabel.setFont ( WebLookAndFeel.globalWindowFont );
            titleLabel.setFontSize ( 13 );
            titleLabel.addComponentListener ( new ComponentAdapter ()
            {
                /**
                 * Saving initial alignment to avoid overwriting provided by the style.
                 */
                private final int initialAlignment = titleLabel.getHorizontalAlignment ();

                @Override
                public void componentResized ( final ComponentEvent e )
                {
                    // Changing title horizontal alignment to avoid title jumping left/right
                    final boolean trimmed = titleLabel.getOriginalPreferredSize ().width > titleLabel.getWidth ();
                    final boolean ltr = titleLabel.getComponentOrientation ().isLeftToRight ();
                    final int alignment = trimmed ? ltr ? LEADING : TRAILING : initialAlignment;
                    titleLabel.setHorizontalAlignment ( alignment );
                }
            } );
            titleComponent.add ( titleLabel, BorderLayout.CENTER );

            // Listen to window icon and title changes
            windowTitleListener = new PropertyChangeListener ()
            {
                @Override
                public void propertyChange ( final PropertyChangeEvent evt )
                {
                    final String property = evt.getPropertyName ();
                    if ( Objects.equals ( property, WebWindow.ICON_IMAGE_PROPERTY ) )
                    {
                        titleIcon.setImage ( getWindowImage () );
                    }
                    else if ( Objects.equals ( property, WebWindow.TITLE_PROPERTY ) )
                    {
                        titleLabel.setText ( getWindowTitle () );
                    }
                }
            };
            window.addPropertyChangeListener ( windowTitleListener );

            // Installing window decoration behavior
            decorationBehavior = new WindowDecorationBehavior ( this );
            decorationBehavior.install ();
        }
        root.add ( titleComponent );
    }

    /**
     * Destroys window title component.
     */
    protected void destroyTitleComponent ()
    {
        if ( titleComponent != null )
        {
            decorationBehavior.uninstall ();
            decorationBehavior = null;
            window.removePropertyChangeListener ( windowTitleListener );
            root.remove ( titleComponent );
            StyleManager.resetStyleId ( titleComponent );
            titleComponent = null;
            titleIcon = null;
            titleLabel = null;
        }
    }

    /**
     * Updates displayed buttons.
     *
     * todo 1. Optimize button updates
     * todo 2. Reference icons in style instead of using hardcoded ones
     * todo 3. Instead of single button for maximize/restore add a new restore button?
     */
    protected void updateButtons ()
    {
        if ( isDecorated () && isComponentInstallAllowed () )
        {
            // Creating new buttons panel
            if ( buttonsPanel == null )
            {
                buttonsPanel = new GroupPane ( StyleId.rootpaneButtonsPanel.at ( root ) );
                buttonsPanel.setPaintSides ( false, true, true, true );
                root.add ( buttonsPanel );
            }

            // Minimize button
            if ( displayMinimizeButton && isFrame () )
            {
                if ( minimizeButton == null )
                {
                    final StyleId minimizeId = StyleId.rootpaneMinimizeButton.at ( buttonsPanel );
                    minimizeButton = new WebButton ( minimizeId )
                    {
                        @Override
                        public Icon getIcon ()
                        {
                            return setupButtonIcons ? minimizeIcon : null;
                        }

                        @Override
                        public Icon getRolloverIcon ()
                        {
                            return setupButtonIcons ? minimizeActiveIcon : null;
                        }
                    };
                    minimizeButton.setName ( "minimize" );
                    minimizeButton.setRolloverEnabled ( true );
                    minimizeButton.addActionListener ( new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            iconify ();
                        }
                    } );
                }
                buttonsPanel.add ( minimizeButton );
            }
            else
            {
                if ( minimizeButton != null )
                {
                    buttonsPanel.remove ( minimizeButton );
                }
            }

            // Maximize button
            if ( displayMaximizeButton && isResizable () && isFrame () )
            {
                if ( maximizeButton == null )
                {
                    final StyleId maximizeId = StyleId.rootpaneMaximizeButton.at ( buttonsPanel );
                    maximizeButton = new WebButton ( maximizeId )
                    {
                        @Override
                        public Icon getIcon ()
                        {
                            return setupButtonIcons ? isMaximized () ? restoreIcon : maximizeIcon : null;
                        }

                        @Override
                        public Icon getRolloverIcon ()
                        {
                            return setupButtonIcons ? isMaximized () ? restoreActiveIcon : maximizeActiveIcon : null;
                        }
                    };
                    maximizeButton.setName ( "maximize" );
                    maximizeButton.setRolloverEnabled ( true );
                    maximizeButton.addActionListener ( new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            if ( isFrame () )
                            {
                                if ( isMaximized () )
                                {
                                    restore ();
                                }
                                else
                                {
                                    maximize ();
                                }
                            }
                        }
                    } );
                }
                buttonsPanel.add ( maximizeButton );
            }
            else
            {
                if ( maximizeButton != null )
                {
                    buttonsPanel.remove ( maximizeButton );
                }
            }

            // Close button
            if ( displayCloseButton )
            {
                if ( closeButton == null )
                {
                    final StyleId closeId = StyleId.rootpaneCloseButton.at ( buttonsPanel );
                    closeButton = new WebButton ( closeId )
                    {
                        @Override
                        public Icon getIcon ()
                        {
                            return setupButtonIcons ? closeIcon : null;
                        }

                        @Override
                        public Icon getRolloverIcon ()
                        {
                            return setupButtonIcons ? closeActiveIcon : null;
                        }
                    };
                    closeButton.setName ( "close" );
                    closeButton.setRolloverEnabled ( true );
                    closeButton.addActionListener ( new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            close ();
                        }
                    } );
                }
                buttonsPanel.add ( closeButton );
            }
            else
            {
                if ( closeButton != null )
                {
                    buttonsPanel.remove ( closeButton );
                }
            }
        }
    }

    /**
     * Destroys window buttons.
     */
    protected void destroyButtons ()
    {
        if ( buttonsPanel != null )
        {
            root.remove ( buttonsPanel );
            buttonsPanel.resetStyleId ();
            buttonsPanel = null;
            minimizeButton = null;
            maximizeButton = null;
            closeButton = null;
        }
    }

    /**
     * Returns window title text.
     * Single spacing workaround allows window dragging even when title is not set.
     * That is required because otherwise title label would shrink to zero size due to missing content.
     *
     * @return window title text
     */
    protected String getWindowTitle ()
    {
        final String title = isDialog () ? dialog.getTitle () : isFrame () ? frame.getTitle () : null;
        final String t = !TextUtils.isBlank ( title ) ? title : emptyTitleText != null ? LM.get ( emptyTitleText ) : null;
        return !TextUtils.isEmpty ( t ) ? t : " ";
    }

    /**
     * Returns window image of suitable size if possible.
     * Image size will not be adjusted here, so make sure to do that if its needed elsewhere.
     *
     * @return window image of suitable size if possible
     */
    protected Image getWindowImage ()
    {
        final Image image;
        final List<Image> images = window != null ? window.getIconImages () : null;
        if ( CollectionUtils.notEmpty ( images ) )
        {
            if ( images.size () > 1 )
            {
                int bestIndex = 0;
                int bestDiff = Math.abs ( images.get ( bestIndex ).getWidth ( null ) - iconSize );
                for ( int i = 1; i < images.size (); i++ )
                {
                    if ( bestDiff == 0 )
                    {
                        break;
                    }
                    final int diff = Math.abs ( images.get ( i ).getWidth ( null ) - iconSize );
                    if ( diff < bestDiff )
                    {
                        bestIndex = i;
                        bestDiff = diff;
                    }
                }
                image = images.get ( bestIndex );
            }
            else
            {
                image = images.get ( 0 );
            }
        }
        else
        {
            image = null;
        }
        return image;
    }

    @Override
    public Window getWindow ()
    {
        return CoreSwingUtils.getWindowAncestor ( root );
    }

    @Override
    public boolean isFrame ()
    {
        return frame != null;
    }

    @Override
    public boolean isDialog ()
    {
        return dialog != null;
    }

    @Override
    public void close ()
    {
        if ( window != null )
        {
            window.dispatchEvent ( new WindowEvent ( window, WindowEvent.WINDOW_CLOSING ) );
        }
    }

    @Override
    public void iconify ()
    {
        if ( frame != null )
        {
            frame.setExtendedState ( Frame.ICONIFIED );
        }
    }

    @Override
    public void maximize ()
    {
        if ( frame != null )
        {
            // Updating maximized bounds for the frame
            frame.setMaximizedBounds ( SystemUtils.getMaximizedBounds ( frame ) );

            // Forcing window to go into maximized state
            frame.setExtendedState ( Frame.MAXIMIZED_BOTH );
        }
    }

    @Override
    public void maximizeWest ()
    {
        if ( frame != null )
        {
            // Updating maximized bounds for the frame
            frame.setMaximizedBounds ( SystemUtils.getMaximizedWestBounds ( frame ) );

            // todo Need to provide exact bounds for west side to work properly
            // todo frame.setBounds ( westBounds );

            // Forcing window to go into maximized state
            frame.setExtendedState ( Frame.MAXIMIZED_VERT );

        }
    }

    @Override
    public void maximizeEast ()
    {
        if ( frame != null )
        {
            // Updating maximized bounds for the frame
            frame.setMaximizedBounds ( SystemUtils.getMaximizedEastBounds ( frame ) );

            // todo Need to provide exact bounds for east side to work properly
            // todo frame.setBounds ( eastBounds );

            // Forcing window to go into maximized state
            frame.setExtendedState ( Frame.MAXIMIZED_VERT );
        }
    }

    @Override
    public void restore ()
    {
        if ( frame != null )
        {
            frame.setExtendedState ( Frame.NORMAL );
        }
    }

    @Override
    protected boolean isResizable ()
    {
        return isDialog () ? dialog.isResizable () : isFrame () && frame.isResizable ();
    }

    @Override
    public boolean isIconified ()
    {
        return isFrame () && ( frame.getExtendedState () & Frame.ICONIFIED ) == Frame.ICONIFIED;
    }

    @Override
    public boolean isMaximized ()
    {
        return isFrame () && ( ( frame.getExtendedState () & Frame.MAXIMIZED_BOTH ) == Frame.MAXIMIZED_BOTH ||
                ( frame.getExtendedState () & Frame.MAXIMIZED_HORIZ ) == Frame.MAXIMIZED_HORIZ ||
                ( frame.getExtendedState () & Frame.MAXIMIZED_VERT ) == Frame.MAXIMIZED_VERT );
    }

    @Override
    public boolean contains ( final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, painter, x, y );
    }

    @Override
    public int getBaseline ( final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, painter, width, height );
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this, painter );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        // return PainterSupport.getPreferredSize ( c, painter );
        return null;
    }
}