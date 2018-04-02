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

package com.alee.laf.information;

import com.alee.api.jdk.Objects;
import com.alee.extended.behavior.ComponentMoveBehavior;
import com.alee.extended.image.WebImage;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.link.AbstractLinkAction;
import com.alee.extended.link.UrlLinkAction;
import com.alee.extended.link.WebLink;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.tab.DocumentData;
import com.alee.extended.tab.WebDocumentPane;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.table.WebTable;
import com.alee.laf.text.WebTextArea;
import com.alee.laf.window.WebFrame;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.managers.icon.LazyIcon;
import com.alee.managers.language.LM;
import com.alee.managers.popup.WebInnerPopup;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.managers.version.VersionInfo;
import com.alee.managers.version.VersionManager;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.JarUtils;
import com.alee.utils.SystemUtils;
import com.alee.utils.jar.JarEntry;
import com.alee.utils.jar.JarStructure;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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
     * Constructs new library information dialog.
     */
    private LibraryInfoDialog ()
    {
        super ( "weblaf.info.title" );
        setIconImages ( WebLookAndFeel.getImages () );

        final ComponentMoveBehavior moveBehavior = new ComponentMoveBehavior ( this );
        moveBehavior.install ();

        final WebDocumentPane documentPane = new WebDocumentPane ( StyleId.of ( "tabs" ) );
        documentPane.setClosable ( false );
        documentPane.setDragEnabled ( false );
        documentPane.setTabMenuEnabled ( false );
        documentPane.openDocument ( new DocumentData ( "general", "weblaf.info.general.title", createGeneralTab () ) );
        documentPane.openDocument ( new DocumentData ( "libraries", "weblaf.info.libraries.title", createLibrariesTab () ), false );
        documentPane.openDocument ( new DocumentData ( "properties", "weblaf.info.properties.title", createPropertiesTab () ), false );
        add ( documentPane );

        setResizable ( false );
        pack ();
        setLocationRelativeTo ( null );
        setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
    }

    /**
     * Returns general tab content.
     *
     * @return general tab content
     */
    private Component createGeneralTab ()
    {
        final WebPanel content = new WebPanel ( StyleId.of ( "general" ), new VerticalFlowLayout ( 30, 30 ) );
        content.add ( createLibraryVersionPanel () );
        content.add ( new WebSeparator ( WebSeparator.HORIZONTAL ) );
        content.add ( createJavaVersionPanel () );
        content.add ( new WebSeparator ( WebSeparator.HORIZONTAL ) );
        content.add ( createOsVersionPanel () );
        return createTabSeparator ( content );
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

        final WebLink version = new WebLink ( versionInfo.toString (), new UrlLinkAction ( "http://weblookandfeel.com" ) );
        version.setBoldFont ();

        final SimpleDateFormat sdf = new SimpleDateFormat ( "dd MMM yyyy", Locale.getDefault () );
        final WebLabel date = new WebLabel ();
        date.setLanguage ( "weblaf.info.general.updated", sdf.format ( new Date ( versionInfo.getDate () ) ) );

        return new GroupPanel ( StyleId.panelTransparent, 15, icon, new GroupPanel ( false, version, date ) );
    }

    /**
     * Returns java version panel.
     *
     * @return java version panel
     */
    private WebPanel createJavaVersionPanel ()
    {
        final WebImage javaIcon = new WebImage ( new LazyIcon ( "java32" ) );

        final WebLink javaVersion = new WebLink ();
        javaVersion.setLanguage ( "weblaf.info.general.java.version", SystemUtils.getJavaVersionString () );
        javaVersion.addAction ( new UrlLinkAction ( "http://www.oracle.com/technetwork/java/javase/overview/" ) );
        javaVersion.setBoldFont ();

        final WebLabel javaName = new WebLabel ( SystemUtils.getJavaName () );

        return new GroupPanel ( StyleId.panelTransparent, 15, javaIcon, new GroupPanel ( false, javaVersion, javaName ) );
    }

    /**
     * Returns operation system version panel.
     *
     * @return operation system version panel
     */
    private WebPanel createOsVersionPanel ()
    {
        final WebImage osIcon = new WebImage ( SystemUtils.getOsIcon ( 32, false ) );

        final WebLink version = new WebLink ( SystemUtils.getOsName (), new UrlLinkAction ( SystemUtils.getOsSite () ) );
        version.setBoldFont ();

        final WebLabel osVersion = new WebLabel ();
        osVersion.setLanguage ( "weblaf.info.general.os.arch", SystemUtils.getOsArch () );

        return new GroupPanel ( StyleId.panelTransparent, 15, osIcon, new GroupPanel ( false, version, osVersion ) );
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
            final JarStructure structure = JarUtils.getJarStructure ( getClass () );

            // Retrieving required files
            final JarEntry licensesFolder = structure.getRoot ().getChildByName ( "licenses" );
            final JarEntry librariesDataFile = licensesFolder.getChildByName ( "libraries.data" );

            // Retrieving additional data for used libraries
            final String librariesDataText = FileUtils.readToString ( structure.getEntryInputStream ( librariesDataFile ) );
            final Map<String, String> librariesData = parseUrls ( librariesDataText );

            // Parsing available libraries info
            final WebPanel librariesPanel = new WebPanel ( StyleId.of ( "libraries" ), new VerticalFlowLayout ( 0, 5 ) );
            for ( final Map.Entry<String, String> library : librariesData.entrySet () )
            {
                for ( final JarEntry child : licensesFolder.getChildren () )
                {
                    if ( Objects.equals ( library.getKey (), child.getName () ) )
                    {
                        final String data = library.getValue ();
                        final int i = data.indexOf ( LIBRARY_DATA_SEPARATOR );
                        final String name = data.substring ( 0, i );
                        final String url = data.substring ( i + LIBRARY_DATA_SEPARATOR.length () );

                        final WebLabel nameLabel = new WebLabel ( name );
                        nameLabel.setBoldFont ();

                        // Library license file
                        final WebLabel licenseLabel = new WebLabel ( "weblaf.info.libraries.license" );
                        final WebLink licenseLink = new WebLink ( new AbstractLinkAction ( child.getName () )
                        {
                            @Override
                            public void linkExecuted ( final ActionEvent event )
                            {
                                try
                                {
                                    final String license = FileUtils.readToString ( structure.getEntryInputStream ( child ) );
                                    final WebInnerPopup licensePopup = new WebInnerPopup ();
                                    final WebTextArea textArea = new WebTextArea ( StyleId.textareaNonOpaque, license );
                                    textArea.setEditable ( false );
                                    licensePopup.add ( new WebScrollPane ( StyleId.scrollpaneTransparentHovering, textArea ) );
                                    licensePopup.showPopupAsModal ( ( Component ) event.getSource (), true, true );
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
                                    LoggerFactory.getLogger ( LibraryInfoDialog.class ).error ( e.toString (), e );
                                }
                            }
                        } );
                        final GroupPanel fileLinkPanel = new GroupPanel ( 5, licenseLabel, licenseLink );

                        // Library site URL
                        final WebLabel siteLabel = new WebLabel ( "weblaf.info.libraries.site" );
                        final WebLink siteLink = new WebLink ( url, new UrlLinkAction ( url ) );
                        final GroupPanel urlLinkPanel = new GroupPanel ( 5, siteLabel, siteLink );

                        // Single library panel
                        librariesPanel.add ( new GroupPanel ( StyleId.of ( "library" ), false, nameLabel, fileLinkPanel, urlLinkPanel ) );
                        break;
                    }
                }
            }

            // Libraries panel scroll
            final WebScrollPane scrollPane = new WebScrollPane ( StyleId.of ( "libraries" ), librariesPanel );
            return createTabSeparator ( scrollPane );
        }
        catch ( final Exception e )
        {
            // Logging exception
            LoggerFactory.getLogger ( LibraryInfoDialog.class ).error ( e.toString (), e );

            // Error label
            final WebLabel errorLabel = new WebLabel ( "weblaf.info.libraries.error", WebLabel.CENTER );
            return createTabSeparator ( errorLabel );
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
        final Map<String, String> librariesUrl = new LinkedHashMap<String, String> ();
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
     * Returns properties tab content.
     *
     * @return properties tab content
     */
    private Component createPropertiesTab ()
    {
        final Object[][] systemPropertiesData = createSystemPropertiesData ();
        final String key = LM.get ( "weblaf.info.properties.key" );
        final String value = LM.get ( "weblaf.info.properties.value" );
        final String[] colums = { key, value };
        final DefaultTableModel model = new DefaultTableModel ( systemPropertiesData, colums );

        final WebTable propertiesTable = new WebTable ( StyleId.tableTransparent, model );
        propertiesTable.setPreferredScrollableViewportSize ( new Dimension ( 1, 1 ) );
        propertiesTable.setEditable ( false );

        final WebScrollPane scrollPane = new WebScrollPane ( StyleId.scrollpaneTransparentHovering, propertiesTable );
        return createTabSeparator ( scrollPane );
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
     * todo Temporary styling addition, remove upon proper tabbed pane UI implementation
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
     * Launches library informations dialog.
     *
     * @param args launch arguments
     */
    public static void main ( final String[] args )
    {
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                // LaF
                WebLookAndFeel.setForceSingleEventsThread ( true );
                WebLookAndFeel.install ();

                // Skin extension
                StyleManager.addExtensions ( new LibraryInfoExtension () );

                // Basic library jar info frame
                final LibraryInfoDialog dialog = new LibraryInfoDialog ();
                dialog.setVisible ( true );
            }
        } );
    }
}