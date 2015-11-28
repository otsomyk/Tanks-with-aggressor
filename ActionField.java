package tank;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class ActionField extends JPanel {

	private boolean COLORDED_MODE = false;
	private BattleField battleField;
	private Tank defender;
	private Bullet bullet;
	private Tiger aggressor;
	private String str;
	private int randX;
	private int randY;

	void runTheGame() throws Exception {
		defender.fire();
		defender.fire();
		defender.fire();

	}

	private boolean checkLimits(Direction direction) {
		if ((defender.getY() == 0) || (defender.getY() >= 512)
				|| (defender.getX() == 0) || (defender.getX() >= 512)) {
			System.out.println("[illegal move] direction: " + direction
					+ " tankX: " + defender.getX() + ", tankY: "
					+ defender.getY());
			return true;
		} else {
			return false;
		}
	}

	public void processMove(Tank tank) throws Exception {
		this.defender = tank;
		int direction = tank.getDirection().getId();
		int step = 4;

		if ((direction == 1 && tank.getY() == 0)
				|| (direction == 2 && tank.getY() >= 512)
				|| (direction == 3 && tank.getX() == 0)
				|| (direction == 4 && tank.getX() >= 512)) {
			return;
		} else

			processTurn(tank);

		for (int cover = 0; cover < 64; cover += step) {
			if (direction == 1) {
				tank.updateY(-step);

			} else if (direction == 2) {
				tank.updateY(step);

			} else if (direction == 3) {
				tank.updateX(-step);

			} else if (direction == 4) {
				tank.updateX(step);

			}
			Thread.sleep(tank.getSpeed());
			repaint();
		}
		Thread.sleep(20);
	}

	public void processTurn(Tank defender) throws Exception {
		repaint();
	}

	public void processFire(Bullet bullet) throws Exception {
		int step = 4;
		int direction = defender.getDirection().getId();
		while ((bullet.getX() > 0 && bullet.getX() < 576)
				&& (bullet.getY() > 0 && bullet.getY() < 576)) {

			if (direction == 1) {
				bullet.updateY(-step);
			} else if (direction == 2) {
				bullet.updateY(step);
			} else if (direction == 3) {
				bullet.updateY(-step);
			} else {
				bullet.updateX(step);
			}
			if (processInterception() == true) {
				bullet.destroy();
				break;
			}

			repaint();
			Thread.sleep(bullet.getSpeed());
		}
	}

	private boolean processInterception() throws Exception {
		String coordOfQuadrant = getQuadrant(bullet.getX(), bullet.getY());
		int x = Integer.parseInt(coordOfQuadrant.split("_")[1]);
		int y = Integer.parseInt(coordOfQuadrant.split("_")[0]);

		if (x >= 0 && x < 9 && y >= 0 && y < 9) {
			if (!battleField.scanQuadrant(y, x).trim().isEmpty()) {
				battleField.updateQuadrant(y, x, "");
				return true;
			}
			// aggressor
			if (checkInterception(
					getQuadrant(aggressor.getX(), aggressor.getY()),
					coordOfQuadrant)) {
				if (aggressor.getArmor() == 0) {
					aggressor.destroy();
					return true;
				} else {
					aggressor.setArmor(0);
					bullet.destroy();
				}
				// defender
				if (checkInterception(
						getQuadrant(defender.getX(), defender.getY()),
						coordOfQuadrant)) {
					defender.destroy();
					return true;
				}
			}
			return false;
		}
	}

	private boolean checkInterception(String object, String quadrant) {
		int objX = Integer.parseInt(object.split("_")[1]);
		int objY = Integer.parseInt(object.split("_")[0]);

		int qudX = Integer.parseInt(quadrant.split("_")[1]);
		int qudY = Integer.parseInt(quadrant.split("_")[0]);

		if (objX >= 0 && objX < 9 && objY >= 0 && objY < 9) {
			if (objX == qudX && objY == qudY) {
				return true;
			}
		}
		return false;
	}

	public String getQuadrant(int x, int y) {
		return y / 64 + "_" + x / 64;
	}

	public String getQuadrantXY(int v, int h) {
		return (v - 1) * 64 + "_" + (h - 1) * 64;
	}

	public ActionField() throws Exception {
		battleField = new BattleField();
		defender = new Tank(this, battleField);
		bullet = new Bullet(-100, -100, Direction.STOP);

		str = battleField.getAggressorLocation();
		randX = Integer.parseInt(str.substring(0, str.indexOf("_")));
		randY = Integer.parseInt(str.substring(str.indexOf("_") + 1));

		aggressor = new Tiger(this, battleField, randX, randY, Direction.DOWN);

		JFrame frame = new JFrame("BATTLE FIELD, DAY 2");
		frame.setLocation(500, 150);
		frame.setMinimumSize(new Dimension(battleField.getBfWidth() + 8,
				battleField.getBfHeigth() + 40));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(this);
		frame.pack();
		frame.setVisible(true);

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int i = 0;
		Color cc;
		for (int v = 0; v < 9; v++) {
			for (int h = 0; h < 9; h++) {
				if (COLORDED_MODE) {
					if (i % 2 == 0) {
						cc = new Color(252, 241, 177);
					} else {
						cc = new Color(233, 243, 255);
					}
				} else {
					cc = new Color(180, 180, 180);
				}
				i++;
				g.setColor(cc);
				g.fillRect(h * 64, v * 64, 64, 64);
			}
		}

		for (int j = 0; j < battleField.getDimentionY(); j++) {
			for (int k = 0; k < battleField.getDimentionX(); k++) {
				if (battleField.scanQuadrant(j, k).equals("B")) {
					String coordinates = getQuadrantXY(j + 1, k + 1);
					int separator = coordinates.indexOf("_");
					int y = Integer.parseInt(coordinates
							.substring(0, separator));
					int x = Integer.parseInt(coordinates
							.substring(separator + 1));
					g.setColor(new Color(0, 0, 255));
					g.fillRect(x, y, 64, 64);
				}
			}
		}

		// defender

		g.setColor(new Color(255, 0, 0));
		g.fillRect(defender.getX(), defender.getY(), 64, 64);

		g.setColor(new Color(0, 255, 0));
		if (defender.getDirection().getId() == 1) {
			g.fillRect(defender.getX() + 20, defender.getY(), 24, 34);
		} else if (defender.getDirection().getId() == 2) {
			g.fillRect(defender.getX() + 20, defender.getY() + 30, 24, 34);
		} else if (defender.getDirection().getId() == 3) {
			g.fillRect(defender.getX(), defender.getY() + 20, 34, 24);
		} else {
			g.fillRect(defender.getX() + 30, defender.getY() + 20, 34, 24);
		}

		// aggressor

		g.setColor(new Color(255, 0, 0));
		g.fillRect(aggressor.getX(), aggressor.getY(), 64, 64);

		g.setColor(new Color(0, 255, 0));
		if (aggressor.getDirection().getId() == 1) {
			g.fillRect(aggressor.getX() + 20, aggressor.getY(), 24, 34);
		} else if (aggressor.getDirection().getId() == 2) {
			g.fillRect(aggressor.getX() + 20, aggressor.getY() + 30, 24, 34);
		} else if (aggressor.getDirection().getId() == 3) {
			g.fillRect(aggressor.getX(), aggressor.getY() + 20, 34, 24);
		} else {
			g.fillRect(aggressor.getX() + 30, aggressor.getY() + 20, 34, 24);
		}

		g.setColor(new Color(255, 255, 0));
		g.fillRect(bullet.getX(), bullet.getY(), 14, 14);

	}

}
