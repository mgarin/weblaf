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

package com.alee.laf.desktoppane;

import com.alee.laf.WebFonts;
import com.alee.laf.button.WebButton;
import com.alee.laf.grouping.GroupPane;
import com.alee.laf.label.WebLabel;
import com.alee.laf.rootpane.WebRootPaneUI;
import com.alee.managers.style.StyleId;
import com.alee.utils.CompareUtils;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;

/**
 * @author Mikle Garin
 */

public class WebInternalFrameTitlePane extends JComponent
{
    /**
     * Action names.
     */
    protected static final String CLOSE_CMD = UIManager.getString ( "InternalFrameTitlePane.closeButtonText" );
    protected static final String ICONIFY_CMD = UIManager.getString ( "InternalFrameTitlePane.minimizeButtonText" );
    protected static final String RESTORE_CMD = UIManager.getString ( "InternalFrameTitlePane.restoreButtonText" );
    protected static final String MAXIMIZE_CMD = UIManager.getString ( "InternalFrameTitlePane.maximizeButtonText" );

    /**
     * Title pane parent.
     * It is either {@link javax.swing.JInternalFrame} or {@link javax.swing.JInternalFrame.JDesktopIcon}.
     */
    protected final JComponent parent;

    /**
     * {@link javax.swing.JInternalFrame} for this title pane.
     */
    protected final JInternalFrame frame;

    /**
     * Title pane UI elements.
     */
    protected WebLabel titleLabel;
    protected GroupPane buttonsPanel;
    protected JButton iconButton;
    protected JButton maxButton;
    protected JButton closeButton;

    /**
     * Actions.
     */
    protected Action closeAction;
    protected Action maximizeAction;
    protected Action iconifyAction;
    protected Action restoreAction;

    /**
     * Events handler.
     */
    protected Handler handler;

    /**
     * Constructs new internal frame title pane.
     *
     * @param parent either {@link javax.swing.JInternalFrame} or {@link javax.swing.JInternalFrame.JDesktopIcon}
     * @param frame  {@link javax.swing.JInternalFrame} for this title pane
     */
    public WebInternalFrameTitlePane ( final JComponent parent, final JInternalFrame frame )
    {
        super ();
        this.parent = parent;
        this.frame = frame;
        initializeActions ();
        initializeUI ();
    }

    /**
     * Initializes button actions.
     */
    protected void initializeActions ()
    {
        maximizeAction = new MaximizeAction ();
        iconifyAction = new IconifyAction ();
        closeAction = new CloseAction ();
        restoreAction = new RestoreAction ();
    }

    /**
     * Initializes title pane UI.
     */
    protected void initializeUI ()
    {
        final boolean isIconPane = parent instanceof JInternalFrame.JDesktopIcon;

        setLayout ( new BorderLayout () );

        final Icon titleIcon = new Icon ()
        {
            @Override
            public void paintIcon ( final Component c, final Graphics g, final int x, final int y )
            {
                if ( frame.getFrameIcon () != null )
                {
                    frame.getFrameIcon ().paintIcon ( c, g, x, y );
                }
            }

            @Override
            public int getIconWidth ()
            {
                return frame.getFrameIcon () != null ? frame.getFrameIcon ().getIconWidth () : 16;
            }

            @Override
            public int getIconHeight ()
            {
                return frame.getFrameIcon () != null ? frame.getFrameIcon ().getIconHeight () : 16;
            }
        };
        titleLabel = new WebLabel ( StyleId.internalframeTitleLabel.at ( frame ), titleIcon, WebLabel.LEFT )
        {
            @Override
            public String getText ()
            {
                return frame.getTitle ();
            }
        };
        titleLabel.setFont ( WebFonts.getSystemTitleFont () );
        add ( titleLabel, BorderLayout.CENTER );

        buttonsPanel = new GroupPane ( StyleId.internalframeButtonsPanel.at ( parent ) );
        buttonsPanel.setPaintSides ( isIconPane, true, true, true );

        iconButton = new WebButton ( StyleId.internalframeMinimizeButton.at ( buttonsPanel ) );
        iconButton.setEnabled ( frame.isIconifiable () );
        iconButton.addActionListener ( iconifyAction );
        if ( frame.isIconifiable () )
        {
            buttonsPanel.add ( iconButton );
        }

        maxButton = new WebButton ( StyleId.internalframeMaximizeButton.at ( buttonsPanel ) );
        maxButton.setEnabled ( frame.isMaximizable () );
        maxButton.addActionListener ( maximizeAction );
        if ( frame.isMaximizable () )
        {
            buttonsPanel.add ( maxButton );
        }

        closeButton = new WebButton ( StyleId.internalframeCloseButton.at ( buttonsPanel ) );
        closeButton.setEnabled ( frame.isClosable () );
        closeButton.addActionListener ( closeAction );
        if ( frame.isClosable () )
        {
            buttonsPanel.add ( closeButton );
        }

        updateIcons ();

        add ( buttonsPanel, BorderLayout.EAST );
    }

