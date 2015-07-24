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

import com.alee.extended.panel.WebButtonGroup;
import com.alee.extended.window.ComponentMoveAdapter;
import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.*;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Custom UI for JRootPane component.
 * This UI also includes custom frame and dialog decorations.
 *
 * @author Mikle Garin
 */

public class WebRootPaneUI extends BasicRootPaneUI implements SwingConstants
{
    /**
     * todo 1. Resizable using sides when decorated
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
    protected Color topBg = WebRootPaneStyle.topBg;
    protected Color middleBg = WebRootPaneStyle.middleBg;
    protected Color borderColor = WebRootPaneStyle.borderColor;
    protected Color innerBorderColor = WebRootPaneStyle.innerBorderColor;
    protected int shadeWidth = WebRootPaneStyle.shadeWidth;
    protected int inactiveShadeWidth = WebRootPaneStyle.inactiveShadeWidth;
    protected int round = WebRootPaneStyle.round;
    protected Insets margin = WebRootPaneStyle.margin;
    protected boolean drawWatermark = WebRootPaneStyle.drawWatermark;
    protected ImageIcon watermark = WebRootPaneStyle.watermark;
    protected int maxTitleWidth = WebRootPaneStyle.maxTitleWidth;
    protected String emptyTitleText = "   ";

    /**
     * Displayed window elements.
     */
    protected boolean showTitleComponent = WebRootPaneStyle.showTitleComponent;
    protected boolean showMenuBar = WebRootPaneStyle.showMenuBar;
    protected boolean showWindowButtons = WebRootPaneStyle.showWindowButtons;
    protected boolean showMinimizeButton = WebRootPaneStyle.showMinimizeButton;
    protected boolean showMaximizeButton = WebRootPaneStyle.showMaximizeButton;
    protected boolean showCloseButton = WebRootPaneStyle.showCloseButton;
    protected boolean groupButtons = WebRootPaneStyle.groupButtons;
    protected boolean attachButtons = WebRootPaneStyle.attachButtons;
    protected boolean showResizeCorner = WebRootPaneStyle.showResizeCorner;

    /**
     * Runtime variables
     */
    protected boolean styled = false;
    protected JRootPane root;
    protected Window window;
    protected Frame frame;
    protected Dialog dialog;
    protected int state;
    protected LayoutManager layoutManager;
    protected LayoutManager savedOldLayout;
    protected WindowFocusListener windowFocusListener;
    protected PropertyChangeListener titleChangeListener;
    protected PropertyChangeListener resizableChangeListener;
    protected WindowStateListener windowStateListener;
    protected JComponent titleComponent;
    protected WebButtonGroup windowButtons;
    protected WebResizeCorner resizeCorner;

    /**
     * Returns an instance of the WebRootPaneUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebRootPaneUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebRootPaneUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        root = ( JRootPane ) c;

        // Default settings
        SwingUtils.setOrientation ( root );
        root.setBackground ( StyleConstants.backgroundColor );

        // Decoration
        installWindowDecorations ();
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        super.uninstallUI ( c );

        // Decoration
        uninstallWindowDecorations ();

        // Variables
        layoutManager = null;
        root = null;
    }

    /**
     * UI parameters
     */

    public boolean isStyled ()
    {
        return styled;
    }

    public Color getTopBg ()
    {
        return topBg;
    }

    public void setTopBg ( final Color topBg )
    {
        this.topBg = topBg;
        root.repaint ();
    }

    public Color getMiddleBg ()
    {
        return middleBg;
    }

    public void setMiddleBg ( final Color middleBg )
    {
        this.middleBg = middleBg;
        root.repaint ();
    }

    public Color getBorderColor ()
    {
        return borderColor;
    }

    public void setBorderColor ( final Color borderColor )
    {
        this.borderColor = borderColor;
        root.repaint ();
    }

    public Color getInnerBorderColor ()
    {
        return innerBorderColor;
    }

