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

package com.alee.extended.tab;

import com.alee.api.merge.Mergeable;
import com.alee.utils.general.Pair;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.List;

/**
 * {@link WebDocumentPane} structure state holder.
 * Each {@link DocumentPaneState} represents state of a single {@link PaneData} or {@link SplitData}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see PaneData
 * @see SplitData
 * @see DocumentData
 * @see WebDocumentPane#getDocumentPaneState()
 * @see WebDocumentPane#setDocumentPaneState(DocumentPaneState)
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see DocumentPaneSettingsProcessor
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */
@XStreamAlias ( "DocumentPaneState" )
public class DocumentPaneState implements Mergeable, Cloneable, Serializable
{
    /**
     * todo 1. Split this class into three different ones: main, split, pane
     */

    /**
     * Whether or not this document pane structure level represents split.
     */
    @XStreamAsAttribute
    protected Boolean split;

    /**
     * Selected document identifier on this document pane structure level.
     * Specified only when this is not split.
     */
    @XStreamAsAttribute
    protected String selectedId;

    /**
     * Identifiers of documents opened on this document pane structure level.
     * Specified only when this is not split.
     */
    @XStreamImplicit
    protected List<String> documentIds;

    /**
     * Split orientation on this document pane structure level.
     * Specified only when this is split.
     * todo Replace with {@link com.alee.api.data.Orientation}
     */
    @XStreamAsAttribute
    protected Integer splitOrientation;

    /**
     * Split divider location on this document pane structure level.
     * We have to save only proportions, otherwise it will be incorrect in case split size changes.
     * Specified only when this is split.
     */
    @XStreamAsAttribute
    protected Double dividerLocation;

    /**
     * Split side states on this document pane structure level.
     * Specified only when this is split.
     * todo Replace with two separate fields
     */
    protected Pair<DocumentPaneState, DocumentPaneState> splitState;

    /**
     * Constructs default {@link DocumentPaneState}.
     */
    public DocumentPaneState ()
    {
        this.split = false;
    }

    /**
     * Constructs new {@link DocumentPaneState} for {@link PaneData}.
     *
     * @param paneData {@link PaneData} to retrieve settings from
     */
    public DocumentPaneState ( final PaneData paneData )
    {
        this.split = false;
        final DocumentData selected = paneData.getSelected ();
        this.selectedId = selected != null ? selected.getId () : null;
        this.documentIds = paneData.getDocumentIds ();
    }

    /**
     * Constructs new {@link DocumentPaneState} for {@link SplitData}.
     *
     * @param splitData {@link SplitData} to retrieve settings from
     */
    public DocumentPaneState ( final SplitData splitData )
    {
        this.split = true;
        this.splitOrientation = splitData.getOrientation ();
        this.dividerLocation = splitData.getDividerLocation ();
        final StructureData firstData = splitData.getFirst ();
        final DocumentPaneState firstState = firstData != null ? firstData.getDocumentPaneState () : null;
        final StructureData lastData = splitData.getLast ();
        final DocumentPaneState lastState = lastData != null ? lastData.getDocumentPaneState () : null;
        this.splitState = new Pair<DocumentPaneState, DocumentPaneState> ( firstState, lastState );
    }

    /**
     * Returns whether or not this document pane structure level represents split.
     *
     * @return {@code true} if this document pane structure level represents split, {@code false} otherwise
     */
    public Boolean isSplit ()
    {
        return split != null && split;
    }

    /**
     * Sets whether or not this document pane structure level represents split.
     *
     * @param split whether or not this document pane structure level represents split
     */
    public void setSplit ( final Boolean split )
    {
        this.split = split;
    }

    /**
     * Returns selected document identifier on this document pane structure level.
     *
     * @return selected document identifier on this document pane structure level
     */
    public String getSelectedId ()
    {
        return selectedId;
    }

    /**
     * Sets selected document identifier on this document pane structure level.
     *
     * @param selectedId selected document identifier on this document pane structure level
     */
    public void setSelectedId ( final String selectedId )
    {
        this.selectedId = selectedId;
    }

    /**
     * Returns identifiers of documents opened on this document pane structure level.
     *
     * @return identifiers of documents opened on this document pane structure level
     */
    public List<String> getDocumentIds ()
    {
        return documentIds;
    }

    /**
     * Sets identifiers of documents opened on this document pane structure level.
     *
     * @param documentIds identifiers of documents opened on this document pane structure level
     */
    public void setDocumentIds ( final List<String> documentIds )
    {
        this.documentIds = documentIds;
    }

    /**
     * Returns split orientation on this document pane structure level.
     *
     * @return split orientation on this document pane structure level
     */
    public Integer getSplitOrientation ()
    {
        return splitOrientation;
    }

    /**
     * Sets split orientation on this document pane structure level.
     *
     * @param orientation split orientation on this document pane structure level
     */
    public void setSplitOrientation ( final Integer orientation )
    {
        this.splitOrientation = orientation;
    }

    /**
     * Returns split divider location on this document pane structure level.
     *
     * @return split divider location on this document pane structure level
     */
    public Double getDividerLocation ()
    {
        return dividerLocation;
    }

    /**
     * Sets split divider location on this document pane structure level.
     *
     * @param location split divider location on this document pane structure level
     */
    public void setDividerLocation ( final Double location )
    {
        this.dividerLocation = location;
    }

    /**
     * Returns split side states on this document pane structure level.
     *
     * @return split side states on this document pane structure level
     */
    public Pair<DocumentPaneState, DocumentPaneState> getSplitState ()
    {
        return splitState;
    }

    /**
     * Sets split side states on this document pane structure level.
     *
     * @param splitState split side states on this document pane structure level
     */
    public void setSplitState ( final Pair<DocumentPaneState, DocumentPaneState> splitState )
    {
        this.splitState = splitState;
    }
}