import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

public class Servidor extends Observable implements Runnable {

	// declaro el servidor y el socket para la conexion con el cliente
	private ServerSocket ss;
	private Socket s;
	private MulticastSocket ms;

	private Verde v;
	private Morado m;
	
	//para sincronizar los personajes
	private boolean esperando;

	// declaro la bandeja de entrada y salida de mensajes
	private DataInputStream entrada;
	private DataOutputStream salida;

	private final int puerto = 1889;

	private boolean conectado;

	public Servidor(Verde v, Morado m) {
		this.v = v;
		this.m = m;
		
		esperando = false;
		conectado = false;
		
		try {
			ss = new ServerSocket(puerto);
			System.out.println("--------Inicio Servidor-------");
			s = ss.accept();
			System.out.println("--------Conectadoooo :3 -------");

			entrada = new DataInputStream(s.getInputStream());
			salida = new DataOutputStream(s.getOutputStream());

			conectado = true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		// mientras se esé conectado
		while (conectado) {
			try {
				System.out.println("-----Esperando Mensaje----");
				recibirMensaje();
				Thread.sleep(16);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	// Método que me permitira leer los mensajes que lleguen a la entrada
	private void recibirMensaje() throws IOException {
		String mensaje = entrada.readUTF();
		System.out.println("-----Recibi Mensaje------");
		// validar el mensaje que llego
		System.out.println("direccion " + mensaje);
		setChanged();
		notifyObservers(mensaje);
		//v.moverPersonajeVerde(mensaje);
		clearChanged();
	}

}
