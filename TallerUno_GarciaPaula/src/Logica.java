import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class Logica implements Observer, Runnable {

	private PApplet app;
	private PImage arenaD, arenaV, arenaM;
	private float x, y, vx, vy;

	private PGraphics arena;
	private Verde v;
	private Morado m;

	private ArrayList<Splash> splashs;
	private ArrayList<Boost> boosts;

	Servidor serv;

	private ArrayList<String> ips;
	private byte[] ip;

	int itemSelect = 0;

	public Logica(PApplet app) {
		this.app = app;
		iniciarServidor();
		inicializarVariables();
		obtenerIps();
		cargarImagenes();
	}

	public void iniciarServidor() {
		serv = new Servidor(v, m);
		// le aviso que siempre esté pendiente
		serv.addObserver(this);

		Thread t = new Thread(serv);
		t.start();

	}

	public void obtenerIps() {
		ips = new ArrayList<String>();
		try {
			ip = InetAddress.getLocalHost().getAddress();
		} catch (UnknownHostException e) {
			return;
		}
	}

	private void inicializarVariables() {
		x = app.width / 2;
		y = app.height / 2;

		v = new Verde(this, 100, 600, app);
		m = new Morado(this, 1100, 600, app);

		splashs = new ArrayList<Splash>();
		boosts = new ArrayList<Boost>();
		// splash = new Splash(app.random(100,1100),app.random(100,600), app);

		arena = app.createGraphics(1200, 700);
		arena.beginDraw();
		arena.background(255, 255, 255, 0);
		arena.endDraw();

	}

	private void cargarImagenes() {
		arenaD = app.loadImage("../data/default.png");
	}

	public void run() {

		for (int i = 0; i <= 255; i++) {
			final int j = i;

			try {
				ip[3] = (byte) j;
				InetAddress address = InetAddress.getByAddress(ip);

				String ipObject = address.toString().substring(1);
				if (address.isReachable(5000)) {
					if ((splashs.size() + boosts.size()) < 30) {
						boosts.add(new Boost(app.random(100, 1100), app.random(100, 600), app));

						itemSelect = (int) app.random(1, 3);
						System.out.println(itemSelect + "RANDOM NUMBER");
					}
					// System.out.println(ipObject + " is on the network");
					// System.out.println(ips.size() + " networks quantity");
				} else {
					System.out.println(ipObject + "NOT");
					if (splashs.size() > 0 | +boosts.size() > 0) {
						// splashs.remove(splashs.size() - 1);

						boosts.remove(boosts.size() - 1);
					}
				}
				Thread.sleep(1000);

			} catch (Exception e) {
				e.printStackTrace();
			}
			switch (itemSelect) {
			case 0:

				break;

			case 1:
				boosts.add(new Boost(app.random(100, 1100), app.random(100, 600), app));

				break;
			case 2:
				splashs.add(new Splash(app.random(100, 1100), app.random(100, 600), app));

				break;
			}
		}
	}

	public void mostrar() {
		app.image(arenaD, x, y);
		app.image(arena, x, y);
		
		for (int i = 0; i < splashs.size(); i++) {
			splashs.get(i).pintar();
		}

		for (int i = 0; i < boosts.size(); i++) {
			boosts.get(i).pintar();
		}
		v.pintar();
		m.pintar();

		

		pixeles();
		validarCercaniaItemsPersonaje();

	}

	public void validarCercaniaItemsPersonaje() {
		for (int i = 0; i < splashs.size(); i++) {

			if (PApplet.dist(v.getX(), v.getY(), splashs.get(i).getX(), splashs.get(i).getY()) < 50) {
				splashs.get(i).setActivado(true);
				splashs.remove(i);
			}

		}
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
						// System.out.println(v.getPuntaje() + "PUNTAJEEEEEEE");

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
