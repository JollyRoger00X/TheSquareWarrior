package squarewarrior;

public class PlatformList {
	Platform head;
	private int length;
	
	public PlatformList(){
		length=0;
		head = null;
	}
	
	public Platform head(){
		return head;
	}
	
	public void push(Platform p){
		p.setNext(head);
		head = p;
		length++;
	}
	
	public Platform pop(){
		if(head == null)
			return null;
		else{
			Platform p = head;
			head = head.getNext();
			length--;
			return p;
		}
	}
	
	public int getLength(){
		return length;
	}

}
