package compressor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.lang.Math;


import tree.Tree;
import tree.BinaryNode;

public class HuffmanEncode {

	//Return a List of EncodeType where for each element it's own encode
	public static List<EncodeType> createEncodeList(Map<String, Integer> itemsFrequency){
		
		return createEncode(itemsFrequency);
	}
	
	
	//Return a Map of valKey -> encode where for each valKey/element its own encode
	public static Map<String, String> createEncodeMap(Map<String, Integer> itemsFrequency){
		
		var encodeList = createEncode(itemsFrequency);
		
		var returnMap = new HashMap<String, String>();
		for (var x : encodeList)
			returnMap.put(x.getValue(), x.getEncode());
		
		return returnMap;
	}
	
	
	//Return the shannon entropy
	public static Double ShannonEntropy(Map<String, Integer> itemsFrequency) {
		if (itemsFrequency.size() == 0)
			return 0.0;
		
		Set<Entry<String, Integer>> itemsSet = itemsFrequency.entrySet();
		
		Double freqSum = 0.0;
		for ( var x : itemsSet)
			freqSum += x.getValue();
		
		Double ShannonEntropy = 0.0;
		for ( var x : itemsSet) {
			Double Pr = x.getValue() / freqSum;
			Double logArg = 1 / Pr;
			Double log2 = Math.log(logArg) / Math.log(2);	// change logarithm base using  log_a (b) = ( log_c (b) / log_c (a) )
			ShannonEntropy += (Pr * log2);
		}
		
		return ShannonEntropy;
	}
	
	
	//Return expected Lenght (itemsFrequency and itemsEncode must be Maps with same keys or Exception is thrown)
	public static Double expectedLenght(Map<String, Integer> itemsFrequency, Map<String, String> itemsEncode) {
		if (itemsFrequency.size() == 0 || itemsEncode.size() == 0)
			return 0.0;
		if (itemsFrequency.size() != itemsEncode.size())
			throw new IllegalArgumentException("The two list should have same keys");
		
		Set<Entry<String, Integer>> itemsFreqSet = itemsFrequency.entrySet();
		
		Double freqSum = 0.0;
		for ( var x : itemsFreqSet)
			freqSum += x.getValue();
		
		Double expectedLenght = 0.0;
		for ( var x : itemsFreqSet) {
			Double Pr = x.getValue() / freqSum;
			String encode = itemsEncode.get(x.getKey());
			if (encode == null)
				throw new RuntimeException("The two list should have same keys, error for key '" + encode + "'");
			
			expectedLenght += Pr * ( (double) encode.length() );
		}

		return expectedLenght;
	}
	
	
//	****************************  PRIVATE/AUXILIAR STATIC METHOD  ****************************

