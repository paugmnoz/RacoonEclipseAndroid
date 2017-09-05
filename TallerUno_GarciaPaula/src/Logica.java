import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class Logica implements Observer {

	private PApplet app;
	private PImage arenaD, arenaV, arenaM;
	private float x, y, vx, vy;

	private PGraphics arena;
	private Verde v;
	private Morado m;
	
	private Splash splash;

	Servidor serv;

	public Logica(PApplet app) {
		this.app = app;
		iniciarServidor();
		inicializarVariables();
		cargarImagenes();
	}

	public void iniciarServidor() {
		serv = new Servidor(v, m);
		// le aviso que siempre esté pendiente
		serv.addObserver(this);

		Thread t = new Thread(serv);
		t.start();

	}

	public Logica() {
	}

	private void inicializarVariables() {
		x = app.width / 2;
		y = app.height / 2;

		v = new Verde(this, 100, 600, app);
		m = new Morado(this, 1100, 600, app);
		
		splash = new Splash(app.random(100,1100),app.random(100,600), app);

		arena = app.createGraphics(1200, 700);
		arena.beginDraw();
		arena.background(255, 255, 255, 0);
		arena.endDraw();

	}

	private void cargarImagenes() {
		arenaD = app.loadImage("../data/default.png");
	}

	public void mostrar() {
		app.image(arenaD, x, y);
		app.image(arena, x, y);
		v.pintar();
		m.pintar();
		
		splash.pintar();
		pixeles();
	}

	public void pixeles() {

		vx = v.getX();
		vy = v.getY();
		PImage arenaRef = v.getArenaRef();
		arena.loadPixels();
		arenaRef.loadPixels();

		for (int py = 0; py < arena.height; py++) {
			for (int px = 0; px < arena.width; px++) {

				int i = px + (py * arena.width);

				if (PApplet.dist(vx, vy, px, py) < 50) {

					float r = app.red(arena.pixels[i]);
					float g = app.green(arena.pixels[i]);
					float b = app.blue(arena.pixels[i]);

					float _r = app.red(arenaRef.pixels[i]);
					float _g = app.green(arenaRef.pixels[i]);
					float _b = app.blue(arenaRef.pixels[i]);

					if (r != _r || g != _g || b != _b) {
						arena.pixels[i] = app.color(_r, _g, _b);
						if (!v.quieto) {
							v.setPuntaje(v.getPuntaje() + 1);
						}
						System.out.println(v.getPuntaje() + "PUNTAJEEEEEEE");

					}
				}
			}
		}

		arena.updatePixels();

	}

	public void pantallas() {

	}

	@Override
	public void update(Observable o, Object arg) {
		String mensaje = (String) arg;

		if (mensaje.equals("A")) {
			v.setArriba(true);
			v.setaDer(false);
			v.setDer(false);
			v.setAbDer(false);
			v.setAbajo(false);
			v.setAbIz(false);
			v.setIzq(false);
			v.setaIz(false);
			v.setQuieto(false);
		} else if (mensaje.equals("AD")) {
			v.setaDer(true);
			v.setDer(false);
			v.setArriba(false);
			v.setAbDer(false);
			v.setAbajo(false);
			v.setAbIz(false);
			v.setIzq(false);
			v.setQuieto(false);
			v.setaIz(false);
		} else if (mensaje.equals("D")) {
			v.setDer(true);
			v.setArriba(false);
			v.setaDer(false);
			v.setAbDer(false);
			v.setAbajo(false);
			v.setAbIz(false);
			v.setIzq(false);
			v.setaIz(false);
			v.setQuieto(false);

		} else if (mensaje.equals("AbD")) {
			v.setQuieto(false);
			v.setAbDer(true);
			v.setArriba(false);
			v.setaDer(false);
			v.setDer(false);
			v.setAbajo(false);
			v.setAbIz(false);
			v.setIzq(false);
			v.setaIz(false);

		} else if (mensaje.equals("Ab")) {
			v.setQuieto(false);
			v.setAbajo(true);
			v.setArriba(false);
			v.setaDer(false);
			v.setDer(false);
			v.setAbDer(false);
			v.setAbIz(false);
			v.setIzq(false);
			v.setaIz(false);

		} else if (mensaje.equals("AbI")) {
			v.setQuieto(false);
			v.setAbIz(true);
			v.setArriba(false);
			v.setaDer(false);
			v.setDer(false);
			v.setAbDer(false);
			v.setAbajo(false);
			v.setIzq(false);
			v.setaIz(false);

		} else if (mensaje.equals("Iz")) {
			v.setIzq(true);
			v.setArriba(false);
			v.setaDer(false);
			v.setDer(false);
			v.setAbDer(false);
			v.setQuieto(false);
			v.setAbIz(false);
			v.setAbajo(false);
			v.setaIz(false);
		} else if (mensaje.equals("AI")) {
			v.setQuieto(false);
			v.setaIz(true);
			v.setArriba(false);
			v.setaDer(false);
			v.setDer(false);
			v.setAbDer(false);
			v.setAbajo(false);
			v.setAbIz(false);
			v.setIzq(false);

		} else if (mensaje.equals("C")) {
			v.setQuieto(true);
			v.setArriba(false);
			v.setaDer(false);
			v.setDer(false);
			v.setAbDer(false);
			v.setAbajo(false);
			v.setAbIz(false);
			v.setIzq(false);
			v.setaIz(false);

		} else if (mensaje.equals("Nada")) {
			v.setQuieto(true);
			v.setArriba(false);
			v.setaDer(false);
			v.setDer(false);
			v.setAbDer(false);
			v.setAbajo(false);
			v.setAbIz(false);
			v.setIzq(false);
			v.setaIz(false);
		}

		/*
		 * switch (mensaje) { case "A": v.setArriba(true); break; case "AD":
		 * v.setaDer(true); break; case "D": v.setDer(true); break; case "AbD":
		 * v.setAbDer(true); break; case "Ab": v.setAbajo(true); break; case "AbI":
		 * v.setAbIz(true); break; case "Iz": v.setIzq(true); break; case "AI":
		 * v.setaIz(true); break; case "C": v.setQuieto(true); break; case "Nada":
		 * v.setQuieto(true); break; default: break; }
		 */
	}

	public void setArena(PGraphics arena) {
		this.arena = arena;
	}

	public PGraphics getArena() {
		return arena;
	}

	public PImage getArenaD() {
		return arenaD;
	}
}
