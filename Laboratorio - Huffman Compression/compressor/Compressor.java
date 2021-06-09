package compressor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import compressor.decoder.Decoder;

import javax.management.RuntimeErrorException;

public class Compressor {
	//eof char is used when you have to decode the file otherwise is useless 
	//(if there is char 'eof' in the encode map encode for the other character could be different)
	protected final Map<String, String> encode;	//encode map with char 'eof'
	protected final Map<String, String> encodeNoEOF;	//encode map without char 'eof'
	
	//Map char -> frequency in the file
	protected final Map<String, Integer> itemsFrequency;
	protected final Map<String, Integer> itemsFrequencyNoEOF;
	
	//Create a compressor object that create an encode map where for each char in the file called "filename_toCompress"
	//create it's own encode
	public Compressor(String filename) throws FileNotFoundException {
		Objects.requireNonNull(filename);
		
		this.itemsFrequency = FileItemsCounter.countItems(filename);
		this.itemsFrequencyNoEOF = FileItemsCounter.countItemsNoEOF(filename);
		
		this.encode = HuffmanEncode.createEncodeMap( this.itemsFrequency );
		this.encodeNoEOF = HuffmanEncode.createEncodeMap( this.itemsFrequencyNoEOF );
	}
	
	//Map print Method
	public void printMapEncode() { System.out.println(this.encode.toString()); }
	public void printMapEncodeNoEOF() { System.out.println(this.encodeNoEOF.toString()); }
	public void printMapItemsFrequency() { System.out.println(this.itemsFrequency.toString()); }
	public void printMapItemsFrequencyNoEOF() { System.out.println(this.itemsFrequencyNoEOF.toString()); }
	
	//Shannon Entropy
	public Double ShannonEntropy() { return HuffmanEncode.ShannonEntropy(this.itemsFrequency); }
	public Double ShannonEntropyNoEOF() { return HuffmanEncode.ShannonEntropy(this.itemsFrequencyNoEOF); }
	
	//Expected Length  L(C,X)
	public Double expectedLenght() { return HuffmanEncode.expectedLenght(itemsFrequency, encode); }
	public Double expectedLenghtNoEOF() { return HuffmanEncode.expectedLenght(itemsFrequencyNoEOF, encodeNoEOF); }
	
	
	//Compress the file called filename_notCompressed with the created encode
	//after the compression a new file is created with name filename_Compressed
	public void compress(String filename_toCompress, String filename_Compressed) throws IOException {
		Objects.requireNonNull(filename_toCompress);
		Objects.requireNonNull(filename_Compressed);
		
		var readFile = FileOpener.openRead(filename_toCompress);
		var writeFile = FileOpener.openWrite_asByteStream(filename_Compressed);
		
		String line = null;
		byte buffer = 0x00;
		int buf_index = 0;
		final int BUFFER_SIZE = 8; //Force buffer size to be 8 bits instead sizeof(int)
		
		while( ( line = readFile.readLine() ) != null) {
			
			for(int i = 0; i < line.length(); i++) {
				
				String encodeForChar = this.encode.get(line.charAt(i) + "");
				if (encodeForChar == null)
					throw new RuntimeException("Can't find encode for value '" + line.charAt(i) + "' !");
				for (int j = 0; j < encodeForChar.length(); j++) {
					
					if("1".equals(encodeForChar.charAt(j) + ""))  
						buffer = (byte) ((buffer << 1) | 1); 
					else 
						buffer = (byte) (buffer << 1); 
					buf_index += 1;
					
					if((buf_index % (BUFFER_SIZE)) == 0) {
						writeFile.write(buffer);
						buffer = 0x00;
						buf_index = 0;
					}
				}
			}
		}
		//File compress done now write eof
		writeEOF(buffer, buf_index, BUFFER_SIZE, writeFile);
		
		//Closing file
		readFile.close();
		writeFile.close();
	}
	public void compress(String filename_toCompress) throws IOException { compress(filename_toCompress, "CompressedFile.txt");}
	
	
	//write 'eof' encode (end of file), after fill the buffer with 0 and write 
	private void writeEOF(int buffer, int buf_index, int BUFFER_SIZE, FileOutputStream writeFile) throws IOException {

		String encodeForChar = this.encode.get("eof");
		if (encodeForChar == null) 
			throw new RuntimeErrorException(null, "Can't write end of file!");
		
		for (int j = 0; j < encodeForChar.length(); j++) {
			
			if("1".equals(encodeForChar.charAt(j) + "")) 
				buffer = (buffer << 1) | 1;
			else 
				buffer = buffer << 1;
			buf_index += 1;
			
			if((buf_index % BUFFER_SIZE) == 0) {
				writeFile.write(buffer);
				buffer = 0;
				buf_index = 0;
			}
		}
		
		//Fill the rest of the buffer and write it	
		buffer = buffer << (BUFFER_SIZE - buf_index);
		writeFile.write(buffer);
		
	}


	//Compress the file writing the encode not bit to bit but as a String in the file called filename_Compressed
	//Then the file is not really compressed and the total size of the file may be higher than the original size
	//Unlike the method called compress the char 'eof' is not written at the end of the file
	public void compressWritingEncode(String filename_toCompress, String filename_Compressed) throws IOException {
		Objects.requireNonNull(filename_toCompress);
		Objects.requireNonNull(filename_Compressed);
		
		var readFile = FileOpener.openRead(filename_toCompress);
		var writeFile = FileOpener.openWrite(filename_Compressed);
		
		String line = null;
		while( ( line = readFile.readLine() ) != null) {
			
			for(int i = 0; i < line.length(); i++) {
				
				String encodeForChar = this.encodeNoEOF.get(line.charAt(i) + "");
				if (encodeForChar == null)
					throw new RuntimeException("Can't find encode for value '" + line.charAt(i) + "' !");
				writeFile.write(encodeForChar);
			}
		}
		writeFile.close();
		readFile.close();
	}
	public void compressWritingEncode(String filename_toCompress) throws IOException { compressWritingEncode(filename_toCompress, "notReal_CompressedFile.txt"); }

	
	//Return a Decoder Objects used for decoding files with char that have same frequency
	public Decoder Decoder() { return new Decoder(this.itemsFrequency); }
}
