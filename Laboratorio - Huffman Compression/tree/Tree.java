package tree;

public class Tree<T> {
	public BinaryNode<T> root;
	
	public Tree(BinaryNode<T> root) {
		this.root = root;
	}
	
	public Tree() {
		this.root = null;
	}
	
	public BinaryNode<T> getRoot() { return root; }
	public void setRoot(BinaryNode<T> root) { this.root = root; }
	
	public void printTree() {
		printRecAux(root);
	}
	
	private void printRecAux(BinaryNode<T> node) {
		
		if(node != null)
		{
			printRecAux(node.getLeft());
			printRecAux(node.getRight());
			System.out.println(node.getPayload().toString());
		}
	}
}
