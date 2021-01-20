import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.io.*;

public class Main {
	
	public static EZSound backSound = EZ.addSound("background.wav");
	public static EZSound failSound = EZ.addSound("failsound.wav");

	public static void main(String[] args) throws FileNotFoundException {
		EZ.initialize(1500, 800);	
		EZ.setBackgroundColor(Color.DARK_GRAY);
		backSound.loop();
		boolean startGame = true;
		while(startGame) {
			Random randomGenerator = new Random();
			Timer timer = new Timer();

			FileReader fr = new FileReader("Project_3.txt");
			Scanner sc = new Scanner(fr);

			//create hero options
			Hero[] hero = new Hero[2];
			EZImage[] attack = new EZImage[2];
			for(int i=0; i<2; i++) { 
				hero[i] = new Hero (sc.next(), 800, 400) ;
				hero[i].hide();
				attack[i] = EZ.addImage(sc.next(),hero[i].getX(), hero[i].getY());
				attack[i].hide();
			}

			//set-up for selection screen
			EZImage[] arrows = new EZImage[2];

			for(int i=0; i<2; i++){
				arrows[i] = EZ.addImage(sc.next(), sc.nextInt(), sc.nextInt());
			}

			EZText choose = EZ.addText(750, 100, "CHOOSE YOUR CHARACTER", Color.WHITE, 60); 
			choose.setFont("8-BIT WONDER.TTF");
			EZText enter = EZ.addText(750, 700, "Press Enter To Start", Color.WHITE, 60); 
			enter.setFont("8-BIT WONDER.TTF");
			EZImage potionSign = EZ.addImage("sign1.png", 1250, 400);
			EZImage controlSign = EZ.addImage("sign2.png", 250, 400);

			boolean start = true;
			int num = 1;

			//character selection screen
			while(start) { 
				num = Hero.playernum(arrows[0], arrows[1],num);

				EZ.pause(250);
				arrows[0].hide();
				arrows[1].hide();
				EZ.pause(250);
				arrows[0].show();
				arrows[1].show();

				switch (num) {
				case 0:
					hero[num].show();
					hero[1].hide();
					EZ.refreshScreen();
					break;
				case 1:
					hero[num].show();
					hero[0].hide();
					EZ.refreshScreen();
					break;
				}
				if(EZInteraction.wasKeyReleased(KeyEvent.VK_ENTER)) {
					start = false;
				}
				EZ.refreshScreen();
			}

			//gameplay set-up
			Hero mainHero = hero[num]; //rename variables for simplicity
			EZImage mainAttack = attack[num];

			EZImage background = EZ.addImage("park.jpg", 750, 400);
			mainHero.scaleBy(.3333);
			mainAttack.scaleBy(.3333);
			mainHero.pullToFront();

			//create health bar
			int health = 10;
			EZImage[] hpBar = new EZImage[10];
			for (int i=0; i<10; i++) {
				hpBar[i] = EZ.addImage(sc.next(),mainHero.getX(), mainHero.getY()-50);

			}
			EZText healthBar = EZ.addText(mainHero.getX()-45, mainHero.getY()-50, Integer.toString(health), Color.WHITE, 15); 
			healthBar.setFont("8-BIT WONDER.TTF");

			sc.close();

			//create enemy list
			ArrayList<Enemy> villain = new ArrayList<Enemy>();
			mainHero.pullToFront();
			mainAttack.pullToFront();
			EZImage background2 = EZ.addImage("trees.png", 750, 400);
			EZText points = EZ.addText(1400, 60, "0", Color.WHITE, 60); 

			//create potions for health
			Potion[] potion = new Potion[5];
			for(int i=0; i<5; i++) {
				potion[i] = new Potion("potion.png", 1500, 800);
			}

			//score tracker
			int scoreA = -1;
			int scoreB = 0;


			boolean run = true;

			while (run) {
				//components for the playable character
				mainHero.controller(mainHero.getImage(), 5, 5, mainAttack);
				mainHero.boundary(1500, 800);

				//enemy components
				if (scoreA < scoreB) {//creates new enemies based for score amount
					scoreA = scoreB;
					if (scoreB%2 == 0) {
						villain.add(new Enemy("enemy.png", randomGenerator.nextInt(1500) , randomGenerator.nextInt(800),mainHero, 1500, 800));
					}
					background2.pullToFront();
					points.pullToFront();
				}
				for (int i=0; i<villain.size(); i++) { //moves enemies
					villain.get(i).go();
				}

				//components for health bar
				//ends game
				if (health == 0) {
					run = false;
					backSound.stop();
					if(!failSound.isPlaying()) {
						failSound.play();
					}
					
				}
				//moves health bar image to follow player
				healthBar.translateTo(mainHero.getImage().getXCenter()-45, mainHero.getImage().getYCenter()-50); 
				for (int i=0; i<10; i++) {
					hpBar[i].translateTo(mainHero.getImage().getXCenter(), mainHero.getImage().getYCenter()-50);
				}
				//changes health points
				for (int i=0; i<villain.size(); i++) {
					health = mainHero.health(villain.get(i), health, hpBar);
				}
				healthBar.setMsg(Integer.toString(health));

				//attack components for player
				mainAttack.translateTo(mainHero.getX(), mainHero.getY()); //attack image follows player
				if (mainHero.attack()) {
					//attacking animation
					mainHero.getImage().hide();
					mainAttack.show();
					timer.schedule(new TimerTask(){
						public void run() {
							mainHero.getImage().show();
							mainAttack.hide(); 
						}
					}, 150);
					//attack kills enemies and player gets points
					for (int i=0; i<villain.size(); i++) {
						if(mainAttack.isPointInElement(villain.get(i).getX(), villain.get(i).getY())){
							System.out.println("HIT!");//system check
							villain.get(i).dead();
							scoreB++;
							points.setMsg(Integer.toString(scoreB));
							
						}
					}
				}

				//potion components
				for(int i=0; i<5; i++) { //potion interactions
					if (potion[i].getImage().isPointInElement(mainHero.getX(), mainHero.getY())) {
						health++;
						potion[i].collect();
					}
					if (!((potion[i].getY() > 150) || (potion[i].getY() < 1350))) {
						potion[i].setPosition(randomGenerator.nextInt(1500) , randomGenerator.nextInt(800));
					}
					//potions regenerate every 10 secs
					potion[i].respawn();
				}

				Hero.attack = false;
				EZ.refreshScreen();
			}

			//end screen
			EZText end = EZ.addText(750, 300, "GAME OVER", Color.RED, 80);
			EZText again = EZ.addText(750, 500, "Press enter to try again", Color.RED, 50);
			EZText finalScore = EZ.addText(750, 400, "final score "+scoreB, Color.BLACK, 50);
			
			//restarts game
			while(true) {
				if(EZInteraction.wasKeyReleased(KeyEvent.VK_ENTER)) {
					EZ.removeAllEZElements();
					backSound.loop();
					break;
				}
			}
		}
	}
}




