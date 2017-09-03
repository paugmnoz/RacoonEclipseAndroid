import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;

public class MainRacoon extends PApplet {

	private Logica app;


	public static void main(String[] args) {
		PApplet.main("MainRacoon");
	}

	public void settings() {
		size(1200, 700);
	}

	public void setup() {
		app = new Logica(this);
		
	}

	public void draw() {
		imageMode(CENTER);
		app.mostrar();
	}

	public void mouseClicked() {
		app.pantallas();
	}



}
