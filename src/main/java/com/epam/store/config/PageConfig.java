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

public class PageConfig {
    private static final PageConfig instance = new PageConfig();
    private static final String CONFIG_FILE_NAME = "page-config.xml";
    private static final String CONFIG_SCHEMA_NAME = "page-config.xsd";
    private XMLStreamReader reader;
    private HashMap<String, String> uriByPageNameMap;
    private List<String> pagesWithUriParameters;

    private PageConfig() {
        uriByPageNameMap = new HashMap<>();
        pagesWithUriParameters = new ArrayList<>();
        try {
            reader = getReader();
            validate(reader);
            //reopen reader because validator closed the stream
            reader = getReader();
            parseConfig();
        } catch (XMLStreamException | SAXException | IOException e) {
            throw new XmlConfigException(e);
        }
    }

    public static PageConfig getInstance() {
        return instance;
    }

    public HashMap<String, String> getUriByPageNameMap() {
        return uriByPageNameMap;
    }

    public List<String> getPagesWithUriParameters() {
        return pagesWithUriParameters;
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

    private void parseConfig() throws XMLStreamException {
        String elementName;
        while (reader.hasNext()) {
            reader.next();
            if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {
                elementName = reader.getLocalName();
                if (elementName.equals("page-mapping")) {
                    String page = null;
                    String uri = null;
                    while (!elementEnds(reader, "page-mapping")) {
                        reader.next();
                        if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {
                            elementName = reader.getLocalName();
                            if (elementName.equals("page-name")) {
                                page = reader.getElementText();
                            } else if (elementName.equals("uri")) {
                                uri = reader.getElementText();
                            }
                        }
                    }
                    uriByPageNameMap.put(page, uri);
                } else if (elementName.equals("pages-with-uri-parameters")) {
                    parsePagesWithUriParameters(reader);
                }
            }
        }
    }

    private void parsePagesWithUriParameters(XMLStreamReader reader) throws XMLStreamException {
        while (!elementEnds(reader, "pages-with-uri-parameters")) {
            reader.next();
            if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {
                String elementName = reader.getLocalName();
                if (elementName.equals("page-name")) {
                    pagesWithUriParameters.add(reader.getElementText());
                }
            }
        }
    }

    private boolean elementEnds(XMLStreamReader reader, String elementName) {
        return reader.getEventType() == XMLStreamReader.END_ELEMENT && reader.getLocalName().equals(elementName);
    }
}
