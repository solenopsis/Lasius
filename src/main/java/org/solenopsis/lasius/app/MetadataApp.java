package org.solenopsis.lasius.app;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;
import javax.xml.namespace.QName;
import org.flossware.util.properties.impl.FilePropertiesMgr;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.credentials.impl.PropertiesCredentials;
import org.solenopsis.lasius.sforce.wsimport.metadata.*;
import org.solenopsis.lasius.wsimport.WebServiceTypeEnum;
import org.solenopsis.lasius.wsimport.session.mgr.impl.SingleSessionMgr;
import org.solenopsis.lasius.wsimport.session.security.SecurityWebSvc;
import org.solenopsis.lasius.wsimport.session.security.impl.EnterpriseSecurityWebSvc;
import org.solenopsis.lasius.wsimport.session.security.impl.PartnerSecurityWebSvc;
import org.solenopsis.lasius.wsimport.websvc.impl.DefaultWebSvc;
import org.solenopsis.lasius.wsimport.websvc.impl.MetadataWebSvc;

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

//            System.out.println(PackageXml.computePackage(describeMetadata));
        }
    }

    public static void emitMetadata(final String msg, final Credentials credentials, final SecurityWebSvc securityWebSvc, final String apiVersion) throws Exception {
        System.out.println();

        System.out.println(msg);

        final MetadataWebSvc webSvc = new MetadataWebSvc(apiVersion);

        emitMetadata("PIPELINE", webSvc.createPort(new SingleSessionMgr(credentials, securityWebSvc)), apiVersion);
        emitMetadata("SIMPLE", webSvc.createPort(securityWebSvc.login(credentials)), apiVersion);

        final DefaultWebSvc<MetadataPortType> defaultWebSvc1 = new DefaultWebSvc<MetadataPortType> ("/wsdl/metadata.wsdl", WebServiceTypeEnum.METADATA_SERVICE, new MetadataService(), MetadataPortType.class, "24.0");
        emitMetadata("PIPELINE - Default web service - service", defaultWebSvc1.createPort(new SingleSessionMgr(credentials, securityWebSvc)), apiVersion);
        emitMetadata("SIMPLE - Default web service - service", defaultWebSvc1.createPort(securityWebSvc.login(credentials)), apiVersion);

        final DefaultWebSvc<MetadataPortType> defaultWebSvc2 = new DefaultWebSvc<MetadataPortType> ("/wsdl/metadata.wsdl", WebServiceTypeEnum.METADATA_SERVICE, new QName("http://soap.sforce.com/2006/04/metadata", "MetadataService"), MetadataPortType.class, "24.0");
        emitMetadata("PIPELINE - Default web service - QName", defaultWebSvc2.createPort(new SingleSessionMgr(credentials, securityWebSvc)), apiVersion);
        emitMetadata("SIMPLE - Default web service - QName", defaultWebSvc2.createPort(securityWebSvc.login(credentials)), apiVersion);
    }

    public static void main(final String[] args) throws Exception {
        LogManager.getLogManager().readConfiguration(MetadataApp.class.getResourceAsStream("/logging.properties"));

        final String env = "test-dev.properties";

        Credentials credentials = new PropertiesCredentials(new FilePropertiesMgr(System.getProperty("user.home") + "/.solenopsis/credentials/" + env));

        emitMetadata("Partner WSDL", credentials, new PartnerSecurityWebSvc(), credentials.getApiVersion());
        //emitMetadata("Enterprise WSDL", credentials, new EnterpriseSecurityWebSvc(), credentials.getApiVersion());
    }
}
