package org.sew569.softwaremigrator;

import java.net.URI;
import java.net.URISyntaxException;

public class Helper {
	public static URI getFileURI(String fileName) throws URISyntaxException {

		URI binUri = ModelLoader.class.getClassLoader().getResource(fileName).toURI();
		URI uri = null;

		if (binUri.toString().indexOf("bin") > -1) {
			uri = new URI(binUri.toString().replaceAll("bin", "src"));
		} else {
			uri = binUri;
		}

		return uri;
	}
}
