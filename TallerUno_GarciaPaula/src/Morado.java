import processing.core.PApplet;
import processing.core.PImage;

public class Morado extends Jugador{
	private Thread hilov;

	public Morado(Logica ref, float x, float y, PApplet app) {
		super(ref, x, y, app);

		cargarimagenes();
		
		hilov = new Thread(this);
		hilov.start();
	}

	private void cargarimagenes() {
		mapache = new PImage[17];

		for (int i = 0; i < mapache.length; i++) {
			mapache[i] = app.loadImage("../data/RM/RM_" + i + ".png");
		}
	}

	public void pintar() {
		app.image(mapache[numFrame], x + _x + boost, y + _y + boost);
	}

	// método para animar el personaje
	public void animacion() {
		if (app.frameCount % 4 == 0) {
			numFrame++;
			if (numFrame >= mapache.length) {
				numFrame = 0;
			}
		}
	}

	public void mover() {

	}

	public void run() {
		while (true) {
			animacion();
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}