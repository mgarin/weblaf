package com.alee.extended.tab;

import java.io.Serializable;

/**
 * Class containing information about {@link WebDocumentPane} drag operation.
 *
 * @author nsofronov
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see WebDocumentPane
 */
public class DocumentPaneTransferInfo implements Serializable
{
    /**
     * {@link WebDocumentPane} unique ID.
     */
    protected final String documentPaneId;

    /**
     * Whether or not source {@link WebDocumentPane} allows drag between panes.
     */
    protected final Boolean dragBetweenPanesEnabled;

    /**
     * Constructs new drag operation information.
     *
     * @param documentPane source {@link WebDocumentPane}
     */
    public DocumentPaneTransferInfo ( final WebDocumentPane documentPane )
    {
        this.documentPaneId = documentPane.getId ();
        this.dragBetweenPanesEnabled = documentPane.isDragBetweenPanesEnabled ();
    }

    /**
     * Returns {@link WebDocumentPane} unique identifier.
     *
     * @return {@link WebDocumentPane} unique identifier
     */
    public String getDocumentPaneId ()
    {
        return documentPaneId;
    }

    /**
     * Returns whether or not source {@link WebDocumentPane} allows drag between panes.
     *
     * @return true if source {@link WebDocumentPane} allows drag between panes, false otherwise
     */
    public Boolean getDragBetweenPanesEnabled ()
    {
        return dragBetweenPanesEnabled;
    }
}