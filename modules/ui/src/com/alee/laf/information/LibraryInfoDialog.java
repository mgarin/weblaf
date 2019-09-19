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

import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Objects;
import com.alee.api.version.Version;
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
import com.alee.laf.window.WebDialog;
import com.alee.laf.window.WebFrame;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.icon.LazyIcon;
import com.alee.managers.language.LM;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.SystemUtils;
import com.alee.utils.jar.JarEntry;
import com.alee.utils.jar.JarStructure;
import com.alee.utils.swing.extensions.KeyEventRunnable;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.*;

/**
 * Library information dialog.
 *
 * @author Mikle Garin
 */
public class LibraryInfoDialog extends WebFrame
{
    /**
     * Library data separator.
     */
    @NotNull
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
    @NotNull
    private Component createGeneralTab ()
    {
        final WebPanel content = new WebPanel ( StyleId.of ( "general" ), new VerticalFlowLayout ( 30, 30 ) );
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
    @NotNull
    private WebPanel createLibraryVersionPanel ()
    {
        final Version version = new Version ( LibraryInfoDialog.class );

        final WebImage icon = new WebImage ( WebLookAndFeel.getIcon ( 32 ) );

        final WebLink versionNumber = new WebLink ( version.toString (), new UrlLinkAction ( "http://weblookandfeel.com" ) );
        versionNumber.setBoldFont ();

        final WebLabel versionName = new WebLabel ( version.name () );

        return new GroupPanel ( StyleId.panelTransparent, 15, icon, new GroupPanel ( false, versionNumber, versionName ) );
    }

    /**
     * Returns java version panel.
     *
     * @return java version panel
     */
    @NotNull
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
    @NotNull
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
    @NotNull
    private Component createLibrariesTab ()
    {
        Component tab = null;
        try
        {
            // Parsing jar structure
            final JarStructure structure = new JarStructure ( getClass () );

            // Retrieving required files
            final JarEntry licensesFolder = structure.getRoot ().getChildByName ( "licenses" );
            final JarEntry librariesDataFile = licensesFolder.getChildByName ( "libraries.data" );

            // Retrieving additional data for used libraries
            final String librariesDataText = FileUtils.readToString ( librariesDataFile.getInputStream () );
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
                                final String license = FileUtils.readToString ( child.getInputStream () );

                                final WebDialog licenseDialog = new WebDialog ( LibraryInfoDialog.this, child.getName () );

                                final WebTextArea textArea = new WebTextArea ( StyleId.textareaNonOpaque, license );
                                textArea.setEditable ( false );
                                textArea.onKeyPress ( Hotkey.ESCAPE, new KeyEventRunnable ()
                                {
                                    @Override
                                    public void run ( @NotNull final KeyEvent e )
                                    {
                                        licenseDialog.dispose ();
                                    }
                                } );
                                licenseDialog.add ( new WebScrollPane ( StyleId.scrollpaneTransparentHovering, textArea ) );

                                licenseDialog.setSize ( 800, 600 );
                                licenseDialog.setLocationRelativeTo ( LibraryInfoDialog.this );
                                licenseDialog.setVisible ( true );
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
            tab = new WebScrollPane ( StyleId.of ( "libraries" ), librariesPanel );
        }
        catch ( final Exception e )
        {
            // Logging exception
            LoggerFactory.getLogger ( LibraryInfoDialog.class ).error ( e.toString (), e );
        }
        if ( tab == null )
        {
            // Error label
            tab = new WebLabel ( "weblaf.info.libraries.error", WebLabel.CENTER );
        }
        return tab;
    }

    /**
     * Returns parsed libraries data.
     *
     * @param librariesUrlText libraries data file content
     * @return parsed libraries data
     */
    @NotNull
    private Map<String, String> parseUrls ( @NotNull final String librariesUrlText )
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
    @NotNull
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

        return new WebScrollPane ( StyleId.scrollpaneTransparentHovering, propertiesTable );
    }

    /**
     * Returns system properties table data.
     *
     * @return system properties table data
     */
    @NotNull
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