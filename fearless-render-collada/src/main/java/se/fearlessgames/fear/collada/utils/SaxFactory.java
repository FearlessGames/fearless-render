package se.fearlessgames.fear.collada.utils;

import org.jdom.*;
import se.fearlessgames.fear.collada.ColladaException;
import se.fearlessgames.fear.collada.data.DataCache;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class SaxFactory extends DefaultJDOMFactory {
	private final DataCache dataCache;
	private Element currentElement;
	private BufferType bufferType = BufferType.None;
	private int count = 0;
	private final List<String> list = new ArrayList<String>();

	public SaxFactory(final DataCache dataCache) {
		this.dataCache = dataCache;
	}

	@Override
	public Text text(final String text) {
		try {
			switch (bufferType) {
				case Float: {
					final String normalizedText = Text.normalizeString(text);
					if (normalizedText.length() == 0) {
						return new Text("");
					}
					final StringTokenizer tokenizer = new StringTokenizer(normalizedText, " ");
					final float[] floatArray = new float[count];
					for (int i = 0; i < count; i++) {
						floatArray[i] = Float.parseFloat(tokenizer.nextToken().replace(",", "."));
					}

					dataCache.getFloatArrays().put(currentElement, floatArray);

					return new Text("");
				}
				case Double: {
					final String normalizedText = Text.normalizeString(text);
					if (normalizedText.length() == 0) {
						return new Text("");
					}
					final StringTokenizer tokenizer = new StringTokenizer(normalizedText, " ");
					final double[] doubleArray = new double[count];
					for (int i = 0; i < count; i++) {
						doubleArray[i] = Double.parseDouble(tokenizer.nextToken().replace(",", "."));
					}

					dataCache.getDoubleArrays().put(currentElement, doubleArray);

					return new Text("");
				}
				case Int: {
					final String normalizedText = Text.normalizeString(text);
					if (normalizedText.length() == 0) {
						return new Text("");
					}
					final StringTokenizer tokenizer = new StringTokenizer(normalizedText, " ");
					final int[] intArray = new int[count];
					int i = 0;
					while (tokenizer.hasMoreTokens()) {
						intArray[i++] = Integer.parseInt(tokenizer.nextToken());
					}

					dataCache.getIntArrays().put(currentElement, intArray);

					return new Text("");
				}
				case P: {
					list.clear();
					final String normalizedText = Text.normalizeString(text);
					if (normalizedText.length() == 0) {
						return new Text("");
					}
					final StringTokenizer tokenizer = new StringTokenizer(normalizedText, " ");
					while (tokenizer.hasMoreTokens()) {
						list.add(tokenizer.nextToken());
					}
					final int listSize = list.size();
					final int[] intArray = new int[listSize];
					for (int i = 0; i < listSize; i++) {
						intArray[i] = Integer.parseInt(list.get(i));
					}

					dataCache.getIntArrays().put(currentElement, intArray);

					return new Text("");
				}
			}
		} catch (final NoSuchElementException e) {
			throw new ColladaException("Number of values in collada array does not match its count attribute: " + count, e);
		}
		return new Text(Text.normalizeString(text));
	}

	@Override
	public void setAttribute(final Element parent, final Attribute a) {
		if ("id".equals(a.getName())) {
			dataCache.getIdCache().put(a.getValue(), parent);
		} else if ("sid".equals(a.getName())) {
			dataCache.getSidCache().put(a.getValue(), parent);
		} else if ("count".equals(a.getName())) {
			try {
				count = a.getIntValue();
			} catch (final DataConversionException e) {
				e.printStackTrace();
			}
		}

		super.setAttribute(parent, a);
	}

	@Override
	public Element element(final String name, final Namespace namespace) {
		currentElement = super.element(name);
		handleTypes(name);
		return currentElement;
	}

	@Override
	public Element element(final String name, final String prefix, final String uri) {
		currentElement = super.element(name);
		handleTypes(name);
		return currentElement;
	}

	@Override
	public Element element(final String name, final String uri) {
		currentElement = super.element(name);
		handleTypes(name);
		return currentElement;
	}

	@Override
	public Element element(final String name) {
		currentElement = super.element(name);
		handleTypes(name);
		return currentElement;
	}

	private void handleTypes(final String name) {
		if ("float_array".equals(name)) {
			bufferType = BufferType.Float;
		} else if ("double_array".equals(name)) {
			bufferType = BufferType.Double;
		} else if ("int_array".equals(name)) {
			bufferType = BufferType.Int;
		} else if ("p".equals(name)) {
			bufferType = BufferType.P;
		} else {
			bufferType = BufferType.None;
		}
	}
}
