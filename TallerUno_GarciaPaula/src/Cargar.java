import processing.core.PApplet;
import processing.core.PImage;

public class Cargar extends Thread {
	
	private PApplet app;
	private PImage arenaD, arenaV, arenaM, fondogris, botonplay, how, go, temp;
	private PImage inicio[], ins[], ganV[], ganM[], score[];
	
	public Cargar(PApplet app) {
		this.app = app;
	}
	//animaciones del inicio
	private void inicio() {
		inicio = new PImage[15];
		for (int i = 0; i < inicio.length; i++) {
			inicio[i] = app.loadImage("../data/Animaciones/Inicio/Inicio_" + i + ".png");
		}
	}

	@Override
	public void run() {
		
		try {
			inicio();
			ins();
			score();
			ganV();
			ganM();
			cargarImagenes();
			Thread.sleep(16);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	//imagenes estáticas
	public void cargarImagenes() {
		arenaD = app.loadImage("../data/default.png");
		fondogris = app.loadImage("../data/F.png");
		botonplay = app.loadImage("../data/Button.png");
		how = app.loadImage("../data/how.png");
		go = app.loadImage("../data/go.png");
		temp = app.loadImage("../data/temp.png");
	}

	public void ganM() {

		ganM = new PImage[12];
		for (int i = 0; i < ganM.length; i++) {
			ganM[i] = app.loadImage("../data/Animaciones/Ganador/Ganador_" + i + ".png");
		}
	}

	public void ganV() {

		ganV = new PImage[12];
		for (int i = 0; i < ganV.length; i++) {
			ganV[i] = app.loadImage("../data/Animaciones/GanadorV/GanadorV_" + i + ".png");
		}
	}

	public void score() {

		score = new PImage[10];
		for (int i = 0; i < score.length; i++) {
			score[i] = app.loadImage("../data/Animaciones/score 2/score 2_" + i + ".png");
		}
		
	}

	public void ins() {
		ins = new PImage[42];
		for (int i = 0; i < ins.length; i++) {
			ins[i] = app.loadImage("../data/Animaciones/Ins/Ins_" + i + ".png");
		}
	}
	
	public PImage getArenaD() {
		return arenaD;
	}
	
	public PImage getArenaM() {
		return arenaM;
	}
	
	public PImage getArenaV() {
		return arenaV;
	}
	
	public PImage getBotonplay() {
		return botonplay;
	}
	
	public PImage getFondogris() {
		return fondogris;
	}
	
	public PImage[] getGanM() {
		return ganM;
	}
	
	public PImage[] getGanV() {
		return ganV;
	}
	
	public PImage getGo() {
		return go;
	}
	
	public PImage[] getIns() {
		return ins;
	}
	
	public PImage[] getInicio() {
		return inicio;
	}
	
	public PImage[] getScore() {
		return score;
	}
	
	public PImage getHow() {
		return how;
	}
	
}
