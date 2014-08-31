package org.solenopsis.lasius.wsimport.metadata.util;

import org.flossware.wsimport.service.util.WebServiceUtil;
import org.solenopsis.lasius.wsdls.metadata.DescribeMetadataObject;
import org.solenopsis.lasius.wsdls.metadata.DescribeMetadataResult;
import org.solenopsis.lasius.wsdls.metadata.MetadataService;

/**
 *
 * The package.xml utility functions.
 *
 * @author sfloess
 *
 */
public class PackageXml {

    public static void computeNamesElement(final StringBuilder sb, DescribeMetadataObject describeMetadataObject) {
        sb.append("        <name>").append(describeMetadataObject.getXmlName()).append("</name>\n");
    }

    public static void computeMembersElement(final StringBuilder sb, DescribeMetadataObject describeMetadataObject) {
        sb.append("        <members>*</members>\n");
    }

    public static void computeTypesElement(final StringBuilder sb, DescribeMetadataObject describeMetadataObject) {
        sb.append("    <types>\n");

        computeMembersElement(sb, describeMetadataObject);
        computeNamesElement(sb, describeMetadataObject);

        sb.append("    </types>\n");
    }

    public static void computePackage(final StringBuilder sb, final DescribeMetadataResult describeMetadataResult) {
        sb.append("<package xmlns=\"").append(WebServiceUtil.getQName(MetadataService.class)).append("\">\n");

        for (final DescribeMetadataObject describeMetadataObject : describeMetadataResult.getMetadataObjects()) {
            computeTypesElement(sb, describeMetadataObject);
        }

        sb.append("</package>");
    }

    public static String computePackage(final DescribeMetadataResult describeMetadataResult) {
        final StringBuilder sb = new StringBuilder();

        computePackage(sb, describeMetadataResult);

        return sb.toString();
    }
}
