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

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.groups.android.AndroidStylingGroup;
import com.alee.examples.groups.breadcrumb.BreadcrumbsGroup;
import com.alee.examples.groups.button.ButtonsGroup;
import com.alee.examples.groups.checkbox.CheckBoxesGroup;
import com.alee.examples.groups.collapsible.CollapsiblePanesGroup;
import com.alee.examples.groups.colorchooser.ColorChoosersGroup;
import com.alee.examples.groups.combobox.ComboBoxesGroup;
import com.alee.examples.groups.date.DateChoosersGroup;
import com.alee.examples.groups.desktoppane.DesktopPaneGroup;
import com.alee.examples.groups.docpane.DocumentPaneGroup;
import com.alee.examples.groups.dynamicmenu.DynamicMenuGroup;
import com.alee.examples.groups.field.FieldsGroup;
import com.alee.examples.groups.filechooser.FileChoosersGroup;
import com.alee.examples.groups.futurico.FuturicoStylingGroup;
import com.alee.examples.groups.gallery.GalleryGroup;
import com.alee.examples.groups.image.ImagesGroup;
import com.alee.examples.groups.label.LabelsGroup;
import com.alee.examples.groups.list.ListsGroup;
import com.alee.examples.groups.menubar.MenuBarsGroup;
import com.alee.examples.groups.ninepatcheditor.NinePatchEditorGroup;
import com.alee.examples.groups.notification.NotificationsGroup;
import com.alee.examples.groups.optionpane.OptionPanesGroup;
import com.alee.examples.groups.overlay.OverlayGroup;
import com.alee.examples.groups.painter.PaintersGroup;
import com.alee.examples.groups.panel.PanelsGroup;
import com.alee.examples.groups.popover.PopOverGroup;
import com.alee.examples.groups.popup.PopupsGroup;
import com.alee.examples.groups.progress.ProgressGroup;
import com.alee.examples.groups.progressbar.ProgressBarsGroup;
import com.alee.examples.groups.scrollpane.ScrollPaneGroup;
import com.alee.examples.groups.slider.SlidersGroup;
import com.alee.examples.groups.splitpane.SplitPanesGroup;
import com.alee.examples.groups.statusbar.StatusBarsGroup;
import com.alee.examples.groups.tabbedpane.TabbedPanesGroup;
import com.alee.examples.groups.table.TablesGroup;
import com.alee.examples.groups.textarea.TextAreasGroup;
import com.alee.examples.groups.toolbar.ToolbarsGroup;
import com.alee.examples.groups.tooltip.TooltipsGroup;
import com.alee.examples.groups.transition.TransitionsGroup;
import com.alee.examples.groups.tree.TreesGroup;
import com.alee.examples.groups.window.WindowsGroup;
import com.alee.extended.image.WebImage;
import com.alee.extended.layout.TableLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.WebOverlay;
import com.alee.extended.window.WebProgressDialog;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.tabbedpane.TabbedPaneStyle;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.WebCustomTooltip;
import com.alee.utils.*;
import com.alee.utils.file.FileDownloadListener;
import com.alee.utils.reflection.JarEntry;
import com.alee.utils.reflection.JarStructure;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: mgarin Date: 23.01.12 Time: 12:02
 */

public class ExamplesManager
{
    // Example manager icons
    private static final ImageIcon presentationIcon = new ImageIcon ( ExamplesManager.class.getResource ( "icons/presentation.png" ) );
    private static final ImageIcon logoIcon = new ImageIcon ( ExamplesManager.class.getResource ( "icons/logo.png" ) );
    private static final ImageIcon linkIcon = new ImageIcon ( ExamplesManager.class.getResource ( "icons/link.png" ) );

    // Loaded example groups
    private static List<ExampleGroup> exampleGroups = null;

