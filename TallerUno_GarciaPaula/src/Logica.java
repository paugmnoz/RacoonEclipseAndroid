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

	private int id;
	private String direccion;

	private ArrayList<Item> items;

	Servidor serv;

	private ArrayList<String> ips;

	private byte[] ip;

	private int numVerdes;
	private int numAzules;

	private HiloServidor controladorCliente;

	int itemSelect = 0;
	public boolean puntaje;

	public Logica(PApplet app) {
		this.app = app;
		iniciarServidor();
		inicializarVariables();
		obtenerIps();
		cargarImagenes();
	}

	public void iniciarServidor() {
		serv = new Servidor(this);

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

		items = new ArrayList<Item>();

		arena = app.createGraphics(1200, 700);
		arena.beginDraw();
		arena.background(255, 255, 255, 0);
		arena.endDraw();

		puntaje = false;

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
					if (items.size() < 30) {
						// items.add(new Boost(app.random(100, 1100), app.random(100, 600), app, this));
						itemSelect = (int) app.random(1, 3);
						// System.out.println(itemSelect + "RANDOM NUMBER");
					}
					// System.out.println(ipObject + " is on the network");
					// System.out.println(ips.size() + " networks quantity");
				} else {
					// System.out.println(ipObject + "NOT");
					if (!items.isEmpty()) {
						items.remove(0);
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
				items.add(new Boost(app.random(100, 1100), app.random(100, 600), app, this));

				break;
			case 2:
				items.add(new Splash(app.random(100, 1100), app.random(100, 600), app, this));

				break;
			}
		}
	}

	public void mostrar() {
		app.image(arenaD, x, y);
		app.image(arena, x, y);

		for (int i = 0; i < items.size(); i++) {
			items.get(i).pintar();
		}

		v.pintar();
		m.pintar();

		pixeles();
		validarCercaniaItemsPersonaje();
		contarPixeles();

	}
	
	//para validar la recoleccion de items 
	public void validarCercaniaItemsPersonaje() {
		for (int i = 0; i < items.size(); i++) {

			Item it = items.get(i);
			
			//si con el personaje verde paso cerca
			if (PApplet.dist(v.getX(), v.getY(), it.getX(), it.getY()) < 50) {
				//si es splash
				if (it instanceof Splash) {
					System.out.println("he tocado SPLASHSSSSSSSSSSSSSt");
					//activar el poder
					it.setActivado(true);
					//llamar el metodo que realizara la funcion del item
					splash(it.getX(), it.getY(), "verde", it);
				} else if (it instanceof Boost) {
					it.setActivado(true);
					it.efectoPersonaje(v);
					System.out.println("he tocado boooooossst");
					
				}
				items.remove(i);
			} else if (PApplet.dist(m.getX(), m.getY(), it.getX(), it.getY()) < 50) {
				if (it instanceof Splash) {
					System.out.println("he tocado SPLASHSSSSSSSSSSSSSt");
					it.setActivado(true);
				} else if (it instanceof Boost) {
					System.out.println("he tocado boooooossst");
					it.setActivado(true);
				}
				items.remove(i);
			}

		}

	}

	public void splash(float f, float h, String quien, Item item) {
		float sx = f;
		float sy = h;
		Item ite = item;
		String quienToco = quien;
		while (ite.isActivado()) {
			PImage arenaRef = v.getArenaRef();
			PImage arenaRefDos = m.getArenaMorada();
			arena.loadPixels();
			arenaRef.loadPixels();
			arenaRefDos.loadPixels();

			for (int py = 0; py < arena.height; py++) {
				for (int px = 0; px < arena.width; px++) {

					int i = px + (py * arena.width);

					if (PApplet.dist(sx, sy, px, py) < 150) {

						float r = app.red(arena.pixels[i]);
						float g = app.green(arena.pixels[i]);
						float b = app.blue(arena.pixels[i]);

						float _r = app.red(arenaRef.pixels[i]);
						float _g = app.green(arenaRef.pixels[i]);
						float _b = app.blue(arenaRef.pixels[i]);

						float mr = app.red(arenaRefDos.pixels[i]);
						float mg = app.green(arenaRefDos.pixels[i]);
						float mb = app.blue(arenaRefDos.pixels[i]);
						if (quienToco.equals("verde")) {
							arena.pixels[i] = app.color(_r, _g, _b);

						} else if (quienToco.equals("morado")) {
							arena.pixels[i] = app.color(mr, mg, mb);
						}
					}
				}
			}
			arena.updatePixels();
			ite.setActivado(false);
		}
	}

	public void pixeles() {

		vx = v.getX();
		vy = v.getY();

		float mx = m.getX();
		float my = m.getY();
		PImage arenaRef = v.getArenaRef();
		PImage arenaRefDos = m.getArenaMorada();
		arena.loadPixels();
		arenaRef.loadPixels();
		arenaRefDos.loadPixels();

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
				} else if (PApplet.dist(mx, my, px, py) < 50) {

					float r = app.red(arena.pixels[i]);
					float g = app.green(arena.pixels[i]);
					float b = app.blue(arena.pixels[i]);

					float mr = app.red(arenaRefDos.pixels[i]);
					float mg = app.green(arenaRefDos.pixels[i]);
					float mb = app.blue(arenaRefDos.pixels[i]);

					if (r != mr || g != mg || b != mb) {
						arena.pixels[i] = app.color(mr, mg, mb);
						if (!m.quieto) {
							m.setPuntaje(v.getPuntaje() + 1);
						}
						// System.out.println(v.getPuntaje() + "PUNTAJEEEEEEE");
					}
				}
			}
		}

		arena.updatePixels();

	}

	public void contarPixeles() {

		while (puntaje) {
			app.colorMode(PApplet.HSB, 360, 100, 100);
			for (int py = 0; py < arena.height; py++) {
				for (int px = 0; px < arena.width; px++) {

					int i = px + (py * arena.width);
					float h = app.hue(arena.pixels[i]);
					float s = app.saturation(arena.pixels[i]);
					float b = app.brightness(arena.pixels[i]);

					if (h > 155 && s > 80 && b > 90) {
						numVerdes++;
					} else if (h > 245 && s > 67 && b > 93) {
						numAzules++;
					}
				}
			}
			System.out.println("Numero de pixeles " + numVerdes);
			System.out.println("Numero de pixeles MORADOS " + numAzules);
			app.colorMode(PApplet.RGB);
		}

	}

	public void pantallas() {

	}

	@Override
	public void update(Observable o, Object arg) {
		this.controladorCliente = (HiloServidor) o;

		String mensaje = (String) arg;

		if (mensaje.contains("dir")) {
		//	System.out.println("Hola soy Raccon verde");

			String[] partes = mensaje.split(":");
			id = Integer.parseInt(partes[1]);
			direccion = partes[2];
			if (id == 1) {
				if (direccion.equals("A")) {
					v.setArriba(true);
					v.setaDer(false);
					v.setDer(false);
					v.setAbDer(false);
					v.setAbajo(false);
					v.setAbIz(false);
					v.setIzq(false);
					v.setaIz(false);
					v.setQuieto(false);
				} else if (direccion.equals("AD")) {
					v.setaDer(true);
					v.setDer(false);
					v.setArriba(false);
					v.setAbDer(false);
					v.setAbajo(false);
					v.setAbIz(false);
					v.setIzq(false);
					v.setQuieto(false);
					v.setaIz(false);
				} else if (direccion.equals("D")) {
					v.setDer(true);
					v.setArriba(false);
					v.setaDer(false);
					v.setAbDer(false);
					v.setAbajo(false);
					v.setAbIz(false);
					v.setIzq(false);
					v.setaIz(false);
					v.setQuieto(false);

				} else if (direccion.equals("AbD")) {
					v.setQuieto(false);
					v.setAbDer(true);
					v.setArriba(false);
					v.setaDer(false);
					v.setDer(false);
					v.setAbajo(false);
					v.setAbIz(false);
					v.setIzq(false);
					v.setaIz(false);

				} else if (direccion.equals("Ab")) {
					v.setQuieto(false);
					v.setAbajo(true);
					v.setArriba(false);
					v.setaDer(false);
					v.setDer(false);
					v.setAbDer(false);
					v.setAbIz(false);
					v.setIzq(false);
					v.setaIz(false);

				} else if (direccion.equals("AbI")) {
					v.setQuieto(false);
					v.setAbIz(true);
					v.setArriba(false);
					v.setaDer(false);
					v.setDer(false);
					v.setAbDer(false);
					v.setAbajo(false);
					v.setIzq(false);
					v.setaIz(false);

				} else if (direccion.equals("Iz")) {
					v.setIzq(true);
					v.setArriba(false);
					v.setaDer(false);
					v.setDer(false);
					v.setAbDer(false);
					v.setQuieto(false);
					v.setAbIz(false);
					v.setAbajo(false);
					v.setaIz(false);
				} else if (direccion.equals("AI")) {
					v.setQuieto(false);
					v.setaIz(true);
					v.setArriba(false);
					v.setaDer(false);
					v.setDer(false);
					v.setAbDer(false);
					v.setAbajo(false);
					v.setAbIz(false);
					v.setIzq(false);

				} else if (direccion.equals("C") || direccion.equals("Nada")) {
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
			} else if (id == 2) {
				if (direccion.equals("A")) {
					m.setArriba(true);
					m.setaDer(false);
					m.setDer(false);
					m.setAbDer(false);
					m.setAbajo(false);
					m.setAbIz(false);
					m.setIzq(false);
					m.setaIz(false);
					m.setQuieto(false);
				} else if (direccion.equals("AD")) {
					m.setaDer(true);
					m.setDer(false);
					m.setArriba(false);
					m.setAbDer(false);
					m.setAbajo(false);
					m.setAbIz(false);
					m.setIzq(false);
					m.setQuieto(false);
					m.setaIz(false);
				} else if (direccion.equals("D")) {
					m.setDer(true);
					m.setArriba(false);
					m.setaDer(false);
					m.setAbDer(false);
					m.setAbajo(false);
					m.setAbIz(false);
					m.setIzq(false);
					m.setaIz(false);
					m.setQuieto(false);

				} else if (direccion.equals("AbD")) {
					m.setQuieto(false);
					m.setAbDer(true);
					m.setArriba(false);
					m.setaDer(false);
					m.setDer(false);
					m.setAbajo(false);
					m.setAbIz(false);
					m.setIzq(false);
					m.setaIz(false);
				} else if (direccion.equals("Ab")) {
					m.setQuieto(false);
					m.setAbajo(true);
					m.setArriba(false);
					m.setaDer(false);
					m.setDer(false);
					m.setAbDer(false);
					m.setAbIz(false);
					m.setIzq(false);
					m.setaIz(false);

				} else if (direccion.equals("AbI")) {
					m.setQuieto(false);
					m.setAbIz(true);
					m.setArriba(false);
					m.setaDer(false);
					m.setDer(false);
					m.setAbDer(false);
					m.setAbajo(false);
					m.setIzq(false);
					m.setaIz(false);
				} else if (direccion.equals("Iz")) {
					m.setIzq(true);
					m.setArriba(false);
					m.setaDer(false);
					m.setDer(false);
					m.setAbDer(false);
					m.setQuieto(false);
					m.setAbIz(false);
					m.setAbajo(false);
					m.setaIz(false);
				} else if (direccion.equals("AI")) {
					m.setQuieto(false);
					m.setaIz(true);
					m.setArriba(false);
					m.setaDer(false);
					m.setDer(false);
					m.setAbDer(false);
					m.setAbajo(false);
					m.setAbIz(false);
					m.setIzq(false);

				} else if (direccion.equals("C") || direccion.equals("Nada")) {
					m.setQuieto(true);
					m.setArriba(false);
					m.setaDer(false);
					m.setDer(false);
					m.setAbDer(false);
					m.setAbajo(false);
					m.setAbIz(false);
					m.setIzq(false);
					m.setaIz(false);
				}
			}
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
