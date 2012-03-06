package se.fearlessgames.fear.collada.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fearlessgames.fear.collada.data.DataCache;


public class ColladaMeshUtils {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private final DataCache _dataCache;
	private final ColladaDOMUtil _colladaDOMUtil;

	public ColladaMeshUtils(final DataCache dataCache,
							final ColladaDOMUtil colladaDOMUtil) {
		_dataCache = dataCache;
		_colladaDOMUtil = colladaDOMUtil;

	}

}