    public void setInnerBorderColor ( final Color innerBorderColor )
    {
        this.innerBorderColor = innerBorderColor;
        root.repaint ();
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    public void setShadeWidth ( final int shadeWidth )
    {
        final int diff = shadeWidth - this.shadeWidth;
        this.shadeWidth = shadeWidth;
        if ( styled && window != null )
        {
            final Rectangle b = window.getBounds ();
            window.setBounds ( b.x - diff, b.y - diff, b.width + diff * 2, b.height + diff * 2 );
            installBorder ();
            root.revalidate ();
            root.repaint ();
        }
    }

    public int getInactiveShadeWidth ()
    {
        return inactiveShadeWidth;
    }

    public void setInactiveShadeWidth ( final int inactiveShadeWidth )
    {
        this.inactiveShadeWidth = inactiveShadeWidth;
        if ( styled )
        {
            installBorder ();
            root.revalidate ();
            root.repaint ();
        }
    }

    public int getRound ()
    {
        return round;
    }

    public void setRound ( final int round )
    {
        this.round = round;
        if ( styled )
        {
            updateWindowButtonsStyle ();
            root.revalidate ();
            root.repaint ();
        }
    }

    public Insets getMargin ()
    {
        return margin;
    }

    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        if ( styled )
        {
            installBorder ();
            root.revalidate ();
            root.repaint ();
        }
    }

    public boolean isDrawWatermark ()
    {
        return drawWatermark;
    }

    public void setDrawWatermark ( final boolean drawWatermark )
    {
        this.drawWatermark = drawWatermark;
        if ( styled )
        {
            root.repaint ();
        }
    }

    public ImageIcon getWatermark ()
    {
        if ( watermark == null && drawWatermark )
        {
            watermark = new ImageIcon ( WebRootPaneUI.class.getResource ( "icons/watermark.png" ) );
        }
        return watermark;
    }

    public void setWatermark ( final ImageIcon watermark )
    {
        this.watermark = watermark;
    }

    public int getMaxTitleWidth ()
    {
        return maxTitleWidth;
    }

    public void setMaxTitleWidth ( final int maxTitleWidth )
    {
        this.maxTitleWidth = maxTitleWidth;
        if ( isStyled () && titleComponent != null )
        {
            titleComponent.revalidate ();
            titleComponent.repaint ();
        }
    }

    public String getEmptyTitleText ()
    {
        return emptyTitleText;
    }

    public void setEmptyTitleText ( final String emptyTitleText )
    {
        this.emptyTitleText = emptyTitleText;
        if ( isStyled () && titleComponent != null )
        {
            titleComponent.revalidate ();
            titleComponent.repaint ();
        }
    }

    public JComponent getTitleComponent ()
    {
        return titleComponent;
    }

    public void setTitleComponent ( final JComponent titleComponent )
    {
        // todo Mark as custom title component
        this.titleComponent = titleComponent;
        root.revalidate ();
    }

    public WebButtonGroup getWindowButtons ()
    {
        return windowButtons;
    }

    public WebResizeCorner getResizeCorner ()
    {
        return resizeCorner;
    }

    /**
     * Window elements settings
     */

    public boolean isShowResizeCorner ()
    {
        return showResizeCorner;
    }

    public void setShowResizeCorner ( final boolean showResizeCorner )
    {
        this.showResizeCorner = showResizeCorner;
        root.revalidate ();
    }

    public boolean isShowTitleComponent ()
    {
        return showTitleComponent;
    }

    public void setShowTitleComponent ( final boolean showTitleComponent )
    {
        this.showTitleComponent = showTitleComponent;
        root.revalidate ();
    }

    public boolean isShowWindowButtons ()
    {
        return showWindowButtons;
    }

    public void setShowWindowButtons ( final boolean showWindowButtons )
    {
        this.showWindowButtons = showWindowButtons;
        root.revalidate ();
    }

    public boolean isShowMinimizeButton ()
    {
        return showMinimizeButton;
    }

    public void setShowMinimizeButton ( final boolean showMinimizeButton )
    {
        this.showMinimizeButton = showMinimizeButton;
        updateButtons ();
        root.revalidate ();
    }

