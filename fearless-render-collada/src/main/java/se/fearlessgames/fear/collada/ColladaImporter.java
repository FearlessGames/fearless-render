package se.fearlessgames.fear.collada;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.input.SAXHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import se.fearlessgames.fear.collada.data.DataCache;
import se.fearlessgames.fear.collada.utils.AssetDataParser;
import se.fearlessgames.fear.collada.utils.ColladaDOMUtil;
import se.fearlessgames.fear.collada.utils.ColladaMeshUtils;
import se.fearlessgames.fear.collada.utils.SaxFactory;

import java.io.InputStream;

public class ColladaImporter {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public ColladaStorage load(InputStream inputStream) {
		ColladaStorage colladaStorage = new ColladaStorage();
		DataCache dataCache = new DataCache();
		Element collada = readCollada(inputStream, dataCache);


		ColladaDOMUtil colladaDomUtil = new ColladaDOMUtil(dataCache);
		ColladaMeshUtils colladaMeshUtils = new ColladaMeshUtils(dataCache, colladaDomUtil);

		colladaStorage.setAssetData(new AssetDataParser(collada.getChild("asset")).parse());

		return colladaStorage;
	}

	private Element readCollada(InputStream inputStream, final DataCache dataCache) {
		try {
			final SAXBuilder builder = new SAXBuilder() {
				@Override
				protected SAXHandler createContentHandler() {
					return new SAXHandler(new SaxFactory(dataCache)) {
						@Override
						public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
							// Just kill what's usually done here...
						}

					};
				}
			};

			final Document doc = builder.build(inputStream);
			return doc.getRootElement();
		} catch (final Exception e) {
			throw new RuntimeException("Unable to load collada resource from source", e);
		}
	}

}
