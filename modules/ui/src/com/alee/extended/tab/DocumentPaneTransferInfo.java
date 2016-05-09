package com.alee.extended.tab;

import java.io.Serializable;

/**
 * Class containing information about {@link com.alee.extended.tab.WebDocumentPane} drag operation.
 *
 * @author nsofronov
 */

public class DocumentPaneTransferInfo implements Serializable
{
    /**
     * {@link com.alee.extended.tab.WebDocumentPane} unique ID.
     */
    protected final String documentPaneId;

    /**
     * Whether or not source {@link com.alee.extended.tab.WebDocumentPane} allows drag between panes.
     */
    protected final Boolean dragBetweenPanesEnabled;

    /**
     * Constructs new drag operation information.
     *
     * @param documentPane source {@link com.alee.extended.tab.WebDocumentPane}
     */
    public DocumentPaneTransferInfo ( final WebDocumentPane documentPane )
    {
        this.documentPaneId = documentPane.getId ();
        this.dragBetweenPanesEnabled = documentPane.isDragBetweenPanesEnabled ();
    }

    /**
     * Returns {@link com.alee.extended.tab.WebDocumentPane} unique ID.
     *
     * @return {@link com.alee.extended.tab.WebDocumentPane} unique ID
     */
    public String getDocumentPaneId ()
    {
        return documentPaneId;
    }

    /**
     * Returns whether or not source {@link com.alee.extended.tab.WebDocumentPane} allows drag between panes.
     *
     * @return true if source {@link com.alee.extended.tab.WebDocumentPane} allows drag between panes, false otherwise
     */
    public Boolean getDragBetweenPanesEnabled ()
    {
        return dragBetweenPanesEnabled;
    }
}