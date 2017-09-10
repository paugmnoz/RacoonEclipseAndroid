import processing.core.PApplet;
import processing.core.PImage;

public class Morado extends Jugador {
	private Thread hilov;
	private PImage arenaMorada;

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

		mAD = new PImage[17];
		for (int i = 0; i < mAD.length; i++) {
			mAD[i] = app.loadImage("../data/RM/RM45/RM45_" + i + ".png");
		}

		mD = new PImage[17];
		for (int i = 0; i < mD.length; i++) {
			mD[i] = app.loadImage("../data/RM/RM90/RM90_" + i + ".png");
		}

		mDA = new PImage[17];
		for (int i = 0; i < mDA.length; i++) {
			mDA[i] = app.loadImage("../data/RM/RM135/RM135_" + i + ".png");
		}

		mAB = new PImage[17];
		for (int i = 0; i < mAB.length; i++) {
			mAB[i] = app.loadImage("../data/RM/RM360/RM360_" + i + ".png");
		}

		mAIz = new PImage[17];
		for (int i = 0; i < mAIz.length; i++) {
			mAIz[i] = app.loadImage("../data/RM/RM225/RM225_" + i + ".png");
		}

		mIz = new PImage[17];
		for (int i = 0; i < mIz.length; i++) {
			mIz[i] = app.loadImage("../data/RM/RM270/RM270_" + i + ".png");
		}
		mIzA = new PImage[17];
		for (int i = 0; i < mIzA.length; i++) {
			mIzA[i] = app.loadImage("../data/RM/RM315/RM315_" + i + ".png");
		}

		arenaMorada = app.loadImage("../data/m.png");
	}

	public void pintar() {
		if (arriba) {
			app.image(mapache[numFrame], x, y);

		} else if (aDer) {
			app.image(mAD[numFrame], x, y);

		} else if (der) {
			app.image(mD[numFrame], x, y);

		} else if (abDer) {
			app.image(mDA[numFrame], x, y);

		} else if (abajo) {
			app.image(mAB[numFrame], x, y);

			y += _y;
		} else if (abIz) {
			app.image(mAIz[numFrame], x, y);

		} else if (Izq) {
			app.image(mIz[numFrame], x, y);

		} else if (aIz) {
			app.image(mIzA[numFrame], x, y);

		} else if (quieto) {
			app.image(mapache[numFrame], x, y);

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
	//	if (x <= app.width - 50 && x >= 50 && y <= app.height - 50 && y >= 50) {
		if (arriba) {
			_y = -4 - boost;
			y += _y;
			x = x;
		} else if (aDer) {
			_y = -3 - boost;
			_x = 3 + boost;
			x += _x;
			y += _y;
		} else if (der) {
			y = y;
			_x = 3 + boost;
			x += _x;
		} else if (abDer) {
			_y = 3 + boost;
			_x = 3 + boost;
			x += _x;
			y += _y;
		} else if (abajo) {
			_y = 3 + boost;
			x = x;
			y += _y;
		} else if (abIz) {
			_y = 3 + boost;
			_x = -3 - boost;
			x += _x;
			y += _y;
		} else if (Izq) {
			_x = -3 - boost;
			x += _x;
			y = y;
		} else if (aIz) {
			_x = -3 - boost;
			_y = -3 - boost;
			x += _x;
			y += _y;
		} else if (quieto) {
			x = x;
			y = y;
		}
	//	}
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

	public PImage getArenaMorada() {
		return arenaMorada;
	}

}