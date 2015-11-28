package tank;

public class T34 extends Tank {

	@Override
	public String toString() {
		return "T34 []";
	}

	public void move(Direction direction) throws Exception {
		super.move();
	}

	public T34(TankColor color, int maxSpeed, int crew) {
		super(color, maxSpeed, crew);

	}

}
