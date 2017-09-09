import processing.core.PApplet;
import processing.core.PImage;

public abstract class Item implements Runnable {

	protected float x, y;
	protected PApplet app;
	protected int numFrame;
	protected PImage item[];
	protected boolean activado;
	protected Logica ref;

	public Item(float x, float y, PApplet app, Logica ref) {
		this.x = x;
		this.y = y;
		this.app = app;
		this.ref = ref;

		activado = false;
	}

	public abstract void pintar();

	public void animacion() {
		if (app.frameCount % 4 == 0) {
			numFrame++;
			if (numFrame >= item.length) {
				numFrame = 0;
			}
		}
	}

	public abstract void efectoPersonaje(Jugador uno);

	public void setActivado(boolean activado) {
		this.activado = activado;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	public boolean isActivado() {
		return activado;
	}
}
