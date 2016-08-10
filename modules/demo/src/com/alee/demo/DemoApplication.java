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

import com.alee.api.data.CompassDirection;
import com.alee.demo.api.Example;
import com.alee.demo.api.ExampleData;
import com.alee.demo.api.ExamplesManager;
import com.alee.demo.skin.AdaptiveExtension;
import com.alee.demo.skin.DarkSkinExtension;
import com.alee.demo.skin.LightSkinExtension;
import com.alee.demo.skin.decoration.FeatureStateBackground;
import com.alee.demo.ui.examples.ExamplesFrame;
import com.alee.demo.ui.widgets.SkinChooser;
import com.alee.extended.behavior.ComponentResizeBehavior;
import com.alee.extended.canvas.WebCanvas;
import com.alee.extended.dock.WebDockablePane;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.link.UrlLinkAction;
import com.alee.extended.link.WebLink;
import com.alee.extended.magnifier.MagnifierGlass;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.WebOverlay;
import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.extended.tab.*;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.window.WebFrame;
import com.alee.managers.language.LM;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.style.Skin;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.managers.version.VersionManager;
import com.alee.skin.dark.DarkSkin;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.XmlUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * WebLaF demo application containing various component and feature examples.
 *
 * @author Mikle Garin
 */

public final class DemoApplication extends WebFrame
{
    /**
     * Available application and example skins.
     */
    public static ArrayList<Skin> skins;

