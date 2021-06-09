package tree;

public class BinaryNode<T>{

	protected T payload;
	protected BinaryNode<T> left;
	protected BinaryNode<T> right;
	
	//Constructor method
	public BinaryNode(T payload, BinaryNode<T> left, BinaryNode<T> right) {
		this.payload = payload;
		this.left = left;
		this.right = right;
	}
	
	//Create a binary node with no left and right child ( = null)
	public BinaryNode(T payload) {
		this(payload, null, null);
	}
	
	//Create a binary node with no payload and no left and right child ( = null)
	public BinaryNode() {
		this(null, null, null);
	}
	
	//Setter method
	public void setPayload(T payload) { this.payload = payload; }
	public void setLeft(BinaryNode<T> leftChild) { this.left = leftChild; }
	public void setRight(BinaryNode<T> rightChild) { this.right = rightChild; }
	public void setChild(BinaryNode<T> leftChild, BinaryNode<T> rightChild) {
		this.left = leftChild;
		this.right = rightChild;
	}
	
	//Getter method
	public T getPayload() { return payload; }
	public BinaryNode<T> getLeft() { return left; }
	public BinaryNode<T> getRight() { return right; }
	
	public BinaryNode<T> deepCopy() { return new BinaryNode<T>(payload, left, right); }
	
	@Override
	public String toString() { return payload.toString(); }
}
