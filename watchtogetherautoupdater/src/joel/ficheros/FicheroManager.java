package joel.ficheros;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FicheroManager {

	private String ruta;

	public FicheroManager(String ruta) {
		this.ruta = ruta;
	}

	public boolean existe() {
		File archivo = new File(ruta);
		return archivo.exists();
	}

	public void crearSiNoExiste() {

		try {
			File archivo = new File(ruta);
			if (!archivo.exists()) {
				archivo.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> leerTodo() {
		ArrayList<String> lineas = new ArrayList<String>();
		try {
			Scanner in = new Scanner(new File(ruta));
			while (in.hasNextLine()) {
				String linea = in.nextLine().trim();
				if (!linea.isEmpty() || linea.isBlank()) {
					lineas.add(linea);
				}
			}
			in.close();
			return lineas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	public void escribir(ArrayList<String> lineas) {
		if (lineas.size() > 0) {
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(ruta));
				for (String linea : lineas) {
					out.write(linea);
					out.newLine();
				}
				out.flush();
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void unzip(String destDirectoryName) throws IOException {
		System.out.println("Descomprimiendo " + ruta);
		String destDirectory = destDirectoryName;
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}

		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(this.ruta));
		ZipEntry entry = zipIn.getNextEntry();

		while (entry != null) {
			String filePath = destDirectory + File.separator + entry.getName();

			if (!entry.isDirectory()) {
				// If the entry is a file, create parent directories if they don't exist
				File parentDir = new File(filePath).getParentFile();
				if (!parentDir.exists()) {
					parentDir.mkdirs();
				}

				// Extract the file
				extraerFichero(zipIn, filePath);
			} else {
				// If the entry is a directory, create it
				File dir = new File(filePath);
				dir.mkdirs();
			}

			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}

		zipIn.close();
		borrar();
	}

	private void extraerFichero(ZipInputStream zipIn, String filePath) throws IOException {
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
			byte[] bytesIn = new byte[4096];
			int read;
			while ((read = zipIn.read(bytesIn)) != -1) {
				bos.write(bytesIn, 0, read);
			}
		}
	}

	public void borrar() {
		System.out.println("Borrando " + ruta);
		File archivo = new File(ruta);
		if (archivo.exists()) {
			archivo.delete();
		}
	}

}