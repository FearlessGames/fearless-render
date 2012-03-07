package se.fearlessgames.fear.collada.data;

public class Joint {
	/**
	 * Root node ID
	 */
	public static final short NO_PARENT = Short.MIN_VALUE;

	/** The inverse transform of this Joint in its bind position. */
	//private final Transform _inverseBindPose = new Transform(Transform.IDENTITY);

	/**
	 * A name, for display or debugging purposes.
	 */
	private final String _name;

	protected short _index;

	/**
	 * Index of our parent Joint, or NO_PARENT if we are the root.
	 */
	protected short _parentIndex;

	/**
	 * Construct a new Joint object using the given name.
	 *
	 * @param name the name
	 */
	public Joint(final String name) {
		_name = name;
	}


	/**
	 * @return the human-readable name of this joint.
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Set the index of this joint's parent within the containing Skeleton's joint array.
	 *
	 * @param parentIndex the index, or NO_PARENT if this Joint is root (has no parent)
	 */
	public void setParentIndex(final short parentIndex) {
		_parentIndex = parentIndex;
	}

	public short getParentIndex() {
		return _parentIndex;
	}

	public void setIndex(final short index) {
		_index = index;
	}

	public short getIndex() {
		return _index;
	}

	@Override
	public String toString() {
		return "Joint: '" + getName() + "'";
	}
}
