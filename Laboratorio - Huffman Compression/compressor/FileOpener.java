package compressor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileOpener {
	
	public static BufferedReader openRead(String filename) throws FileNotFoundException {
		return new BufferedReader( new InputStreamReader( new FileInputStream( filename )));
	}
	
	public static BufferedWriter openWrite(String filename) throws IOException {
		return new BufferedWriter( new FileWriter ( filename ));
	}
	
	
	public static FileOutputStream openWrite_asByteStream(String filename) throws FileNotFoundException {
		return new FileOutputStream(new File(filename));
	}
	
	public static FileInputStream openRead_asByteStream(String filename) throws FileNotFoundException {
		return new FileInputStream(new File(filename));
	}
}
