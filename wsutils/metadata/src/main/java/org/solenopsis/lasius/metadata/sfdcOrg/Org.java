/*
 * Copyright (C) 2015 Scot P. Floess
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.solenopsis.lasius.metadata.sfdcOrg;

import java.util.List;

/**
 * Defines an org.
 *
 * @author Scot P. Floess
 */
public interface Org {

    /**
     * Return all metadata types for the org.
     *
     * @see <a href= "http://www.salesforce.com/us/developer/docs/api_meta/Content/meta_types_list.htm">Metadata
     * Types</a>
     *
     * @return a
     */
    List<String> getTypes();

    /**
     * Return all members for a given type.
     *
     * @see <a href= "http://www.salesforce.com/us/developer/docs/api_meta/Content/meta_types_list.htm">Metadata
     * Types</a>
     *
     * @param type is the type desired.
     *
     * @return all members of <code>type</code>.
     */
    List<String> getMembers(String type);

    /**
     * Convert the org to a package.xml.
     *
     * @return a package.xml for the org.
     */
    String asPackageXml();

    /**
     * Computes a destructiveChanges XML based on toCompare. When comparing, if something is missing from self but
     * exists in <code>toCompare</code>, it will be included.
     *
     * @param toCompare the org to compare self with.
     *
     * @return a desctructiveChanges.xml that can be applied to <code>toCompare</code>.
     */
    String asDestructiveChangesXml(Org toCompare);
}
