import processing.core.PApplet;
import processing.core.PImage;

public class Verde extends Jugador {
	private Thread hilov;
	private PImage arenaFinal;

	public Verde(Logica ref, float x, float y, PApplet app) {
		super(ref, x, y, app);

		cargarimagenes();

		hilov = new Thread(this);
		hilov.start();
	}

	private void cargarimagenes() {

		arenaRef = app.loadImage("../data/v.png");
		arenaFinal = ref.getArenaD();

		mapache = new PImage[17];
		for (int i = 0; i < mapache.length; i++) {
			mapache[i] = app.loadImage("../data/RV/RV_" + i + ".png");
		}

		mAD = new PImage[17];
		for (int i = 0; i < mAD.length; i++) {
			mAD[i] = app.loadImage("../data/RVAnimations/RV45/RV45_" + i + ".png");
		}

		mD = new PImage[17];
		for (int i = 0; i < mD.length; i++) {
			mD[i] = app.loadImage("../data/RVAnimations/RV90/RV90_" + i + ".png");
		}

		mDA = new PImage[17];
		for (int i = 0; i < mDA.length; i++) {
			mDA[i] = app.loadImage("../data/RVAnimations/RV135/RV135_" + i + ".png");
		}

		mAB = new PImage[17];
		for (int i = 0; i < mAB.length; i++) {
			mAB[i] = app.loadImage("../data/RVAnimations/RV180/RV180_" + i + ".png");
		}

		mAIz = new PImage[17];
		for (int i = 0; i < mAIz.length; i++) {
			mAIz[i] = app.loadImage("../data/RVAnimations/RV225/RV225_" + i + ".png");
		}

		mIz = new PImage[17];
		for (int i = 0; i < mIz.length; i++) {
			mIz[i] = app.loadImage("../data/RVAnimations/RV270/RV270_" + i + ".png");
		}
		mIzA = new PImage[17];
		for (int i = 0; i < mIzA.length; i++) {
			mIzA[i] = app.loadImage("../data/RVAnimations/RV315/RV315_" + i + ".png");
		}
	}

	public void pintar() {

		if (arriba) {
			app.image(mapache[numFrame], x + boost, y + boost);
			_y = -2;
			y += _y;
			x = x;
		} else if (aDer) {
			app.image(mAD[numFrame], x + boost, y + boost);
			_y = -1;
			_x = 1;
			x += _x;
			y += _y;
		} else if (der) {
			app.image(mD[numFrame], x + boost, y + boost);
			y = y;
			_x = 2;
			x += _x;
		} else if (abDer) {
			app.image(mDA[numFrame], x + boost, y + boost);
			_y = 1;
			_x = 1;
			x += _x;
			y += _y;
		} else if (abajo) {
			app.image(mAB[numFrame], x + boost, y + boost);
			_y = 2;
			x = x;
			y += _y;
		} else if (abIz) {
			app.image(mAIz[numFrame], x + boost, y + boost);
			_y = 1;
			_x = -1;
			x += _x;
			y += _y;
		} else if (Izq) {
			app.image(mIz[numFrame], x + boost, y + boost);
			_x = -2;
			x += _x;
			y = y;
		} else if (aIz) {
			app.image(mIzA[numFrame], x + boost, y + boost);
			_x = -1;
			_y = -1;
			x += _x;
			y += _y;
		} else if (quieto) {
			app.image(mapache[numFrame], x + boost, y + boost);
			x = x;
			y = y;
		}
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

	public void pintarArena() {
		System.out.println("pintarD::::");
		arenaFinal.loadPixels();
		System.out.println("no pintaaaaaaaaaaaaaaaaaaaaaaa");
		arenaRef.loadPixels();

		for (int py = 0; py < arenaFinal.height; py++) {
			for (int px = 0; px < arenaFinal.width; px++) {

				int i = px + (py * arenaFinal.width);

				if (PApplet.dist(x, y, px, py) < 100) {

					float r = app.red(arenaFinal.pixels[i]);
					float g = app.green(arenaFinal.pixels[i]);
					float b = app.blue(arenaFinal.pixels[i]);

					float _r = app.red(arenaRef.pixels[i]);
					float _g = app.green(arenaRef.pixels[i]);
					float _b = app.blue(arenaRef.pixels[i]);

					if (r != _r || g != _g || b != _b) {
						arenaFinal.pixels[i] = app.color(_r, _g, _b);
						
					}
				}
			}
		}

		arenaFinal.updatePixels();

	}

	public void run() {
		while (true) {
			animacion();
			mover();
			// pintarArena();
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void moverPersonajeVerde(final String mensaje) {
		this.mensaje = mensaje;
		if (mensaje.equals("A")) {
			_y = -4;
		}
	}

}