	//Return a List of EncodeType where for each element it's own encode
	//private static method that call all the aux method in order
	private static List<EncodeType> createEncode(Map<String, Integer> itemsFrequency){
		var OrderedItemsList = fillElementInOrder(itemsFrequency);
		var HuffTree = createEncodeTree(OrderedItemsList);
		writeEncodeInHuffTree(HuffTree);
		
		return OrderedItemsList;
	}
	
	
	//Return a list of type StringIntType ordered by frequency
	//null -> is returned if there aren't items in the map passed as argument or if its null
	private static List<EncodeType> fillElementInOrder(Map<String, Integer> itemsFrequency){
		if (itemsFrequency.size() == 0 || itemsFrequency == null )
			return null;
		
		//Create the return list and making itemsFrequency dictionary iterable using Set
		List<EncodeType> returnList = new LinkedList<>();
		Set<Entry<String, Integer>> itemsSet = itemsFrequency.entrySet();
		
		//Fill the list
		var itemsIterator = itemsSet.iterator();
		while(itemsIterator.hasNext()) 
		{
			var x = itemsIterator.next();
			EncodeType aux = new EncodeType(x.getKey(), x.getValue());
			returnList.add(aux);
		}

		//Sort the list
		returnList = selSort(returnList);
		
		return returnList;
	}
	
	
	//Simple selectionSort sorting by frequency
	//All frequency value of the list must be != null or an Exception will be thrown
	private static List<EncodeType> selSort(List<EncodeType> listToSort){	
		for (int i = 0; i < listToSort.size() - 1; i++) {
			for (int j = i + 1; j < listToSort.size(); j++) {
				if(listToSort.get(i).getFrequency() > listToSort.get(j).getFrequency()) {
					var aux_i = listToSort.get(i);
					listToSort.set(i, listToSort.get(j));
					listToSort.set(j, aux_i);
				}
				
			}
		}
		
		return listToSort;
	}
	
	
	//Return the Huffman Encode Tree without the encode for each leaf
	//Keep notice that the tree returned is a tree where the payload of each leaf contains a 'pointer' to the corresponding node in EncodeList
	//Graphical example:
	// EncodeList = { val=first freq=1 encod=null;  val=second freq=1 encod=null }
	//
	//	Tree:					nodeNull			nodeNull -> is the root node that has payload-> value = null, frequency = 2, encode=null
	//						  /			\
	//					firstNode	  secondNode	firstNode -> is a leaf and its payload is the node of the list with val=first
	//												firstNode -> is a leaf and its payload is the node of the list with val=second
	private static Tree<EncodeType> createEncodeTree(List<EncodeType> EncodeList){
		//Encode list must have elements
		if(EncodeList == null || EncodeList.size() == 0)
			throw new IllegalArgumentException();
		
		List<BinaryNode<EncodeType>> auxList = new LinkedList<>();
		for (var x : EncodeList) 
			auxList.add(new BinaryNode<EncodeType>(x));

		//Creating the tree
		while(auxList.size() > 1) {
			var aux_1 = auxList.get(0);
			var aux_2 = auxList.get(1);
				
			//Creating payload for the tree node (value of payload is null when the node is not a leaf),
			//connect together the two node with lower frequency 
			var auxEncodeType = new EncodeType(null, aux_1.getPayload().getFrequency() + aux_2.getPayload().getFrequency());
			var auxNode = new BinaryNode<>(auxEncodeType, aux_1, aux_2);
			
			//After connect them together remove them from the list
			auxList.remove(0);
			auxList.remove(0);
			
			//After removing the two node, insert the new node in auxList, in order by frequency
			if (auxList.size() == 0)
				auxList.add(0, auxNode);
			else {
				int i = 0;
				while(i < auxList.size() && (auxNode.getPayload().getFrequency() > auxList.get(i).getPayload().getFrequency())) {
					i += 1;
				}
				auxList.add(i, auxNode);
			}
		}
		
		//return the created tree without the encode for each leaf, 
		//the element of the list auxList at index 0, after the while, is the root of the tree
		return new Tree<EncodeType>(auxList.get(0));
	}

	
	//Write the Encode for each leaf in the Huffman Tree before invoking this method make sure to create the the tree invoking createEncodeTree
	private static void writeEncodeInHuffTree(Tree<EncodeType> tree) {
		var root = tree.getRoot();
		
		if(root.getPayload().getValue() != null)
			root.getPayload().setEncode("1");
		else {
			writeEncodeAuxRec(root, "");
		}
	}
	
	
	//Aux method used by createEncodeTree for writing the Encode for each leaf in the Huffman Tree visiting it using recursion
	private static void writeEncodeAuxRec(BinaryNode<EncodeType> node, String encode) {
		//Base case
		if (node == null)
			return;
		if (node.getPayload().getValue() != null) {
			node.getPayload().setEncode(encode);
			return;
		}
		
		writeEncodeAuxRec(node.getLeft(), new String(encode + "0"));
		writeEncodeAuxRec(node.getRight(), new String(encode + "1"));
	}

}
