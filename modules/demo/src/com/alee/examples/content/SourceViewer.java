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

package com.alee.examples.content;

import com.alee.examples.content.themes.EditorTheme;
import com.alee.extended.breadcrumb.WebBreadcrumb;
import com.alee.extended.breadcrumb.WebBreadcrumbButton;
import com.alee.extended.breadcrumb.WebBreadcrumbLabel;
import com.alee.extended.image.WebImage;
import com.alee.extended.layout.ToolbarLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.global.GlobalConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.combobox.WebComboBoxCellRenderer;
import com.alee.laf.combobox.WebComboBoxElement;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.list.WebListCellRenderer;
import com.alee.laf.list.WebListElement;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebWindow;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.scroll.WebScrollPaneUI;
import com.alee.laf.tabbedpane.TabbedPaneStyle;
import com.alee.laf.text.WebTextField;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.managers.log.Log;
import com.alee.managers.popup.PopupAdapter;
import com.alee.managers.popup.PopupWay;
import com.alee.managers.popup.WebButtonPopup;
import com.alee.managers.popup.WebPopup;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.utils.*;
import com.alee.utils.compare.Filter;
import com.alee.utils.reflection.JarEntry;
import com.alee.utils.reflection.JarEntryType;
import com.alee.utils.reflection.JarStructure;
import com.alee.utils.swing.WebTimer;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;

/**
 * User: mgarin Date: 05.03.12 Time: 12:38
 */

public class SourceViewer extends WebPanel
{
    public static final ImageIcon browseIcon = new ImageIcon ( SourceViewer.class.getResource ( "icons/browse.png" ) );
    public static final ImageIcon classSearchIcon = new ImageIcon ( SourceViewer.class.getResource ( "icons/classSearch.png" ) );

    private static final String SETTINGS_PREFIX = "SourceViewer.";

    private final JarStructure jarStructure;

    private final WebPanel toolBar;
    private final WebBreadcrumb classPath;
    private final ViewTabbedPane viewTabbedPane;
    private final ChangeListener viewChangeListener;

    private final Object activeEditorsLock = new Object ();
    private final Map<JarEntry, RSyntaxTextArea> activeEditors = new HashMap<JarEntry, RSyntaxTextArea> ();

    private WebPopup classSearchPopup;
    private WebTextField classSearchField;
    private WebTimer classSearchHintsDelay;
    private WebWindow classSearchHintsPopup;
    private WebList classSearchHintsList;
    private String lastSearchedText = null;
    private Component lastFocusBeforeSearch = null;

    private WebComboBox theme;
    private WebToggleButton allowCodeFolding;
    private WebToggleButton paintTabLines;
    private WebToggleButton showWhitespaces;
    private WebToggleButton showEol;

