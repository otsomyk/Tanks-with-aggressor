package tank;

public enum Direction {
	STOP(0), UP(1), DOWN(2), LEFT(3), RIGHT(4);

	private int id;

	private Direction(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
