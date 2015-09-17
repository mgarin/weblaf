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
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.managers.style.StyleId;
import com.alee.utils.*;
import com.alee.utils.reflection.JarEntry;
import com.alee.utils.xml.ResourceFile;

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
    protected static final ImageIcon settingsIcon = new ImageIcon ( AbstractExample.class.getResource ( "icons/info/settings.png" ) );
    protected static final ImageIcon inspectorIcon = new ImageIcon ( AbstractExample.class.getResource ( "icons/info/inspector.png" ) );
    protected static final ImageIcon styleIcon = new ImageIcon ( AbstractExample.class.getResource ( "icons/info/style.png" ) );
    protected static final ImageIcon sourceIcon = new ImageIcon ( AbstractExample.class.getResource ( "icons/info/source.png" ) );

    private static final String commentStart = "/*";
    private static final String commentEnd = "*/\n\n";

    /**
     * Previews cache.
     */
    private List<Preview> previews;

    /**
     * Preview pane.
     */
    private WebPanel examplesPane;

    @Override
    public Icon getIcon ()
    {
        return new ImageIcon ( getClass ().getResource ( "icons/" + getId () + ".png" ) );
    }

    @Override
    public String getTitle ()
    {
        return "demo." + getGroupId () + "." + getId () + ".title";
    }

    @Override
    public String getDescription ()
    {
        return "demo." + getGroupId () + "." + getId () + ".description";
    }

    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.stable;
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
    protected abstract ResourceFile getStyleFile ();

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
        return new GroupPanel ( GroupingType.none, false, createPreviewToolBar (), getPreviewContent () ).setPreferredWidth ( 0 );
    }

    /**
     * Returns preview content.
     *
     * @return preview content
     */
    protected WebPanel getPreviewContent ()
    {
        if ( examplesPane == null )
        {
            // Creating preview components
            examplesPane = new WebPanel ( DemoStyles.examplesPane, new VerticalFlowLayout ( true, false ) );
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
                CollectionUtils.addAllNonNull ( components, preview.getEqualizableWidthComponent ( previewComponent ) );
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
        toolbar.add ( createMagnifierTool () );
        toolbar.addToEnd ( createEnabledStateTool () );
        toolbar.addSpacingToEnd ();
        toolbar.addToEnd ( createOrientationTool () );
        return toolbar;
    }

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
                // todo
                for ( final Preview preview : getPreviews () )
                {
                    // preview.
                }
            }
        } );
        return enabled;
    }

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
                // todo
                WebLookAndFeel.changeOrientation ();

                for ( final Preview preview : getPreviews () )
                {
                    // preview.
                }
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
        settings.openDocument ( new DocumentData ( "settings", settingsIcon, "demo.content.settings", createSettings () ) );
        settings.openDocument ( new DocumentData ( "inspector", inspectorIcon, "demo.content.inspector", createInspector () ) );
        settings.openDocument ( new DocumentData ( "style", styleIcon, "demo.content.style", createStyle () ) );
        settings.openDocument ( new DocumentData ( "source", sourceIcon, "demo.content.source", createSource () ) );
        SwingUtils.equalizeComponentsWidth ( Arrays.asList ( AbstractButton.TEXT_CHANGED_PROPERTY ), titles );
        return settings.setPreferredWidth ( 0 );
    }

    /**
     * Returns settings area content.
     *
     * @return settings area content
     */
    protected JComponent createSettings ()
    {
        return new WebPanel ();
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