    public synchronized static List<ExampleGroup> getExampleGroups ()
    {
        if ( exampleGroups == null )
        {
            exampleGroups = new ArrayList<ExampleGroup> ();
            exampleGroups.add ( new ButtonsGroup () );
            exampleGroups.add ( new LabelsGroup () );
            exampleGroups.add ( new TooltipsGroup () );
            exampleGroups.add ( new FieldsGroup () );
            exampleGroups.add ( new TextAreasGroup () );
            exampleGroups.add ( new ComboBoxesGroup () );
            exampleGroups.add ( new CheckBoxesGroup () );
            exampleGroups.add ( new BreadcrumbsGroup () );
            exampleGroups.add ( new DateChoosersGroup () );
            exampleGroups.add ( new FileChoosersGroup () );
            exampleGroups.add ( new ColorChoosersGroup () );
            exampleGroups.add ( new ProgressBarsGroup () );
            exampleGroups.add ( new ProgressGroup () );
            exampleGroups.add ( new SlidersGroup () );
            exampleGroups.add ( new MenuBarsGroup () );
            exampleGroups.add ( new ToolbarsGroup () );
            exampleGroups.add ( new StatusBarsGroup () );
            exampleGroups.add ( new ListsGroup () );
            exampleGroups.add ( new TablesGroup () );
            exampleGroups.add ( new TreesGroup () );
            exampleGroups.add ( new PanelsGroup () );
            //            exampleGroups.add ( new FocusTrackingGroup () );
            exampleGroups.add ( new PaintersGroup () );
            exampleGroups.add ( new OverlayGroup () );
            exampleGroups.add ( new SplitPanesGroup () );
            exampleGroups.add ( new ScrollPaneGroup () );
            exampleGroups.add ( new TabbedPanesGroup () );
            exampleGroups.add ( new DocumentPaneGroup () );
            exampleGroups.add ( new CollapsiblePanesGroup () );
            exampleGroups.add ( new DesktopPaneGroup () );
            exampleGroups.add ( new WindowsGroup () );
            exampleGroups.add ( new OptionPanesGroup () );
            exampleGroups.add ( new PopupsGroup () );
            exampleGroups.add ( new PopOverGroup () );
            exampleGroups.add ( new NotificationsGroup () );
            exampleGroups.add ( new DynamicMenuGroup () );
            exampleGroups.add ( new GalleryGroup () );
            exampleGroups.add ( new TransitionsGroup () );
            exampleGroups.add ( new ImagesGroup () );
            exampleGroups.add ( new AndroidStylingGroup () );
            exampleGroups.add ( new FuturicoStylingGroup () );
            exampleGroups.add ( new NinePatchEditorGroup () );
            //            exampleGroups.add ( new LanguageGroup () );
            //            exampleGroups.add ( new ComplexGroup () );
        }
        return exampleGroups;
    }

    public static JarStructure createJarStructure ( final WebProgressDialog progress )
    {
        // Download listener in case of remote jar-file (for e.g. demo loaded from .jnlp)
        final FileDownloadListener listener = new FileDownloadListener ()
        {
            private int totalSize = 0;

            @Override
            public void sizeDetermined ( final int totalSize )
            {
                // Download started
                this.totalSize = totalSize;
                updateProgress ( 0 );
            }

            @Override
            public void partDownloaded ( final int totalBytesDownloaded )
            {
                // Some part loaded
                updateProgress ( totalBytesDownloaded );
            }

            @Override
            public boolean shouldStopDownload ()
            {
                return false;
            }

            private void updateProgress ( final int downloaded )
            {
                // Updating progress text
                progress.setText ( "<html>Loading source files... <b>" +
                        FileUtils.getFileSizeString ( downloaded, 1 ) + "</b> of <b>" +
                        FileUtils.getFileSizeString ( totalSize, 1 ) + "</b> done</html>" );
            }

            @Override
            public void fileDownloaded ( final File file )
            {
                // Updating progress text
                progress.setText ( "Creating source files structure..." );
            }

            @Override
            public void fileDownloadFailed ( final Throwable e )
            {
                // Updating progress text
                progress.setText ( "Filed to download source files" );
            }
        };

        // Creating structure using any of classes contained inside jar
        progress.setText ( "Creating source files structure..." );
        final List<String> extensions = Arrays.asList ( ".java", ".png", ".gif", ".jpg", ".txt", ".xml" );
        final List<String> packages = Arrays.asList ( "com/alee", "licenses" );
        final JarStructure jarStructure = ReflectUtils.getJarStructure ( ExamplesManager.class, extensions, packages, listener );

        // Updating some of package icons
        jarStructure.setPackageIcon ( WebLookAndFeelDemo.class.getPackage (), new ImageIcon ( WebLookAndFeel.getImages ().get ( 0 ) ) );
        for ( final ExampleGroup exampleGroup : getExampleGroups () )
        {
            jarStructure.setClassIcon ( exampleGroup.getClass (), ( ImageIcon ) exampleGroup.getGroupIcon () );
        }

        return jarStructure;
    }

