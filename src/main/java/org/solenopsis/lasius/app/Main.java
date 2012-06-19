package org.solenopsis.lasius.app;

import java.util.ArrayList;
import java.util.List;
import org.solenopsis.lasius.credentials.Credentials;
import org.solenopsis.lasius.credentials.impl.PropertiesCredentials;
import org.solenopsis.lasius.properties.impl.FilePropertiesMgr;
import org.solenopsis.lasius.sforce.soap.metadata.*;
import org.solenopsis.lasius.ws.SecurityWebSvc;
import org.solenopsis.lasius.ws.decorator.RetryLoginDecorator;
import org.solenopsis.lasius.ws.security.EnterpriseSecurityWebSvc;
import org.solenopsis.lasius.ws.security.PartnerSecurityWebSvc;
import org.solenopsis.lasius.ws.standard.MetadataWebSvc;

/**
 *
 * The purpose of this class is
 *
 * @author sfloess
 *
 */
public class Main {
    public static void emitMetadata(final String msg, final SecurityWebSvc securityWebSvc, final double apiVersion) throws Exception {
        final MetadataWebSvc metadataSvc = new MetadataWebSvc(securityWebSvc);
        
        //final AutoLoginDecorator<MetadataPortType> webService = new AutoLoginDecorator(metadataSvc);
        final RetryLoginDecorator<MetadataPortType> webService = new RetryLoginDecorator(metadataSvc);
        
        final DescribeMetadataResult describeMetadata = webService.getPort().describeMetadata(apiVersion);
        
        final List<DescribeMetadataObject> metadataObjects = describeMetadata.getMetadataObjects();  
        
        int index = 0;
        
        for (final DescribeMetadataObject dmo : metadataObjects) {
        
            if (index % 2 == 0) {
                System.out.println("**LOGGING OUT***");
                securityWebSvc.logout();
            }
            
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
                        final List<FileProperties> filePropertiesList = webService.getPort().listMetadata(metaDataQuertyList, 24);
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

            final List<FileProperties> filePropertiesList = webService.getPort().listMetadata(metaDataQuertyList, 24);
            for (final FileProperties fileProperties : filePropertiesList) {
                System.out.println ("Full name:      " + fileProperties.getFullName());
                System.out.println ("     file name: " + fileProperties.getFileName());
                System.out.println ("     type:      " + fileProperties.getType());
            };            
        
            System.out.println("\n\n");
            
//            System.out.println(PackageXml.computePackage(describeMetadata));
            
        }

    }
        
    public static void main(final String[] args) throws Exception {
        //final String env = "prod.properties";
        final String env = "test-dev.properties";
        
        Credentials credentials = new PropertiesCredentials(new FilePropertiesMgr(System.getProperty("user.home") + "/.solenopsis/credentials/" + env));
        
        double apiVersion = Double.parseDouble(credentials.getApiVersion());
        
        emitMetadata("Enterprise WSDL", new EnterpriseSecurityWebSvc(credentials), apiVersion);
        emitMetadata("Partner WSDL", new PartnerSecurityWebSvc(credentials), apiVersion);

    }
}
