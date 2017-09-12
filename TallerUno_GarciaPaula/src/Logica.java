import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ddf.minim.AudioPlayer;
import ddf.minim.AudioSample;
import ddf.minim.Minim;
import jogamp.opengl.GLBufferObjectTracker.CreateStorageDispatch;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;

public class Logica implements Observer, Runnable {
	// variables de imagen
	private PApplet app;
	private PImage arenaD, arenaV, arenaM, fondogris, botonplay, how, go, temp;
	private PImage inicio[], ins[], ganV[], ganM[], score[], cargarA;
	private float x, y, vx, vy;
	// variables para crear el lienzo donde se jugara
	private PGraphics arena;
	private Verde v;
	private Morado m;

	// para iniciar el hilo y cargar las imagenes antes
	private Cargar cargar;

	// Audios de trigger y canciones de fondo
	private Minim minim;
	private AudioSample splashAudio, boostAudio;
	private AudioPlayer music, back, points;

	// variable para identificacion de clientes
	private int id, numFrameC;
	private String direccion;
	// Arreglo que contendrá los items del juego
	private ArrayList<Item> items;
	// para la fuente
	private PFont fuente;

	Servidor serv;// para empezar el servidor

	private ArrayList<String> ips; // para guardar mis ips y añadir objetos items

	private byte[] ip; // para obtener una ip del localhost

	private int numVerdes; // para calcular el puntaje
	private int numAzules; // para calcular el puntaje

	// Para el tiempo
	private int min, s;
	private int pantalla, numFrame, numFrameGan; // manejar pantallas y frames de animaciones

	private HiloServidor controladorCliente; // para manejar los hilos de comunicacion de los clientes

	int itemSelect = 0; // inicializo el switch de los items
	public boolean puntaje; // para activar el metodo que define el puntaje

	public Logica(PApplet app, Minim minim) {
		this.app = app;
		this.minim = minim;
		pantalla = 6;
		cargar();
		cargarPantallaCarga();
		inicializarVariables();
		obtenerIps();
		cargarAudios();
	}

	private void cargarPantallaCarga() {
		cargarA = app.loadImage("../data/Cargar/cargar_21.png");
	}

	// inicializo la carga de imagenes
	public void cargar() {
		cargar = new Cargar(app);
		cargar.start();
	}

	// cargo los audios
	private void cargarAudios() {
		splashAudio = minim.loadSample("../data/splash.mp3", 512);
		music = minim.loadFile("../data/music.mp3", 512);
		back = minim.loadFile("../data/back.mp3", 512);
		points = minim.loadFile("../data/points.mp3", 512);
		boostAudio = minim.loadSample("../data/boost.mp3", 512);
	}

	// para iniciar el servidor y jugar
	public void iniciarServidor() {
		serv = new Servidor(this);
		Thread t = new Thread(serv);
		t.start();
	}

	// inicio el array de items, el de las ips, y le entrego el localHost
	public void obtenerIps() {
		items = new ArrayList<Item>();

		ips = new ArrayList<String>();
		try {
			ip = InetAddress.getLocalHost().getAddress();
		} catch (UnknownHostException e) {
			return;
		}
	}

	// Inicializo las variables
	private void inicializarVariables() {
		x = app.width / 2;
		y = app.height / 2;

		v = new Verde(this, 100, 600, app);
		m = new Morado(this, 1100, 600, app);

		arena = app.createGraphics(1200, 700);
		arena.beginDraw();
		arena.background(255, 255, 255, 0);
		arena.endDraw();

		min = 0;
		s = 59;

		fuente = app.loadFont("../data/FredokaOne-Regular-50.vlw");
		puntaje = false;

		numFrame = 0;
		numFrameGan = 0;

	}

	// cargo las imágenes, les entrego la referencia del cargar
	private void cargarImagenes() {
		arenaD = cargar.getArenaD();
		inicio = cargar.getInicio();
		ganM = cargar.getGanM();
		ganV = cargar.getGanV();
		score = cargar.getScore();
		ins = cargar.getIns();

		fondogris = cargar.getFondogris();
		botonplay = cargar.getBotonplay();
		how = cargar.getHow();
		go = cargar.getGo();
	}