    /**
     * Updates button icons.
     */
    protected void updateIcons ()
    {
        iconButton.setIcon ( frame.isIcon () ? WebRootPaneUI.restoreIcon : WebRootPaneUI.minimizeIcon );
        iconButton.setRolloverIcon ( frame.isIcon () ? WebRootPaneUI.restoreActiveIcon : WebRootPaneUI.minimizeActiveIcon );

        maxButton.setIcon ( frame.isIcon () ? WebRootPaneUI.maximizeIcon :
                frame.isMaximum () ? WebRootPaneUI.restoreIcon : WebRootPaneUI.maximizeIcon );
        maxButton.setRolloverIcon ( frame.isIcon () ? WebRootPaneUI.maximizeActiveIcon :
                frame.isMaximum () ? WebRootPaneUI.restoreActiveIcon : WebRootPaneUI.maximizeActiveIcon );

        closeButton.setIcon ( WebRootPaneUI.closeIcon );
        closeButton.setRolloverIcon ( WebRootPaneUI.closeActiveIcon );
    }

    /**
     * Updates action enabled state.
     */
    protected void updateActions ()
    {
        restoreAction.setEnabled ( frame.isMaximum () || frame.isIcon () );
        maximizeAction.setEnabled (
                ( frame.isMaximizable () && !frame.isMaximum () && !frame.isIcon () ) || ( frame.isMaximizable () && frame.isIcon () ) );
        iconifyAction.setEnabled ( frame.isIconifiable () && !frame.isIcon () );
        closeAction.setEnabled ( frame.isClosable () );
    }

    /**
     * Installs title pane.
     */
    protected void install ()
    {
        handler = new Handler ();
        frame.addPropertyChangeListener ( handler );
    }

    /**
     * Uninstalls title pane.
     */
    protected void uninstall ()
    {
        frame.removePropertyChangeListener ( handler );
        handler = null;
    }

    /**
     * Title pane events handler.
     */
    protected class Handler implements LayoutManager, PropertyChangeListener
    {
        @Override
        public void propertyChange ( final PropertyChangeEvent evt )
        {
            final String prop = evt.getPropertyName ();
            if ( CompareUtils.equals ( prop, JInternalFrame.FRAME_ICON_PROPERTY ) )
            {
                titleLabel.repaint ();
            }
            else if ( CompareUtils.equals ( prop, JInternalFrame.IS_SELECTED_PROPERTY ) )
            {
                repaint ();
            }
            else if ( CompareUtils.equals ( prop, JInternalFrame.IS_ICON_PROPERTY, JInternalFrame.IS_MAXIMUM_PROPERTY ) )
            {
                updateIcons ();
                updateActions ();
            }
            else if ( CompareUtils.equals ( prop, WebInternalFrame.CLOSABLE_PROPERTY, WebInternalFrame.MAXIMIZABLE_PROPERTY,
                    WebInternalFrame.ICONABLE_PROPERTY ) )
            {
                buttonsPanel.removeAll ();
                if ( frame.isIconifiable () )
                {
                    buttonsPanel.add ( iconButton );
                }
                if ( frame.isMaximizable () )
                {
                    buttonsPanel.add ( maxButton );
                }
                if ( frame.isClosable () )
                {
                    buttonsPanel.add ( closeButton );
                }
                updateActions ();
                revalidate ();
                repaint ();
            }
        }

        @Override
        public void addLayoutComponent ( final String name, final Component c )
        {
        }

        @Override
        public void removeLayoutComponent ( final Component c )
        {
        }

        @Override
        public Dimension preferredLayoutSize ( final Container c )
        {
            return minimumLayoutSize ( c );
        }

        @Override
        public Dimension minimumLayoutSize ( final Container c )
        {
            // Calculate width.
            int width = 22;

            if ( frame.isClosable () )
            {
                width += 19;
            }
            if ( frame.isMaximizable () )
            {
                width += 19;
            }
            if ( frame.isIconifiable () )
            {
                width += 19;
            }

            final FontMetrics fm = frame.getFontMetrics ( getFont () );
            final String frameTitle = frame.getTitle ();
            final int title_w = frameTitle != null ? SwingUtilities2.stringWidth ( frame, fm, frameTitle ) : 0;
            final int title_length = frameTitle != null ? frameTitle.length () : 0;

            // Leave room for three characters in the title.
            if ( title_length > 3 )
            {
                final int subtitle_w = SwingUtilities2.stringWidth ( frame, fm, frameTitle.substring ( 0, 3 ) + "..." );
                width += ( title_w < subtitle_w ) ? title_w : subtitle_w;
            }
            else
            {
                width += title_w;
            }

            // Calculate height.
            final Icon icon = frame.getFrameIcon ();
            int fontHeight = fm.getHeight ();
            fontHeight += 2;
            int iconHeight = 0;
            if ( icon != null )
            {
                // SystemMenuBar forces the icon to be 16x16 or less.
                iconHeight = Math.min ( icon.getIconHeight (), 16 );
            }
            iconHeight += 2;

            final int height = Math.max ( fontHeight, iconHeight );

            final Dimension dim = new Dimension ( width, height );

            // Take into account the border insets if any.
            if ( getBorder () != null )
            {
                final Insets insets = getBorder ().getBorderInsets ( c );
                dim.height += insets.top + insets.bottom;
                dim.width += insets.left + insets.right;
            }
            return dim;
        }

