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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
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
import com.alee.utils.collection.ImmutableList;
import com.alee.utils.jar.JarEntry;
import com.alee.utils.jar.JarStructure;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Special manager that handles examples and their resources for {@link DemoApplication}.
 *
 * @author Mikle Garin
 */
public final class ExamplesManager
{
    /**
     * Top-level example groups.
     * These will be displayed in {@link com.alee.demo.frames.examples.ExamplesTree}.
     */
    @NotNull
    private static final List<ExampleGroup> groups = new ArrayList<ExampleGroup> ();

    /**
     * Demo application JAR structure.
     * It is required to retrieve example sources and navigate through them.
     */
    @Nullable
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
     * Returns {@link DemoApplication} JAR structure.
     * In process of structure retrieval JAR might be downloaded to local machine.
     * This might be a case when application was launched through JNLP laucher.
     *
     * @return {@link DemoApplication} JAR structure
     */
    @Nullable
    private static JarStructure createJarStructure ()
    {
        JarStructure jarStructure;
        try
        {
            // Creating structure using any of classes contained inside jar
            // progress.setText ( "Creating source files structure..." );
            jarStructure = new JarStructure (
                    DemoApplication.class,
                    new ImmutableList<String> ( ".java", ".png", ".gif", ".jpg", ".txt", ".xml" ),
                    new ImmutableList<String> ( "com/alee", "licenses" )
            );

            // Applying some custom icons
            // todo Apply example icons as well?
            jarStructure.setPackageIcon ( DemoApplication.class.getPackage (), new ImageIcon ( WebLookAndFeel.getImages ().get ( 0 ) ) );
            for ( final ExampleGroup exampleGroup : getGroups () )
            {
                jarStructure.setClassIcon ( exampleGroup.getClass (), ( ImageIcon ) exampleGroup.getIcon () );
            }
        }
        catch ( final Exception e )
        {
            jarStructure = null;
            LoggerFactory.getLogger ( ExamplesManager.class ).error ( "Unable to read DemoApplication JAR structure", e );
        }
        return jarStructure;
    }

    /**
     * Returns class JAR entry.
     *
     * @param classType class type
     * @return class JAR entry
     */
    @Nullable
    public static JarEntry getClassEntry ( @NotNull final Class<?> classType )
    {
        return jarStructure != null ? jarStructure.getClassEntry ( classType ) : null;
    }

    /**
     * Returns top-level available example groups.
     *
     * @return top-level available example groups
     */
    @NotNull
    public static List<ExampleGroup> getGroups ()
    {
        return CollectionUtils.copy ( groups );
    }
}