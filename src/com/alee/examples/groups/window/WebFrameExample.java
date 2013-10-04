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

package com.alee.examples.groups.window;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.examples.content.FeatureState;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.painter.TitledBorderPainter;
import com.alee.extended.panel.BorderPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.WebButtonGroup;
import com.alee.extended.window.ComponentMoveAdapter;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.menu.MenuBarStyle;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.rootpane.WebRootPaneUI;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.slider.WebSlider;
import com.alee.laf.text.WebTextField;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * User: mgarin Date: 19.12.12 Time: 14:56
 */

public class WebFrameExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Frame";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled frame decoration";
    }

    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        WebButton showFrame = new WebButton ( "Show frame", loadIcon ( "frame.png" ) );
        showFrame.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                // Enabling frame decoration
                boolean decorateFrames = WebLookAndFeel.isDecorateFrames ();
                WebLookAndFeel.setDecorateFrames ( true );

                // Opening frame
                ExampleFrame exampleFrame = new ExampleFrame ();
                exampleFrame.pack ();
                exampleFrame.setLocationRelativeTo ( owner );
                exampleFrame.setVisible ( true );

                // Restoring frame decoration option
                WebLookAndFeel.setDecorateFrames ( decorateFrames );
            }
        } );
        return new GroupPanel ( showFrame );
    }

    private class ExampleFrame extends WebFrame
    {
        public ExampleFrame () throws HeadlessException
        {
            super ( "Example frame" );
            setIconImages ( WebLookAndFeel.getImages () );
            setDefaultCloseOperation ( WebFrame.DISPOSE_ON_CLOSE );

            ComponentMoveAdapter.install ( getRootPane (), ExampleFrame.this );

            // Sample menu bar
            final WebMenuBar menuBar = new WebMenuBar ();
            menuBar.setUndecorated ( true );
            menuBar.setBorder ( BorderFactory.createEmptyBorder ( 2, 2, 2, 2 ) );
            menuBar.add ( new WebMenu ( "Menu 1" )
            {
                {
                    add ( new WebMenuItem ( "Menu item 1" ) );
                    add ( new WebMenuItem ( "Menu item 2" ) );
                    addSeparator ();
                    add ( new WebMenuItem ( "Menu item 3" ) );
                }
            } );
            menuBar.add ( new WebMenu ( "Menu 2" )
            {
                {
                    add ( new WebMenuItem ( "Menu item 1" ) );
                    add ( new WebMenuItem ( "Menu item 2" ) );
                    add ( new WebMenuItem ( "Menu item 3" ) );
                }
            } );
            menuBar.add ( new WebMenu ( "Menu 3" )
            {
                {
                    add ( new WebMenuItem ( "Menu item 1" ) );
                    add ( new WebMenuItem ( "Menu item 2" ) );
                }
            } );
            menuBar.add ( new WebMenu ( "Menu 4" )
            {
                {
                    add ( new WebMenuItem ( "Menu item 1" ) );
                    addSeparator ();
                    add ( new WebMenuItem ( "Menu item 2" ) );
                }
            } );
            setJMenuBar ( menuBar );

            // Options panel
            add ( new BorderPanel ( new WebPanel ( new VerticalFlowLayout ( 10, 10 ) )
            {
                {
                    setMargin ( 15 );

                    final TitledBorderPainter titledBorderPainter = new TitledBorderPainter ( "Window settings" );
                    titledBorderPainter.setTitleOffset ( 10 );
                    titledBorderPainter.setRound ( Math.max ( 0, ExampleFrame.this.getRound () - 2 ) );
                    setPainter ( titledBorderPainter );

                    final WebCheckBox showTitle = new WebCheckBox ( "Show title" )
                    {
                        {
                            setSelected ( ExampleFrame.this.isShowTitleComponent () );
                            addItemListener ( new ItemListener ()
                            {
                                @Override
                                public void itemStateChanged ( ItemEvent e )
                                {
                                    ExampleFrame.this.setShowTitleComponent ( isSelected () );
                                    ExampleFrame.this.pack ();
                                }
                            } );
                        }
                    };
                    add ( new GroupPanel ( 10, showTitle, new WebTextField ( ExampleFrame.this.getTitle (), 1 )
                    {
                        {
                            putClientProperty ( GroupPanel.FILL_CELL, true );
                            setEnabled ( showTitle.isSelected () );
                            showTitle.addItemListener ( new ItemListener ()
                            {
                                @Override
                                public void itemStateChanged ( ItemEvent e )
                                {
                                    setEnabled ( showTitle.isSelected () );
                                }
                            } );
                            addCaretListener ( new CaretListener ()
                            {
                                @Override
                                public void caretUpdate ( CaretEvent e )
                                {
                                    if ( !ExampleFrame.this.getTitle ().equals ( getText () ) )
                                    {
                                        ExampleFrame.this.setTitle ( getText () );
                                        ExampleFrame.this.pack ();
                                    }
                                }
                            } );
                        }
                    } ) );

                    add ( new WebSeparator ( false, true ) );

                    final WebCheckBox showWindowButtons = new WebCheckBox ( "Show window buttons" )
                    {
                        {
                            setSelected ( ExampleFrame.this.isShowWindowButtons () );
                            addItemListener ( new ItemListener ()
                            {
                                @Override
                                public void itemStateChanged ( ItemEvent e )
                                {
                                    ExampleFrame.this.setShowWindowButtons ( isSelected () );
                                    ExampleFrame.this.pack ();
                                }
                            } );
                        }
                    };
                    add ( new GroupPanel ( 10, showWindowButtons, new WebButtonGroup ( new WebToggleButton ( WebRootPaneUI.minimizeIcon )
                    {
                        {
                            setSelected ( ExampleFrame.this.isShowMinimizeButton () );
                            addItemListener ( new ItemListener ()
                            {
                                @Override
                                public void itemStateChanged ( ItemEvent e )
                                {
                                    ExampleFrame.this.setShowMinimizeButton ( isSelected () );
                                }
                            } );
                        }
                    }, new WebToggleButton ( WebRootPaneUI.maximizeIcon )
                    {
                        {
                            setSelected ( ExampleFrame.this.isShowMaximizeButton () );
                            addItemListener ( new ItemListener ()
                            {
                                @Override
                                public void itemStateChanged ( ItemEvent e )
                                {
                                    ExampleFrame.this.setShowMaximizeButton ( isSelected () );
                                }
                            } );
                        }
                    }, new WebToggleButton ( WebRootPaneUI.closeIcon )
                    {
                        {
                            setSelected ( ExampleFrame.this.isShowCloseButton () );
                            addItemListener ( new ItemListener ()
                            {
                                @Override
                                public void itemStateChanged ( ItemEvent e )
                                {
                                    ExampleFrame.this.setShowCloseButton ( isSelected () );
                                }
                            } );
                        }
                    }
                    )
                    {
                        {
                            setButtonsMargin ( 2, 4, 2, 4 );
                            setButtonsDrawFocus ( false );
                            setEnabled ( showWindowButtons.isSelected () );
                            showWindowButtons.addItemListener ( new ItemListener ()
                            {
                                @Override
                                public void itemStateChanged ( ItemEvent e )
                                {
                                    setEnabled ( showWindowButtons.isSelected () );
                                }
                            } );
                        }
                    } ) );

                    add ( new WebCheckBox ( "Attach window buttons to sides" )
                    {
                        {
                            setSelected ( ExampleFrame.this.isAttachButtons () );
                            addItemListener ( new ItemListener ()
                            {
                                @Override
                                public void itemStateChanged ( ItemEvent e )
                                {
                                    ExampleFrame.this.setAttachButtons ( isSelected () );
                                    ExampleFrame.this.pack ();
                                }
                            } );
                        }
                    } );

                    add ( new WebCheckBox ( "Group window buttons" )
                    {
                        {
                            setSelected ( ExampleFrame.this.isGroupButtons () );
                            addItemListener ( new ItemListener ()
                            {
                                @Override
                                public void itemStateChanged ( ItemEvent e )
                                {
                                    ExampleFrame.this.setGroupButtons ( isSelected () );
                                    ExampleFrame.this.pack ();
                                }
                            } );
                        }
                    } );

                    add ( new WebSeparator ( false, true ) );

                    final WebCheckBox showMenuBar = new WebCheckBox ( "Show menu bar" )
                    {
                        {
                            setSelected ( ExampleFrame.this.isShowMenuBar () );
                            addItemListener ( new ItemListener ()
                            {
                                @Override
                                public void itemStateChanged ( ItemEvent e )
                                {
                                    ExampleFrame.this.setShowMenuBar ( isSelected () );
                                    ExampleFrame.this.pack ();
                                }
                            } );
                        }
                    };
                    add ( new GroupPanel ( 10, showMenuBar, new WebComboBox ( new String[]{ "undecorated", "attached", "standalone" } )
                    {
                        {
                            addActionListener ( new ActionListener ()
                            {
                                @Override
                                public void actionPerformed ( ActionEvent e )
                                {
                                    int i = getSelectedIndex ();
                                    if ( i == 0 )
                                    {
                                        menuBar.setUndecorated ( true );
                                    }
                                    else
                                    {
                                        menuBar.setUndecorated ( false );
                                        menuBar.setMenuBarStyle ( i == 1 ? MenuBarStyle.attached : MenuBarStyle.standalone );
                                    }
                                    ExampleFrame.this.pack ();
                                }
                            } );
                            setEnabled ( showMenuBar.isSelected () );
                            showMenuBar.addItemListener ( new ItemListener ()
                            {
                                @Override
                                public void itemStateChanged ( ItemEvent e )
                                {
                                    setEnabled ( showMenuBar.isSelected () );
                                }
                            } );
                        }
                    } ) );

                    add ( new WebSeparator ( false, true ) );

                    add ( new WebCheckBox ( "Show resize corner" )
                    {
                        {
                            setSelected ( ExampleFrame.this.isShowResizeCorner () );
                            addItemListener ( new ItemListener ()
                            {
                                @Override
                                public void itemStateChanged ( ItemEvent e )
                                {
                                    ExampleFrame.this.setShowResizeCorner ( isSelected () );
                                    ExampleFrame.this.pack ();
                                }
                            } );
                        }
                    } );

                    add ( new WebSeparator ( false, true ) );

                    add ( new WebPanel ()
                    {
                        {
                            setOpaque ( false );
                            setLayout ( new BorderLayout ( 10, 0 ) );
                            add ( new WebLabel ( "Corners round" ), BorderLayout.LINE_START );
                            add ( new WebSlider ( 0, 10, ExampleFrame.this.getRound () )
                            {
                                {
                                    putClientProperty ( GroupPanel.FILL_CELL, true );
                                    addChangeListener ( new ChangeListener ()
                                    {
                                        @Override
                                        public void stateChanged ( ChangeEvent e )
                                        {
                                            menuBar.setRound ( Math.max ( 0, ExampleFrame.this.getRound () - 2 ) );
                                            titledBorderPainter.setRound ( Math.max ( 0, ExampleFrame.this.getRound () - 2 ) );
                                            ExampleFrame.this.setRound ( getValue () );
                                        }
                                    } );
                                }
                            }, BorderLayout.LINE_END );
                        }
                    } );
                    add ( new WebPanel ()
                    {
                        {
                            setOpaque ( false );
                            setLayout ( new BorderLayout ( 10, 0 ) );
                            add ( new WebLabel ( "Shade width" ), BorderLayout.LINE_START );
                            add ( new WebSlider ( 0, 50, ExampleFrame.this.getShadeWidth () )
                            {
                                {
                                    putClientProperty ( GroupPanel.FILL_CELL, true );
                                    addChangeListener ( new ChangeListener ()
                                    {
                                        @Override
                                        public void stateChanged ( ChangeEvent e )
                                        {
                                            ExampleFrame.this.setShadeWidth ( getValue () );
                                        }
                                    } );
                                }
                            }, BorderLayout.LINE_END );
                        }
                    } );
                }
            }, 10 ) );
        }
    }
}