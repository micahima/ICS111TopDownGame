import java.util.Random;

//Dillion Kaneshiro

public class Enemy {

	private EZImage thanosImage;
	private int x, y; 
	private int destx, desty;
	private int rangex, rangey;	
	private Hero mainHero;
	private int speed;
	

	Random rg = new Random();

	public Enemy(String filename,int posx, int posy, Hero hero, int rx, int ry) {
		x = posx;
		y = posy;
		rangex = rx; 
		rangey = ry;
		thanosImage = EZ.addImage(filename, posx, posy);
		mainHero = hero;
		speed = 1;
		setModImagePosition(x, y);
		setRandomDirection();
	}

	public int getX() {
		return x;
	}

	public EZImage getImage() {
		return thanosImage;
	}

	public int getY() {
		return y;
	}

	//Set destination for enemy
	public void setRandomDirection(){

		int ranx = rg.nextInt(rangex);
		int rany = rg.nextInt(rangey);	

		setDestination(ranx,rany);
	}

	public void setDestination(int posx, int posy) { //Enemy moves randomly until hero gets close enough 
		if((mainHero.getX() <  getX()+200) && (mainHero.getX() >  getX()-200)
				&& (mainHero.getY() < getY()+200) && (mainHero.getY() >  getY()-200)) {
			destx = mainHero.getX();
			desty = mainHero.getY();
			speed = 3;
		}else {
			destx = posx; 
			desty = posy;
			speed = 1;
		}
	}

	 //controls movements of enemy
	public void go() {
		if (x > destx) {
			moveLeft(speed); 
			setDestination(destx,desty);
		}
		if (x < destx) {
			moveRight(speed);
			setDestination(destx,desty);
		}
		if (y > desty) {
			moveUp(speed); 
			setDestination(destx,desty);
		}
		if (y < desty) {
			moveDown(speed);
			setDestination(destx,desty);
		}
		if (y < desty && x < destx) {
			thanosImage.rotateTo(225);
		}
		if (y > desty && x > destx) {
			thanosImage.rotateTo(45);
		}
		if (y < desty && x > destx) {
			thanosImage.rotateTo(315);
		}
		if (y > desty && x < destx) {
			thanosImage.rotateTo(135);
		}
		
		if ((x <= destx+(speed-1) && x >= destx-(speed-1)) && (y <= desty+(speed-1) && y >= desty-(speed-1))) {
			setRandomDirection();
		}
	}


	public void setPosition(int posx, int posy) {
		x = posx;
		y = posy;
		setModImagePosition(x, y);
	}

	private void setModImagePosition(int posx, int posy) {
		thanosImage.translateTo(posx, posy);
	}

	public void moveLeft(int step) {
		x = x - step;
		thanosImage.rotateTo(0);
		setModImagePosition(x, y);
	}

	public void moveRight(int step) {
		x = x + step;
		thanosImage.rotateTo(180);
		setModImagePosition(x, y);
	}

	public void moveUp(int step) {
		y = y - step;
		thanosImage.rotateTo(90);
		setModImagePosition(x, y);
	}

	public void moveDown(int step) {
		y = y + step;
		thanosImage.rotateTo(270);
		setModImagePosition(x, y);
	}
	
	//resets enemy when killed
	public void dead() {
		setPosition(rg.nextInt(1501)+500,rg.nextInt(801)+500);
		setRandomDirection();
		speed = 1;
	}
}