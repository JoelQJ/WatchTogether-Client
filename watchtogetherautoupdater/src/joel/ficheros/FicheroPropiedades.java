package joel.ficheros;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class FicheroPropiedades {
	private URL url;
	private ArrayList<String> ficheroOnline;
	public static String ip = "127.0.0.1";
	public FicheroPropiedades(String[] args) {
		try {
			if(args.length > 0)
				ip = args[0];
			
			this.url = new URI(String.format("http://%s:8080/version.txt",ip)).toURL();
			ficheroOnline = leerTodo();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String versionInstalada() {
		FicheroManager versionTXT = new FicheroManager(".\\version.txt");

		if (versionTXT.existe())
			return versionTXT.leerTodo().get(0);
		else
			return null;
	}

	public void setVersionInstalada() {
		FicheroManager versionTXT = new FicheroManager(".\\version.txt");
		versionTXT.escribir(new ArrayList<String>(Arrays.asList(new String[] { buscarElemento("general").get("version") })));
	}

	public HashMap<String, String> buscarElemento(String elementoABucar) {
		HashMap<String, String> elementos = new HashMap<String, String>();
		try {
			Iterator<String> in = ficheroOnline.iterator();
			while (in.hasNext()) {
				String linea = in.next().trim().replaceAll("\\s+", "");
				if (!linea.isEmpty() || linea.isBlank()) {

					if (linea.equals("-" + elementoABucar)) {
						while (in.hasNext()) {
							String elemento = in.next().trim().replaceAll("\\s+", "");

							if (elemento.equals("----"))
								break;

							elementos.put(elemento.split(";")[0], elemento.split(";")[1]);

						}
					}

				}
			}
			return elementos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public ArrayList<String> leerTodo() {
		ArrayList<String> lineas = new ArrayList<String>();
		try {
			Scanner in = new Scanner(url.openStream());
			while (in.hasNextLine()) {
				String linea = in.nextLine().trim();
				if (!linea.isEmpty() || linea.isBlank()) {
					lineas.add(linea);
				}
			}
			in.close();
			System.out.println(lineas + " dddd");
			return lineas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<String> buscarTodosLosElementosPrimarios() {
		ArrayList<String> elementos = new ArrayList<String>();

		// Iterar sobre cada elemento de la lista
		for (String texto : ficheroOnline) {

			if (texto.startsWith("-")) {
				if (!texto.equals("----")) {
					char[] arregloCaracteres = texto.toCharArray();
					arregloCaracteres[0] = ' ';
					String textoFinal = new String(arregloCaracteres);

					elementos.add(textoFinal.trim());
				}
			}

		}

		return elementos;
	}
}
