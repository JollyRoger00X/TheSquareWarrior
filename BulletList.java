package squarewarrior;


public class BulletList {
	private Bullet head = null;
	private int length = 0;
	private boolean empty = true;

	
	public BulletList(){
		
	}
	

	public int getLength(){
		return length;
	}
	
	public boolean empty(){
		return empty;
	}
	
	public Bullet getHead(){
		return head;
	}
	
	/**
	 * returns the bullet at the top of the stack of bullets
	 * @return the head of the bullet stack
	 */
	public Bullet pop(){
		if(head == null)
			return null;
		else {
			Bullet ths = head;
			head = head.getNext();
			length--;
			if(head == null) empty = true;
			return ths;
		}
	}
	
	/**
	 * Adds a bullet to the list of bullets
	 * @param b the bullet to be added
	 */
	public void push(Bullet b){
		if(b!=null){
			b.setNext(head);
			head = b;
			length++;
			empty = false;
		}
	}
}
