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

package com.alee.managers.settings;

import com.alee.extended.colorchooser.GradientColorData;
import com.alee.extended.colorchooser.GradientData;
import com.alee.extended.colorchooser.WebGradientColorChooser;
import com.alee.extended.date.WebDateField;
import com.alee.extended.dock.WebDockablePane;
import com.alee.extended.dock.data.AbstractDockableElement;
import com.alee.extended.dock.data.DockableContentElement;
import com.alee.extended.dock.data.DockableListContainer;
import com.alee.extended.dock.data.DockableFrameElement;
import com.alee.extended.panel.WebAccordion;
import com.alee.extended.panel.WebCollapsiblePane;
import com.alee.extended.tab.DocumentPaneState;
import com.alee.extended.tab.WebDocumentPane;
import com.alee.laf.colorchooser.HSBColor;
import com.alee.laf.tree.NodeState;
import com.alee.laf.tree.TreeState;
import com.alee.laf.tree.WebTree;
import com.alee.managers.settings.processors.*;
import com.alee.utils.XmlUtils;

/**
 * Minor additions over core SettingsManager.
 *
 * @author Mikle Garin
 */

public class WebSettingsManager
{
    /**
     * Whether SettingsManager is initialized or not.
     */
    protected static boolean initialized = false;

    /**
     * Initializes SettingsManager.
     */
    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // Ensure SettingsManager is initialized
            SettingsManager.initialize ();

            // Register additional component settings processors
            ComponentSettingsManager.registerSettingsProcessor ( WebDocumentPane.class, DocumentPaneSettingsProcessor.class );
            ComponentSettingsManager.registerSettingsProcessor ( WebTree.class, TreeSettingsProcessor.class );
            ComponentSettingsManager.registerSettingsProcessor ( WebDateField.class, DateFieldSettingsProcessor.class );
            ComponentSettingsManager.registerSettingsProcessor ( WebCollapsiblePane.class, CollapsiblePaneSettingsProcessor.class );
            ComponentSettingsManager.registerSettingsProcessor ( WebAccordion.class, AccordionSettingsProcessor.class );
            ComponentSettingsManager
                    .registerSettingsProcessor ( WebGradientColorChooser.class, GradientColorChooserSettingsProcessor.class );
            ComponentSettingsManager.registerSettingsProcessor ( WebDockablePane.class, DockablePaneSettingsProcessor.class );

            // Initializing data aliases
            XmlUtils.processAnnotations ( DocumentPaneState.class );
            XmlUtils.processAnnotations ( TreeState.class );
            XmlUtils.processAnnotations ( NodeState.class );
            XmlUtils.processAnnotations ( GradientData.class );
            XmlUtils.processAnnotations ( GradientColorData.class );
            XmlUtils.processAnnotations ( HSBColor.class );
            XmlUtils.processAnnotations ( AbstractDockableElement.class );
            XmlUtils.processAnnotations ( DockableContentElement.class );
            XmlUtils.processAnnotations ( DockableFrameElement.class );
            XmlUtils.processAnnotations ( DockableListContainer.class );
        }
    }
}