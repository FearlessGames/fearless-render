package se.fearlessgames.fear.collada.utils;

import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import se.fearlessgames.fear.ColorRGBA;
import se.fearlessgames.fear.collada.ColladaException;
import se.fearlessgames.fear.collada.data.DataCache;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ColladaDOMUtil {
	private final Logger logger = Logger.getLogger(ColladaDOMUtil.class.getName());

	private final DataCache dataCache;

	public ColladaDOMUtil(final DataCache dataCache) {
		this.dataCache = dataCache;
	}

	/**
	 * Find element with specific id
	 *
	 * @param baseUrl url specifying target id
	 * @return element with specific id or null if not found
	 */
	public Element findTargetWithId(final String baseUrl) {
		return dataCache.getIdCache().get(parseUrl(baseUrl));
	}

	/**
	 * Find element with specific sid
	 *
	 * @param baseUrl url specifying target sid
	 * @return element with specific id or null if not found
	 */
	public Element findTargetWithSid(final String baseUrl) {
		return dataCache.getSidCache().get(parseUrl(baseUrl));
	}

	/**
	 * Select nodes through an XPath query and return all hits as a List
	 *
	 * @param element root element to start search on
	 * @param query   XPath expression
	 * @return the list of selected items, which may be of types: {@link Element}, {@link org.jdom.Attribute}, {@link org.jdom.Text},
	 *         {@link org.jdom.CDATA}, {@link org.jdom.Comment}, {@link org.jdom.ProcessingInstruction}, Boolean, Double, or String.
	 */
	public List<?> selectNodes(final Element element, final String query) {
		final XPath xPathExpression = getXPathExpression(query);

		try {
			return xPathExpression.selectNodes(element);
		} catch (final JDOMException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	/**
	 * Select nodes through an XPath query and returns the first hit
	 *
	 * @param element root element to start search on
	 * @param query   XPath expression
	 * @return the first selected item, which may be of types: {@link Element}, {@link org.jdom.Attribute}, {@link org.jdom.Text},
	 *         {@link org.jdom.CDATA}, {@link org.jdom.Comment}, {@link org.jdom.ProcessingInstruction}, Boolean, Double, String, or
	 *         <code>null</code> if no item was selected.
	 */
	public Object selectSingleNode(final Element element, final String query) {
		final XPath xPathExpression = getXPathExpression(query);

		try {
			return xPathExpression.selectSingleNode(element);
		} catch (final JDOMException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String parseUrl(String baseUrl) {
		if (baseUrl.startsWith("#")) {
			baseUrl = baseUrl.substring(1);
		}
		return baseUrl;
	}

	/**
	 * Compiles and return an XPath expression. Expressions are cached.
	 *
	 * @param query XPath query to compile
	 * @return new XPath expression object
	 */
	private XPath getXPathExpression(final String query) {

		if (dataCache.getxPathExpressions().containsKey(query)) {
			return dataCache.getxPathExpressions().get(query);
		}

		XPath xPathExpression = null;
		try {
			xPathExpression = XPath.newInstance(query);
		} catch (final JDOMException e) {
			e.printStackTrace();
		}

		dataCache.getxPathExpressions().put(query, xPathExpression);

		return xPathExpression;
	}

	/**
	 * Parses the text under a node and returns it as a float array.
	 *
	 * @param node node to parse content from
	 * @return parsed float array
	 */
	public float[] parseFloatArray(final Element node) {
		if (dataCache.getFloatArrays().containsKey(node)) {
			return dataCache.getFloatArrays().get(node);
		}

		final String content = node.getText();

		final List<String> list = new ArrayList<String>();
		final StringTokenizer tokenizer = new StringTokenizer(content, " ");
		while (tokenizer.hasMoreTokens()) {
			list.add(tokenizer.nextToken());
		}
		final int listSize = list.size();
		final float[] floatArray = new float[listSize];
		for (int i = 0; i < listSize; i++) {
			floatArray[i] = Float.parseFloat(list.get(i).replace(",", "."));
		}

		dataCache.getFloatArrays().put(node, floatArray);

		return floatArray;
	}

	/**
	 * Parses the text under a node and returns it as a double array.
	 *
	 * @param node node to parse content from
	 * @return parsed double array
	 */
	public double[] parseDoubleArray(final Element node) {
		if (dataCache.getDoubleArrays().containsKey(node)) {
			return dataCache.getDoubleArrays().get(node);
		}

		final String content = node.getText();

		final List<String> list = new ArrayList<String>();
		final StringTokenizer tokenizer = new StringTokenizer(content, " ");
		while (tokenizer.hasMoreTokens()) {
			list.add(tokenizer.nextToken());
		}
		final int listSize = list.size();
		final double[] doubleArray = new double[listSize];
		for (int i = 0; i < listSize; i++) {
			doubleArray[i] = Double.parseDouble(list.get(i).replace(",", "."));
		}

		dataCache.getDoubleArrays().put(node, doubleArray);

		return doubleArray;
	}

	/**
	 * Parses the text under a node and returns it as an int array.
	 *
	 * @param node node to parse content from
	 * @return parsed int array
	 */
	public int[] parseIntArray(final Element node) {
		if (dataCache.getIntArrays().containsKey(node)) {
			return dataCache.getIntArrays().get(node);
		}

		final String content = node.getText();

		final List<String> list = new ArrayList<String>();
		final StringTokenizer tokenizer = new StringTokenizer(content, " ");
		while (tokenizer.hasMoreTokens()) {
			list.add(tokenizer.nextToken());
		}
		final int listSize = list.size();
		final int[] intArray = new int[listSize];
		for (int i = 0; i < listSize; i++) {
			intArray[i] = Integer.parseInt(list.get(i));
		}

		dataCache.getIntArrays().put(node, intArray);

		return intArray;
	}

	/**
	 * Parses the text under a node and returns it as a boolean array.
	 *
	 * @param node node to parse content from
	 * @return parsed boolean array
	 */
	public boolean[] parseBooleanArray(final Element node) {
		if (dataCache.getDoubleArrays().containsKey(node)) {
			return dataCache.getBooleanArrays().get(node);
		}

		final String content = node.getText();

		final List<String> list = new ArrayList<String>();
		final StringTokenizer tokenizer = new StringTokenizer(content, " ");
		while (tokenizer.hasMoreTokens()) {
			list.add(tokenizer.nextToken());
		}
		final int listSize = list.size();
		final boolean[] booleanArray = new boolean[listSize];
		for (int i = 0; i < listSize; i++) {
			booleanArray[i] = Boolean.parseBoolean(list.get(i));
		}

		dataCache.getBooleanArrays().put(node, booleanArray);

		return booleanArray;
	}

	/**
	 * Parses the text under a node and returns it as a string array.
	 *
	 * @param node node to parse content from
	 * @return parsed string array
	 */
	public String[] parseStringArray(final Element node) {
		if (dataCache.getStringArrays().containsKey(node)) {
			return dataCache.getStringArrays().get(node);
		}

		final String content = node.getText();

		final List<String> list = new ArrayList<String>();
		final StringTokenizer tokenizer = new StringTokenizer(content, " ");
		while (tokenizer.hasMoreTokens()) {
			list.add(tokenizer.nextToken());
		}
		final String[] stringArray = list.toArray(new String[list.size()]);

		dataCache.getStringArrays().put(node, stringArray);

		return stringArray;
	}

	/**
	 * Strips the namespace from all nodes in a tree.
	 *
	 * @param rootElement Root of strip operation
	 */
	@SuppressWarnings("unchecked")
	public void stripNamespace(final Element rootElement) {
		rootElement.setNamespace(null);

		final List children = rootElement.getChildren();
		final Iterator i = children.iterator();
		while (i.hasNext()) {
			final Element child = (Element) i.next();
			stripNamespace(child);
		}
	}

	/**
	 * Parse an int value in an attribute.
	 *
	 * @param input		 Element containing the attribute
	 * @param attributeName Attribute name to parse a value for
	 * @return parsed integer
	 */
	public int getAttributeIntValue(final Element input, final String attributeName, final int defaultVal) {
		final Attribute attribute = input.getAttribute(attributeName);
		if (attribute != null) {
			try {
				return attribute.getIntValue();
			} catch (final DataConversionException e) {
				logger.log(Level.WARNING, "Could not parse int value", e);
			}
		}
		return defaultVal;
	}

	/**
	 * Convert a Collada color description into an Ardor3D ColorRGBA
	 *
	 * @param colorDescription Collada color description
	 * @return Ardor3d ColorRGBA
	 */
	public ColorRGBA getColor(final String colorDescription) {
		if (colorDescription == null) {
			throw new ColladaException("Null color description not allowed", null);
		}

		final String[] values = dataCache.getPattern().split(colorDescription.replace(",", "."));

		if (values.length < 3 || values.length > 4) {
			throw new ColladaException("Expected color definition of length 3 or 4 - got " + values.length
					+ " for description: " + colorDescription, colorDescription);
		}

		try {
			return new ColorRGBA(Float.parseFloat(values[0]), Float.parseFloat(values[1]), Float.parseFloat(values[2]),
					values.length == 4 ? Float.parseFloat(values[3]) : 1.0f);
		} catch (final NumberFormatException e) {
			throw new ColladaException("Unable to parse float number", colorDescription, e);
		}
	}

	/**
	 * Find Element with semantic POSITION under an element with inputs
	 *
	 * @param v
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Element getPositionSource(final Element v) {
		for (final Element input : (List<Element>) v.getChildren("input")) {
			if ("POSITION".equals(input.getAttributeValue("semantic"))) {
				final Element n = findTargetWithId(input.getAttributeValue("source"));
				if (n != null && "source".equals(n.getName())) {
					return n;
				}
			}
		}

		// changed this to throw an exception instead - otherwise, there will just be a nullpointer exception
		// outside. This provides much more information about what went wrong / Petter
		// return null;
		throw new ColladaException("Unable to find POSITION semantic for inputs under DaeVertices", v);
	}
}