	// Este metodo me ayuda a obtener las ips del host, en ella evaluo
	// constantemente la red
	// si se encuentra un red Reachable se agregará un item mientras que cuando
	// encuentre
	// que no lo sea, se eliminará el primer item que esté activo en la pantalla
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
						itemSelect = (int) app.random(1, 4);
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

			case 3:
				items.add(new Boost(app.random(100, 1100), app.random(100, 600), app, this));

				break;
			case 4:
				items.add(new Splash(app.random(100, 1100), app.random(100, 600), app, this));

				break;
			}
		}
	}

	public void mostrar() {
		back.play();
		switch (pantalla) {

		// --------------Pantalla de Inicio----------------------
		case 0:
			app.image(fondogris, x, y);
			app.image(inicio[numFrame], x, y);
			if (app.frameCount % 4 == 0) {
				numFrame++;
				if (numFrame >= inicio.length) {
					numFrame = 14;
				}
			}
			if (PApplet.dist(app.mouseX, app.mouseY, 607, 466) < 50) {
				app.image(botonplay, x, y);
			} else if (app.mouseX >= 552 && app.mouseY >= 583 && app.mouseX <= 682 && app.mouseY <= 603) {
				app.image(how, x, y);
			}
			break;

		// --------------Pantalla de Instrucciones----------------------
		case 1:
			app.image(arenaD, x, y);
			app.image(arena, x, y);

			// evaluar numero de jugadores y asi empezar el juego
			if (serv.getClientes().size() == 1) {
				v.pintar();
				app.textFont(fuente, 50);
				app.text("1:00", x - 50, 100);
				app.textFont(fuente, 40);

				app.text("You have one minute to paint all you can!", 230, y - 100);
				app.textFont(fuente, 30);

				app.text("Waiting for player 2 ...", 450, y + 20);
			} else if (serv.getClientes().size() == 2) {
				for (int i = 0; i < items.size(); i++) {
					items.get(i).pintar();
				}
				v.pintar();
				m.pintar();
				back.close();
				music.play();
				pixeles();
				validarCercaniaItemsPersonaje();
				tiempo();
				/*
				 * if (app.frameCount % 600 == 0) { puntaje = true; contarPixeles();
				 * System.out.println("puntaje verde" + numVerdes);
				 * System.out.println("puntaje morado" + numAzules); puntaje = false; }
				 */

			}

			break;
		// --------------Pantalla de Juego----------------------
		case 2:
			app.image(ins[numFrame], x, y);
			if (app.frameCount % 5 == 0) {
				numFrame++;
				if (numFrame >= inicio.length) {
					numFrame = 41;
				}
			}

			if (app.mouseX >= 714 && app.mouseY >= 571 && app.mouseX <= 894 && app.mouseY <= 629) {
				app.image(go, x, y);
			}
			break;

		// --------------Pantalla de Puntaje----------------------
		case 3:

			points.play();
			app.image(fondogris, x, y);
			app.image(score[numFrame], x, y);
			if (app.frameCount % 4 == 0) {
				numFrame++;
				if (numFrame >= score.length) {
					numFrame = 9;
				}
			}

			app.text(numVerdes / 1000, 254, 475);
			app.text(numAzules / 1000, 900, 475);

			if (numFrame == 9) {
				if (app.frameCount % 660 == 0) {
					if ((numVerdes / 1000) > (numAzules / 1000)) {

						pantalla = 4;
					} else if ((numVerdes / 1000) < (numAzules / 1000)) {
						pantalla = 5;
					}
				}
			}

			break;

		// --------------Pantalla de Ganador----------------------

		case 4:
			app.image(ganV[numFrameGan], x, y);
			if (app.frameCount % 4 == 0) {
				numFrameGan++;
				if (numFrameGan >= ganV.length) {
					numFrameGan = 11;
				}
			}
			break;

		case 5:
			app.image(ganM[numFrameGan], x, y);
			if (app.frameCount % 4 == 0) {
				numFrameGan++;
				if (numFrameGan >= ganM.length) {
					numFrameGan = 11;
				}
			}
			break;
		// ------------- PANTALLA CARGA-----------//
		case 6:
			app.background(0, 255, 0);
			app.image(cargarA, x, y);
			if (cargar.getState() == Thread.State.TERMINATED) {
				cargarImagenes();
				pantalla = 0;
			}
			break;
		}

	}

	// Calculo el contador del tiempo
	private void tiempo() {
		app.textFont(fuente, 50);

		if (app.frameCount % 61 == 0) {
			if (s <= 59 && s > 0) {
				if (serv.getClientes().size() == 2) {
					s--;
					min = 0;
				}
			}
		}

		if (min == 0 && s == 0) {
			System.out.println("puntaje" + puntaje);
			puntaje = true;
			System.out.println("puntaje" + puntaje);
			contarPixeles();
			puntaje = false;
			System.out.println("puntaje" + puntaje);
		}
		if (s == 0) {
			if (app.frameCount % 61 == 0) {
				s = -1;
			}
		}
		vertiempo();
	}

	// pinto el tiempo en la interfaz
	private void vertiempo() {
		if (s >= 10) {
			app.text(min + ":" + s, x - 50, 100);
		} else if (s < 10 && s > 0) {
			app.text(min + ":0" + s, x - 50, 100);
		} else if (s < 0) {
			app.text(" ", x - 50, 100);
		}
	}

	// para validar la recoleccion de items
	public void validarCercaniaItemsPersonaje() {
		for (int i = 0; i < items.size(); i++) {

			Item it = items.get(i);

			// si con el personaje verde paso cerca
			if (PApplet.dist(v.getX(), v.getY(), it.getX(), it.getY()) < 50) {
				// si es splash
				if (it instanceof Splash) {
					// System.out.println("he tocado SPLASHSSSSSSSSSSSSSt");
					// activar el poder
					it.setActivado(true);
					splashAudio.trigger();
					// llamar el metodo que realizara la funcion del item
					splash(it.getX(), it.getY(), "verde", it);
				} else if (it instanceof Boost) {
					it.setActivado(true);
					it.efectoPersonaje(v);
					boostAudio.trigger();
					// System.out.println("he tocado boooooossst");
				}
				items.remove(i);
			} else if (PApplet.dist(m.getX(), m.getY(), it.getX(), it.getY()) < 50) {
				if (it instanceof Splash) {
					// System.out.println("he tocado SPLASHSSSSSSSSSSSSSt");
					it.setActivado(true);
					splashAudio.trigger();
					splash(it.getX(), it.getY(), "morado", it);

				} else if (it instanceof Boost) {
					// System.out.println("he tocado boooooossst");
					it.setActivado(true);
					it.efectoPersonaje(m);
					boostAudio.trigger();
				}
				items.remove(i);
			}
		}

	}

	// pinto los pixeles cuando se activa un item de tipo splash
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

	// método que me permite que cada jugador cambie el color de la pantalla
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

	// metodo que utilizo para evaluar la cantidad de area pintada por ambos colores
	// y obtener el puntaje
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
			puntaje = false;
			numFrame = 0;

			pantalla = 3;

			for (int i = 0; i < serv.getClientes().size(); i++) {
				serv.getClientes().get(i).setConectado(false);
			}
		}

	}
	
	//metodos del click
	public void pantallas() {
		System.out.println("x" + app.mouseX + "y" + app.mouseY);
		if (pantalla == 0) {

			if (PApplet.dist(app.mouseX, app.mouseY, 607, 466) < 50) {
				iniciarServidor();

				pantalla = 1;
			} else if (app.mouseX >= 552 && app.mouseY >= 583 && app.mouseX <= 682 && app.mouseY <= 603) {
				numFrame = 0;
				pantalla = 2;
			}

		} else if (pantalla == 2) {
			if (app.mouseX >= 714 && app.mouseY >= 571 && app.mouseX <= 894 && app.mouseY <= 629) {
				iniciarServidor();

				pantalla = 1;
			}
		}
	}

	//metodo para evaluar los mensajes de los clientes
	@Override
	public void update(Observable o, Object arg) {
		this.controladorCliente = (HiloServidor) o;

		String mensaje = (String) arg;

		if (mensaje.contains("dir")) {
			// System.out.println("Hola soy Raccon verde");

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
					v.setaDer(true);

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
					v.setAbDer(true);

					v.setArriba(false);
					v.setQuieto(false);
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
