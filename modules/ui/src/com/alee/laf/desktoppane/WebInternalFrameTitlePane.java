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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.extended.image.WebImage;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.grouping.GroupPane;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebRootPaneUI;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.Serializable;

/**
 * {@link JInternalFrame} and {@link javax.swing.JInternalFrame.JDesktopIcon} title pane.
 *
 * @author Mikle Garin
 */
public class WebInternalFrameTitlePane extends WebPanel implements SwingConstants
{
    /**
     * todo 1. Add action implementations from {@link javax.swing.plaf.basic.BasicInternalFrameTitlePane}
     * todo 2. Add system menu from {@link javax.swing.plaf.basic.BasicInternalFrameTitlePane}
     */

    /**
     * Title pane parent.
     * It is either {@link JInternalFrame} or {@link javax.swing.JInternalFrame.JDesktopIcon}.
     */
    @NotNull
    protected final JComponent parent;

    /**
     * {@link JInternalFrame} for this title pane.
     */
    @NotNull
    protected final JInternalFrame frame;

    /**
     * Title pane UI elements.
     */
    @NotNull
    protected WebImage titleIcon;
    @NotNull
    protected WebLabel titleLabel;
    @NotNull
    protected GroupPane buttonsPanel;
    @NotNull
    protected JButton minimizeButton;
    @NotNull
    protected JButton maximizeButton;
    @NotNull
    protected JButton closeButton;

    /**
     * Events handler.
     */
    @Nullable
    protected Handler handler;

