import processing.core.PApplet;
import processing.core.PImage;

public class Splash extends Item {

	private Thread splash;

	public Splash(float x, float y, PApplet app) {
		super(x, y, app);

		item = new PImage[8];
		for (int i = 0; i < item.length; i++) {
			item[i] = app.loadImage("../data/Splash/Splash_" + i + ".png");
		}

		splash = new Thread(this);
		splash.start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				animacion();
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void pintar() {
		app.image(item[numFrame], x, y);
	}

	@Override
	public void efectoPersonaje(float _x, float _y) {

	}

}
