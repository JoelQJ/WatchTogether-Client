package joel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import joel.ficheros.DownloadManager;
import joel.ficheros.FicheroManager;
import joel.ficheros.FicheroPropiedades;

public class Principal {
	private static final SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	public static void main(String[] args) {
		try {
			final PrintStream originalOut = System.out;
			File file = new File("log.txt");
			FileOutputStream fos = new FileOutputStream(file, true);
			PrintStream ps = new PrintStream(fos) {
				@Override
				public void println(String x) {
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					String mensaje = formato.format(timestamp) + " " + x;
					if (x.isBlank() || x.isEmpty()) {
						originalOut.println("");
						super.println("");
					} else {
						originalOut.println(mensaje);
						super.println(mensaje);
					}
				}

			};
			System.setErr(ps);
			System.setOut(ps);
			System.out.println("");
			System.out.println("##############################################");
			
			FicheroPropiedades propiedadesFichero = new FicheroPropiedades(args);

			HashMap<String, String> propiedades = propiedadesFichero.buscarElemento("general");
			System.out.println("Version Pyronix launcher instalada:"+propiedadesFichero.versionInstalada() + " online:"+propiedades.get("version"));
			if (propiedadesFichero.versionInstalada() != null) {
				if (!propiedadesFichero.versionInstalada().equals(propiedades.get("version"))) {
					actualizar(propiedadesFichero, propiedades, args);
				} else {
					ejecutar(args);
				}
			} else {
				actualizar(propiedadesFichero, propiedades, args);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void ejecutar(String[] args) {
		try {
			ArrayList<String> command = new ArrayList<>();
			command.add(".\\Utena comparte.exe");
			if(args != null)
				command.addAll(Arrays.asList(args));
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			System.out.println("Abriendo Utena comparte, con comandos:" + command);
			processBuilder.start();
			System.exit(-1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void actualizar(FicheroPropiedades propiedadesFichero, HashMap<String, String> propiedades, String[] args) {
		try {
			FicheroManager launcher = new FicheroManager(".\\Utena comparte.exe");
			FicheroManager zipWatchTogether = new DownloadManager().descargar(propiedades.get("enlace").replace("%ip%", FicheroPropiedades.ip),".\\Utena_comparte.zip");
			if (zipWatchTogether != null) {
				launcher.borrar();
				zipWatchTogether.unzip(".\\");
				propiedadesFichero.setVersionInstalada();
			}
			ejecutar(args);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
