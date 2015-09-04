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

package com.alee.demo;

import com.alee.demo.api.Example;
import com.alee.demo.api.ExampleData;
import com.alee.demo.api.ExamplesManager;
import com.alee.demo.skin.DemoApplicationSkin;
import com.alee.demo.ui.examples.ExamplesFrame;
import com.alee.extended.dock.DockingPaneLayout;
import com.alee.extended.dock.WebDockablePane;
import com.alee.extended.panel.WebOverlay;
import com.alee.extended.tab.DocumentAdapter;
import com.alee.extended.tab.DocumentData;
import com.alee.extended.tab.PaneData;
import com.alee.extended.tab.WebDocumentPane;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.style.StyleManager;
import com.alee.managers.version.VersionManager;

import javax.swing.*;
import java.awt.*;

/**
 * WebLaF demo application containing various component and feature examples.
 *
 * @author Mikle Garin
 */

public class DemoApplication extends WebFrame
{
    /**
     * Demo application instance.
     */
    private static DemoApplication instance;

    /**
     * Demo application base UI elements.
     */
    private WebDockablePane dockablePane;
    private ExamplesFrame examplesFrame;
    private WebDocumentPane<DocumentData> contentPane;

    /**
     * Returns demo application instance.
     *
     * @return demo application instance
     */
    public static DemoApplication getInstance ()
    {
        if ( instance == null )
        {
            instance = new DemoApplication ();
        }
        return instance;
    }

    /**
     * Initializes demo application content.
     */
    private void initialize ()
    {
        updateTitle ();
        initializeDocks ();
        initializeContent ();
        setupFrame ();
    }

    /**
     * Initializes demo application dockable frames.
     */
    private void initializeDocks ()
    {
        dockablePane = new WebDockablePane ();
        add ( dockablePane, BorderLayout.CENTER );

        examplesFrame = new ExamplesFrame ();
        dockablePane.add ( examplesFrame, DockingPaneLayout.LEFT_FRAME );
    }

    /**
     * Initializes demo application content pane.
     */
    private void initializeContent ()
    {
        contentPane = new WebDocumentPane<DocumentData> ();

        final WebOverlay overlay = new WebOverlay ( contentPane );
        final WebLabel overlayContent = new WebLabel ( "demo.content.empty" );
        contentPane.addDocumentListener ( new DocumentAdapter<DocumentData> ()
        {
            @Override
            public void opened ( final DocumentData document, final PaneData<DocumentData> pane, final int index )
            {
                overlayContent.setVisible ( false );
            }

            @Override
            public void closed ( final DocumentData document, final PaneData<DocumentData> pane, final int index )
            {
                overlayContent.setVisible ( contentPane.getDocumentsCount () == 0 );
            }
        } );
        overlay.addOverlay ( overlayContent, SwingConstants.CENTER, SwingConstants.CENTER );

        dockablePane.add ( overlay, DockingPaneLayout.CONTENT );
    }

    /**
     * Initializes demo application frame.
     */
    private void setupFrame ()
    {
        setIconImages ( WebLookAndFeel.getImages () );
        setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE );
        setSize ( 900, 600 );
        setLocationRelativeTo ( null );
    }

    /**
     * Updates demo application title.
     */
    public void updateTitle ()
    {
        setTitle ( VersionManager.getLibraryVersion ().toString () /*+ ( selectedDemo != null ? " - " + selectedDemo.getName () : "" )*/ );
    }

    /**
     * Opens specified example in content pane.
     *
     * @param example example to open
     */
    public void open ( final Example example )
    {
        contentPane.openDocument ( ExampleData.forExample ( example ) );
    }

    /**
     * Displays demo application.
     */
    public void display ()
    {
        SwingUtilities.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                initialize ();
                setVisible ( true );
            }
        } );
    }

    /**
     * Properly launches demo application.
     *
     * @param args launch arguments
     */
    public static void main ( final String[] args )
    {
        // Exampler settings location
        SettingsManager.setDefaultSettingsDirName ( ".weblaf-demo" );
        SettingsManager.setDefaultSettingsGroup ( "WebLookAndFeelDemo" );
        SettingsManager.setSaveOnChange ( true );

        // Demo application languages
        LanguageManager.setSupportedLanguages ( LanguageManager.ENGLISH, LanguageManager.RUSSIAN );
        LanguageManager.setDefaultLanguage ( LanguageManager.ENGLISH );

        // Demo application skin
        // It extends default WebLaF skin and adds some custom styling
        StyleManager.setDefaultSkin ( DemoApplicationSkin.class.getCanonicalName () );

        // Look and Feel
        WebLookAndFeel.install ();

        // Loading demo dictionary
        LanguageManager.addDictionary ( DemoApplication.class, "language/language.xml" );

        // Initializing demo application managers
        ExamplesManager.initialize ();

        // Starting demo application
        DemoApplication.getInstance ().display ();
    }
}