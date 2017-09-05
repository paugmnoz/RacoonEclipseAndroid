import processing.core.PApplet;

public class Boost extends Item {

	public Boost(float x, float y, PApplet app) {
		super(x, y,app);
		for (int i = 0; i < item.length; i++) {
			item[i] = app.loadImage("../data/Boost/Boost_" + i + ".png");
		}
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
	public void efectoPersonaje() {
	}

}