    public SourceViewer ( final JarStructure jarStructure )
    {
        super ();

        this.jarStructure = jarStructure;

        toolBar = new WebPanel ( true, new ToolbarLayout () );
        toolBar.setPaintSides ( false, false, true, false );
        toolBar.setShadeWidth ( 0 );
        add ( toolBar, BorderLayout.NORTH );

        classPath = new WebBreadcrumb ( false );
        classPath.setEncloseLastElement ( true );
        classPath.setElementMargin ( 4, 6, 4, 6 );
        classPath.setOpaque ( false );
        toolBar.add ( classPath, ToolbarLayout.FILL );

        toolBar.add ( createClassSearch (), ToolbarLayout.END );
        toolBar.add ( createSettings (), ToolbarLayout.END );


        viewTabbedPane = new ViewTabbedPane ();
        viewTabbedPane.setTabbedPaneStyle ( TabbedPaneStyle.attached );
        viewChangeListener = new ChangeListener ()
        {
            @Override
            public void stateChanged ( final ChangeEvent e )
            {
                updateClassPath ( viewTabbedPane.getSelectedEntry (), false );
            }
        };
        viewTabbedPane.addChangeListener ( viewChangeListener );
        viewTabbedPane.addViewListener ( new ViewListener ()
        {
            @Override
            public void viewOpened ( final JarEntry entry )
            {
                //
            }

            @Override
            public void viewClosed ( final JarEntry entry )
            {
                synchronized ( activeEditorsLock )
                {
                    // Removing opened editor
                    final RSyntaxTextArea removed = activeEditors.remove ( entry );

                    // If it was code editor - cleanup its resources
                    if ( removed != null )
                    {
                        ( ( CodeLinkGenerator ) removed.getLinkGenerator () ).destroy ();
                    }
                }
                updateClassPath ( viewTabbedPane.getSelectedEntry (), false );
            }
        } );
        HotkeyManager.registerHotkey ( viewTabbedPane, Hotkey.ALT_LEFT, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                final int tabCount = viewTabbedPane.getTabCount ();
                if ( tabCount > 0 )
                {
                    final int index = viewTabbedPane.getSelectedIndex ();
                    viewTabbedPane.setSelectedIndex ( index > 0 ? index - 1 : tabCount - 1 );
                }
            }
        } );
        HotkeyManager.registerHotkey ( viewTabbedPane, Hotkey.ALT_RIGHT, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                final int tabCount = viewTabbedPane.getTabCount ();
                if ( tabCount > 0 )
                {
                    final int index = viewTabbedPane.getSelectedIndex ();
                    viewTabbedPane.setSelectedIndex ( index < tabCount - 1 ? index + 1 : 0 );
                }
            }
        } );
        add ( viewTabbedPane, BorderLayout.CENTER );

        updateClassPath ( null, false );
    }

    private WebButton createClassSearch ()
    {
        final WebButton classSearch = new WebButton ( classSearchIcon );
        classSearch.setDrawFocus ( false );
        classSearch.setRolloverDecoratedOnly ( true );
        classSearch.addHotkey ( Hotkey.CTRL_N );
        classSearch.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                showClassSearchPopup ();
            }
        } );


        classSearchField = new WebTextField ( 20, false );
        classSearchField.setHideInputPromptOnFocus ( false );
        classSearchField.setInputPrompt ( "Enter class name here..." );
        HotkeyManager.registerHotkey ( classSearchField, Hotkey.ESCAPE, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                hideClassSearchPopup ();
            }
        } );

        final WebImage leadingComponent = new WebImage ( classSearchIcon );
        leadingComponent.setMargin ( 2 );
        classSearchField.setLeadingComponent ( leadingComponent );

        classSearchPopup = new WebPopup ();
        classSearchPopup.setCloseOnFocusLoss ( true );
        classSearchPopup.add ( classSearchField );
        classSearchPopup.setDefaultFocusComponent ( classSearchField );

        classSearchHintsPopup = new WebWindow ( classSearchPopup )
        {
            @Override
            public Dimension getPreferredSize ()
            {
                final Dimension ps = super.getPreferredSize ();
                ps.width = Math.max ( classSearchField.getWidth (), ps.width );
                return ps;
            }
        };
        classSearchHintsPopup.setFocusable ( false );
        classSearchHintsPopup.setAlwaysOnTop ( true );
        classSearchPopup.addFocusableChild ( classSearchHintsPopup );

        classSearchHintsList = new WebList ( new DefaultListModel () );
        classSearchHintsList.setFocusable ( false );
        classSearchHintsList.setRolloverSelectionEnabled ( true );
        classSearchHintsList.setSelectionMode ( ListSelectionModel.SINGLE_SELECTION );
        classSearchHintsList.setCellRenderer ( new WebListCellRenderer ()
        {
            @Override
            public Component getListCellRendererComponent ( final JList list, final Object value, final int index, final boolean isSelected,
                                                            final boolean cellHasFocus )
            {
                final JarEntry entry = ( JarEntry ) value;
                final WebListElement renderer =
                        ( WebListElement ) super.getListCellRendererComponent ( list, value, index, isSelected, cellHasFocus );
                renderer.setIcon ( entry.getIcon () );
                renderer.setText ( entry.getName () );
                return renderer;
            }
        } );
        classSearchHintsList.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( SwingUtils.isLeftMouseButton ( e ) )
                {
                    openSelectedHint ();
                }
            }
        } );

        HotkeyManager.registerHotkey ( classSearchField, Hotkey.HOME, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                if ( classSearchHintsList.getModelSize () > 0 )
                {
                    classSearchHintsList.setSelectedIndex ( 0 );
                }
            }
        } );
        HotkeyManager.registerHotkey ( classSearchField, Hotkey.UP, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                if ( classSearchHintsList.getModelSize () > 0 )
                {
                    final int index = classSearchHintsList.getSelectedIndex ();
                    if ( index > 0 )
                    {
                        classSearchHintsList.setSelectedIndex ( index - 1 );
                    }
                    else
                    {
                        classSearchHintsList.setSelectedIndex ( classSearchHintsList.getModelSize () - 1 );
                    }
                }
            }
        } );
        HotkeyManager.registerHotkey ( classSearchField, Hotkey.DOWN, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                if ( classSearchHintsList.getModelSize () > 0 )
                {
                    final int index = classSearchHintsList.getSelectedIndex ();
                    if ( index < classSearchHintsList.getModelSize () - 1 )
                    {
                        classSearchHintsList.setSelectedIndex ( index + 1 );
                    }
                    else
                    {
                        classSearchHintsList.setSelectedIndex ( 0 );
                    }
                }
            }
        } );
        HotkeyManager.registerHotkey ( classSearchField, Hotkey.END, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                if ( classSearchHintsList.getModelSize () > 0 )
                {
                    classSearchHintsList.setSelectedIndex ( classSearchHintsList.getModelSize () - 1 );
                }
            }
        } );
        HotkeyManager.registerHotkey ( classSearchField, Hotkey.ENTER, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                openSelectedHint ();
            }
        } );

        final WebScrollPane foundClassesScroll = new WebScrollPane ( classSearchHintsList );
        foundClassesScroll.setShadeWidth ( 0 );
        foundClassesScroll.setRound ( 0 );
        classSearchHintsPopup.add ( foundClassesScroll );

        classSearchPopup.addComponentListener ( new ComponentAdapter ()
        {
            @Override
            public void componentMoved ( final ComponentEvent e )
            {
                updateHintsLocation ();
            }

            @Override
            public void componentResized ( final ComponentEvent e )
            {
                updateHintsLocation ();
            }
        } );

        classSearchPopup.addPopupListener ( new PopupAdapter ()
        {
            @Override
            public void popupWillBeOpened ()
            {
                lastSearchedText = null;
                lastFocusBeforeSearch = FocusManager.getFocusOwner ();
            }

            @Override
            public void popupOpened ()
            {
                updateHints ();
            }

            @Override
            public void popupClosed ()
            {
                hideHints ();
                if ( lastFocusBeforeSearch != null )
                {
                    lastFocusBeforeSearch.requestFocusInWindow ();
                }
            }
        } );

        classSearchField.addCaretListener ( new CaretListener ()
        {
            @Override
            public void caretUpdate ( final CaretEvent e )
            {
                if ( classSearchHintsDelay == null )
                {
                    classSearchHintsDelay = new WebTimer ( 500, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            updateHints ();
                        }
                    } );
                    classSearchHintsDelay.setRepeats ( false );
                }
                if ( classSearchField.getText ().trim ().length () > 0 )
                {
                    classSearchHintsDelay.restart ();
                }
                else
                {
                    classSearchHintsDelay.stop ();
                    hideHints ();
                }
            }
        } );

        return classSearch;
    }

    private void openSelectedHint ()
    {
        if ( classSearchHintsList.getSelectedIndex () != -1 )
        {
            final JarEntry entry = ( JarEntry ) classSearchHintsList.getSelectedValue ();
            hideClassSearchPopup ();
            viewEntry ( entry );
        }
    }

    private void updateHints ()
    {
        final String text = classSearchField.getText ().trim ();

        // Ignore empty text
        if ( text.trim ().length () == 0 )
        {
            hideHints ();
            return;
        }

        // Updating hints window if needed
        if ( !CompareUtils.equals ( lastSearchedText, text ) )
        {
            // Saving old selection
            final Object oldSelection = classSearchHintsList.getSelectedValue ();

            // Clearing list
            final DefaultListModel model = ( DefaultListModel ) classSearchHintsList.getModel ();
            model.clear ();

            // Look for classes
            final List<JarEntry> found = jarStructure.findSimilarEntries ( text, new Filter<JarEntry> ()
            {
                @Override
                public boolean accept ( final JarEntry object )
                {
                    return object.getType ().equals ( JarEntryType.javaEntry );
                }
            } );
            if ( found.size () > 0 )
            {
                classSearchField.setForeground ( Color.BLACK );

                // Filling list with results
                for ( final JarEntry entry : found )
                {
                    model.addElement ( entry );
                }

                // Updating visible rows
                classSearchHintsList.setVisibleRowCount ( Math.min ( model.size (), 10 ) );

                // Restoring selection if possible
                final int index = oldSelection != null ? model.indexOf ( oldSelection ) : 0;
                classSearchHintsList.setSelectedIndex ( index != -1 ? index : 0 );

                // Packing popup
                classSearchHintsPopup.pack ();

                // Displaying hints window
                if ( !classSearchHintsPopup.isVisible () )
                {
                    classSearchHintsPopup.setVisible ( true );
                }
            }
            else
            {
                classSearchField.setForeground ( Color.RED );

                // Hiding hints window
                if ( classSearchHintsPopup.isVisible () )
                {
                    classSearchHintsPopup.setVisible ( false );
                }
            }

            lastSearchedText = text;
        }
    }

    private void updateHintsLocation ()
    {
        final Point p = classSearchField.getLocationOnScreen ();
        classSearchHintsPopup.setLocation ( p.x, p.y + classSearchField.getHeight () + 2 );
    }

    private void hideHints ()
    {
        if ( classSearchHintsPopup != null )
        {
            classSearchHintsPopup.setVisible ( false );
        }
    }

    private void showClassSearchPopup ()
    {
        if ( !classSearchPopup.isShowing () )
        {
            final Dimension size = getSize ();
            final Dimension ps = classSearchPopup.getPreferredSize ();
            classSearchPopup.showPopup ( this, size.width / 2 - ps.width / 2, size.height / 3 - ps.height / 2, ps.width, ps.height );
            classSearchField.selectAll ();
        }
    }

    private void hideClassSearchPopup ()
    {
        if ( classSearchPopup.isShowing () )
        {
            classSearchPopup.hidePopup ();
        }
    }

    private WebButton createSettings ()
    {
        final WebButton settings = new WebButton ( new ImageIcon ( SourceViewer.class.getResource ( "icons/settings.png" ) ) );
        settings.setDrawFocus ( false );
        settings.setRolloverDecoratedOnly ( true );

        final WebButtonPopup wbp = new WebButtonPopup ( settings, PopupWay.downLeft );

        final WebPanel popupContent = new WebPanel ( new VerticalFlowLayout ( 5, 5 ) );
        popupContent.setMargin ( 5 );
        popupContent.setOpaque ( false );
        wbp.setContent ( popupContent );

        theme = new WebComboBox ( EditorTheme.values () );
        theme.registerSettings ( SETTINGS_PREFIX + "theme", 0 );
        theme.setRenderer ( new WebComboBoxCellRenderer ()
        {
            @Override
            public Component getListCellRendererComponent ( final JList list, final Object value, final int index, final boolean isSelected,
                                                            final boolean cellHasFocus )
            {
                final EditorTheme editorTheme = ( EditorTheme ) value;
                final WebComboBoxElement renderer =
                        ( WebComboBoxElement ) super.getListCellRendererComponent ( list, value, index, isSelected, cellHasFocus );
                renderer.setIcon ( editorTheme.getIcon () );
                renderer.setText ( editorTheme.getName () );
                return renderer;
            }
        } );
        theme.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                synchronized ( activeEditorsLock )
                {
                    final String themeName = theme.getSelectedItem ().toString ().toLowerCase ();
                    for ( final Map.Entry<JarEntry, RSyntaxTextArea> entry : activeEditors.entrySet () )
                    {
                        loadTheme ( themeName, entry.getValue () );
                    }
                }
            }
        } );
        popupContent.add ( theme );

        allowCodeFolding = new WebToggleButton ( loadEditorIcon ( "allowCodeFolding" ) );
        allowCodeFolding.registerSettings ( SETTINGS_PREFIX + "allowCodeFolding", false );
        allowCodeFolding.addItemListener ( new ItemListener ()
        {
            @Override
            public void itemStateChanged ( final ItemEvent e )
            {
                synchronized ( activeEditorsLock )
                {
                    for ( final Map.Entry<JarEntry, RSyntaxTextArea> entry : activeEditors.entrySet () )
                    {
                        entry.getValue ().setCodeFoldingEnabled ( allowCodeFolding.isSelected () );
                    }
                }
            }
        } );
        final WebLabel allowCodeFoldingLabel = new WebLabel ( "Allow code folding" );
        allowCodeFoldingLabel.setDrawShade ( true );
        allowCodeFoldingLabel.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( SwingUtils.isLeftMouseButton ( e ) )
                {
                    allowCodeFolding.requestFocusInWindow ();
                    allowCodeFolding.doClick ();
                }
            }
        } );
        popupContent.add ( new GroupPanel ( 5, allowCodeFolding, allowCodeFoldingLabel ) );

        paintTabLines = new WebToggleButton ( loadEditorIcon ( "paintTabLines" ) );
        paintTabLines.registerSettings ( SETTINGS_PREFIX + "paintTabLines", false );
        paintTabLines.addItemListener ( new ItemListener ()
        {
            @Override
            public void itemStateChanged ( final ItemEvent e )
            {
                synchronized ( activeEditorsLock )
                {
                    for ( final Map.Entry<JarEntry, RSyntaxTextArea> entry : activeEditors.entrySet () )
                    {
                        entry.getValue ().setPaintTabLines ( paintTabLines.isSelected () );
                    }
                }
            }
        } );
        final WebLabel paintTabLinesLabel = new WebLabel ( "Paint tab lines" );
        paintTabLinesLabel.setDrawShade ( true );
        paintTabLinesLabel.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( SwingUtils.isLeftMouseButton ( e ) )
                {
                    paintTabLines.requestFocusInWindow ();
                    paintTabLines.doClick ();
                }
            }
        } );
        popupContent.add ( new GroupPanel ( 5, paintTabLines, paintTabLinesLabel ) );

        showWhitespaces = new WebToggleButton ( loadEditorIcon ( "showWhitespaces" ) );
        showWhitespaces.registerSettings ( SETTINGS_PREFIX + "showWhitespaces", false );
        showWhitespaces.addItemListener ( new ItemListener ()
        {
            @Override
            public void itemStateChanged ( final ItemEvent e )
            {
                synchronized ( activeEditorsLock )
                {
                    for ( final Map.Entry<JarEntry, RSyntaxTextArea> entry : activeEditors.entrySet () )
                    {
                        entry.getValue ().setWhitespaceVisible ( showWhitespaces.isSelected () );
                    }
                }
            }
        } );
        final WebLabel showWhitespacesLabel = new WebLabel ( "Show whitespaces" );
        showWhitespacesLabel.setDrawShade ( true );
        showWhitespacesLabel.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( SwingUtils.isLeftMouseButton ( e ) )
                {
                    showWhitespaces.requestFocusInWindow ();
                    showWhitespaces.doClick ();
                }
            }
        } );
        popupContent.add ( new GroupPanel ( 5, showWhitespaces, showWhitespacesLabel ) );

        showEol = new WebToggleButton ( loadEditorIcon ( "showEol" ) );
        showEol.registerSettings ( SETTINGS_PREFIX + "showEol", false );
        showEol.addItemListener ( new ItemListener ()
        {
            @Override
            public void itemStateChanged ( final ItemEvent e )
            {
                synchronized ( activeEditorsLock )
                {
                    for ( final Map.Entry<JarEntry, RSyntaxTextArea> entry : activeEditors.entrySet () )
                    {
                        entry.getValue ().setEOLMarkersVisible ( showEol.isSelected () );
                    }
                }
            }
        } );
        final WebLabel showEolLabel = new WebLabel ( "Show end of line" );
        showEolLabel.setDrawShade ( true );
        showEolLabel.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( SwingUtils.isLeftMouseButton ( e ) )
                {
                    showEol.requestFocusInWindow ();
                    showEol.doClick ();
                }
            }
        } );
        popupContent.add ( new GroupPanel ( 5, showEol, showEolLabel ) );

        return settings;
    }

    private ImageIcon loadEditorIcon ( final String name )
    {
        return new ImageIcon ( SourceViewer.class.getResource ( "icons/editor/" + name + ".png" ) );
    }

    public JarStructure getJarStructure ()
    {
        return jarStructure;
    }

    public void updateClassPath ( final Class classType )
    {
        updateClassPath ( jarStructure.getClassEntry ( classType ), true );
    }

    public void updateClassPath ( final JarEntry lastEntry, final boolean openInEditor )
    {
        classPath.removeAll ();

        // Root element
        final JarEntry root = jarStructure.getRoot ();
        final WebBreadcrumbButton rootElement = new WebBreadcrumbButton ();
        rootElement.setIcon ( root.getIcon () );
        TooltipManager.setTooltip ( rootElement, root.getIcon (), jarStructure.getJarLocation () );
        rootElement.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final WebPopupMenu rootMenu = new WebPopupMenu ();

                final WebMenuItem showInFS = new WebMenuItem ( "Show in folder", browseIcon );
                showInFS.addActionListener ( new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        try
                        {
                            final File jarFile = new File ( jarStructure.getJarLocation () );
                            Desktop.getDesktop ().browse ( jarFile.getParentFile ().toURI () );
                        }
                        catch ( final Throwable ex )
                        {
                            Log.error ( this, ex );
                        }
                    }
                } );
                rootMenu.add ( showInFS );

                final List<JarEntry> entries = jarStructure.getChildEntries ( root );
                if ( entries.size () > 0 )
                {
                    rootMenu.addSeparator ();
                    for ( final JarEntry entry : entries )
                    {
                        rootMenu.add ( createEntryMenuItem ( entry ) );
                    }
                }

                rootMenu.showBelowMiddle ( rootElement );
            }
        } );
        classPath.add ( rootElement );

        if ( lastEntry != null )
        {
            // All other elements
            final List<JarEntry> path = lastEntry.getPath ();
            for ( final JarEntry entry : path )
            {
                if ( entry.getType ().equals ( JarEntryType.packageEntry ) )
                {
                    final WebBreadcrumbButton element = new WebBreadcrumbButton ();
                    element.setIcon ( entry.getIcon () );
                    element.setText ( entry.getName () );
                    element.addActionListener ( new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            final List<JarEntry> entries = jarStructure.getChildEntries ( entry );
                            if ( entries.size () > 0 )
                            {
                                final WebPopupMenu packageMenu = new WebPopupMenu ();
                                for ( final JarEntry menuEntry : entries )
                                {
                                    packageMenu.add ( createEntryMenuItem ( menuEntry ) );
                                }
                                packageMenu.showBelowMiddle ( element );
                            }
                        }
                    } );
                    classPath.add ( element );
                }
                else
                {
                    final WebBreadcrumbLabel element = new WebBreadcrumbLabel ();
                    element.setIcon ( entry.getIcon () );
                    element.setText ( entry.getName () );
                    classPath.add ( element );
                }
            }
        }

        if ( openInEditor && lastEntry != null )
        {
            if ( lastEntry.getType ().equals ( JarEntryType.jarEntry ) || lastEntry.getType ().equals ( JarEntryType.packageEntry ) )
            {
                // Queueing last element menu to let breadcrumb update element locations first
                SwingUtilities.invokeLater ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        // Opening last element menu if it is a package
                        ( ( WebBreadcrumbButton ) classPath.getLastComponent () ).doClick ();
                    }
                } );
            }
            else
            {
                // Viewing last element if it is a file
                viewTabbedPane.removeChangeListener ( viewChangeListener );
                viewEntry ( lastEntry );
                viewTabbedPane.addChangeListener ( viewChangeListener );
            }
        }
    }

    public void viewEntry ( final JarEntry entry )
    {
        if ( viewTabbedPane.isOpenedEntry ( entry ) )
        {
            viewTabbedPane.selectEntry ( entry );
        }
        else
        {
            viewTabbedPane.viewEntry ( entry, createEntryViewer ( entry ) );
        }
        viewTabbedPane.transferFocus ();
    }

    public void closeEntryView ( final JarEntry entry )
    {
        viewTabbedPane.closeEntry ( entry );
    }

    private Component createEntryViewer ( final JarEntry entry )
    {
        if ( entry.getType ().equals ( JarEntryType.classEntry ) || entry.getType ().equals ( JarEntryType.javaEntry ) ||
                entry.getType ().equals ( JarEntryType.fileEntry ) )
        {
            final String ext = FileUtils.getFileExtPart ( entry.getName (), false ).toLowerCase ();
            if ( GlobalConstants.IMAGE_FORMATS.contains ( ext ) )
            {
                // todo A better image viewer (actually a new component - WebImageViewer)

                // Image file viewer
                final WebImage image = new WebImage ();
                image.setIcon ( ImageUtils.loadImage ( getEntryInputStream ( entry ) ) );

                // Image scroll
                final WebScrollPane imageScroll = new WebScrollPane ( image, false );
                imageScroll.setVerticalScrollBarPolicy ( WebScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
                imageScroll.setHorizontalScrollBarPolicy ( WebScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
                return imageScroll;
            }
            else
            {
                // Source code viewer
                final RSyntaxTextArea source = new RSyntaxTextArea ();

                // Syntax style
                boolean libraryCode = false;
                if ( ext.equals ( "java" ) || ext.equals ( "class" ) )
                {
                    source.setSyntaxEditingStyle ( SyntaxConstants.SYNTAX_STYLE_JAVA );
                    libraryCode = true;
                }
                else if ( ext.equals ( "xml" ) )
                {
                    source.setSyntaxEditingStyle ( SyntaxConstants.SYNTAX_STYLE_XML );
                }
                else if ( ext.equals ( "html" ) )
                {
                    source.setSyntaxEditingStyle ( SyntaxConstants.SYNTAX_STYLE_HTML );
                }
                else if ( ext.equals ( "css" ) )
                {
                    source.setSyntaxEditingStyle ( SyntaxConstants.SYNTAX_STYLE_CSS );
                }
                else if ( ext.equals ( "js" ) )
                {
                    source.setSyntaxEditingStyle ( SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT );
                }
                else if ( ext.equals ( "php" ) )
                {
                    source.setSyntaxEditingStyle ( SyntaxConstants.SYNTAX_STYLE_PHP );
                }
                else if ( ext.equals ( "sql" ) )
                {
                    source.setSyntaxEditingStyle ( SyntaxConstants.SYNTAX_STYLE_SQL );
                }
                else
                {
                    source.setSyntaxEditingStyle ( SyntaxConstants.SYNTAX_STYLE_NONE );
                }

                // Settings
                source.setEditable ( false );
                source.setMargin ( new Insets ( 0, 5, 0, 0 ) );
                source.setAntiAliasingEnabled ( true );
                source.setUseFocusableTips ( true );
                source.setTabSize ( 4 );
                source.setCodeFoldingEnabled ( allowCodeFolding.isSelected () );
                source.setPaintTabLines ( paintTabLines.isSelected () );
                source.setWhitespaceVisible ( showWhitespaces.isSelected () );
                source.setEOLMarkersVisible ( showEol.isSelected () );
                ( ( RSyntaxTextAreaHighlighter ) source.getHighlighter () ).setDrawsLayeredHighlights ( false );

                // Source code
                source.setText ( libraryCode ? loadSource ( entry ) : loadString ( entry ) );
                source.setCaretPosition ( 0 );

                // "Jump to source"-like action
                source.setHyperlinksEnabled ( true );
                source.setLinkGenerator ( new CodeLinkGenerator ( entry ) );

                // Saving opened editor
                synchronized ( activeEditorsLock )
                {
                    activeEditors.put ( entry, source );
                }

                // Special code viewer scroll pane
                final RTextScrollPane sourceScroll = new RTextScrollPane ( source );
                sourceScroll.setVerticalScrollBarPolicy ( WebScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
                ( ( WebScrollPaneUI ) sourceScroll.getUI () ).setDrawBorder ( false );

                // Source code viewer theme
                loadTheme ( theme.getSelectedItem ().toString ().toLowerCase (), source );

                return sourceScroll;
            }
        }
        return new WebLabel ();
    }

    private static final Map<String, Theme> themesCache = new HashMap<String, Theme> ();

    private void loadTheme ( final String themeName, final RSyntaxTextArea source )
    {
        if ( themesCache.containsKey ( themeName ) )
        {
            final Theme theme = themesCache.get ( themeName );
            if ( theme != null )
            {
                theme.apply ( source );
            }
        }
        else
        {
            try
            {
                final Theme theme = Theme.load ( SourceViewer.class.getResourceAsStream ( "themes/" + themeName + ".xml" ) );
                theme.apply ( source );
                themesCache.put ( themeName, theme );
            }
            catch ( final IOException e )
            {
                Log.error ( this, e );
                themesCache.put ( themeName, null );
            }
        }
    }

    private static final String commentStart = "/*";
    private static final String commentEnd = "*/\n\n";

    private String loadSource ( final JarEntry lastEntry )
    {
        String source = loadString ( lastEntry );

        // Removing space-eating license notice
        if ( source.startsWith ( commentStart ) )
        {
            final int index = source.indexOf ( commentEnd );
            if ( index != -1 )
            {
                source = source.substring ( index + commentEnd.length () );
            }
        }

        return source;
    }

    private String loadString ( final JarEntry lastEntry )
    {
        return FileUtils.readToString ( getEntryInputStream ( lastEntry ) );
    }

    private InputStream getEntryInputStream ( final JarEntry entry )
    {
        try
        {
            return new ZipFile ( jarStructure.getJarLocation () ).getInputStream ( entry.getZipEntry () );
        }
        catch ( final IOException e )
        {
            Log.error ( this, e );
            return null;
        }
    }

    private WebMenuItem createEntryMenuItem ( final JarEntry entry )
    {
        final WebMenuItem entryItem = new WebMenuItem ( entry.getName (), entry.getIcon () );
        entryItem.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                updateClassPath ( entry, true );
            }
        } );
        return entryItem;
    }

    public void addViewListener ( final ViewListener listener )
    {
        this.viewTabbedPane.addViewListener ( listener );
    }

    public void removeViewListener ( final ViewListener listener )
    {
        this.viewTabbedPane.removeViewListener ( listener );
    }

    protected class CodeLinkGenerator implements LinkGenerator
    {
        /**
         * Cached link generation results.
         */
        protected Map<Dimension, LinkGeneratorResult> results = new HashMap<Dimension, LinkGeneratorResult> ();

        /**
         * Entry displayed in the editor to which this link generator is attached.
         */
        protected JarEntry entry;

        /**
         * Constructs new code link generator for the specified jar entry.
         *
         * @param entry jar entry
         */
        public CodeLinkGenerator ( final JarEntry entry )
        {
            super ();
            this.entry = entry;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LinkGeneratorResult isLinkAtOffset ( final RSyntaxTextArea source, final int pos )
        {
            final String code = source.getText ();
            final int wordStart = TextUtils.getWordStart ( code, pos );
            final int wordEnd = TextUtils.getWordEnd ( code, pos );
            final String word = code.substring ( wordStart, wordEnd );
            final Dimension key = new Dimension ( wordStart, wordEnd );

            final LinkGeneratorResult value;
            if ( results.containsKey ( key ) )
            {
                value = results.get ( key );
            }
            else
            {
                if ( word != null )
                {
                    final JarEntry classByName = jarStructure.findEntryByName ( word );
                    if ( classByName != null && ( classByName.getType ().equals ( JarEntryType.classEntry ) ||
                            classByName.getType ().equals ( JarEntryType.javaEntry ) ) && classByName != entry )
                    {
                        value = new LinkGeneratorResult ()
                        {
                            @Override
                            public HyperlinkEvent execute ()
                            {
                                updateClassPath ( classByName, true );
                                return new HyperlinkEvent ( this, HyperlinkEvent.EventType.EXITED, null );
                            }

                            @Override
                            public int getSourceOffset ()
                            {
                                return wordStart;
                            }
                        };
                    }
                    else
                    {
                        value = null;
                    }
                }
                else
                {
                    value = null;
                }
                results.put ( key, value );
            }
            return value;
        }

        /**
         * Clears link search cache.
         */
        public void destroy ()
        {
            entry = null;
            results.clear ();
        }
    }
}