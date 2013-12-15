package org.solenopsis.lasius.wsimport.metadata.impl;

import java.util.List;
import java.util.Properties;
import org.solenopsis.wsdls.metadata.FileProperties;
import org.solenopsis.lasius.wsimport.metadata.PropertiesFactory;

/**
 *
 * API to create properties from FileProperties
 *
 * @author sfloess
 *
 */
public class DefaultPropertiesFactory implements PropertiesFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    public Properties createProperties(final List<FileProperties> filePropertiesList) {
        final Properties retVal = new Properties();

        final StringBuilder sb = new StringBuilder();

        String name = null;

        for (final FileProperties fileProperties : filePropertiesList) {
            sb.append(fileProperties.getFullName()).append(' ');

            name = fileProperties.getType();
        }

        if (null != name && ! "".equals (name)) {
            retVal.setProperty(name, sb.toString());
        }

        return retVal;
    }
}
