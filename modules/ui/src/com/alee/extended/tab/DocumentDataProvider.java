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

/**
 * This is a custom data provider used by WebDocumentPane.
 *
 * @author Mikle Garin
 */

public interface DocumentDataProvider<T extends DocumentData>
{
    /**
     * Provides document data for the specified document ID.
     *
     * @param documentId document ID to provide document data for
     * @return document data for the specified document ID
     */
    public T provide ( final String documentId );
}