    public static WebTabbedPane createExampleTabs ( final WebLookAndFeelDemo owner, final WebProgressDialog load )
    {
        // All example groups
        load.setText ( "Loading groups list" );
        final List<ExampleGroup> exampleGroups = getExampleGroups ();
        load.setMinimum ( 0 );
        load.setMaximum ( exampleGroups.size () + 1 );
        load.setProgress ( 0 );

        // Example tabs
        final WebTabbedPane exampleTabs = new WebTabbedPane ();
        exampleTabs.setTabbedPaneStyle ( TabbedPaneStyle.attached );
        // exampleTabs.setTabLayoutPolicy ( WebTabbedPane.SCROLL_TAB_LAYOUT );

        // Progress component
        final IconProgress ip = ( IconProgress ) load.getMiddleComponent ();

        // Creating all examples
        int progress = 1;
        for ( final ExampleGroup group : exampleGroups )
        {
            // Updating progress state
            load.setText ( "Loading group: " + group.getGroupName () );
            load.setProgress ( progress );
            progress++;

            // Updating progress icons
            final Icon gi = group.getGroupIcon ();
            ip.addLoadedElement ( gi );

            // Adding group view to new tab
            exampleTabs.addTab ( group.getGroupName (), gi, createGroupView ( owner, group ) );

            // Applying foreground settings
            exampleTabs.setSelectedForegroundAt ( exampleTabs.getTabCount () - 1, group.getPreferredForeground () );

            // Applying specific group settings to tab
            group.modifyExampleTab ( exampleTabs.getTabCount () - 1, exampleTabs );
        }
        load.setProgress ( progress );

        return exampleTabs;
    }

