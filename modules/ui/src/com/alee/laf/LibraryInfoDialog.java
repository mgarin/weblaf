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

package com.alee.laf;

import com.alee.extended.image.WebImage;
import com.alee.extended.label.WebLinkLabel;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.window.ComponentMoveBehavior;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.tabbedpane.TabStretchType;
import com.alee.laf.tabbedpane.TabbedPaneStyle;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.laf.table.WebTable;
import com.alee.laf.text.WebTextArea;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.managers.language.LM;
import com.alee.managers.language.LanguageAdapter;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.log.Log;
import com.alee.managers.popup.WebInnerPopup;
import com.alee.managers.style.StyleId;
import com.alee.managers.version.VersionInfo;
import com.alee.managers.version.VersionManager;
import com.alee.utils.FileUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.SystemUtils;
import com.alee.utils.reflection.JarEntry;
import com.alee.utils.reflection.JarStructure;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Library information dialog.
 *
 * @author Mikle Garin
 */

public class LibraryInfoDialog extends WebFrame
{
    /**
     * todo 1. Fix inner popup with license close action
     */

    /**
     * Library data separator.
     */
    private static final String LIBRARY_DATA_SEPARATOR = " - ";

    /**
     * Library information dialog instance.
     */
    private static LibraryInfoDialog instance;

    /**
     * Returns library information dialog instance.
     *
     * @return library information dialog instance
     */
    public static LibraryInfoDialog getInstance ()
    {
        if ( instance == null )
        {
            instance = new LibraryInfoDialog ();
        }
        return instance;
    }

    /**
     * Constructs new library information dialog.
     */
    private LibraryInfoDialog ()
    {
        super ( "weblaf.info.title" );
        setIconImages ( WebLookAndFeel.getImages () );
        ComponentMoveBehavior.install ( this );

        final WebTabbedPane tab = new WebTabbedPane ();
        tab.setTabbedPaneStyle ( TabbedPaneStyle.attached );
        tab.setTabStretchType ( TabStretchType.always );
        tab.addTab ( LM.get ( "weblaf.info.general.title" ), createTabSeparator ( createGeneralTab () ) );
        tab.addTab ( LM.get ( "weblaf.info.libraries.title" ), createTabSeparator ( createLibrariesTab () ) );
        tab.addTab ( LM.get ( "weblaf.info.properties.title" ), createTabSeparator ( createPropertiesTab () ) );
        LanguageManager.addLanguageListener ( new LanguageAdapter ()
        {
            @Override
            public void languageUpdated ()
            {
                tab.setTitleAt ( 0, LM.get ( "weblaf.info.general.title" ) );
                tab.setTitleAt ( 1, LM.get ( "weblaf.info.libraries.title" ) );
                tab.setTitleAt ( 2, LM.get ( "weblaf.info.properties.title" ) );
            }
        } );
        add ( tab );

        setResizable ( false );
        pack ();
        setLocationRelativeTo ( null );
        setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
    }

    /**
     * Returns tabbed pane separator.
     *
     * @param component tab content
     * @return tabbed pane separator
     */
    private Component createTabSeparator ( final Component component )
    {
        final WebPanel content = new WebPanel ();
        content.add ( new TabAreaSeparator (), BorderLayout.NORTH );
        content.add ( component, BorderLayout.CENTER );
        return content;
    }

    /**
     * Tabbed pane separator component.
     * todo Replace when new tabbed pane styling is available
     */
    private class TabAreaSeparator extends JComponent
    {
        /**
         * Custom component view.
         *
         * @param g graphics
         */
        @Override
        protected void paintComponent ( final Graphics g )
        {
            g.setColor ( new Color ( 237, 237, 237 ) );
            g.fillRect ( 0, 0, getWidth (), getHeight () - 1 );
            g.setColor ( Color.GRAY );
            g.drawLine ( 0, getHeight () - 1, getWidth () - 1, getHeight () - 1 );
        }

        /**
         * Returns custom component preferred size.
         *
         * @return custom component preferred size
         */
        @Override
        public Dimension getPreferredSize ()
        {
            return new Dimension ( 0, 4 );
        }
    }

    /**
     * Returns general tab content.
     *
     * @return general tab content
     */
    private Component createGeneralTab ()
    {
        final WebPanel content = new WebPanel ( StyleId.panelWhite, new VerticalFlowLayout ( 30, 30 ) );
        content.setMargin ( 60, 70, 60, 70 );

        content.add ( createLibraryVersionPanel () );
        content.add ( new WebSeparator ( WebSeparator.HORIZONTAL ) );
        content.add ( createJavaVersionPanel () );
        content.add ( new WebSeparator ( WebSeparator.HORIZONTAL ) );
        content.add ( createOsVersionPanel () );

        return content;
    }

