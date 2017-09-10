import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

public class Servidor implements Observer, Runnable {

	// declaro el servidor y el socket para la conexion con el cliente
	private ServerSocket ss;
	private MulticastSocket ms;

	private Logica ref;

	private ArrayList<HiloServidor> clientes;

	// para sincronizar los personajes
	private boolean esperando;

	private final int puerto = 1889;

	private boolean conectado;

	public Servidor(Logica ref) {

		this.ref = ref;

		esperando = false;
		conectado = false;

		clientes = new ArrayList<HiloServidor>();

		try {
			ss = new ServerSocket(puerto);
			conectado = true;

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("--------Inicio Servidor, esperando clientes-------");
	}

	@Override
	public void run() {
		System.out.println("--------aceptar cliente ... -------");
		while (conectado) {
			try {
				// Aceptar el cliente
				Socket s = ss.accept();
				// crear hilo para el cliente nuevo
				HiloServidor hs = new HiloServidor(s, conectado, clientes.size());
				// añadir el observador del cliente
				hs.addObserver(ref);
				hs.addObserver(this);
				// correr hilo del cliente
				new Thread(hs).start();
				// añadir el cliente a un arreglo
				clientes.add(hs);
				System.out.println("-------------- Tenemos: " + clientes.size() + " clientes");
				Thread.sleep(100);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("-----CLIENTES----");
			// le aviso que siempre esté pendiente

		}

	}

	@Override
	public void update(Observable o, Object arg) {
		HiloServidor controlCliente = (HiloServidor) o;

		String mensaje = (String) arg;

		// si me preguntan cual es el id
		if (mensaje.equals("Cual es mi Id")) {
			// enviar el mensaje al cliente de su Id
			controlCliente.enviarMensaje("Id:" + clientes.size());
		}

		// si recibo que el cliente se desconecto
		if (mensaje.equalsIgnoreCase("cliente desconectado")) {
			// eliminar el cliente del arraylist
			clientes.remove(controlCliente);
			System.out.println("[Servidor] Tenemos: " + clientes.size() + " clientes");
		}

	}
	
	public ArrayList<HiloServidor> getClientes() {
		return clientes;
	}

}