    /**
     * Constructs new internal frame title pane.
     *
     * @param parent either {@link JInternalFrame} or {@link javax.swing.JInternalFrame.JDesktopIcon}
     * @param frame  {@link JInternalFrame} for this title pane
     */
    public WebInternalFrameTitlePane ( @NotNull final JComponent parent, @NotNull final JInternalFrame frame )
    {
        super ( StyleId.internalframeTitlePanel.at ( parent ), new BorderLayout () );
        this.parent = parent;
        this.frame = frame;

        titleIcon = new WebImage ( StyleId.internalframeTitleIcon.at ( this ), frame.getFrameIcon () );
        add ( titleIcon, BorderLayout.LINE_START );

        titleLabel = new WebLabel ( StyleId.internalframeTitleLabel.at ( this ), frame.getTitle () );
        titleLabel.setFont ( WebLookAndFeel.globalWindowFont );
        titleLabel.setFontSize ( 13 );
        titleLabel.addComponentListener ( new ComponentAdapter ()
        {
            /**
             * Saving initial alignment to avoid overwriting provided by the style.
             */
            private final int initialAlignment = titleLabel.getHorizontalAlignment ();

            @Override
            public void componentResized ( @NotNull final ComponentEvent e )
            {
                // Changing title horizontal alignment to avoid title jumping left/right
                final boolean trimmed = titleLabel.getOriginalPreferredSize ().width > titleLabel.getWidth ();
                final boolean ltr = titleLabel.getComponentOrientation ().isLeftToRight ();
                final int alignment = trimmed ? ltr ? LEADING : TRAILING : initialAlignment;
                titleLabel.setHorizontalAlignment ( alignment );
            }
        } );
        add ( titleLabel, BorderLayout.CENTER );

        buttonsPanel = new GroupPane ( StyleId.internalframeButtonsPanel.at ( this ) );
        buttonsPanel.setPaintSides ( false, true, true, true );

        minimizeButton = new WebButton ( StyleId.internalframeMinimizeButton.at ( buttonsPanel ) );
        minimizeButton.setName ( "minimize" );
        minimizeButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( @NotNull final ActionEvent e )
            {
                iconify ();
            }
        } );

        maximizeButton = new WebButton ( StyleId.internalframeMaximizeButton.at ( buttonsPanel ) );
        maximizeButton.setName ( "maximize" );
        maximizeButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( @NotNull final ActionEvent e )
            {
                maximize ();
            }
        } );

        closeButton = new WebButton ( StyleId.internalframeCloseButton.at ( buttonsPanel ) );
        closeButton.setName ( "close" );
        closeButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( @NotNull final ActionEvent e )
            {
                close ();
            }
        } );

        updateButtonIcons ();
        placeButtons ();

        add ( buttonsPanel, BorderLayout.LINE_END );
    }

    /**
     * Updates button icons.
     * todo Replace icons with appropriate references in style
     */
    protected void updateButtonIcons ()
    {
        final boolean icon = frame.isIcon ();
        minimizeButton.setIcon ( icon ? WebRootPaneUI.restoreIcon : WebRootPaneUI.minimizeIcon );
        minimizeButton.setRolloverIcon ( icon ? WebRootPaneUI.restoreActiveIcon : WebRootPaneUI.minimizeActiveIcon );

        final boolean maximum = frame.isMaximum ();
        maximizeButton.setIcon ( maximum && !icon ? WebRootPaneUI.restoreIcon : WebRootPaneUI.maximizeIcon );
        maximizeButton.setRolloverIcon ( maximum && !icon ? WebRootPaneUI.restoreActiveIcon : WebRootPaneUI.maximizeActiveIcon );

        closeButton.setIcon ( WebRootPaneUI.closeIcon );
        closeButton.setRolloverIcon ( WebRootPaneUI.closeActiveIcon );
    }

    /**
     * Places buttons on the title pane.
     */
    protected void placeButtons ()
    {
        buttonsPanel.removeAll ();
        if ( frame.isIconifiable () )
        {
            buttonsPanel.add ( minimizeButton );
        }
        if ( frame.isMaximizable () )
        {
            buttonsPanel.add ( maximizeButton );
        }
        if ( frame.isClosable () )
        {
            buttonsPanel.add ( closeButton );
        }
        revalidate ();
        repaint ();
    }

    /**
     * Removes specified button from the title pane.
     *
     * @param button button to remove
     */
    protected void removeButton ( @NotNull final AbstractButton button )
    {
        buttonsPanel.remove ( button );
        revalidate ();
        repaint ();
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
    protected class Handler implements PropertyChangeListener, Serializable
    {
        @Override
        public void propertyChange ( @NotNull final PropertyChangeEvent evt )
        {
            final String prop = evt.getPropertyName ();
            if ( Objects.equals ( prop, JInternalFrame.FRAME_ICON_PROPERTY ) )
            {
                titleIcon.setImage ( frame.getFrameIcon () );
            }
            else if ( Objects.equals ( prop, JInternalFrame.TITLE_PROPERTY ) )
            {
                titleLabel.setText ( frame.getTitle () );
            }
            else if ( Objects.equals ( prop, JInternalFrame.IS_SELECTED_PROPERTY ) )
            {
                repaint ();
            }
            else if ( Objects.equals ( prop, JInternalFrame.IS_ICON_PROPERTY, JInternalFrame.IS_MAXIMUM_PROPERTY ) )
            {
                updateButtonIcons ();
            }
            else if ( Objects.equals ( prop, WebInternalFrame.ICONABLE_PROPERTY ) )
            {
                toggleButton ( evt, minimizeButton );
            }
            else if ( Objects.equals ( prop, WebInternalFrame.MAXIMIZABLE_PROPERTY ) )
            {
                toggleButton ( evt, maximizeButton );
            }
            else if ( Objects.equals ( prop, WebInternalFrame.CLOSABLE_PROPERTY ) )
            {
                toggleButton ( evt, closeButton );
            }
        }

        /**
         * Toggles availability of specific button.
         *
         * @param evt    property change event
         * @param button button to toggle availability for
         */
        protected void toggleButton ( @NotNull final PropertyChangeEvent evt, @NotNull final JButton button )
        {
            if ( evt.getOldValue () != evt.getNewValue () )
            {
                if ( ( Boolean ) evt.getNewValue () )
                {
                    placeButtons ();
                }
                else
                {
                    removeButton ( button );
                }
            }
        }
    }

    /**
     * Iconifies internal frame.
     */
    protected void iconify ()
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

    /**
     * Maximizes or restores internal frame size.
     */
    protected void maximize ()
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

    /**
     * Closes internal frame.
     */
    protected void close ()
    {
        if ( frame.isClosable () )
        {
            frame.doDefaultCloseAction ();
        }
    }
}