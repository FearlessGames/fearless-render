package se.fearlessgames.fear.collada.utils;

import se.fearlessgames.fear.collada.data.DataCache;

import java.util.logging.Logger;

public class ColladaMeshUtils {
	private static final Logger logger = Logger.getLogger(ColladaMeshUtils.class.getName());
	private final DataCache _dataCache;
	private final ColladaDOMUtil _colladaDOMUtil;

	public ColladaMeshUtils(final DataCache dataCache,
							final ColladaDOMUtil colladaDOMUtil) {
		_dataCache = dataCache;
		_colladaDOMUtil = colladaDOMUtil;

	}

}
