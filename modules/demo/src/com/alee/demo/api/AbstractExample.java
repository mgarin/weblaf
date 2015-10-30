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
import com.alee.demo.Icons;
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.button.WebSwitch;
import com.alee.extended.inspector.InterfaceInspector;
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
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.skin.CustomSkin;
import com.alee.managers.style.skin.Skin;
import com.alee.managers.style.skin.dark.DarkWebSkin;
import com.alee.managers.style.skin.flat.FlatWebSkin;
import com.alee.managers.style.skin.web.WebSkin;
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
     * Previews cache.
     */
    protected List<Preview> previews;

    /**
     * Preview pane.
     */
    protected WebPanel examplesPane;

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
    public String getStyleCode ()
    {
        final ResourceFile styleFile = getStyleFile ();
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
     * @return style file representing styles for this example
     */
    protected ResourceFile getStyleFile ()
    {
        return new ResourceFile ( ResourceLocation.nearClass, "resources/" + getStyleFileName () + ".xml", WebSkin.class );
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
        final JComponent content = new WebScrollPane ( StyleId.scrollpaneUndecorated, getPreviewContent () );
        return new GroupPanel ( GroupingType.fillLast, false, createPreviewToolBar (), content ).setPreferredWidth ( 0 );
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
            examplesPane = new WebPanel ( DemoStyles.previewsPanel, new VerticalFlowLayout ( true, false ) );
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
     * Returns skin chooser tool component.
     *
     * @return skin chooser tool component
     */
    protected JComponent createSkinsTool ()
    {
        final ArrayList<CustomSkin> skins = CollectionUtils.asList ( new WebSkin (), new DarkWebSkin (), new FlatWebSkin () );
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
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final JComponent content = getPreviewContent ();
                SwingUtils.setEnabledRecursively ( content, !content.isEnabled () );
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
        settings.openDocument ( new DocumentData ( "style", Icons.style, "demo.content.style", createStyle () ) );
        settings.openDocument ( new DocumentData ( "source", Icons.source, "demo.content.source", createSource () ) );
        settings.openDocument ( new DocumentData ( "inspector", Icons.inspector, "demo.content.inspector", createInspector () ) );
        SwingUtils.equalizeComponentsWidth ( Arrays.asList ( AbstractButton.TEXT_CHANGED_PROPERTY ), titles );
        return settings.setPreferredWidth ( 0 );
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
     * Returns source code area content.
     *
     * @return source code area content
     */
    protected JComponent createSource ()
    {
        return createSourceArea ( getSourceCode (), SyntaxPreset.java, SyntaxPreset.viewable );
    }

    /**
     * Returns style source area content.
     *
     * @return style source area content
     */
    protected JComponent createStyle ()
    {
        return createSourceArea ( getStyleCode (), SyntaxPreset.xml );
    }

    /**
     * Returns source area component.
     *
     * @param source  source code
     * @param presets syntax presets
     * @return source area component
     */
    protected JComponent createSourceArea ( final String source, final SyntaxPreset... presets )
    {
        final WebSyntaxArea sourceViewer = new WebSyntaxArea ( source, presets );
        sourceViewer.applyPresets ( SyntaxPreset.base );
        sourceViewer.applyPresets ( SyntaxPreset.margin );
        sourceViewer.applyPresets ( SyntaxPreset.size );
        sourceViewer.applyPresets ( SyntaxPreset.historyLimit );
        sourceViewer.setCaretPosition ( 0 );
        return sourceViewer.createScroll ( StyleId.syntaxareaScrollUndecorated );
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
}