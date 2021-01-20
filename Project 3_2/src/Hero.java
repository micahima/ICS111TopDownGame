import java.awt.event.KeyEvent;
import java.util.*;

//Micah Imakyure

public class Hero{

	private EZImage heroPicture;
	private int x;
	private int y;
	public static boolean attack;
	int HP;
	Timer timer;
	public EZSound slashSound = EZ.addSound("slashsound.wav");

	public Hero(String filename, int startX, int startY){ //constructor
		x = startX; y = startY;
		heroPicture = EZ.addImage(filename, x, y);
		attack = false;
	}
	
	//controls players movements
	public void controller(EZImage image, int speed, int angle, EZImage swing) { 
		int forwardSpeed = speed;
		if (EZInteraction.isKeyDown(KeyEvent.VK_D)) {
			heroPicture.turnRight(angle);
			swing.turnRight(angle);
			if (EZInteraction.wasKeyReleased(KeyEvent.VK_SPACE)) {
				attack = true;
				System.out.println("ATTACK!");
				if(!slashSound.isPlaying()) {
					slashSound.play();
				}
				
			}
		} else if (EZInteraction.isKeyDown(KeyEvent.VK_A)) { 
			heroPicture.turnLeft(angle);
			swing.turnLeft(angle);
			if (EZInteraction.wasKeyReleased(KeyEvent.VK_SPACE)) {
				attack = true;
				System.out.println("ATTACK!");
				if(!slashSound.isPlaying()) {
					slashSound.play();
				}
			}
		} else if (EZInteraction.isKeyDown(KeyEvent.VK_W)) {
			heroPicture.moveForward(forwardSpeed);
			setHeroPos(heroPicture.getXCenter(),heroPicture.getYCenter());
			if (EZInteraction.wasKeyReleased(KeyEvent.VK_SPACE)) {
				attack = true;
				System.out.println("ATTACK!");
				if(!slashSound.isPlaying()) {
					slashSound.play();
				}
			}
		} else if (EZInteraction.isKeyDown(KeyEvent.VK_S)) {
			heroPicture.moveForward(-forwardSpeed);
			setHeroPos(heroPicture.getXCenter(),heroPicture.getYCenter());
			if (EZInteraction.wasKeyReleased(KeyEvent.VK_SPACE)) {
				attack = true;
				System.out.println("ATTACK!");
				if(!slashSound.isPlaying()) {
					slashSound.play();
				}
			}
		}
		if (EZInteraction.wasKeyReleased(KeyEvent.VK_SPACE)) {
			attack = true;
			System.out.println("ATTACK!"); //system check
			if(!slashSound.isPlaying()) {
				slashSound.play();
			}
		}
		
	}
	
	//Used to correct image/object displacement
	public void hide() {
		heroPicture.hide();
	}

	public void show() {
		heroPicture.show();
	}

	public void remove() {
		hide();
		setPos(2500,1800);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public EZImage getImage() {
		return heroPicture;
	}

	public void scaleBy(double s) {
		heroPicture.scaleBy(s);
	}

	public void setPos(int posX, int posY) {
		heroPicture.translateTo(posX, posY);
		x = posX; y = posY;
	}

	public void setHeroPos(int posX, int posY) {
		x = posX; y = posY;
	}

	public void pullToFront() {
		heroPicture.pullToFront();
	}

	 //used to pick players character
	public static int playernum(EZImage a, EZImage b, int head) {
		int clickX, clickY;
		if (EZInteraction.wasMouseLeftButtonReleased()) {
			clickX = EZInteraction.getXMouse();
			clickY = EZInteraction.getYMouse();
			if(a.isPointInElement(clickX, clickY)) {
				head--;
				if(head<0) {
					head = 1;
				}
			} else if(b.isPointInElement(clickX, clickY)) {
				head++;
				if(head>1) {
					head = 0;
				}
			}
		}
		if (EZInteraction.wasKeyPressed(KeyEvent.VK_LEFT)) {
			head--;
			if(head<0) {
				head = 1;
			}
		}
		if (EZInteraction.wasKeyPressed(KeyEvent.VK_RIGHT)) {
			head++;
			if(head>1) {
				head = 0;
			}
		}
		EZ.refreshScreen();
		return head;
	}

	 //creates a boundary for players movements.
	public void boundary(int boundXH, int boundYH) {
		if (heroPicture.getXCenter() > boundXH) {
			heroPicture.translateTo(0, getY());
		}else if (heroPicture.getXCenter() < 0) {
			heroPicture.translateTo(boundXH, getY());
		}
		if (heroPicture.getYCenter() > boundYH) {
			heroPicture.translateTo(getX(), 0);
		}else if (heroPicture.getYCenter() < 0) {
			heroPicture.translateTo(getX(), boundYH);
		}
	}

	//attack variable
	public boolean attack() {
		return attack;
	}

	 //control health of player
	public int health(Enemy enemy,int health, EZImage[] bar) {
		HP = health;
		if (!attack) {
			if(((enemy.getX() > heroPicture.getXCenter() - 40) && (enemy.getX() < heroPicture.getXCenter() + 40)
					&& (enemy.getY() > heroPicture.getYCenter() - 40) && (enemy.getY() < heroPicture.getYCenter() + 40))&&HP>0) {
				heroPicture.moveForward(-100);
				setHeroPos(heroPicture.getXCenter(),heroPicture.getYCenter());
				HP--;
			}
		}

		switch (HP) {
		case 10:
			bar[0].show();
			break;
		case 9:
			bar[0].hide();
			bar[1].show();
			break;
		case 8:
			bar[0].hide();
			bar[1].hide();
			bar[2].show();
			break;
		case 7:
			for (int i=0; i<3; i++) {
				bar[i].hide();
			}
			bar[3].show();
			break;
		case 6:
			for (int i=0; i<4; i++) {
				bar[i].hide();
			}
			bar[4].show();
			break;
		case 5:
			for (int i=0; i<5; i++) {
				bar[i].hide();
			}
			bar[5].show();
			break;
		case 4:
			for (int i=0; i<6; i++) {
				bar[i].hide();
			}
			bar[6].show();
			break;
		case 3:
			for (int i=0; i<7; i++) {
				bar[i].hide();
			}
			bar[7].show();
			break;
		case 2:
			for (int i=0; i<8; i++) {
				bar[i].hide();
			}
			bar[8].show();
			break;
		case 1:
			for (int i=0; i<9; i++) {
				bar[i].hide();
			}
			bar[9].show();
			break;
		}

		return HP;
	}
}
