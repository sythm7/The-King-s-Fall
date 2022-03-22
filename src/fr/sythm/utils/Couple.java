package fr.sythm.utils;

public class Couple<E1, E2> {
	
	private E1 firstElement;
	
	private E2 secondElement;
	
	public Couple(E1 firstElement, E2 secondElement) {
		this.setFirstElement(firstElement);
		this.setSecondElement(secondElement);
	}

	public E1 getFirstElement() {
		return firstElement;
	}

	public void setFirstElement(E1 firstElement) {
		this.firstElement = firstElement;
	}

	public E2 getSecondElement() {
		return secondElement;
	}

	public void setSecondElement(E2 secondElement) {
		this.secondElement = secondElement;
	}
	
	@Override
	public int hashCode() {
		return (this.firstElement.hashCode() + this.secondElement.hashCode()) * 31;
	}

	
	// 2 couples are the same if the first and second elements are equals, but also if the first equals the other second and the other first equals the second.
	// For example (5,1) and (1,5) are equals.
	public boolean equals(Object obj) {
		
		if(! (obj instanceof Couple))
			return false;
		
		Couple<?, ?> couple = (Couple<?, ?>) obj;
		
		return this.firstElement.equals(couple.firstElement) && this.secondElement.equals(couple.secondElement) || 
				this.firstElement.equals(couple.secondElement) && this.secondElement.equals(couple.firstElement);
	}
	
}
