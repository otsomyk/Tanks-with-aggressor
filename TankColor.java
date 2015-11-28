package tank;

public enum TankColor {
	BLACK(0), DARK_GREEN(1), DARK_BLUE(2), DARK_RED(3), WHITE(4), BLUE(5), GREEN(6), GREY(7), RED(8), LIGHT_GREEN(9), 
	LIGHT_BLUE(10), LIGHT_RED(11), LIGHT_GREY(12), DARK_GREY(13), PURPLE(14), LIGHT_PURPLE(15);

	private int id;

	TankColor(int id) {
		this.id = id;
	}

	static TankColor getDefaultColor() {
		return BLACK;
	}
}
