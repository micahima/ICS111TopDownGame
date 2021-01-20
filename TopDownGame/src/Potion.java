import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

//Adam Parrilla

public class Potion {

	private EZImage potionPicture;

	private int x, y;		
	private boolean alive;
	private int rangeX, rangeY;
	Random rg = new Random();
	Timer timer = new Timer();
	public EZSound drinkSound = EZ.addSound("resources/drinksound.wav");


	public Potion(String potionImage, int rX, int rY) {
		x = rg.nextInt(rX);
		y = rg.nextInt(rY);
		rangeX = rX;
		rangeY = rY;
		potionPicture = EZ.addImage(potionImage, x, y);
		alive = true;
	}

	public EZImage getImage() {
		return potionPicture;
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;

	}
	private void setmoneyImagePosition(int posx, int posy) {
		if (alive){
			potionPicture.translateTo(posx, posy);
		}
	}

	//collection of potions
	public void collect() {
		potionPicture.hide();
		setPosition(-100, -100);
		if(!drinkSound.isPlaying()) {
			drinkSound.play();
		}
		timer.schedule(new TimerTask(){//delayed respawn for potions
			@Override
			public void run() {
				alive = false;
			}
		},5000);
	}

	//resets potions after they're collected
	public void respawn() {
		if (!alive) {
					alive = true;
					System.out.println("respawn");//system check
					potionPicture.show();
					setPosition(rg.nextInt(rangeX), rg.nextInt(rangeY));
		}
	}

	
	public void setPosition(int posX, int posY) {
		x=posX;
		y=posY;
		setmoneyImagePosition(posX, posY);
	}

}
