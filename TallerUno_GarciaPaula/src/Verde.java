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
	
	//cargo mis imagenes de las vistas del personaje
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
			app.image(mapache[numFrame], x, y);

		} else if (aDer) {
			app.image(mAD[numFrame], x, y);

		} else if (der) {
			app.image(mD[numFrame], x, y);

		} else if (abDer) {
			app.image(mDA[numFrame], x, y);

		} else if (abajo) {
			app.image(mAB[numFrame], x, y);

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

}