    public boolean isShowMaximizeButton ()
    {
        return showMaximizeButton;
    }

    public void setShowMaximizeButton ( final boolean showMaximizeButton )
    {
        this.showMaximizeButton = showMaximizeButton;
        updateButtons ();
        root.revalidate ();
    }

    public boolean isShowCloseButton ()
    {
        return showCloseButton;
    }

    public void setShowCloseButton ( final boolean showCloseButton )
    {
        this.showCloseButton = showCloseButton;
        updateButtons ();
        root.revalidate ();
    }

    public boolean isGroupButtons ()
    {
        return groupButtons;
    }

    public void setGroupButtons ( final boolean groupButtons )
    {
        this.groupButtons = groupButtons;
        updateButtons ();
        root.revalidate ();
        root.repaint ();
    }

    public boolean isAttachButtons ()
    {
        return attachButtons;
    }

    public void setAttachButtons ( final boolean attachButtons )
    {
        this.attachButtons = attachButtons;
        updateButtons ();
        root.revalidate ();
        root.repaint ();
    }

    public boolean isShowMenuBar ()
    {
        return showMenuBar;
    }

    public void setShowMenuBar ( final boolean showMenuBar )
    {
        this.showMenuBar = showMenuBar;
        root.revalidate ();
    }

    /**
     * Listening to decoration changes
     */

    @Override
    public void propertyChange ( final PropertyChangeEvent e )
    {
        super.propertyChange ( e );

        final String propertyName = e.getPropertyName ();
        if ( propertyName == null )
        {
            return;
        }

        // Install decorations
        if ( propertyName.equals ( WebLookAndFeel.WINDOW_DECORATION_STYLE_PROPERTY ) )
        {
            final JRootPane root = ( JRootPane ) e.getSource ();
            final int style = root.getWindowDecorationStyle ();

            // Removing old decoration
            uninstallWindowDecorations ();

            // Adding new decoration if needed
            if ( style != JRootPane.NONE )
            {
                installWindowDecorations ();
            }
        }
    }

    /**
     * Decoration install and uninstall methods
     */

    protected void installWindowDecorations ()
    {
        if ( root.getWindowDecorationStyle () != JRootPane.NONE )
        {
            window = root != null ? SwingUtils.getWindowAncestor ( root ) : null;
            frame = window instanceof Frame ? ( Frame ) window : null;
            dialog = window instanceof Dialog ? ( Dialog ) window : null;
            installProperties ();
            installListeners ();
            installTransparency ();
            installBorder ();
            installLayout ();
            installDecorationComponents ();
            styled = true;
        }
    }

    protected void uninstallWindowDecorations ()
    {
        if ( styled )
        {
            uninstallProperties ();
            uninstallListeners ();
            uninstallTransparency ();
            uninstallBorder ();
            uninstallLayout ();
            uninstallDecorationComponents ();
            window = null;
            frame = null;
            dialog = null;
            styled = false;
        }
    }

    /**
     * Specific properties
     */

    protected void installProperties ()
    {
        //
    }

    protected void uninstallProperties ()
    {
        if ( isFrame () )
        {
            // Maximum frame size
            frame.setMaximizedBounds ( null );
        }
    }

    /**
     * Listeners
     */

