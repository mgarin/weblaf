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

import com.alee.api.annotations.NotNull;
import com.alee.api.data.CompassDirection;
import com.alee.api.jdk.SerializableSupplier;
import com.alee.api.version.Version;
import com.alee.demo.api.example.Example;
import com.alee.demo.api.example.ExampleData;
import com.alee.demo.content.ExamplesManager;
import com.alee.demo.frames.examples.ExamplesFrame;
import com.alee.demo.frames.inspector.InspectorFrame;
import com.alee.demo.frames.source.SourceFrame;
import com.alee.demo.frames.style.StyleFrame;
import com.alee.demo.skin.*;
import com.alee.demo.skin.decoration.FeatureStateBackground;
import com.alee.demo.ui.tools.*;
import com.alee.extended.behavior.ComponentResizeBehavior;
import com.alee.extended.canvas.WebCanvas;
import com.alee.extended.dock.SidebarButtonVisibility;
import com.alee.extended.dock.WebDockablePane;
import com.alee.extended.label.TextWrap;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.AlignLayout;
import com.alee.extended.link.UrlLinkAction;
import com.alee.extended.link.WebLink;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.WebOverlay;
import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.extended.tab.*;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WindowState;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.laf.window.WebFrame;
import com.alee.managers.language.LM;
import com.alee.managers.language.LanguageLocaleUpdater;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.data.Dictionary;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.style.Skin;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.skin.dark.DarkSkin;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.XmlUtils;
import com.alee.utils.swing.Customizer;