    /**
     * Developer contacts.
     * Also used as data for some examples.
     */
    public static final String WEBLAF_EMAIL = "mgarin@alee.com";
    public static final String WEBLAF_SITE = "http://weblookandfeel.com/";
    public static final String WEBLAF_GITHUB = "https://github.com/mgarin/weblaf";
    public static final String WEBLAF_GITTER = "https://gitter.im/mgarin/weblaf";

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
        initializeStatus ();
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
        dockablePane.addFrame ( examplesFrame );
    }

    /**
     * Initializes demo application content pane.
     */
    private void initializeContent ()
    {
        contentPane = new WebDocumentPane<DocumentData> ();
        contentPane.setClosable ( true );
        contentPane.setDragEnabled ( true );
        contentPane.setDragBetweenPanesEnabled ( false );
        contentPane.setSplitEnabled ( true );
        contentPane.onDocumentSelection ( new DocumentDataRunnable<DocumentData> ()
        {
            @Override
            public void run ( final DocumentData document, final PaneData<DocumentData> pane, final int index )
            {
                updateTitle ();
            }
        } );

        final WebOverlay overlay = new WebOverlay ( contentPane );
        overlay.setStyleId ( StyleId.panelDecorated );

        final WebPanel overlayContainer = new WebPanel ( DemoStyles.emptycontentPanel );

        final StyleId overlayLabelId = DemoStyles.emptycontentLabel.at ( overlayContainer );
        final WebStyledLabel guide = new WebStyledLabel ( overlayLabelId, "demo.content.empty", DemoIcons.compass36 ).changeFontSize ( 5 );
        final WebStyledLabel issues = new WebStyledLabel ( overlayLabelId, "demo.content.issues", DemoIcons.bug36 ).changeFontSize ( 5 );
        overlayContainer.add ( new GroupPanel ( 20, false, guide, issues ) );

        contentPane.addDocumentListener ( new DocumentAdapter<DocumentData> ()
        {
            @Override
            public void opened ( final DocumentData document, final PaneData<DocumentData> pane, final int index )
            {
                overlayContainer.setVisible ( false );
            }

            @Override
            public void closed ( final DocumentData document, final PaneData<DocumentData> pane, final int index )
            {
                overlayContainer.setVisible ( contentPane.getDocumentsCount () == 0 );
            }
        } );
        overlay.addOverlay ( overlayContainer, SwingConstants.CENTER, SwingConstants.CENTER );

        dockablePane.setContent ( overlay );
    }

    /**
     * Initializes status bar and its content.
     */
    private void initializeStatus ()
    {
        final WebStatusBar statusBar = new WebStatusBar ();

        statusBar.add ( new WebLink ( DemoStyles.resourceLink, DemoIcons.icon16, "demo.statusbar.resources.weblaf",
                new UrlLinkAction ( WEBLAF_SITE ) ) );

        statusBar.add ( new WebLink ( DemoStyles.resourceLink, DemoIcons.github16, "demo.statusbar.resources.github",
                new UrlLinkAction ( WEBLAF_GITHUB ) ) );

        statusBar.add ( new WebLink ( DemoStyles.resourceLink, DemoIcons.gitter16, "demo.statusbar.resources.gitter",
                new UrlLinkAction ( WEBLAF_GITTER ) ) );

        statusBar.addToEnd ( new SkinChooser () );

        statusBar.addSpacingToEnd ();

        final WebToggleButton magnifierButton = new WebToggleButton ( "demo.statusbar.tool.magnifier", DemoIcons.magnifier16 );
        magnifierButton.addActionListener ( new ActionListener ()
        {
            private final MagnifierGlass magnifier = new MagnifierGlass ();

            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                magnifier.displayOrDispose ( DemoApplication.this );
            }
        } );
        statusBar.addToEnd ( magnifierButton );

        statusBar.addSpacingToEnd ();

        final WebMemoryBar memoryBar = new WebMemoryBar ();
        memoryBar.setPreferredWidth ( 150 );
        statusBar.addToEnd ( memoryBar );

        final WebCanvas resizeCorner = new WebCanvas ( StyleId.canvasGripperSE );
        ComponentResizeBehavior.install ( resizeCorner, CompassDirection.southEast );
        statusBar.addToEnd ( resizeCorner );

        add ( statusBar, BorderLayout.SOUTH );

        // Custom status bar margin for notification manager
        NotificationManager.setMargin ( 0, 0, memoryBar.getPreferredSize ().height, 0 );
    }

    /**
     * Initializes demo application frame.
     */
    private void setupFrame ()
    {
        setIconImages ( WebLookAndFeel.getImages () );
        setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE );
        setSize ( 1200, 800 );
        setLocationRelativeTo ( null );
    }

    /**
     * Updates demo application title.
     */
    public void updateTitle ()
    {
        final DocumentData doc = contentPane != null ? contentPane.getSelectedDocument () : null;
        setTitle ( VersionManager.getLibraryVersion ().toString () + ( doc != null ? " - " + LM.get ( doc.getTitle () ) : "" ) );
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
        SwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                initialize ();
                setVisible ( true );
                setExtendedState ( JFrame.MAXIMIZED_BOTH );
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
        SwingUtils.enableEventQueueLogging ();
        SwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                // Configuring settings location
                SettingsManager.setDefaultSettingsDirName ( ".weblaf-demo" );
                SettingsManager.setDefaultSettingsGroup ( "WebLookAndFeelDemo" );
                SettingsManager.setSaveOnChange ( true );

                // Configurting availale languages
                LanguageManager.setLanguages ( LanguageManager.ENGLISH, LanguageManager.RUSSIAN );

                // Adding demo data aliases before styles using it are read
                XmlUtils.processAnnotations ( FeatureStateBackground.class );

                // Installing Look and Feel
                WebLookAndFeel.install ( /*DarkSkin.class*/ );
                skins = CollectionUtils.asList ( StyleManager.getSkin (), new DarkSkin () );

                // Adding demo application skin extensions
                // They contain all custom styles demo application uses
                StyleManager.addExtensions ( new AdaptiveExtension (), new LightSkinExtension (), new DarkSkinExtension () );

                // Loading demo dictionary
                LanguageManager.addDictionary ( DemoApplication.class, "language/language.xml" );

                // Initializing demo application managers
                ExamplesManager.initialize ();

                // Starting demo application
                DemoApplication.getInstance ().display ();

                // Interface inspector used for demo aplication testing
                // InterfaceInspector.showPopOver ( DemoApplication.getInstance ().getRootPane () );
            }
        } );
    }
}