    protected void installListeners ()
    {
        windowFocusListener = new WindowFocusListener ()
        {
            @Override
            public void windowGainedFocus ( final WindowEvent e )
            {
                root.repaint ();
            }

            @Override
            public void windowLostFocus ( final WindowEvent e )
            {
                root.repaint ();
            }
        };
        window.addWindowFocusListener ( windowFocusListener );

        // Listen to window icon and title changes
        titleChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                titleComponent.revalidate ();
                titleComponent.repaint ();
            }
        };
        window.addPropertyChangeListener ( WebLookAndFeel.WINDOW_ICON_PROPERTY, titleChangeListener );
        window.addPropertyChangeListener ( WebLookAndFeel.WINDOW_TITLE_PROPERTY, titleChangeListener );

        // Listen to window resizability changes
        resizableChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateButtons ();
            }
        };
        window.addPropertyChangeListener ( WebLookAndFeel.WINDOW_RESIZABLE_PROPERTY, resizableChangeListener );

        if ( isFrame () )
        {
            state = frame.getState ();
            windowStateListener = new WindowStateListener ()
            {
                @Override
                public void windowStateChanged ( final WindowEvent e )
                {
                    state = e.getNewState ();
                    installBorder ();
                }
            };
            window.addWindowStateListener ( windowStateListener );
        }
    }

    protected void uninstallListeners ()
    {
        window.removeWindowFocusListener ( windowFocusListener );
        window.removePropertyChangeListener ( WebLookAndFeel.WINDOW_ICON_PROPERTY, titleChangeListener );
        window.removePropertyChangeListener ( WebLookAndFeel.WINDOW_TITLE_PROPERTY, titleChangeListener );
        window.removePropertyChangeListener ( WebLookAndFeel.WINDOW_RESIZABLE_PROPERTY, resizableChangeListener );
        if ( isFrame () )
        {
            window.removeWindowStateListener ( windowStateListener );
        }
    }

    /**
     * Window transparency
     */

    protected void installTransparency ()
    {
        if ( ProprietaryUtils.isWindowTransparencyAllowed () )
        {
            root.setOpaque ( false );
            ProprietaryUtils.setWindowOpaque ( window, false );
        }
    }

    protected void uninstallTransparency ()
    {
        if ( ProprietaryUtils.isWindowTransparencyAllowed () )
        {
            root.setOpaque ( true );
            ProprietaryUtils.setWindowOpaque ( window, true );
        }
    }

    /**
     * Decoration border install and uninstall methods
     */

    protected void installBorder ()
    {
        if ( !isFrameMaximized () )
        {
            root.setBorder ( BorderFactory
                    .createEmptyBorder ( shadeWidth + 1 + margin.top, shadeWidth + 1 + margin.left, shadeWidth + 1 + margin.bottom,
                            shadeWidth + 1 + margin.right ) );
        }
        else
        {
            root.setBorder ( BorderFactory.createEmptyBorder ( 1 + margin.top, 1 + margin.left, 1 + margin.bottom, 1 + margin.right ) );
        }
    }

    protected void uninstallBorder ()
    {
        root.setBorder ( null );
    }

    /**
     * Appropriate LayoutManager for the window decorations install and uninstall methods
     */

    protected void installLayout ()
    {
        if ( layoutManager == null )
        {
            layoutManager = new WebRootPaneLayout ();
        }
        savedOldLayout = root.getLayout ();
        root.setLayout ( layoutManager );
    }

    protected void uninstallLayout ()
    {
        if ( savedOldLayout != null )
        {
            root.setLayout ( savedOldLayout );
            savedOldLayout = null;
        }
    }

    /**
     * Resize corner
     */

    protected void installDecorationComponents ()
    {
        // Title
        titleComponent = createDefaultTitleComponent ();
        root.add ( titleComponent );

        // Buttons
        updateButtons ();

        // Resize corner
        resizeCorner = new WebResizeCorner ();
        root.add ( resizeCorner );
    }

    protected JComponent createDefaultTitleComponent ()
    {
        final WebLabel titleIcon = new WebLabel ()
        {
            @Override
            public Icon getIcon ()
            {
                return getWindowIcon ();
            }
        };

        final TitleLabel titleLabel = new TitleLabel ();
        titleLabel.setDrawShade ( true );
        titleLabel.setHorizontalAlignment ( WebLabel.CENTER );
        titleLabel.addComponentListener ( new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                titleLabel.setHorizontalAlignment ( titleLabel.getRequiredSize ().width > titleLabel.getWidth () ? LEADING : CENTER );
            }
        } );
        SwingUtils.setFontSize ( titleLabel, 13 );

        final WebPanel titlePanel = new WebPanel ( new BorderLayout ( 5, 0 ) );
        titlePanel.setOpaque ( false );
        titlePanel.setMargin ( 4, 5, 4, 10 );
        titlePanel.add ( titleIcon, BorderLayout.LINE_START );
        titlePanel.add ( titleLabel, BorderLayout.CENTER );

        // Window move and max/restore listener
        final ComponentMoveAdapter cma = new ComponentMoveAdapter ()
        {
            @Override
            public void mouseClicked ( final MouseEvent e )
            {
                if ( isFrame () && isShowMaximizeButton () && SwingUtils.isLeftMouseButton ( e ) && e.getClickCount () == 2 )
                {
                    if ( isFrameMaximized () )
                    {
                        restore ();
                    }
                    else
                    {
                        maximize ();
                    }
                }
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                if ( dragging && isFrameMaximized () )
                {
                    initialPoint = new Point ( initialPoint.x + shadeWidth, initialPoint.y + shadeWidth );
                    restore ();
                }
                super.mouseDragged ( e );
            }
        };
        titlePanel.addMouseListener ( cma );
        titlePanel.addMouseMotionListener ( cma );

        return titlePanel;
    }

    /**
     *
     */
    public class TitleLabel extends WebLabel
    {
        /**
         * Returns window title text.
         * There is a small workaround to show window title even when it is empty.
         * That workaround allows window dragging even when title is not set.
         *
         * @return window title text
         */
        @Override
        public String getText ()
        {
            final String title = getWindowTitle ();
            return title != null && !title.equals ( "" ) ? title : emptyTitleText;
        }

        /**
         * Returns preferred title size.
         * There is also a predefined title width limit to force it shrink.
         *
         * @return preferred title size
         */
        @Override
        public Dimension getPreferredSize ()
        {
            final Dimension ps = super.getPreferredSize ();
            ps.width = Math.min ( ps.width, maxTitleWidth );
            return ps;
        }

        /**
         * Returns actual preferred size of the title label.
         *
         * @return actual preferred size of the title label
         */
        public Dimension getRequiredSize ()
        {
            return super.getPreferredSize ();
        }
    }

    protected void updateButtons ()
    {
        // Removing old buttons
        if ( windowButtons != null )
        {
            root.remove ( windowButtons );
        }

        // Creating new buttons
        final boolean isFrame = isFrame ();
        final JComponent[] buttons = new JComponent[ 3 ];
        if ( showMinimizeButton && isFrame )
        {
            final WebButton minimize = new WebButton ( minimizeIcon );
            minimize.setName ( "minimize" );
            minimize.setRolloverIcon ( minimizeActiveIcon );
            minimize.setFocusable ( false );
            minimize.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    iconify ();
                }
            } );
            buttons[ 0 ] = minimize;
        }
        if ( showMaximizeButton && isResizable () && isFrame )
        {
            final WebButton maximize = new WebButton ( maximizeIcon )
            {
                @Override
                public Icon getIcon ()
                {
                    return isFrameMaximized () ? restoreIcon : maximizeIcon;
                }

                @Override
                public Icon getRolloverIcon ()
                {
                    return isFrameMaximized () ? restoreActiveIcon : maximizeActiveIcon;
                }
            };
            maximize.setName ( "maximize" );
            maximize.setRolloverIcon ( maximizeActiveIcon );
            maximize.setFocusable ( false );
            maximize.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    if ( isFrame () )
                    {
                        if ( isFrameMaximized () )
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
            buttons[ 1 ] = maximize;
        }
        if ( showCloseButton )
        {
            final WebButton close = new WebButton ( closeIcon );
            close.setName ( "close" );
            close.setRolloverIcon ( closeActiveIcon );
            close.setFocusable ( false );
            close.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    close ();
                }
            } );
            buttons[ 2 ] = close;
        }

        windowButtons = new WebButtonGroup ( buttons )
        {
            @Override
            public void updateButtonsStyling ()
            {
                if ( groupButtons )
                {
                    super.updateButtonsStyling ();
                }
            }
        };
        updateWindowButtonsStyle ();

        root.add ( windowButtons );
    }

    protected void updateWindowButtonsStyle ()
    {
        if ( windowButtons != null )
        {
            windowButtons.setButtonsDrawFocus ( false );
            windowButtons.setButtonsShadeWidth ( WebRootPaneStyle.buttonsShadeWidth );
            windowButtons.setButtonsRound ( round );
            windowButtons.setButtonsMargin ( WebRootPaneStyle.buttonsMargin );
            if ( attachButtons )
            {
                windowButtons.setButtonsDrawTop ( false );
                windowButtons.setButtonsDrawRight ( round > 0 );
            }
        }
    }

    protected void uninstallDecorationComponents ()
    {
        // Title
        if ( titleComponent != null )
        {
            root.remove ( titleComponent );
            titleComponent = null;
        }

        // Buttons
        if ( windowButtons != null )
        {
            root.remove ( windowButtons );
            windowButtons = null;
        }

        // Resize corner
        if ( resizeCorner != null )
        {
            root.remove ( resizeCorner );
            resizeCorner = null;
        }
    }

    /**
     * Returns window title
     */

    protected String getWindowTitle ()
    {
        if ( isDialog () )
        {
            return dialog.getTitle ();
        }
        else if ( isFrame () )
        {
            return frame.getTitle ();
        }
        else
        {
            return null;
        }
    }

    /**
     * Finds a frame image with most suitable size
     */

    protected ImageIcon getWindowIcon ()
    {
        final List<Image> images = window != null ? window.getIconImages () : null;
        if ( images != null && images.size () > 1 )
        {
            int bestIndex = 0;
            int bestDiff = Math.abs ( images.get ( bestIndex ).getWidth ( null ) - 16 );
            for ( int i = 1; i < images.size (); i++ )
            {
                if ( bestDiff == 0 )
                {
                    break;
                }
                final int diff = Math.abs ( images.get ( i ).getWidth ( null ) - 16 );
                if ( diff < bestDiff )
                {
                    bestIndex = i;
                    bestDiff = diff;
                }
            }
            return generateProperIcon ( images.get ( bestIndex ) );
        }
        else if ( images != null && images.size () == 1 )
        {
            return generateProperIcon ( images.get ( 0 ) );
        }
        else
        {
            return new ImageIcon ();
        }
    }

    protected ImageIcon generateProperIcon ( final Image image )
    {
        if ( image.getWidth ( null ) <= 16 )
        {
            return new ImageIcon ( image );
        }
        else
        {
            return ImageUtils.createPreviewIcon ( image, 16 );
        }
    }


    /**
     * Closes the Window.
     */

    protected void close ()
    {
        if ( window != null )
        {
            window.dispatchEvent ( new WindowEvent ( window, WindowEvent.WINDOW_CLOSING ) );
        }
    }

    /**
     * Iconifies the Frame.
     */

    protected void iconify ()
    {
        if ( frame != null )
        {
            frame.setExtendedState ( Frame.ICONIFIED );
        }
    }

    /**
     * Maximizes the Frame.
     */

    protected void maximize ()
    {
        if ( frame != null )
        {
            // Retrieving screen device configuration
            final GraphicsConfiguration gc = frame.getGraphicsConfiguration ().getDevice ().getDefaultConfiguration ();

            // Updating maximized bounds for the frame
            frame.setMaximizedBounds ( SystemUtils.getMaxWindowBounds ( gc, true ) );

            // Forcing window to go into maximized state
            frame.setExtendedState ( Frame.MAXIMIZED_BOTH );
        }
    }

    /**
     * Restores the Frame size.
     */

    protected void restore ()
    {
        if ( frame != null )
        {
            frame.setExtendedState ( Frame.NORMAL );
        }
    }

    /**
     * Checks if root pane's window is resizable
     */

    protected boolean isResizable ()
    {
        return isDialog () ? dialog.isResizable () : isFrame () && frame.isResizable ();
    }

    /**
     * Checks if root pane is inside a frame
     */

    protected boolean isFrame ()
    {
        return frame != null;
    }

    /**
     * Checks if frame is maximized
     */

    protected boolean isFrameMaximized ()
    {
        return isFrame () && state == Frame.MAXIMIZED_BOTH;
    }

    /**
     * Checks if root pane is inside a dialog
     */

    protected boolean isDialog ()
    {
        return dialog != null;
    }

    /**
     * Custom window decoration
     */

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( styled )
        {
            final Graphics2D g2d = ( Graphics2D ) g;
            final Object aa = GraphicsUtils.setupAntialias ( g2d );
            final boolean max = isFrameMaximized ();

            if ( max )
            {
                // Background
                g2d.setPaint ( new GradientPaint ( 0, 0, topBg, 0, 30, middleBg ) );
                g2d.fillRect ( 0, 0, c.getWidth (), c.getHeight () );

                // Border
                g2d.setPaint ( borderColor );
                g2d.drawRect ( 0, 0, c.getWidth () - 1, c.getHeight () - 1 );
                g2d.setPaint ( innerBorderColor );
                g2d.drawRect ( 1, 1, c.getWidth () - 3, c.getHeight () - 3 );

                // Watermark
                if ( drawWatermark )
                {
                    final Shape old = GraphicsUtils.intersectClip ( g2d, getWatermarkClip ( c ) );
                    g2d.drawImage ( getWatermark ().getImage (), 2, 2, null );
                    GraphicsUtils.restoreClip ( g2d, old );
                }
            }
            else
            {
                // Shade
                if ( shadeWidth > 0 )
                {
                    final int diff = isActive ( c ) ? 0 : shadeWidth - inactiveShadeWidth;
                    getShadeIcon ( c ).paintIcon ( g2d, diff, diff, c.getWidth () - diff * 2, c.getHeight () - diff * 2 );
                }

                // Background
                g2d.setPaint ( new GradientPaint ( 0, shadeWidth, topBg, 0, shadeWidth + 30, middleBg ) );
                g2d.fillRoundRect ( shadeWidth, shadeWidth, c.getWidth () - shadeWidth * 2, c.getHeight () - shadeWidth * 2, round * 2,
                        round * 2 );

                // Border
                g2d.setPaint ( borderColor );
                g2d.drawRoundRect ( shadeWidth, shadeWidth, c.getWidth () - shadeWidth * 2 - 1, c.getHeight () - shadeWidth * 2 - 1,
                        round * 2 - 2, round * 2 - 2 );
                g2d.setPaint ( innerBorderColor );
                g2d.drawRoundRect ( shadeWidth + 1, shadeWidth + 1, c.getWidth () - shadeWidth * 2 - 3, c.getHeight () - shadeWidth * 2 - 3,
                        round * 2 - 4, round * 2 - 4 );

                // Watermark
                if ( drawWatermark )
                {
                    final Shape old = GraphicsUtils.intersectClip ( g2d, getWatermarkClip ( c ) );
                    g2d.drawImage ( getWatermark ().getImage (), shadeWidth + 2, shadeWidth + 2, null );
                    GraphicsUtils.restoreClip ( g2d, old );
                }
            }

            GraphicsUtils.restoreAntialias ( g2d, aa );
        }
    }

    protected RoundRectangle2D.Double getWatermarkClip ( final JComponent c )
    {
        return new RoundRectangle2D.Double ( shadeWidth + 2, shadeWidth + 2, c.getWidth () - shadeWidth * 2 - 3,
                c.getHeight () - shadeWidth * 2 - 3, round * 2 - 4, round * 2 - 4 );
    }

    protected NinePatchIcon getShadeIcon ( final JComponent c )
    {
        if ( shadeWidth > 0 )
        {
            return NinePatchUtils.getShadeIcon ( getShadeWidth ( c ), round, 0.8f );
        }
        else
        {
            return null;
        }
    }

    protected int getShadeWidth ( final JComponent c )
    {
        return isActive ( c ) ? shadeWidth : inactiveShadeWidth;
    }

    protected boolean isActive ( final JComponent c )
    {
        return SwingUtils.getWindowAncestor ( c ).isFocused ();
    }
}