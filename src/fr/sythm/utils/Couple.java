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
	
}
