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


public class ConfigParser {
    private static final ConfigParser instance = new ConfigParser();
    private static final String CONFIG_FILE_NAME = "page-config.xml";
    private static final String CONFIG_SCHEMA_NAME = "page-config.xsd";
    private static final String PAGE_MAPPING_ELEMENT = "page-mapping";
    private static final String PAGE_MAPPING_PAGE_NAME_ELEMENT = "page-name";
    private static final String PAGE_MAPPING_URI_ELEMENT = "uri";
    private static final String PAGES_WITH_URI_PARAMETERS_ELEMENT = "pages-with-uri-parameters";
    private static final String PAGES_WITH_URI_PARAMETERS_PAGE_NAME_ELEMENT = "page-name";
    private static final String START_PAGE_ELEMENT = "start-page";
    private static final String EMPTY_STRING = "";
    private static PageConfig pageConfig = null;

    private ConfigParser() {
    }

    public static ConfigParser getInstance() {
        return instance;
    }

    public PageConfig getPageConfig() {
        if (pageConfig != null) return pageConfig;
        //otherwise parse
        try {
            validate(getReader());
            //reopen reader because validator closed the stream
            pageConfig = parsePageConfig(getReader());
            return pageConfig;
        } catch (XMLStreamException | SAXException | IOException e) {
            throw new XmlConfigException(e);
        }
    }

    private void validate(XMLStreamReader reader) throws SAXException, IOException {
        URL xsdUrl = PageConfig.class.getClassLoader().getResource(CONFIG_SCHEMA_NAME);
        if (xsdUrl != null) {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdUrl);
            Validator validator = schema.newValidator();
            validator.validate(new StAXSource(reader));
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
        while (!elementEnds(reader, PAGE_MAPPING_ELEMENT)) {
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
        while (!elementEnds(reader, PAGES_WITH_URI_PARAMETERS_ELEMENT)) {
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

    private boolean elementEnds(XMLStreamReader reader, String elementName) {
        return reader.getEventType() == XMLStreamReader.END_ELEMENT && reader.getLocalName().equals(elementName);
    }
}
