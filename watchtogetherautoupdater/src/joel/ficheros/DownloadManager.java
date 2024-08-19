package joel.ficheros;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.security.MessageDigest;

public class DownloadManager {

   
	public DownloadManager() {
	}
	

	public FicheroManager descargar(String url, String rutaFichero) {

		try {
			System.out.println("Iniciando descarga de " + rutaFichero);
			URLConnection conexion = new URI(url).toURL().openConnection();

			InputStream stream = conexion.getInputStream();
			FileOutputStream fos = new FileOutputStream(rutaFichero);
			
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			byte[] array = new byte[1000];
			int leido = stream.read(array);
			while (leido > 0) {
				fos.write(array, 0, leido);
				digest.update(array, 0, leido);
				leido = stream.read(array);
				
			}
			stream.close();
			fos.close();
			FicheroManager file = new FicheroManager(rutaFichero);
			
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


}