    public static Component createGroupView ( final WebLookAndFeelDemo owner, final ExampleGroup group )
    {
        // Creating group view
        Component exampleView;
        final List<Example> examples = group.getGroupExamples ();
        if ( group.isSingleExample () && examples.size () == 1 )
        {
            final Example example = examples.get ( 0 );
            exampleView = example.getPreview ( owner );
        }
        else
        {
            final List<Component> preview = new ArrayList<Component> ();

            final WebPanel groupPanel = new WebPanel ()
            {
                @Override
                public void setEnabled ( final boolean enabled )
                {
                    for ( final Component previewComponent : preview )
                    {
                        SwingUtils.setEnabledRecursively ( previewComponent, enabled );
                    }
                    super.setEnabled ( enabled );
                }
            };
            groupPanel.putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, true );
            groupPanel.setOpaque ( false );
            exampleView = groupPanel;

            final int rowsAmount = examples.size () > 1 ? examples.size () * 2 - 1 : 1;
            final double[] rows = new double[ 6 + rowsAmount ];
            rows[ 0 ] = TableLayout.FILL;
            rows[ 1 ] = 20;
            rows[ 2 ] = TableLayout.PREFERRED;
            for ( int i = 3; i < rows.length - 3; i++ )
            {
                rows[ i ] = TableLayout.PREFERRED;
            }
            rows[ rows.length - 3 ] = TableLayout.PREFERRED;
            rows[ rows.length - 2 ] = 20;
            rows[ rows.length - 1 ] = TableLayout.FILL;

            final double[] columns =
                    { 20, 1f - group.getContentPartSize (), TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED,
                            TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED,
                            group.getContentPartSize (), 20 };

            final TableLayout groupLayout = new TableLayout ( new double[][]{ columns, rows } );
            groupLayout.setHGap ( 4 );
            groupLayout.setVGap ( 4 );
            groupPanel.setLayout ( groupLayout );

            groupPanel.add ( group.modifySeparator ( createVerticalSeparator () ), "2,0,2," + ( rows.length - 1 ) );
            groupPanel.add ( group.modifySeparator ( createVerticalSeparator () ), "4,0,4," + ( rows.length - 1 ) );
            groupPanel.add ( group.modifySeparator ( createVerticalSeparator () ), "6,0,6," + ( rows.length - 1 ) );
            groupPanel.add ( group.modifySeparator ( createVerticalSeparator () ), "8,0,8," + ( rows.length - 1 ) );

            groupPanel.add ( group.modifySeparator ( createHorizontalSeparator () ), "0,2," + ( columns.length - 1 ) + ",2" );
            groupPanel.add ( group.modifySeparator ( createHorizontalSeparator () ),
                    "0," + ( rows.length - 3 ) + "," + ( columns.length - 1 ) + "," +
                            ( rows.length - 3 )
            );

            int row = 3;
            for ( final Example example : examples )
            {
                // Title & description
                groupPanel.add ( createDescription ( example, group ), "1," + row );

                // Marks
                final Component mark = createMark ( owner, example );
                groupPanel.add ( mark, "3," + row );

                // Source code
                final Component source = createSourceButton ( owner, example );
                groupPanel.add ( source, "5," + row );

                // More usage examples
                final Component usage = createPresentationButton ( example );
                groupPanel.add ( usage, "7," + row );

                SwingUtils.equalizeComponentsSize ( mark, source, usage );

                // Preview
                final Component previewComponent = createPreview ( owner, example );
                groupPanel.add ( previewComponent, "9," + row );
                preview.add ( previewComponent );

                // Rows separator
                if ( row > 3 )
                {
                    groupPanel.add ( group.modifySeparator ( createHorizontalSeparator () ),
                            "0," + ( row - 1 ) + "," + ( columns.length - 1 ) + "," + ( row - 1 ), 0 );
                }

                row += 2;
            }
        }

        if ( group.isShowWatermark () )
        {
            final WebImage linkImage = new WebImage ( logoIcon );
            linkImage.setCursor ( Cursor.getPredefinedCursor ( Cursor.HAND_CURSOR ) );

            TooltipManager.setTooltip ( linkImage, linkIcon, "Library site", TooltipWay.trailing );

            linkImage.addMouseListener ( new MouseAdapter ()
            {
                @Override
                public void mousePressed ( final MouseEvent e )
                {
                    WebUtils.browseSiteSafely ( WebLookAndFeelDemo.WEBLAF_SITE );
                }
            } );

            final WebOverlay linkOverlay = new WebOverlay ( exampleView, linkImage, WebOverlay.LEADING, WebOverlay.BOTTOM );
            linkOverlay.setOverlayMargin ( 15, 15, 15, 15 );
            linkOverlay.setOpaque ( false );

            exampleView = linkOverlay;
        }

