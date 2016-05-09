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

package com.alee.demo.api;

import com.alee.demo.DemoApplication;
import com.alee.demo.icons.DemoIcons;
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.button.WebSwitch;
import com.alee.extended.inspector.InterfaceInspector;
import com.alee.extended.label.WebLinkLabel;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.extended.syntax.SyntaxPreset;
import com.alee.extended.syntax.WebSyntaxArea;
import com.alee.extended.tab.DefaultTabTitleComponentProvider;
import com.alee.extended.tab.DocumentData;
import com.alee.extended.tab.PaneData;
import com.alee.extended.tab.WebDocumentPane;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.grouping.GroupPane;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.managers.log.Log;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.Skin;
import com.alee.skin.dark.DarkSkin;
import com.alee.skin.web.WebSkin;
import com.alee.utils.*;
import com.alee.utils.reflection.JarEntry;
import com.alee.utils.xml.ResourceFile;
import com.alee.utils.xml.ResourceLocation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mikle Garin
 */

public abstract class AbstractExample extends AbstractExampleElement implements Example
{
    protected static final ImageIcon magnifierIcon = new ImageIcon ( AbstractExample.class.getResource ( "icons/magnifier.png" ) );
    protected static final ImageIcon enabledIcon = new ImageIcon ( AbstractExample.class.getResource ( "icons/enabled.png" ) );
    protected static final ImageIcon disabledIcon = new ImageIcon ( AbstractExample.class.getResource ( "icons/disabled.png" ) );
    protected static final ImageIcon ltrIcon = new ImageIcon ( AbstractExample.class.getResource ( "icons/ltr.png" ) );
    protected static final ImageIcon rtlIcon = new ImageIcon ( AbstractExample.class.getResource ( "icons/rtl.png" ) );

    protected static final String commentStart = "/*";
    protected static final String commentEnd = "*/\n\n";

    /**
     * We actually have to use separate {@link com.alee.skin.web.WebSkin} instance here since demo uses its own one.
     */
    protected static final ArrayList<Skin> skins =
            CollectionUtils.<Skin>asList ( new WebSkin (), new DarkSkin ()/*, new FlatWebSkin ()*/ );

    /**
     * Previews cache.
     */
    protected List<Preview> previews;

    /**
     * Preview pane.
     */
    protected WebPanel examplesPane;

    /**
     * Skin currently selected for example previews.
     */
    protected Skin selectedSkin = skins.get ( 0 );

    @Override
    public Icon getIcon ()
    {
        return loadIcon ( getId () + ".png" );
    }

    @Override
    public FeatureState getFeatureState ()
    {
        final List<Preview> previews = getPreviews ();
        final List<FeatureState> states = new ArrayList<FeatureState> ( previews.size () );
        for ( final Preview preview : previews )
        {
            states.add ( preview.getFeatureState () );
        }
        return ExampleUtils.getResultingState ( states );
    }

    @Override
    public final String getWikiAddress ()
    {
        final String name = getWikiArticleName ();
        return name != null ? "https://github.com/mgarin/weblaf/wiki/" + name.replaceAll ( " ", "-" ) : null;
    }

    /**
     * Returns wiki article name.
     *
     * @return wiki article name
     */
    public String getWikiArticleName ()
    {
        return null;
    }

    @Override
    public String getStyleCode ( final Skin skin )
    {
        final ResourceFile styleFile = getStyleFile ( skin );
        switch ( styleFile.getLocation () )
        {
            case nearClass:
            {
                final Class<Object> nearClass = ReflectUtils.getClassSafely ( styleFile.getClassName () );
                return FileUtils.readToString ( nearClass, styleFile.getSource () );
            }
            case filePath:
            {
                return FileUtils.readToString ( new File ( styleFile.getSource () ) );
            }
            case url:
            {
                return FileUtils.readToString ( NetUtils.getURL ( styleFile.getSource () ) );
            }
            default:
            {
                return "";
            }
        }
    }

    /**
     * Returns style file representing styles for this example.
     * Styling system doesn't really force you to create separate files, but default style has them for convenience.
     * Demo application uses that fact to show separate examples for each specific component.
     *
     * @param skin skin to retrieve style file for
     * @return style file representing styles for this example
     */
    protected ResourceFile getStyleFile ( final Skin skin )
    {
        final String path = "resources/" + getStyleFileName () + ".xml";
        final ResourceFile resource = new ResourceFile ( ResourceLocation.nearClass, path, skin.getClass () );
        if ( skin.getClass ().getResource ( path ) == null )
        {
            Log.get ().warn ( "Unable to find style resource: " + path + " for skin: " + skin );
        }
        return resource;
    }

    /**
     * Returns example style file name.
     *
     * @return example style file name
     */
    protected String getStyleFileName ()
    {
        return getId ();
    }

