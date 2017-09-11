import java.util.Observable;
import java.util.Observer;

import ddf.minim.AudioSample;
import ddf.minim.Minim;
import processing.core.PApplet;

public class MainRacoon extends PApplet {

	private Logica app;
	private Thread ipHilo;

	private Minim minim;

	public static void main(String[] args) {
		PApplet.main("MainRacoon");
	}

	public void settings() {
		size(1200, 700);
	}

	public void setup() {
		minim = new Minim(this);
		app = new Logica(this, minim);
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

	public void exit() {
		// app.contarPixeles();
	}

}
