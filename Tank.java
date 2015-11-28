package tank;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Tank {
	private int speed = 10;

	// current position on BF
	private int X;
	private int Y;
	private Direction direction;
	private Bullet bullet;

	private ActionField af;
	private BattleField bf;
	private TankColor color;
	private int crew;
	private int maxSpeed;
	private boolean COLORDED_MODE = false;

	public TankColor getColor() {
		return color;
	}

	public int getCrew() {
		return crew;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public Tank(ActionField af, BattleField bf) {
		this(af, bf, 128, 250, Direction.UP);
	}

	public Tank(ActionField af, BattleField bf, int x, int y,
			Direction direction) {
		this.af = af;
		this.bf = bf;
		this.X = X;
		this.Y = Y;
		this.direction = direction;
	}

	public Tank(TankColor color, int maxSpeed, int crew) {
		this.color = color;
		this.crew = crew;
		this.maxSpeed = maxSpeed;
	}

	public void turn(Direction direction) throws Exception {
		this.direction = direction;
		af.processTurn(this);
	}

	public void move() throws Exception {
		af.processMove(this);
	}

	public void destroy() {
		X = -100;
		Y = -100;
	}

	public void fire() throws Exception {
		Bullet bullet = new Bullet(X + 25, Y + 25, getDirection());
		af.processFire(bullet);
	}

	public void moveToQuadrant(int v, int h) throws Exception {
		int x = Integer.valueOf(af.getQuadrantXY(v, h).substring(0,
				af.getQuadrantXY(v, h).indexOf("_")));
		int y = Integer.valueOf(af.getQuadrantXY(v, h).substring(
				af.getQuadrantXY(v, h).indexOf("_") + 1,
				af.getQuadrantXY(v, h).length()));
		int i = 0;
		int tankX0 = x;
		int tankY0 = y;

		if (tankY0 < y) {
			while (i < (y - tankY0) / 64) {
				moveAndFire(Direction.DOWN);
			}
		} else if (tankY0 > y) {
			while (i < (tankY0 - y) / 64) {
				moveAndFire(Direction.UP);
			}
		}

		if (tankX0 < x) {
			while (i < (x - tankX0) / 64) {
				moveAndFire(Direction.RIGHT);
			}
		} else if (tankX0 > x) {
			while (i < (tankX0 - x) / 64) {
				moveAndFire(Direction.LEFT);
			}
		}
	}

	void moveAndFire(Direction direction) throws Exception {
		int i = 0;
		move();
		// moveRandom();
		i++;
		fire();
	}

	public void moveRandom() throws Exception {
		int rnd = (int) (System.currentTimeMillis() / 100) % 5;
		while (true) {
			if (rnd == 1) {
				turn(Direction.UP);
				move();
			} else if (rnd == 2) {
				turn(Direction.DOWN);
				move();
			} else if (rnd == 3) {
				turn(Direction.LEFT);
				move();
			} else {
				turn(Direction.RIGHT);
				move();

				Thread.sleep(speed);
			}
		}
	}

	public void clean() throws Exception {
		for (int i = 1; i <= 9; i++) {
			for (int k = 1; k <= 9; k++) {
				if (!bf.equals(" ")) {
					moveToQuadrant(k, i);
				}
			}
		}
	}
	
	public void updateX(int x) {
		this.X += X;
	}

	public void updateY(int y) {
		this.Y += Y;
	}

	public Direction getDirection() {
		return direction;
	}

	public int getX() {
		return X;
	}

	public int getY() {
		return Y;
	}

	public int getSpeed() {
		return speed;
	}

}
