package org.sew569.softwaremigrator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.collect.ArrayListMultimap;

public class ConfigHandler {
	private String configFile;

	public ConfigHandler(String configFile) {
		this.configFile = configFile;
	}

	// note that a library entry will only exist if it has been previously chosen
	public Map<String, Integer> getLibrariesAndCount(String partName) {
		Map<String, Integer> results = new HashMap<String, Integer>();
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(configFile);
			Node partNode = getPartNode(partName, doc);
			if (partNode instanceof Element) {
				Element partElement = (Element) partNode;
				NodeList libNodes = partElement.getElementsByTagName("library");
				for (int i = 0; i < libNodes.getLength(); i++) {
					Node l = libNodes.item(i);
					results.put(l.getAttributes().getNamedItem("name").getNodeValue(),
							Integer.parseInt(l.getAttributes().getNamedItem("count").getNodeValue()));
				}
			}
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return results;
	}

	public void updateConfigFile(ArrayListMultimap<String, String> partToLibs) throws Exception {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(configFile);

			for (String part : partToLibs.keySet()) {
				Node partNode = getPartNode(part, doc);
				for (String lib : partToLibs.get(part)) {

					boolean updated = false;
					if (partNode instanceof Element) {
						Element partElement = (Element) partNode;
						NodeList libNodes = partElement.getElementsByTagName("library");
						// If lib entry exists, update count
						for (int i = 0; i < libNodes.getLength(); i++) {
							if (libNodes.item(i).getAttributes().getNamedItem("name").getNodeValue().toLowerCase()
									.equals(lib.toLowerCase())) {
								int count = Integer.parseInt(
										libNodes.item(i).getAttributes().getNamedItem("count").getNodeValue());
								libNodes.item(i).getAttributes().getNamedItem("count")
										.setNodeValue(Integer.toString(count + 1));
								updated = true;
							}
						}
					}

					// Otherwise add new child node
					if (!updated) {
						Element newLib = doc.createElement("library");
						newLib.setAttribute("name", lib);
						newLib.setAttribute("count", "1");
						partNode.appendChild(newLib);
					}
				}
			}

			// Write content

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(configFile));
			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			/*
			 * } catch (TransformerException e) { // TODO Auto-generated catch
			 * block e.printStackTrace();
			 */
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Node getPartNode(String partName, Document doc) {
		NodeList parts = doc.getElementsByTagName("part");
		for (int i = 0; i < parts.getLength(); i++) {
			String partTitle = parts.item(i).getAttributes().getNamedItem("title").getNodeValue();
			if (partName.toLowerCase().contains(partTitle.toLowerCase())) {
				return parts.item(i);
			}
		}
		return null;
	}
}
