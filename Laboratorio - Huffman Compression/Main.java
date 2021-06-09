import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import compressor.Compressor;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("****************  HUFFMAN COMPRESSION  ****************\n");
		System.out.println("Inserire il nome del file di input (includere .txt)");
		System.out.println("\t- il file di input deve essere nella working directory (" + System.getProperty("user.dir") + ")");
		System.out.println("\t- oppure inserire il path assoluto del file (ad ogni \\ inserire \\\\  es: C:\\\\Users\\\\username\\\\Desktop\\\\Prova.txt)");
		System.out.print("INSERIRE NOME FILE:\t");
		Scanner in = new Scanner(System.in);
		
		//Create compressor object
		var filename = in.next();
		var compressor = new Compressor(filename);
		
		System.out.println("\nsi vuole scrivere il file in modalità bit a bit? (Compressione reale)");
		System.out.print("(S\\N)\t");
		in = new Scanner(System.in);
		String option = in.next().toLowerCase().charAt(0) + "";

		while (!option.equals("s") && !option.equals("n")) {
			System.out.println("Opzione non consentita inserire 's' (si) o 'n' (no)");
			System.out.print("(S\\N)\t");
			in = new Scanner(System.in);
			option = in.next().toLowerCase().charAt(0) + "";
		}
		in.close();
		System.out.println("\n*******************************************************\n");
		
		try {
			
			if (option.equals("s")) {
				compressor.compress(filename);
				System.out.println("Il file compresso è stato creato correttamente con nome \"CompressedFile.txt\"");
				System.out.print("Codifica utilizzata per la compressione:  "); compressor.printMapEncode();
				System.out.print("Frequenza dei caratteri nel file:  "); compressor.printMapItemsFrequency();
				
				System.out.println("\n - Entropia di Shannon = " + compressor.ShannonEntropy());
				System.out.println(" - Lunghezza Attesa = " + compressor.expectedLenght());
			}
			else {
				compressor.compressWritingEncode(filename);
				System.out.println("Il file compresso è stato creato correttamente con nome \"notReal_CompressedFile.txt\"");
				System.out.print("Codifica utilizzata per la compressione:  "); compressor.printMapEncodeNoEOF();
				System.out.print("Frequenza dei caratteri nel file:  "); compressor.printMapItemsFrequencyNoEOF();
				
				System.out.println("\n - Entropia di Shannon = " + compressor.ShannonEntropyNoEOF());
				System.out.println(" - Lunghezza Attesa = " + compressor.expectedLenghtNoEOF());
			}
			System.out.println("\n*******************************************************");			
		} catch (IOException e) { 
			System.err.println("errore durante l'utilizzo di un file!\n\n");  
			e.printStackTrace();
		}
	}
}
