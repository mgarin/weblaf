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

package com.alee.examples.groups.desktoppane;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.examples.content.FeatureState;
import com.alee.examples.groups.desktoppane.tetris.Tetris;
import com.alee.examples.groups.desktoppane.tetris.TetrisListener;
import com.alee.laf.button.WebButton;
import com.alee.laf.desktoppane.WebDesktopPane;
import com.alee.laf.desktoppane.WebInternalFrame;
import com.alee.laf.menu.WebMenuBar;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * User: mgarin Date: 16.02.12 Time: 16:14
 */

public class DesktopPaneExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Desktop pane";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled desktop pane";
    }

    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Desktop pane
        final WebDesktopPane desktopPane = new WebDesktopPane ();
        desktopPane.setOpaque ( false );

        // Simple frame
        createSimpleFrame ( desktopPane );

        // Tetris frame
        createTetrisFrame ( desktopPane );

        return desktopPane;
    }

    private void createSimpleFrame ( final WebDesktopPane desktopPane )
    {
        final WebInternalFrame internalFrame = new WebInternalFrame ( "Web frame", true, true, true, true );
        internalFrame.setFrameIcon ( loadIcon ( "frame.png" ) );

        final JLabel label = new JLabel ( "Just an empty frame", JLabel.CENTER );
        label.setOpaque ( false );
        internalFrame.add ( label );

        final WebButton internalFrameIcon = new WebButton ( "Web frame", loadIcon ( "webframe.png" ) );
        internalFrameIcon.setRolloverDecoratedOnly ( true );
        internalFrameIcon.setHorizontalTextPosition ( WebButton.CENTER );
        internalFrameIcon.setVerticalTextPosition ( WebButton.BOTTOM );
        internalFrameIcon.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( internalFrameIcon.getClientProperty ( DesktopPaneIconMoveAdapter.DRAGGED_MARK ) != null )
                {
                    return;
                }
                if ( internalFrame.isClosed () )
                {
                    if ( internalFrame.getParent () == null )
                    {
                        desktopPane.add ( internalFrame );
                    }
                    internalFrame.open ();
                    internalFrame.setIcon ( false );
                }
                else
                {
                    internalFrame.setIcon ( !internalFrame.isIcon () );
                }
            }
        } );
        final DesktopPaneIconMoveAdapter ma1 = new DesktopPaneIconMoveAdapter ();
        internalFrameIcon.addMouseListener ( ma1 );
        internalFrameIcon.addMouseMotionListener ( ma1 );
        internalFrameIcon.setBounds ( 25, 125, 100, 75 );
        desktopPane.add ( internalFrameIcon );

        internalFrame.setBounds ( 25 + 100 + 50, 50, 300, 300 );
        internalFrame.close ();
    }

    private void createTetrisFrame ( final WebDesktopPane desktopPane )
    {
        final Tetris tetris = new Tetris ();
        tetris.setUseInternalHotkeys ( false );

        final WebInternalFrame tetrisFrame = new WebInternalFrame ( "Tetris frame", false, true, false, true )
        {
            @Override
            public void setVisible ( final boolean aFlag )
            {
                if ( !aFlag )
                {
                    tetris.pauseGame ();
                }
                super.setVisible ( aFlag );
            }
        };
        tetrisFrame.setFrameIcon ( loadIcon ( "game.png" ) );
        tetrisFrame.add ( tetris );

        final WebMenuBar tetrisMenu = new WebMenuBar ();
        tetrisMenu.add ( new JMenu ( "Game" )
        {
            {
                add ( new JMenuItem ( "New game", loadIcon ( "tetris/new.png" ) )
                {
                    {
                        setAccelerator ( KeyStroke.getKeyStroke ( KeyEvent.VK_F2, 0 ) );
                        addActionListener ( new ActionListener ()
                        {
                            @Override
                            public void actionPerformed ( final ActionEvent e )
                            {
                                tetris.newGame ();
                            }
                        } );
                    }
                } );
                add ( new JMenuItem ( "Unpause game", loadIcon ( "tetris/unpause.png" ) )
                {
                    {
                        tetris.addTetrisListener ( new TetrisListener ()
                        {
                            @Override
                            public void newGameStarted ()
                            {
                                setEnabled ( true );
                                setIcon ( loadIcon ( "tetris/pause.png" ) );
                                setText ( "Pause game" );
                            }

                            @Override
                            public void gamePaused ()
                            {
                                setIcon ( loadIcon ( "tetris/unpause.png" ) );
                                setText ( "Unpause game" );
                            }

                            @Override
                            public void gameUnpaused ()
                            {
                                setIcon ( loadIcon ( "tetris/pause.png" ) );
                                setText ( "Pause game" );
                            }

                            @Override
                            public void gameOver ()
                            {
                                setEnabled ( false );
                                setIcon ( loadIcon ( "tetris/pause.png" ) );
                                setText ( "Pause game" );
                            }
                        } );
                        setAccelerator ( KeyStroke.getKeyStroke ( KeyEvent.VK_P, 0 ) );
                        addActionListener ( new ActionListener ()
                        {
                            @Override
                            public void actionPerformed ( final ActionEvent e )
                            {
                                if ( tetris.isPaused () )
                                {
                                    tetris.unpauseGame ();
                                }
                                else
                                {
                                    tetris.pauseGame ();
                                }
                            }
                        } );
                    }
                } );
                addSeparator ();
                add ( new JMenuItem ( "Close", loadIcon ( "tetris/exit.png" ) )
                {
                    {
                        setAccelerator ( KeyStroke.getKeyStroke ( KeyEvent.VK_F4, KeyEvent.SHIFT_MASK ) );
                        addActionListener ( new ActionListener ()
                        {
                            @Override
                            public void actionPerformed ( final ActionEvent e )
                            {
                                tetris.pauseGame ();
                                tetrisFrame.close ();
                            }
                        } );
                    }
                } );
            }
        } );
        tetrisMenu.add ( new JMenu ( "About" ) );
        tetrisFrame.setJMenuBar ( tetrisMenu );

        final WebButton tetrisFrameIcon = new WebButton ( "Tetris", loadIcon ( "tetris.png" ) );
        tetrisFrameIcon.setRolloverDecoratedOnly ( true );
        tetrisFrameIcon.setHorizontalTextPosition ( WebButton.CENTER );
        tetrisFrameIcon.setVerticalTextPosition ( WebButton.BOTTOM );
        tetrisFrameIcon.addActionListener ( new ActionListener ()
        {
            private boolean loading = false;
            private boolean firstLoad = true;

            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( loading || tetrisFrameIcon.getClientProperty ( DesktopPaneIconMoveAdapter.DRAGGED_MARK ) != null )
                {
                    return;
                }

                tetrisFrameIcon.setIcon ( loadIcon ( "loader.gif" ) );
                loading = true;

                WebTimer.delay ( firstLoad ? 1000 : 100, new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        firstLoad = false;
                        if ( tetrisFrame.isClosed () )
                        {
                            if ( tetrisFrame.getParent () == null )
                            {
                                desktopPane.add ( tetrisFrame );
                            }
                            tetrisFrame.open ();
                            tetrisFrame.setIcon ( false );
                        }
                        else
                        {
                            tetrisFrame.setIcon ( !tetrisFrame.isIcon () );
                        }
                        tetrisFrameIcon.setIcon ( loadIcon ( "tetris.png" ) );
                        loading = false;
                    }
                } );
            }
        } );
        final DesktopPaneIconMoveAdapter ma2 = new DesktopPaneIconMoveAdapter ();
        tetrisFrameIcon.addMouseListener ( ma2 );
        tetrisFrameIcon.addMouseMotionListener ( ma2 );
        tetrisFrameIcon.setBounds ( 25, 25, 100, 75 );
        desktopPane.add ( tetrisFrameIcon );

        tetrisFrame.pack ();
        tetrisFrame.setLocation ( 25 + 100 + 25, 25 );
        tetrisFrame.close ();
    }
}