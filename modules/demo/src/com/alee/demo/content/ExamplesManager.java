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

package com.alee.demo.content;

import com.alee.demo.DemoApplication;
import com.alee.demo.api.example.ExampleGroup;
import com.alee.demo.content.animation.AnimationGroup;
import com.alee.demo.content.button.ButtonsGroup;
import com.alee.demo.content.chooser.ChoosersGroup;
import com.alee.demo.content.container.ContainersGroup;
import com.alee.demo.content.data.DataGroup;
import com.alee.demo.content.desktoppane.DesktopPaneGroup;
import com.alee.demo.content.features.FeaturesGroup;
import com.alee.demo.content.image.ImageGroup;
import com.alee.demo.content.label.LabelsGroup;
import com.alee.demo.content.menu.MenusGroup;
import com.alee.demo.content.progress.ProgressGroup;
import com.alee.demo.content.text.TextComponentsGroup;
import com.alee.demo.content.tooltip.TooltipsGroup;
import com.alee.demo.content.window.WindowsGroup;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.CollectionUtils;
import com.alee.utils.JarUtils;
import com.alee.utils.collection.ImmutableList;
import com.alee.utils.jar.JarEntry;
import com.alee.utils.jar.JarStructure;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Special Demo Application manager that handles examples and their resources.
 *
 * @author Mikle Garin
 */

public final class ExamplesManager
{
    /**
     * Top-level example groups.
     * These will be displayed in {@link com.alee.demo.frames.examples.ExamplesTree}.
     */
    private static final List<ExampleGroup> groups = new ArrayList<ExampleGroup> ();

    /**
     * Demo application JAR structure.
     * It is required to retrieve example sources and navigate through them.
     */
    private static JarStructure jarStructure = null;

    /**
     * Manager initialization mark.
     */
    private static boolean initialized = false;

    /**
     * Initializes LanguageManager settings.
     */
    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // Initializing JAR structure
            jarStructure = createJarStructure ();

            // Initializing top-level example groups
            groups.add ( new ContainersGroup () );
            groups.add ( new LabelsGroup () );
            groups.add ( new TooltipsGroup () );
            groups.add ( new ButtonsGroup () );
            groups.add ( new MenusGroup () );
            groups.add ( new ProgressGroup () );
            groups.add ( new TextComponentsGroup () );
            groups.add ( new DataGroup () );
            groups.add ( new ChoosersGroup () );
            groups.add ( new WindowsGroup () );
            groups.add ( new DesktopPaneGroup () );
            groups.add ( new ImageGroup () );
            groups.add ( new AnimationGroup () );
            groups.add ( new FeaturesGroup () );
        }
    }

    /**
     * Returns Demo Application JAR structure.
     * In process of structure retrieval JAR might be downloaded to local machine.
     * This might be a case when application was launched through JNLP laucher.
     *
     * @return Demo Application JAR structure
     */
    private static JarStructure createJarStructure ()
    {
        // todo Some progress? Or not?
        //        // Download listener in case of remote jar-file (for e.g. demo loaded from .jnlp)
        //        final FileDownloadListener listener = new FileDownloadListener ()
        //        {
        //            private int totalSize = 0;
        //
        //            @Override
        //            public void sizeDetermined ( final int totalSize )
        //            {
        //                // Download started
        //                this.totalSize = totalSize;
        //                updateProgress ( 0 );
        //            }
        //
        //            @Override
        //            public void partDownloaded ( final int totalBytesDownloaded )
        //            {
        //                // Some part loaded
        //                updateProgress ( totalBytesDownloaded );
        //            }
        //
        //            @Override
        //            public boolean shouldStopDownload ()
        //            {
        //                return false;
        //            }
        //
        //            private void updateProgress ( final int downloaded )
        //            {
        //                // Updating progress text
        //                progress.setText ( "<html>Loading source files... <b>" +
        //                        FileUtils.getFileSizeString ( downloaded, 1 ) + "</b> of <b>" +
        //                        FileUtils.getFileSizeString ( totalSize, 1 ) + "</b> done</html>" );
        //            }
        //
        //            @Override
        //            public void fileDownloaded ( final File file )
        //            {
        //                // Updating progress text
        //                progress.setText ( "Creating source files structure..." );
        //            }
        //
        //            @Override
        //            public void fileDownloadFailed ( final Throwable e )
        //            {
        //                // Updating progress text
        //                progress.setText ( "Filed to download source files" );
        //            }
        //        };

        // Creating structure using any of classes contained inside jar
        // progress.setText ( "Creating source files structure..." );
        final List<String> extensions = new ImmutableList<String> ( ".java", ".png", ".gif", ".jpg", ".txt", ".xml" );
        final List<String> packages = new ImmutableList<String> ( "com/alee", "licenses" );
        final JarStructure jarStructure = JarUtils.getJarStructure ( ExamplesManager.class, extensions, packages );

        // Applying some custom icons
        // todo Apply example icons as well?
        jarStructure.setPackageIcon ( DemoApplication.class.getPackage (), new ImageIcon ( WebLookAndFeel.getImages ().get ( 0 ) ) );
        for ( final ExampleGroup exampleGroup : getGroups () )
        {
            jarStructure.setClassIcon ( exampleGroup.getClass (), ( ImageIcon ) exampleGroup.getIcon () );
        }

        return jarStructure;
    }

    /**
     * Returns class JAR entry.
     *
     * @param classType class type
     * @return class JAR entry
     */
    public static JarEntry getClassEntry ( final Class classType )
    {
        return jarStructure.getClassEntry ( classType );
    }

    /**
     * Returns top-level available example groups.
     *
     * @return top-level available example groups
     */
    public static List<ExampleGroup> getGroups ()
    {
        return CollectionUtils.copy ( groups );
    }
}