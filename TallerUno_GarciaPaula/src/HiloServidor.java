import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;

public class HiloServidor extends Observable implements Runnable {

	private Socket s;
	private boolean conectado;
	private int id;

	// declaro la bandeja de entrada y salida de mensajes
	private DataInputStream entrada;
	private DataOutputStream salida;

	public HiloServidor(Socket s, boolean conectado, int id) {
		this.s = s;
		this.conectado = conectado;
		this.id = id;
		// determino los flujos de salida de los mensajes
		try {
			entrada = new DataInputStream(s.getInputStream());
			salida = new DataOutputStream(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// notificar al cliente que se recibió, saludar para notificar el id
		enviarMensaje("Hola Jugador");

		while (conectado) {
			try {
			//	System.out.println("-----Esperando Mensaje----");
				// para recibir los mensajes del cliente
				recibirMensaje();
				Thread.sleep(16);
			}  catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	// Metodo para enviar mensajes al cliente recibe el string del mensaje a enviar
	public void enviarMensaje(String saludo) {

		try {
			// envío el saludo al cliente
			salida.writeUTF(saludo);
			salida.flush();

			System.out.println("ControladorCliente" + id + "envio mensaje " + saludo);
		} catch (IOException e) {

			// Si hay un error en la conexión con el cliente
			try {
				// si hay un mensaje que se ha enviado
				if (salida != null) {
					// cerrar el flujo
					salida.close();
				}
				s.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// liberar el socket
			s = null;
			// desactivar la conexion
			conectado = false;
			// avisar que se desconectó el cliente
			setChanged();
			notifyObservers("Cliente desconectado");
		}

	}

	// Método que me permitira leer los mensajes que lleguen a la entrada
	private void recibirMensaje() {
		String mensaje;
		try {
			mensaje = entrada.readUTF();
		//	System.out.println("-----Recibi Mensaje------");
			// validar el mensaje que llego
		//	System.out.println("direccion " + mensaje);

			setChanged();
			notifyObservers(mensaje);
			clearChanged();

		} catch (IOException e) {

			try {
				if (entrada != null) {
					entrada.close();
				}
				
				s.close();
			} catch (IOException e1) {
				e1.printStackTrace();

			}
			
			s = null;
			conectado = false;
			setChanged();
			notifyObservers("cliente desconectado");
			e.printStackTrace();
		}

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isConectado() {
		return conectado;
	}

	public void setConectado(boolean conectado) {
		this.conectado = conectado;
	}
	


}
