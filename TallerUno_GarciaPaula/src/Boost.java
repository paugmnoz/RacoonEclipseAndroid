import processing.core.PApplet;
import processing.core.PImage;

public class Boost extends Item {

	Thread boostT;

	private int contador;
	private boolean activarContador;
	private Jugador player;

	public Boost(float x, float y, PApplet app, Logica ref) {
		super(x, y, app, ref);

		item = new PImage[8];
		for (int i = 0; i < item.length; i++) {
			item[i] = app.loadImage("../data/Boost/Boost_" + i + ".png");
		}

		boostT = new Thread(this);
		boostT.start();

		contador = 5;
		activarContador = false;
	}

	@Override
	public void run() {
		while (true) {
			try {
				animacion();
				contartiempo();
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
	public void efectoPersonaje(Jugador uno) {
		this.player = uno;
		System.out.println("Efecto" + activado);
		if (activado) {

			activarContador = true;
			System.out.println("EMPEZO CONTADOR");

			if (player instanceof Verde) {
				System.out.println("FUE JUGADOR VERDE");
				player.setBoost(3);
			} else if (player instanceof Morado) {
				System.out.println("FUE JUGADOR MORADO");
				player.setBoost(3);
			}
		}
	}

	public void contartiempo() {

		if (activarContador) {
			System.out.println("CONTADOR  " + activarContador);
			if (app.frameCount % 60 == 0) {
				contador--;
				System.out.println("EMPEZO CONTADOR");
				System.out.println("CONTADOR  " + contador);
				if (contador == 0) {
					contador = 0;
					activado = false;
					activarContador = false;
					System.out.println("CONTADOR  " + activarContador);
					System.out.println("EFECTO  " + activado);
					player.setBoost(0);
					
				}
			}

		}
	}

	public int getContador() {
		return contador;
	}

}
