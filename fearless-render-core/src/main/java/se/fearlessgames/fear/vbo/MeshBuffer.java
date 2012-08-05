package se.fearlessgames.fear.vbo;

public class MeshBuffer {
	public MeshBuffer(String name, int count, int size, int offset) {
		this.name = name;
		this.offset = offset;
		this.count = count;
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public int getCount() {
		return count;
	}

	public int getSize() {
		return size;
	}

	public int getOffset() {
		return offset;
	}

	private String name;	//= "vertex"
	private int	count;		//= 3 (num floats per vertex)
	private int size; 		//= 4 (num bytes for a float)
	private int offset;     // in bytes
}