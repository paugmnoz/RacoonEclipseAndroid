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