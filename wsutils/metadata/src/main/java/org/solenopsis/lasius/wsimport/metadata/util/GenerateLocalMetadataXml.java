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
package org.solenopsis.lasius.wsimport.metadata.util;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.solenopsis.lasius.wsdls.metadata.DescribeMetadataObject;
import org.solenopsis.lasius.wsdls.metadata.DescribeMetadataResult;
import org.solenopsis.lasius.wsdls.metadata.MetadataPortType;

/**
 * When a new version of the metadata API is released, one can run this to generate a template to store the basic
 * metadata information related to directories.
 *
 * @author Scot P. Floess
 */
public class GenerateLocalMetadataXml {

    static class Dir implements Comparable<Dir> {

        final String dirName;
        final String suffix;
        final boolean isFolder;
        final Set<String> metadataTypeSet;

        Dir(final DescribeMetadataObject dmo) {
            this.dirName = dmo.getDirectoryName();
            this.suffix = dmo.getSuffix();
            this.isFolder = dmo.isInFolder();
            this.metadataTypeSet = new TreeSet<>();

            this.metadataTypeSet.add(dmo.getXmlName());

            if (!dmo.isInFolder()) {
                this.metadataTypeSet.addAll(dmo.getChildXmlNames());
            }
        }

        @Override
        public int compareTo(Dir t) {
            return dirName.compareTo(t.dirName);
        }

        public String getDirName() {
            return dirName;
        }

        public String getSuffix() {
            return suffix;
        }

        public boolean isFolder() {
            return isFolder;
        }

        public Set<String> getMetadataTypeSet() {
            return metadataTypeSet;
        }
    }

    static void generateMetadataXml(final StringBuilder sb, final Dir dir) {
        if (dir.getMetadataTypeSet().isEmpty()) {
            return;
        }

        sb.append('\n');

        int childIndex = 0;

        for (final String child : dir.getMetadataTypeSet()) {
            childIndex++;

            sb.append("        <metadata>\n");
            sb.append("            <type>").append(child).append("</type>\n");
            sb.append("            <wildcard></wildcard>\n");
            sb.append("            <xpath></xpath>\n");
            sb.append("            <version></version>\n");
            sb.append("        </metadata>\n");

            if (childIndex < dir.getMetadataTypeSet().size()) {
                sb.append('\n');
            }
        }

    }

    static String generateXml(final Set<Dir> dirSet) throws Exception {
        final StringBuilder sb = new StringBuilder();
        sb.append("<local>\n");

        int total = dirSet.size();
        int index = 0;

        for (final Dir outer : dirSet) {
            index++;
            sb.append("    <dir>\n");
            sb.append("        <name>").append(outer.getDirName()).append("</name>\n");
            sb.append("        <suffix>").append(outer.getSuffix()).append("</suffix>\n");
            sb.append("        <isFolder>").append(outer.isFolder()).append("</isFolder>\n");
            sb.append("        <useMetaFile></useMetaFile>\n");

            generateMetadataXml(sb, outer);

            sb.append("    </dir>\n");

            if (index < total) {
                sb.append('\n');
            }
        }

        sb.append("</local>\n");

        return sb.toString();
    }

    public static String generateXml(final List<DescribeMetadataObject> metadataObjects) throws Exception {
        final Set<Dir> dirSet = new TreeSet<>();

        for (final DescribeMetadataObject dmo : metadataObjects) {
            dirSet.add(new Dir(dmo));
        }

        return generateXml(dirSet);
    }

    public static String generateXml(final DescribeMetadataResult describeMetadata) throws Exception {
        return generateXml(describeMetadata.getMetadataObjects());
    }

    public static String generateXml(final MetadataPortType metadataPort, final String apiVersion) throws Exception {
        return generateXml(metadataPort.describeMetadata(Double.parseDouble(apiVersion)));
    }
}