    /**
     * Returns library version panel.
     *
     * @return library version panel
     */
    private WebPanel createLibraryVersionPanel ()
    {
        final VersionInfo versionInfo = VersionManager.getLibraryVersion ();

        final WebImage icon = new WebImage ( WebLookAndFeel.getIcon ( 32 ) );

        final WebLinkLabel version = new WebLinkLabel ( versionInfo.toString () );
        version.setLink ( "http://weblookandfeel.com", false );
        SwingUtils.setBoldFont ( version );

        final SimpleDateFormat sdf = new SimpleDateFormat ( "dd MMM yyyy", Locale.getDefault () );
        final WebLabel date = new WebLabel ();
        date.setLanguage ( "weblaf.info.general.updated", sdf.format ( new Date ( versionInfo.getDate () ) ) );

        return new GroupPanel ( 15, icon, new GroupPanel ( false, version, date ) );
    }

    /**
     * Returns java version panel.
     *
     * @return java version panel
     */
    private WebPanel createJavaVersionPanel ()
    {
        final WebImage javaIcon = new WebImage ( new ImageIcon ( LibraryInfoDialog.class.getResource ( "icons/java.png" ) ) );

        final WebLinkLabel javaVersion = new WebLinkLabel ();
        javaVersion.setLanguage ( "weblaf.info.general.java.version", SystemUtils.getJavaVersionString () );
        javaVersion.setLink ( "http://www.oracle.com/technetwork/java/javase/overview/", false );
        SwingUtils.setBoldFont ( javaVersion );

        final WebLabel javaName = new WebLabel ( SystemUtils.getJavaName () );

        return new GroupPanel ( 15, javaIcon, new GroupPanel ( false, javaVersion, javaName ) );
    }

    /**
     * Returns operation system version panel.
     *
     * @return operation system version panel
     */
    private WebPanel createOsVersionPanel ()
    {
        final WebImage osIcon = new WebImage ( SystemUtils.getOsIcon ( 32, false ) );

        final WebLinkLabel version = new WebLinkLabel ( SystemUtils.getOsName () );
        version.setLink ( SystemUtils.getOsSite (), false );
        SwingUtils.setBoldFont ( version );

        final WebLabel osVersion = new WebLabel ();
        osVersion.setLanguage ( "weblaf.info.general.os.arch", SystemUtils.getOsArch () );

        return new GroupPanel ( 15, osIcon, new GroupPanel ( false, version, osVersion ) );
    }

    /**
     * Returns libraries tab content.
     *
     * @return libraries tab content
     */
    private Component createLibrariesTab ()
    {
        try
        {
            // Parsing jar structure
            final JarStructure structure = ReflectUtils.getJarStructure ( getClass () );

            // Retrieving required files
            final JarEntry licensesFolder = structure.getRoot ().getChildByName ( "licenses" );
            final JarEntry librariesDataFile = licensesFolder.getChildByName ( "libraries.data" );

            // Retrieving additional data for used libraries
            final String librariesDataText = FileUtils.readToString ( structure.getEntryInputStream ( librariesDataFile ) );
            final Map<String, String> librariesData = parseUrls ( librariesDataText );

            // Parsing available libraries info
            final WebPanel librariesPanel = new WebPanel ( new VerticalFlowLayout ( 0, 5 ) );
            librariesPanel.setMargin ( 5 );
            for ( final JarEntry child : licensesFolder.getChildren () )
            {
                if ( child.getName ().endsWith ( ".txt" ) )
                {
                    final String data = librariesData.get ( child.getName () );
                    final int i = data.indexOf ( LIBRARY_DATA_SEPARATOR );
                    final String name = data.substring ( 0, i );
                    final String url = data.substring ( i + LIBRARY_DATA_SEPARATOR.length () );

                    final WebLabel nameLabel = new WebLabel ( name );
                    SwingUtils.setBoldFont ( nameLabel );

                    // Library license file
                    final WebLinkLabel fileLink = new WebLinkLabel ( child.getName () );
                    fileLink.setLink ( new Runnable ()
                    {
                        @Override
                        public void run ()
                        {
                            try
                            {
                                final String license = FileUtils.readToString ( structure.getEntryInputStream ( child ) );
                                final WebInnerPopup licensePopup = new WebInnerPopup ();
                                final WebTextArea textArea = new WebTextArea ( license );
                                textArea.setEditable ( false );
                                licensePopup.add ( new WebScrollPane ( StyleId.scrollpaneUndecorated, textArea ) );
                                licensePopup.showPopupAsModal ( fileLink, true, true );
                                HotkeyManager.registerHotkey ( textArea, Hotkey.ESCAPE, new HotkeyRunnable ()
                                {
                                    @Override
                                    public void run ( final KeyEvent e )
                                    {
                                        licensePopup.hidePopup ();
                                    }
                                } );
                            }
                            catch ( final IOException e )
                            {
                                Log.error ( this, e );
                            }
                        }
                    } );
                    final WebLabel licenseLabel = new WebLabel ();
                    licenseLabel.setLanguage ( "weblaf.info.libraries.license" );
                    final GroupPanel fileLinkPanel = new GroupPanel ( 5, licenseLabel, fileLink );

                    // Library site URL
                    final WebLinkLabel urlLink = new WebLinkLabel ( url );
                    urlLink.setLink ( url, false );
                    final WebLabel siteLabel = new WebLabel ();
                    siteLabel.setLanguage ( "weblaf.info.libraries.site" );
                    final GroupPanel urlLinkPanel = new GroupPanel ( 5, siteLabel, urlLink );

                    // Single library panel
                    final GroupPanel libraryPanel = new GroupPanel ( false, nameLabel, fileLinkPanel, urlLinkPanel );
                    libraryPanel.setStyleId ( StyleId.panelDecorated );
                    libraryPanel.setMargin ( 5 );
                    libraryPanel.setPreferredWidth ( 0 );
                    librariesPanel.add ( libraryPanel );
                }
            }

            // Libraries panel scroll
            final WebScrollPane scrollPane = new WebScrollPane ( StyleId.scrollpaneUndecorated, librariesPanel );
            scrollPane.setPreferredHeight ( 0 );
            return scrollPane;
        }
        catch ( final Throwable e )
        {
            Log.error ( this, e );
            return createErrorLibrariesTab ();
        }
    }

