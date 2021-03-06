package com.epam.store.config;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class ConfigParser {
    private static final ConfigParser instance = new ConfigParser();
    private static final String CONFIG_FILE_NAME = "page-config.xml";
    private static final String CONFIG_SCHEMA_NAME = "page-config.xsd";
    private static final String PAGE_MAPPING_ELEMENT = "page-mapping";
    private static final String PAGE_MAPPING_PAGE_NAME_ELEMENT = "jsp-page-name";
    private static final String PAGE_MAPPING_URI_ELEMENT = "uri";
    private static final String PAGES_WITH_URI_PARAMETERS_ELEMENT = "pages-with-uri-parameters";
    private static final String PAGES_WITH_URI_PARAMETERS_PAGE_NAME_ELEMENT = "page-name";
    private static final String START_PAGE_ELEMENT = "start-page";
    private static final String EMPTY_STRING = "";

    public PageConfig readPageConfig() {
        try {
            XMLStreamReader reader = getReader();
            validate(reader);
            reader.close();
            //reopen reader because validator closed the stream
            reader = getReader();
            PageConfig pageConfig = parsePageConfig(reader);
            reader.close();
            return pageConfig;
        } catch (XMLStreamException e) {
            throw new ConfigInitializationException(e);
        }
    }

    private void validate(XMLStreamReader reader) {
        URL xsdUrl = PageConfig.class.getClassLoader().getResource(CONFIG_SCHEMA_NAME);
        if (xsdUrl != null) {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            try {
                Schema schema = factory.newSchema(xsdUrl);
                Validator validator = schema.newValidator();
                validator.validate(new StAXSource(reader));
            } catch (SAXException | IOException e) {
                throw new ConfigInitializationException("Error while validating " + CONFIG_FILE_NAME, e);
            }
        }
    }

    private XMLStreamReader getReader() throws XMLStreamException {
        InputStream xmlStream = PageConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
        return XMLInputFactory.newInstance().createXMLStreamReader(xmlStream);
    }

    private PageConfig parsePageConfig(XMLStreamReader reader) throws XMLStreamException {
        Map<String, String> uriByJspPageNameMap = new HashMap<>();
        List<String> pagesWithUriParameters = new ArrayList<>();
        String startPage = EMPTY_STRING;
        while (reader.hasNext()) {
            reader.next();
            if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {
                String elementName = reader.getLocalName();
                switch (elementName) {
                    case PAGE_MAPPING_ELEMENT:
                        parsePageMappingNode(reader, uriByJspPageNameMap);
                        break;
                    case PAGES_WITH_URI_PARAMETERS_ELEMENT:
                        pagesWithUriParameters = parsePagesWithUriParameters(reader);
                        break;
                    case START_PAGE_ELEMENT:
                        startPage = reader.getElementText();
                        break;
                }
            }
        }
        return new PageConfig(uriByJspPageNameMap, pagesWithUriParameters, startPage);
    }

    private void parsePageMappingNode(XMLStreamReader reader, Map<String, String> uriByPageNameMap) throws XMLStreamException {
        String page = null;
        String uri = null;
        while (!elementEnd(reader, PAGE_MAPPING_ELEMENT)) {
            reader.next();
            if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {
                String elementName = reader.getLocalName();
                if (elementName.equals(PAGE_MAPPING_PAGE_NAME_ELEMENT)) {
                    page = reader.getElementText();
                } else if (elementName.equals(PAGE_MAPPING_URI_ELEMENT)) {
                    uri = reader.getElementText();
                }
            }
        }
        uriByPageNameMap.put(page, uri);
    }

    private List<String> parsePagesWithUriParameters(XMLStreamReader reader) throws XMLStreamException {
        List<String> pagesWithUriParameters = new ArrayList<>();
        while (!elementEnd(reader, PAGES_WITH_URI_PARAMETERS_ELEMENT)) {
            reader.next();
            if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {
                String elementName = reader.getLocalName();
                if (elementName.equals(PAGES_WITH_URI_PARAMETERS_PAGE_NAME_ELEMENT)) {
                    pagesWithUriParameters.add(reader.getElementText());
                }
            }
        }
        return pagesWithUriParameters;
    }

    private boolean elementEnd(XMLStreamReader reader, String elementName) {
        return reader.getEventType() == XMLStreamReader.END_ELEMENT && reader.getLocalName().equals(elementName);
    }
}
