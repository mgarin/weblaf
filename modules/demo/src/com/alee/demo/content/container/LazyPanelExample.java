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

package com.alee.demo.content.container;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Supplier;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.WebLafWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.extended.collapsible.WebCollapsiblePane;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.TableLayout;
import com.alee.extended.lazy.*;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.icon.Icons;
import com.alee.managers.language.LM;
import com.alee.managers.style.StyleId;
import com.alee.managers.task.TaskGroup;
import com.alee.managers.task.TaskManager;
import com.alee.utils.CollectionUtils;
import com.alee.utils.ThreadUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * {@link LazyPanel} example.
 *
 * @author Mikle Garin
 */
public class LazyPanelExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "lazypanel";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "lazypanel";
    }

    @NotNull
    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @NotNull
    @Override
    public WikiPage getWikiPage ()
    {
        return new WebLafWikiPage ( "How to Use LazyPanel" );
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        // Registering new custom TaskGroup for performing our lazy load
        TaskManager.registerGroup ( new LazyDataTask () );

        // Creating example previews
        return CollectionUtils.<Preview>asList (
                new LazyPanelPreview ( "init", StyleId.lazypanel, LazyLoadTrigger.onInit, LazyLoadTrigger.onInit ),
                new LazyPanelPreview ( "mixed", StyleId.lazypanel, LazyLoadTrigger.onInit, LazyLoadTrigger.onDisplay ),
                new LazyPanelPreview ( "display", StyleId.lazypanel, LazyLoadTrigger.onDisplay, LazyLoadTrigger.onDisplay ),
                new LazyPanelPreview ( "manual", StyleId.lazypanel, LazyLoadTrigger.manual, LazyLoadTrigger.manual )
        );
    }

    /**
     * Custom {@link TaskGroup} for lazy panel loading.
     * It only uses 1 thread for tasks execution for bottleneck demonstration purposes.
     */
    protected class LazyDataTask extends TaskGroup
    {
        /**
         * {@link LazyDataTask} identifier.
         */
        public static final String ID = "LazyPanelPreview";

        /**
         * Constructs new {@link LazyDataTask}.
         */
        public LazyDataTask ()
        {
            super ( ID, 1 );
        }
    }

    /**
     * Basic {@link LazyPanel} preview.
     */
    protected class LazyPanelPreview extends AbstractStylePreview
    {
        /**
         * Data loading trigger.
         */
        @NotNull
        private final LazyLoadTrigger dataTrigger;

        /**
         * UI loading trigger.
         */
        @NotNull
        private final LazyLoadTrigger uiTrigger;

        /**
         * Constructs new style preview.
         *
         * @param id          preview identifier
         * @param styleId     preview {@link StyleId}
         * @param dataTrigger data loading trigger
         * @param uiTrigger   UI loading trigger
         */
        public LazyPanelPreview ( @NotNull final String id, @NotNull final StyleId styleId,
                                  @NotNull final LazyLoadTrigger dataTrigger, @NotNull final LazyLoadTrigger uiTrigger )
        {
            super ( LazyPanelExample.this, id, FeatureState.updated, styleId );
            this.dataTrigger = dataTrigger;
            this.uiTrigger = uiTrigger;
        }

        @NotNull
        @Override
        protected LayoutManager createPreviewLayout ()
        {
            return new FlowLayout ( FlowLayout.LEADING, 8, 8 );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            return CollectionUtils.asList ( createLazyContent (), createLazyContent () );
        }

        /**
         * Returns new {@link WebPanel} containing lazy content.
         *
         * @return new {@link WebPanel} containing lazy content
         */
        @NotNull
        private WebPanel createLazyContent ()
        {
            final LazyPanel<String, WebLabel> lazyPanel = new LazyPanel<String, WebLabel> ( getStyleId (), dataTrigger, uiTrigger )
            {
                @NotNull
                @Override
                protected String getTaskGroupId ()
                {
                    return LazyDataTask.ID;
                }

                @Nullable
                @Override
                protected JComponent createInitialComponent ()
                {
                    return new WebLabel ( getExampleLanguageKey ( "initial" ), Icons.globe, WebLabel.CENTER );
                }

                @Nullable
                @Override
                protected String loadData ( @NotNull final ProgressCallback callback )
                {
                    callback.setTotal ( 100 );
                    callback.setProgress ( 0 );
                    for ( int i = 0; i < 100; i++ )
                    {
                        ThreadUtils.sleepSafely ( 50 );
                        callback.setProgress ( i + 1 );
                    }
                    return getExampleLanguageKey ( "data" );
                }

                @NotNull
                @Override
                protected WebLabel loadUI ( @Nullable final String data )
                {
                    return new WebLabel ( StyleId.labelShadow, data, Icons.leaf, WebLabel.CENTER );
                }
            };
            lazyPanel.setPadding ( 25 );
            lazyPanel.setVisible ( false );

            final WebCollapsiblePane content = new WebCollapsiblePane ( lazyPanel, false );
            content.setLanguage ( getExampleLanguageKey ( "info" ), dataTrigger, uiTrigger );
            content.setPreferredWidth ( 200 );

            final WebStyledLabel status = new WebStyledLabel ( WebStyledLabel.CENTER );
            status.setLanguage (
                    getExampleLanguageKey ( "status" ),
                    new Supplier<String> ()
                    {
                        @Override
                        public String get ()
                        {
                            return LM.get ( getExampleLanguageKey ( "status." + lazyPanel.getDataState () ) );
                        }
                    },
                    new Supplier<String> ()
                    {
                        @Override
                        public String get ()
                        {
                            return LM.get ( getExampleLanguageKey ( "status." + lazyPanel.getUIState () ) );
                        }
                    }
            );
            lazyPanel.addLazyContentStateListener ( new LazyStateListener ()
            {
                @Override
                public void dataStateChanged ( @NotNull final LazyState oldState, @NotNull final LazyState newState )
                {
                    status.updateLanguage ();
                }

                @Override
                public void uiStateChanged ( @NotNull final LazyState oldState, @NotNull final LazyState newState )
                {
                    status.updateLanguage ();
                }
            } );

            final WebButton reload = new WebButton ( StyleId.buttonIcon, Icons.refresh, new ActionListener ()
            {
                @Override
                public void actionPerformed ( @NotNull final ActionEvent e )
                {
                    lazyPanel.reload ();
                }
            } );

            final WebPanel group = new WebPanel ( StyleId.panelTransparent, new TableLayout ( new double[][]{
                    { TableLayout.FILL, TableLayout.PREFERRED },
                    { TableLayout.PREFERRED, TableLayout.PREFERRED }
            }, 5, 5 ) );
            group.add ( status, "0,0" );
            group.add ( reload, "1,0" );
            group.add ( content, "0,1,1,1" );
            return group;
        }
    }
}