    /**
     * Returns parsed libraries data.
     *
     * @param librariesUrlText libraries data file content
     * @return parsed libraries data
     */
    private Map<String, String> parseUrls ( final String librariesUrlText )
    {
        final Map<String, String> librariesUrl = new HashMap<String, String> ();
        final StringTokenizer st = new StringTokenizer ( librariesUrlText, "\n", false );
        while ( st.hasMoreTokens () )
        {
            final String token = st.nextToken ();
            final int i = token.indexOf ( LIBRARY_DATA_SEPARATOR );
            librariesUrl.put ( token.substring ( 0, i ), token.substring ( i + LIBRARY_DATA_SEPARATOR.length () ) );
        }
        return librariesUrl;
    }

    /**
     * Returns error libraries tab content.
     *
     * @return error libraries tab content
     */
    private Component createErrorLibrariesTab ()
    {
        final WebLabel errorLabel = new WebLabel ( WebLabel.CENTER );
        errorLabel.setLanguage ( "weblaf.info.libraries.error" );
        return errorLabel;
    }

    /**
     * Returns properties tab content.
     *
     * @return properties tab content
     */
    private Component createPropertiesTab ()
    {
        final Object[][] systemPropertiesData = createSystemPropertiesData ();
        final DefaultTableModel model = new DefaultTableModel ( systemPropertiesData, getPropertiesTableColumnNames () );
        final WebTable propertiesTable = new WebTable ( model );
        propertiesTable.setPreferredScrollableViewportSize ( new Dimension ( 1, 1 ) );
        propertiesTable.setEditable ( false );
        LanguageManager.addLanguageListener ( new LanguageAdapter ()
        {
            @Override
            public void languageUpdated ()
            {
                final int[] selection = propertiesTable.getSelectedRows ();

                model.setColumnIdentifiers ( getPropertiesTableColumnNames () );

                for ( final int s : selection )
                {
                    propertiesTable.addRowSelectionInterval ( s, s );
                }
            }
        } );
        return new WebScrollPane ( StyleId.scrollpaneUndecorated, propertiesTable );
    }

    /**
     * Returns system properties table column names.
     *
     * @return system properties table column names
     */
    private String[] getPropertiesTableColumnNames ()
    {
        return new String[]{ LM.get ( "weblaf.info.properties.key" ), LM.get ( "weblaf.info.properties.value" ) };
    }

    /**
     * Returns system properties table data.
     *
     * @return system properties table data
     */
    private Object[][] createSystemPropertiesData ()
    {
        final Properties properties = System.getProperties ();
        final Object[][] data = new Object[ properties.size () ][ 2 ];
        int i = 0;
        for ( final Map.Entry entry : properties.entrySet () )
        {
            data[ i ][ 0 ] = entry.getKey ();
            data[ i ][ 1 ] = entry.getValue ();
            i++;
        }
        Arrays.sort ( data, new Comparator<Object[]> ()
        {
            @Override
            public int compare ( final Object[] o1, final Object[] o2 )
            {
                return ( ( String ) o1[ 0 ] ).compareTo ( ( String ) o2[ 0 ] );
            }
        } );
        return data;
    }

    /**
     * Launches library informations dialog.
     *
     * @param args launch arguments
     */
    public static void main ( final String[] args )
    {
        // L&F
        WebLookAndFeel.install ();

        // Orientation change listener
        HotkeyManager.registerHotkey ( Hotkey.ALT_R, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                WebLookAndFeel.changeOrientation ();
            }
        } );

        // Language change listener
        HotkeyManager.registerHotkey ( Hotkey.ALT_L, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                final String current = LanguageManager.getLanguage ();
                final List<String> supported = LanguageManager.getSupportedLanguages ();
                final int i = supported.indexOf ( current );
                if ( i != -1 )
                {
                    LanguageManager.setLanguage ( i < supported.size () - 1 ? supported.get ( i + 1 ) : supported.get ( 0 ) );
                }
            }
        } );

        // Basic library jar info frame
        LibraryInfoDialog.getInstance ().setVisible ( true );
    }
}