import javax.swing.*;
import java.awt.*;
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
    private StyleFrame styleFrame;
    private SourceFrame sourceFrame;
    private InspectorFrame inspectorFrame;
    private WebDocumentPane<ExampleData> examplesPane;

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
     * Constructs new {@link DemoApplication}.
     */
    private DemoApplication ()
    {
        super ();
        setIconImages ( WebLookAndFeel.getImages () );
        updateTitle ();
        initializeDocks ();
        initializeToolBar ();
        initializeStatus ();
        setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE );
        registerSettings ( new Configuration<WindowState> ( "application", new SerializableSupplier<WindowState> ()
        {
            @Override
            public WindowState get ()
            {
                return new WindowState ( new Dimension ( 1200, 820 ) );
            }
        } ) );
    }

    /**
     * Initializes demo application dockable frames.
     */
    private void initializeDocks ()
    {
        /**
         * Dockable pane.
         */

        dockablePane = new WebDockablePane ( StyleId.dockablepaneCompact );
        dockablePane.setSidebarButtonVisibility ( SidebarButtonVisibility.anyMinimized );

        /**
         * Content.
         */

        examplesPane = new WebDocumentPane<ExampleData> ( DemoStyles.expamplesPane );
        examplesPane.setClosable ( true );
        examplesPane.setDragEnabled ( true );
        examplesPane.setDragBetweenPanesEnabled ( false );
        examplesPane.setSplitEnabled ( true );
        examplesPane.setTabbedPaneCustomizer ( new Customizer<WebTabbedPane> ()
        {
            @Override
            public void customize ( @NotNull final WebTabbedPane tabbedPane )
            {
                tabbedPane.setTabLayoutPolicy ( JTabbedPane.SCROLL_TAB_LAYOUT );
            }
        } );
        examplesPane.onDocumentSelection ( new DocumentDataRunnable<ExampleData> ()
        {
            @Override
            public void run ( final ExampleData document, final PaneData<ExampleData> pane, final int index )
            {
                updateTitle ();
            }
        } );

        final WebOverlay overlay = new WebOverlay ( StyleId.panel, examplesPane );
        final WebPanel overlayContainer = new WebPanel ( DemoStyles.emptycontentPanel, new AlignLayout () );

        final StyleId guideId = DemoStyles.emptycontentInfoLabel.at ( overlayContainer );
        final WebStyledLabel guide = new WebStyledLabel ( guideId, "demo.content.empty", DemoIcons.compass36 );
        guide.changeFontSize ( 5 ).setBoldFont ().setWrap ( TextWrap.none );

        final StyleId issuesId = DemoStyles.emptycontentWarnLabel.at ( overlayContainer );
        final WebStyledLabel issues = new WebStyledLabel ( issuesId, "demo.content.issues", DemoIcons.bug36 );
        issues.changeFontSize ( 5 ).setBoldFont ().setWrap ( TextWrap.none );

        overlayContainer.add ( new GroupPanel ( 20, false, guide, issues ), "0,0" );
        overlay.addOverlay ( overlayContainer );

        examplesPane.addDocumentListener ( new DocumentAdapter<ExampleData> ()
        {
            @Override
            public void opened ( final ExampleData document, final PaneData<ExampleData> pane, final int index )
            {
                overlayContainer.setVisible ( false );
            }

            @Override
            public void closed ( final ExampleData document, final PaneData<ExampleData> pane, final int index )
            {
                overlayContainer.setVisible ( examplesPane.getDocumentsCount () == 0 );
            }
        } );

        dockablePane.setContent ( overlay );

        /**
         * Frames.
         */

        examplesFrame = new ExamplesFrame ();
        dockablePane.addFrame ( examplesFrame );

        inspectorFrame = new InspectorFrame ( this );
        dockablePane.addFrame ( inspectorFrame );

        sourceFrame = new SourceFrame ( this );
        dockablePane.addFrame ( sourceFrame );

        styleFrame = new StyleFrame ( this );
        dockablePane.addFrame ( styleFrame );

        /**
         * Dockable pane positon.
         * Added last for optimization purpose.
         */

        add ( dockablePane, BorderLayout.CENTER );
    }

    /**
     * Returns content pane.
     *
     * @return content pane
     */
    public WebDocumentPane<ExampleData> getExamplesPane ()
    {
        return examplesPane;
    }

    /**
     * Initializes demo application toolbar and its content.
     */
    private void initializeToolBar ()
    {
        final WebToolBar toolBar = new WebToolBar ( StyleId.toolbarAttachedNorth );
        toolBar.setFloatable ( false );

        toolBar.add ( new SkinChooserTool () );
        toolBar.addSeparator ();
        toolBar.add ( new OrientationChooserTool () );
        toolBar.addSeparator ();
        toolBar.add ( new LanguageChooserTool () );

        toolBar.addToEnd ( new HeatMapTool ( DemoApplication.this ) );
        toolBar.addSeparatorToEnd ();
        toolBar.addToEnd ( new MagnifierToggleTool ( DemoApplication.this ) );

        add ( toolBar, BorderLayout.NORTH );
    }

    /**
     * Initializes status bar and its content.
     */
    private void initializeStatus ()
    {
        final WebStatusBar statusBar = new WebStatusBar ();

        statusBar.add ( new WebLink ( DemoStyles.resourceLink, DemoIcons.java19, "demo.statusbar.resources.weblaf",
                new UrlLinkAction ( WEBLAF_SITE ) ) );

        statusBar.add ( new WebLink ( DemoStyles.resourceLink, DemoIcons.github19, "demo.statusbar.resources.github",
                new UrlLinkAction ( WEBLAF_GITHUB ) ) );

        statusBar.add ( new WebLink ( DemoStyles.resourceLink, DemoIcons.gitter19, "demo.statusbar.resources.gitter",
                new UrlLinkAction ( WEBLAF_GITTER ) ) );

        final WebMemoryBar memoryBar = new WebMemoryBar ();
        memoryBar.setPreferredWidth ( 150 );
        statusBar.addToEnd ( memoryBar );

        final WebCanvas resizeCorner = new WebCanvas ( StyleId.canvasGripperSE );
        new ComponentResizeBehavior ( resizeCorner, CompassDirection.southEast ).install ();
        statusBar.addToEnd ( resizeCorner );

        add ( statusBar, BorderLayout.SOUTH );

        // Custom status bar margin for notification manager
        NotificationManager.setMargin ( 0, 0, memoryBar.getPreferredSize ().height, 0 );
    }

    /**
     * Updates demo application title.
     */
    public void updateTitle ()
    {
        final StringBuilder title = new StringBuilder ();

        // Title & version
        title.append ( "WebLaF " ).append ( new Version ( DemoApplication.class ).toString () );

        // Opened demo
        final DocumentData doc = examplesPane != null ? examplesPane.getSelectedDocument () : null;
        if ( doc != null )
        {
            title.append ( " - " ).append ( LM.get ( doc.getTitle () ) );
        }

        setTitle ( title.toString () );
    }

    /**
     * Opens specified example in content pane.
     *
     * @param example example to open
     */
    public void open ( final Example example )
    {
        examplesPane.openDocument ( ExampleData.forExample ( example ) );
    }

    /**
     * Displays demo application.
     */
    public void display ()
    {
        setVisible ( true );
        examplesFrame.requestFocusInWindow ();
    }

    /**
     * Properly launches demo application.
     *
     * @param args launch arguments
     */
    public static void main ( final String[] args )
    {
        CoreSwingUtils.enableEventQueueLogging ();
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                // Configuring settings location
                SettingsManager.setDefaultSettingsDirName ( ".weblaf-demo" );
                SettingsManager.setDefaultSettingsGroup ( "WebLookAndFeelDemo" );
                SettingsManager.setSaveOnChange ( true );

                // Adding demo data aliases before styles using it are read
                XmlUtils.processAnnotations ( FeatureStateBackground.class );

                // Installing Look and Feel
                WebLookAndFeel.setForceSingleEventsThread ( true );
                WebLookAndFeel.install ();

                // Saving skins for reference
                skins = CollectionUtils.asList ( StyleManager.getSkin (), new DarkSkin () );

                // Adding demo application skin extensions
                // They contain all custom styles demo application uses
                StyleManager.addExtensions ( new AdaptiveExtension (), new LightSkinExtension (), new DarkSkinExtension () );

                // Configurting languages
                LanguageManager.addDictionary ( new Dictionary ( DemoApplication.class, "language/demo-language.xml" ) );
                LanguageManager.addLanguageListener ( new LanguageLocaleUpdater () );

                // Initializing demo application managers
                ExamplesManager.initialize ();

                // Starting demo application
                DemoApplication.getInstance ().display ();
            }
        } );
    }
}