        @Override
        public void layoutContainer ( final Container c )
        {
            final boolean leftToRight = frame.getComponentOrientation ().isLeftToRight ();

            final int w = getWidth ();
            final int h = getHeight ();
            int x;

            final int buttonHeight = closeButton.getIcon ().getIconHeight ();

            x = ( leftToRight ) ? w - 16 - 2 : 2;

            if ( frame.isClosable () )
            {
                closeButton.setBounds ( x, ( h - buttonHeight ) / 2, 16, 14 );
                x += ( leftToRight ) ? -( 16 + 2 ) : 16 + 2;
            }

            if ( frame.isMaximizable () )
            {
                maxButton.setBounds ( x, ( h - buttonHeight ) / 2, 16, 14 );
                x += ( leftToRight ) ? -( 16 + 2 ) : 16 + 2;
            }

            if ( frame.isIconifiable () )
            {
                iconButton.setBounds ( x, ( h - buttonHeight ) / 2, 16, 14 );
            }
        }
    }

    /**
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <tt>WebInternalFrameTitlePane</tt>.
     */
    public class CloseAction extends AbstractAction
    {
        /**
         * Constructs new close action.
         */
        public CloseAction ()
        {
            super ( CLOSE_CMD );
        }

        @Override
        public void actionPerformed ( final ActionEvent e )
        {
            if ( frame.isClosable () )
            {
                frame.doDefaultCloseAction ();
            }
        }
    }

    /**
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <tt>WebInternalFrameTitlePane</tt>.
     */
    public class MaximizeAction extends AbstractAction
    {
        /**
         * Constructs new maximize action.
         */
        public MaximizeAction ()
        {
            super ( MAXIMIZE_CMD );
        }

        @Override
        public void actionPerformed ( final ActionEvent evt )
        {
            if ( frame.isMaximizable () )
            {
                if ( frame.isMaximum () && frame.isIcon () )
                {
                    try
                    {
                        frame.setIcon ( false );
                    }
                    catch ( final PropertyVetoException ignored )
                    {
                    }
                }
                else if ( !frame.isMaximum () )
                {
                    try
                    {
                        frame.setMaximum ( true );
                    }
                    catch ( final PropertyVetoException ignored )
                    {
                    }
                }
                else
                {
                    try
                    {
                        frame.setMaximum ( false );
                    }
                    catch ( final PropertyVetoException ignored )
                    {
                    }
                }
            }
        }
    }

    /**
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <tt>WebInternalFrameTitlePane</tt>.
     */
    public class IconifyAction extends AbstractAction
    {
        /**
         * Constructs new iconify action.
         */
        public IconifyAction ()
        {
            super ( ICONIFY_CMD );
        }

        @Override
        public void actionPerformed ( final ActionEvent e )
        {
            if ( frame.isIconifiable () )
            {
                if ( !frame.isIcon () )
                {
                    try
                    {
                        frame.setIcon ( true );
                    }
                    catch ( final PropertyVetoException ignored )
                    {
                    }
                }
                else
                {
                    try
                    {
                        frame.setIcon ( false );
                    }
                    catch ( final PropertyVetoException ignored )
                    {
                    }
                }
            }
        }
    }

    /**
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <tt>WebInternalFrameTitlePane</tt>.
     */
    public class RestoreAction extends AbstractAction
    {
        /**
         * Constructs new restore action.
         */
        public RestoreAction ()
        {
            super ( RESTORE_CMD );
        }

        @Override
        public void actionPerformed ( final ActionEvent evt )
        {
            if ( frame.isMaximizable () && frame.isMaximum () && frame.isIcon () )
            {
                try
                {
                    frame.setIcon ( false );
                }
                catch ( final PropertyVetoException ignored )
                {
                }
            }
            else if ( frame.isMaximizable () && frame.isMaximum () )
            {
                try
                {
                    frame.setMaximum ( false );
                }
                catch ( final PropertyVetoException ignored )
                {
                }
            }
            else if ( frame.isIconifiable () && frame.isIcon () )
            {
                try
                {
                    frame.setIcon ( false );
                }
                catch ( final PropertyVetoException ignored )
                {
                }
            }
        }
    }
}