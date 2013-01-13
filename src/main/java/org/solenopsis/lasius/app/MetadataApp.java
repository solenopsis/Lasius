package org.solenopsis.lasius.app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.LogManager;
import javax.xml.namespace.QName;
import org.flossware.util.properties.impl.FilePropertiesMgr;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.credentials.impl.PropertiesCredentials;
import org.solenopsis.lasius.sforce.wsimport.metadata.*;
import org.solenopsis.lasius.wsimport.security.SecurityMgr;
import org.solenopsis.lasius.wsimport.security.impl.EnterpriseSecurityMgr;
import org.solenopsis.lasius.wsimport.security.impl.PartnerSecurityMgr;
import org.solenopsis.lasius.wsimport.session.mgr.impl.SingleSessionMgr;
import org.solenopsis.lasius.wsimport.util.PackageXml;
import org.solenopsis.lasius.wsimport.util.SalesforceWebServiceUtil;


/**
 *
 * The purpose of this class is
 *
 * @author sfloess
 *
 */
public class MetadataApp {
    public static void emitMetadata(final String msg, final MetadataPortType metadataPort, final String apiVersion) throws Exception {
        System.out.println(msg);

        final DescribeMetadataResult describeMetadata = metadataPort.describeMetadata(Double.parseDouble(apiVersion));

        final List<DescribeMetadataObject> metadataObjects = describeMetadata.getMetadataObjects();

        int index = 0;

        for (final DescribeMetadataObject dmo : metadataObjects) {
            index++;

            System.out.println("==============================================");

            System.out.println("Dir:       " + dmo.getDirectoryName());
            System.out.println("Suffix:    " + dmo.getSuffix());
            System.out.println("XML:       " + dmo.getXmlName());
            System.out.println("In folder: " + dmo.isInFolder());
            System.out.println("Meta file: " + dmo.isMetaFile());
            System.out.println("Children:");
            for (final String child : dmo.getChildXmlNames()) {
                System.out.println("          " + child);

                if (null != child && ! "".equals(child)) {
                    final ListMetadataQuery query = new ListMetadataQuery();
                    query.setType(child);

                    final List<ListMetadataQuery> metaDataQuertyList = new ArrayList<ListMetadataQuery>();

                    metaDataQuertyList.add(query);

                    try {
                        final List<FileProperties> filePropertiesList = metadataPort.listMetadata(metaDataQuertyList, 24);
                        for (final FileProperties fileProperties : filePropertiesList) {
                            System.out.println ("            Full name:      " + fileProperties.getFullName());
                            System.out.println ("                 file name: " + fileProperties.getFileName());
                            System.out.println ("                 type:      " + fileProperties.getType());
                        }
                    }

                    catch(final Exception e) {
                        System.out.println ("            Problem:  " + e.getMessage());
                    }
                }
            }

            final ListMetadataQuery query = new ListMetadataQuery();
            query.setType(dmo.getXmlName());
            if (null != dmo.getDirectoryName() || ! "".equals(dmo.getDirectoryName())) {
                query.setFolder(dmo.getDirectoryName());
            }

            final List<ListMetadataQuery> metaDataQuertyList = new ArrayList<ListMetadataQuery>();

            metaDataQuertyList.add(query);

            System.out.println();

            final List<FileProperties> filePropertiesList = metadataPort.listMetadata(metaDataQuertyList, 24);
            for (final FileProperties fileProperties : filePropertiesList) {
                System.out.println ("Full name:      " + fileProperties.getFullName());
                System.out.println ("     file name: " + fileProperties.getFileName());
                System.out.println ("     type:      " + fileProperties.getType());
            };

            System.out.println("\n\n");

            System.out.println(PackageXml.computePackage(describeMetadata));
        }
    }

    public static void emitMetadata(final String msg, final Credentials credentials, final SecurityMgr securityWebSvc) throws Exception {
        System.out.println();

        System.out.println(msg);

        emitMetadata("PIPELINE", SalesforceWebServiceUtil.createMetadataPort(new SingleSessionMgr(credentials, securityWebSvc)), credentials.getApiVersion());

        emitMetadata("SIMPLE", SalesforceWebServiceUtil.createMetadataPort(securityWebSvc.login(credentials)), credentials.getApiVersion());
    }

    public static void main(final String[] args) throws Exception {
        LogManager.getLogManager().readConfiguration(MetadataApp.class.getResourceAsStream("/logging.properties"));

        final String env = "test-dev.properties";

        Credentials credentials = new PropertiesCredentials(new FilePropertiesMgr(System.getProperty("user.home") + "/.solenopsis/credentials/" + env));

        emitMetadata("Partner WSDL", credentials, new PartnerSecurityMgr());
        emitMetadata("Enterprise WSDL", credentials, new EnterpriseSecurityMgr());
    }
}
