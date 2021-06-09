package compressor.decoder;

import compressor.FileOpener;
import compressor.HuffmanEncode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Decoder {
	private final Map<String, String> encode;	//encode map with char 'eof'
	
	//Constructor
	public Decoder(Map<String, Integer> itemsFrequency) {
		Objects.requireNonNull(itemsFrequency);
		if(itemsFrequency.get("eof") == null || itemsFrequency.get("eof") != 1)
			throw new IllegalArgumentException("ItemsFrequency must have key 'eof' with value 1 !");
		
		var auxencode = HuffmanEncode.createEncodeMap(itemsFrequency);
		var set = auxencode.entrySet();
		this.encode = new HashMap<String, String>();
		for (var x : set) {
			if(this.encode.containsKey(x.getValue()))
				throw new RuntimeException();
			this.encode.put(x.getValue(), x.getKey());
		}
	}

	
	//Decode file named filename_toDecode in the file named filename_Decoded
	public void decode(String filename_toDecode, String filename_Decoded) throws IOException {
		Objects.requireNonNull(filename_toDecode);
		Objects.requireNonNull(filename_Decoded);
		
		var readFile = FileOpener.openRead_asByteStream(filename_toDecode);
		var writeFile = FileOpener.openWrite(filename_Decoded);
		
		String buffer = "";
		byte[] line = readFile.readNBytes(30);
		int index = 0;
		do {
			for(byte x : line) {
				while (index != 8) {
					byte auxbuf = 0x00;
						
					auxbuf |= (byte) 1;
					auxbuf = (byte) (auxbuf << 7);
						
					byte aux = (byte) (auxbuf & x);
						
					if(aux == auxbuf)
						buffer = buffer + "1";
					else
						buffer = buffer + "0";
						
					x = (byte) (x << 1);
					index += 1;
						
					if (this.encode.get(buffer) != null) {
						if(this.encode.get(buffer).equals("eof")) {
							readFile.close();
							writeFile.close();
							return;
						}
						writeFile.write(this.encode.get(buffer));
						buffer = "";
					}
				}
				index = 0;
			}
		}while( (line = readFile.readNBytes(30)).length > 0);
		writeFile.write(this.encode.get(buffer));
		readFile.close();
		writeFile.close();
		throw new RuntimeException("end of file not found 'eof'");
	}
	public void decode(String filename_toDecode) throws IOException { decode(filename_toDecode, "file_Decoded.txt"); }
}