        return exampleView;
    }

    private static WebSeparator createHorizontalSeparator ()
    {
        final WebSeparator separator = new WebSeparator ( WebSeparator.HORIZONTAL );
        separator.setDrawSideLines ( false );
        return separator;
    }

    private static WebSeparator createVerticalSeparator ()
    {
        final WebSeparator separator = new WebSeparator ( WebSeparator.VERTICAL );
        separator.setDrawSideLines ( false );
        return separator;
    }

    private static Component createDescription ( final Example example, final ExampleGroup group )
    {
        final Color foreground = group.getPreferredForeground ();

        final WebLabel titleLabel = new WebLabel ( example.getTitle (), JLabel.TRAILING );
        titleLabel.setDrawShade ( true );
        titleLabel.setForeground ( foreground );
        if ( foreground.equals ( Color.WHITE ) )
        {
            titleLabel.setShadeColor ( Color.BLACK );
        }

        if ( example.getDescription () == null )
        {
            return titleLabel;
        }
        else
        {
            final WebLabel descriptionLabel = new WebLabel ( example.getDescription (), WebLabel.TRAILING );
            descriptionLabel.setForeground ( Color.GRAY );
            SwingUtils.changeFontSize ( descriptionLabel, -1 );

            final WebPanel vertical = new WebPanel ( new VerticalFlowLayout ( VerticalFlowLayout.MIDDLE, 0, 0, true, false ) );
            vertical.setOpaque ( false );
            vertical.add ( titleLabel );
            vertical.add ( descriptionLabel );

            return vertical;
        }
    }

    private static Component createMark ( final WebLookAndFeelDemo owner, final Example example )
    {
        final FeatureState fs = example.getFeatureState ();
        final ImageIcon fsIcon = fs.getIcon ();
        final WebLabel featureState = new WebLabel ( fsIcon );
        TooltipManager.setTooltip ( featureState, fsIcon, fs.getDescription (), TooltipWay.up );
        featureState.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                owner.showLegend ( featureState, fs );
            }
        } );
        return new CenterPanel ( featureState, true, true );
    }

    private static Component createSourceButton ( final WebLookAndFeelDemo owner, final Example example )
    {
        final Class classType = example.getClass ();

        final WebButton sourceButton = WebButton.createIconWebButton ( JarEntry.javaIcon );
        TooltipManager.setTooltip ( sourceButton, JarEntry.javaIcon, ReflectUtils.getJavaClassName ( classType ), TooltipWay.up );
        sourceButton.setRolloverDecoratedOnly ( true );
        sourceButton.setFocusable ( false );

        sourceButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                owner.showSource ( classType );
            }
        } );

        return new CenterPanel ( sourceButton, false, true );
    }

    private static Component createPresentationButton ( final Example example )
    {
        final WebToggleButton presentation = new WebToggleButton ( presentationIcon );
        presentation.setRolloverDecoratedOnly ( true );
        presentation.setFocusable ( false );

        presentation.setEnabled ( example.isPresentationAvailable () );
        TooltipManager.setTooltip ( presentation, presentationIcon,
                example.isPresentationAvailable () ? "Show presentation" : "There is no presentation available for this component",
                TooltipWay.up );

        if ( presentation.isEnabled () )
        {
            presentation.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    if ( presentation.isSelected () )
                    {
                        example.startPresentation ();
                        TooltipManager.setTooltip ( presentation, presentationIcon, "Stop presentation", TooltipWay.up );
                    }
                    else
                    {
                        example.stopPresentation ();
                        TooltipManager.setTooltip ( presentation, presentationIcon, "Show presentation", TooltipWay.up );
                    }
                }
            } );

            example.doWhenPresentationFinished ( new Runnable ()
            {
                @Override
                public void run ()
                {
                    presentation.setSelected ( false );
                    TooltipManager.setTooltip ( presentation, presentationIcon, "Show presentation", TooltipWay.up );

                    ThreadUtils.sleepSafely ( 250 );

                    final WebCustomTooltip end =
                            TooltipManager.showOneTimeTooltip ( presentation, null, "Presentation has ended", TooltipWay.up );
                    WebTimer.delay ( 1500, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            end.closeTooltip ();
                        }
                    } );
                }
            } );
        }

        return new CenterPanel ( presentation, false, true );
    }

    private static Component createPreview ( final WebLookAndFeelDemo owner, final Example example )
    {
        final WebPanel previewPanel = new WebPanel ();
        previewPanel.setOpaque ( false );
        previewPanel.setLayout ( new TableLayout ( new double[][]{ { example.isFillWidth () ? TableLayout.FILL : TableLayout.PREFERRED },
                { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL } } ) );

        previewPanel.add ( example.getPreview ( owner ), "0,1" );

        return previewPanel;
    }

    public static String createSmallString ()
    {
        return FileUtils.readToString ( ExamplesManager.class, "resources/small.txt" );
    }

    public static String createLongString ()
    {
        return FileUtils.readToString ( ExamplesManager.class, "resources/long.txt" );
    }
}