    @Override
    public String getSourceCode ()
    {
        // Reading source code
        final JarEntry entry = ExamplesManager.getClassEntry ( getClass () );
        String source = entry != null ? FileUtils.readToString ( entry.getInputStream () ) : "";

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

    @Override
    public JComponent createContent ()
    {
        // Preview area
        final JComponent preview = createPreviewArea ();

        // Settings area
        final JComponent info = createInfoArea ();

        // Main split
        final WebSplitPane split = new WebSplitPane ( WebSplitPane.HORIZONTAL_SPLIT, preview, info );
        split.setContinuousLayout ( true );
        split.setResizeWeight ( 0.5f );
        split.setDividerLocation ( 0.5f );
        return split;
    }

    /**
     * Returns preview area content.
     *
     * @return preview area content
     */
    protected JComponent createPreviewArea ()
    {
        final JComponent content = getPreviewContent ();
        final JComponent toolBar = createPreviewToolBar ();
        final JComponent infoBar = createInfoBar ();
        final JComponent contentScroll = new WebScrollPane ( StyleId.scrollpaneUndecorated, content );
        if ( infoBar != null )
        {
            return new GroupPanel ( GroupingType.fillLast, false, toolBar, infoBar, contentScroll ).setPreferredWidth ( 0 );
        }
        else
        {
            return new GroupPanel ( GroupingType.fillLast, false, toolBar, contentScroll ).setPreferredWidth ( 0 );
        }
    }

    /**
     * Returns preview content.
     *
     * @return preview content
     */
    protected JComponent getPreviewContent ()
    {
        if ( examplesPane == null )
        {
            // Creating preview components
            examplesPane = new WebPanel ( DemoStyles.previewsPanel, new VerticalFlowLayout ( 0, -32, true, false ) );
            final List<Component> components = new ArrayList<Component> ();
            final List<Preview> previews = getPreviews ();
            for ( int i = 0; i < previews.size (); i++ )
            {
                // Preview
                final Preview preview = previews.get ( i );

                // Preview component
                final JComponent previewComponent = preview.getPreview ( previews, i );
                examplesPane.add ( previewComponent );

                // Equalizing preview elements
                CollectionUtils.addAllNonNull ( components, preview.getEqualizableWidthComponent () );
            }
            SwingUtils.equalizeComponentsWidth ( Arrays.asList ( AbstractButton.TEXT_CHANGED_PROPERTY ), components );
        }
        return examplesPane;
    }

    /**
     * Returns preview toolbar.
     *
     * @return preview toolbar
     */
    protected JComponent createPreviewToolBar ()
    {
        final WebToolBar toolbar = new WebToolBar ( DemoStyles.toolBar );
        toolbar.setFloatable ( false );

        toolbar.add ( createSkinsTool () );
        toolbar.addSpacing ();
        toolbar.add ( createMagnifierTool () );

        toolbar.addToEnd ( createEnabledStateTool () );
        toolbar.addSpacingToEnd ();
        toolbar.addToEnd ( createOrientationTool () );

        return toolbar;
    }

    /**
     * Returns information bar.
     *
     * @return information bar
     */
    protected JComponent createInfoBar ()
    {
        final String wiki = getWikiAddress ();
        if ( wiki != null )
        {
            final WebPanel infoBar = new WebPanel ( DemoStyles.infoBar, new HorizontalFlowLayout ( 4, false ) );

            infoBar.add ( new WebLabel ( "demo.content.example.wiki", DemoIcons.github16 ) );

            final WebLinkLabel link = new WebLinkLabel ();
            link.setLink ( getWikiArticleName (), wiki );
            link.setIcon ( null );
            infoBar.add ( link );

            return infoBar;
        }
        return null;
    }

    /**
     * Returns skin chooser tool component.
     *
     * @return skin chooser tool component
     */
    protected JComponent createSkinsTool ()
    {
        final WebComboBox skinChooser = new WebComboBox ( skins );
        skinChooser.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final Skin skin = ( Skin ) skinChooser.getSelectedItem ();
                for ( final Preview preview : getPreviews () )
                {
                    preview.applySkin ( skin );
                }
            }
        } );
        return skinChooser;
    }

    /**
     * Returns magnifier tool component.
     *
     * @return magnifier tool component
     */
    protected JComponent createMagnifierTool ()
    {
        final WebToggleButton magnifier = new WebToggleButton ( "demo.content.preview.tool.magnifier", magnifierIcon );
        magnifier.setSelected ( DemoApplication.getInstance ().isMagnifierEnabled () );
        magnifier.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                DemoApplication.getInstance ().switchMagnifier ();
            }
        } );
        DemoApplication.getInstance ().onMagnifierSwitch ( new DemoApplication.MagnifierListener ()
        {
            @Override
            public void switched ( final boolean enabled )
            {
                magnifier.setSelected ( enabled );
            }
        } );
        return magnifier;
    }

    /**
     * Returns enabled state tool component.
     *
     * @return enabled state tool component
     */
    protected JComponent createEnabledStateTool ()
    {
        final WebSwitch enabled = new WebSwitch ( true );
        enabled.setSwitchComponents ( enabledIcon, disabledIcon );
        enabled.setToolTip ( "demo.content.preview.tool.state" );
        enabled.addActionListener ( new ActionListener ()
        {
            private boolean enabled = true;

            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                enabled = !enabled;
                for ( final Preview preview : getPreviews () )
                {
                    preview.applyEnabled ( enabled );
                }
            }
        } );
        return enabled;
    }

    /**
     * Returns orientation tool component.
     *
     * @return orientation tool component
     */
    protected JComponent createOrientationTool ()
    {
        final WebSwitch orientation = new WebSwitch ( true );
        orientation.setSwitchComponents ( ltrIcon, rtlIcon );
        orientation.setToolTip ( "demo.content.preview.tool.orientation" );
        orientation.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                SwingUtils.changeOrientation ( getPreviewContent () );
            }
        } );
        return orientation;
    }

    /**
     * Returns info area content.
     *
     * @return info area content
     */
    protected JComponent createInfoArea ()
    {
        final List<WebLabel> titles = new ArrayList<WebLabel> ();
        final WebDocumentPane settings = new WebDocumentPane ( DemoStyles.stretchedDocumentPane );
        settings.setCloseable ( false );
        settings.setDragEnabled ( false );
        settings.setTabMenuEnabled ( false );
        settings.setTabTitleComponentProvider ( new DefaultTabTitleComponentProvider ()
        {
            @Override
            protected WebLabel createTitleLabel ( final PaneData paneData, final DocumentData document, final MouseAdapter mouseAdapter )
            {
                final WebLabel title = super.createTitleLabel ( paneData, document, mouseAdapter );
                title.setHorizontalAlignment ( WebLabel.CENTER );
                titles.add ( title );
                return title;
            }
        } );
        settings.openDocument ( new DocumentData ( "source", DemoIcons.source16, "demo.content.source", createSource () ) );
        settings.openDocument ( new DocumentData ( "style", DemoIcons.style16, "demo.content.style", createStyle () ) );
        settings.openDocument ( new DocumentData ( "inspector", DemoIcons.inspector16, "demo.content.inspector", createInspector () ) );
        settings.setSelected ( 0 );
        SwingUtils.equalizeComponentsWidth ( Arrays.asList ( AbstractButton.TEXT_CHANGED_PROPERTY ), titles );
        return settings.setPreferredWidth ( 0 );
    }

    /**
     * Returns source code area content.
     *
     * @return source code area content
     */
    protected JComponent createSource ()
    {
        return createCodeArea ( getSourceCode (), SyntaxPreset.java, SyntaxPreset.viewable );
    }

    /**
     * Returns style source area content.
     *
     * @return style source area content
     */
    protected JComponent createStyle ()
    {
        final WebPanel content = new WebPanel ( StyleId.panelWhite, new BorderLayout () );

        final CardLayout viewersLayout = new CardLayout ();
        final WebPanel viewers = new WebPanel ( viewersLayout );

        // Skin style code switch buttons
        final GroupPane skinButtons = new GroupPane ( DemoStyles.skinSelectorsPanel, skins.size () );
        final StyleId buttonStyleId = DemoStyles.skinSelectorButton.at ( skinButtons );
        for ( final Skin skin : skins )
        {
            // Skin switch button
            final ActionListener action = new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    viewersLayout.show ( viewers, skin.getId () );
                }
            };
            skinButtons.add ( new WebToggleButton ( buttonStyleId, skin.getTitle (), skin.getIcon (), skin == selectedSkin, action ) );

            // Skin style code viewer
            viewers.add ( createCodeArea ( getStyleCode ( skin ), SyntaxPreset.xml, SyntaxPreset.viewable ), skin.getId () );
        }
        content.add ( skinButtons, BorderLayout.NORTH );

        // Skin style code viewers container
        content.add ( viewers, BorderLayout.CENTER );

        return content;
    }

    /**
     * Returns inspector area content.
     *
     * @return inspector area content
     */
    private Component createInspector ()
    {
        return new InterfaceInspector ( getPreviewContent () );
    }

    /**
     * Returns cached previews.
     *
     * @return cached previews
     */
    protected List<Preview> getPreviews ()
    {
        if ( previews == null )
        {
            previews = createPreviews ();
        }
        return previews;
    }

    /**
     * Returns all example previews.
     *
     * @return all example previews
     */
    protected abstract List<Preview> createPreviews ();

    /**
     * Returns source area component.
     *
     * @param source  source code
     * @param presets syntax presets
     * @return source area component
     */
    protected JComponent createCodeArea ( final String source, final SyntaxPreset... presets )
    {
        final WebSyntaxArea sourceViewer = new WebSyntaxArea ( source, presets );
        sourceViewer.applyPresets ( SyntaxPreset.base );
        sourceViewer.applyPresets ( SyntaxPreset.margin );
        sourceViewer.applyPresets ( SyntaxPreset.size );
        sourceViewer.applyPresets ( SyntaxPreset.historyLimit );
        sourceViewer.setCaretPosition ( 0 );
        return sourceViewer.createScroll ( StyleId.syntaxareaScrollUndecorated );
    }
}