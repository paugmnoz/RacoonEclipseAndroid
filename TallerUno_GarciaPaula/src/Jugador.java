import java.util.Observable;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public abstract class Jugador extends Observable implements Runnable {
	// Variables para la posición del personaje
	protected float x, y, _x, _y;
	protected Logica ref;
	protected int numFrame, boost;
	protected PImage mapache[], arenaRef;
	protected PImage mAD[], mD[], mDA[], mAB[], mAIz[], mIz[], mIzA[];
	protected PApplet app;

	protected String mensaje;
	protected int puntaje;

	protected boolean arriba, aDer, der, abDer, abajo, abIz, Izq, aIz, quieto;

	// Constructor que traerá los varoles iniciales de cada jugador
	public Jugador(Logica ref, float x, float y, PApplet app) {
		this.ref = ref;
		this.app = app;
		this.x = x;
		this.y = y;

		arriba = false;
		aDer = false;
		der = false;
		abDer = false;
		abajo = false;
		abIz = false;
		Izq = false;
		aIz = false;
		quieto = true;
	}

	// método para visualizar los personajes
	public abstract void pintar();

	// método para mover cada personaje
	public abstract void mover();
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void set_y(float _y) {
		this._y = _y;
	}

	public void set_x(float _x) {
		this._x = _x;
	}

	public void setBoost(int boost) {
		this.boost = boost;
	}

	public void setArriba(boolean arriba) {
		this.arriba = arriba;
	}

	public void setaDer(boolean aDer) {
		this.aDer = aDer;
	}

	public void setAbajo(boolean abajo) {
		this.abajo = abajo;
	}

	public void setAbDer(boolean abDer) {
		this.abDer = abDer;
	}

	public void setAbIz(boolean abIz) {
		this.abIz = abIz;
	}

	public void setaIz(boolean aIz) {
		this.aIz = aIz;
	}

	public void setDer(boolean der) {
		this.der = der;
	}

	public void setIzq(boolean izq) {
		Izq = izq;
	}

	public void setQuieto(boolean quieto) {
		this.quieto = quieto;
	}

	public PImage getArenaRef() {
		return arenaRef;
	}

	public int getPuntaje() {
		return puntaje;
	}

	public void setPuntaje(int puntaje) {
		this.puntaje = puntaje;
	}
	
	public int getBoost() {
		return boost;
	}
	

}
