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
		if (x <= app.width - 50 && x >= 50 && y <= app.height - 50 && y >= 50) {
			if (arriba) {
				_y = -2;
				y += _y;
				x = x;
			} else if (aDer) {
				_y = -2;
				_x = 2;
				x += _x;
				y += _y;
			} else if (der) {
				y = y;
				_x = 2;
				x += _x;
			} else if (abDer) {
				_y = 2;
				_x = 2;
				x += _x;
				y += _y;
			} else if (abajo) {
				_y = 2;
				x = x;
				y += _y;
			} else if (abIz) {
				_y = 2;
				_x = -2;
				x += _x;
				y += _y;
			} else if (Izq) {
				_x = -2;
				x += _x;
				y = y;
			} else if (aIz) {
				_x = -2;
				_y = -2;
				x += _x;
				y += _y;
			}

			else if (quieto) {
				x = x;
				y = y;
			}
		} else {
			x=x;
			y=y;
		}
	}

	public void run() {
		while (true) {
			animacion();
			mover();
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}