import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;

public class MainRacoon extends PApplet {

	private Logica app;
	private Thread ipHilo;

	public static void main(String[] args) {
		PApplet.main("MainRacoon");
	}

	public void settings() {
		size(1200, 700);
	}

	public void setup() {
		app = new Logica(this);
		ipHilo = new Thread(app);
		ipHilo.start();
	}

	public void draw() {
		imageMode(CENTER);
		app.mostrar();
	}

	public void mouseClicked() {
		app.pantallas();
	}



}
