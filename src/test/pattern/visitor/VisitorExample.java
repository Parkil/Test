package test.pattern.visitor;

//Vistor
interface CarElementVisitor {
	void visit(Wheel wheel);
	void visit(Engine engine);
	void visit(Body body);
	void visit(Car car);
}

// Elements
interface CarElement {
	void accept(CarElementVisitor visitor); // CarElements have to provide
											// accept().
}

class Wheel implements CarElement {
	private final String name;

	public Wheel(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public void accept(CarElementVisitor visitor) {
		/*
		 * accept(CarElementVisitor) in Wheel implements
		 * accept(CarElementVisitor) in CarElement, so the call to accept is
		 * bound at run time. This can be considered the first dispatch.
		 * However, the decision to call visit(Wheel) (as opposed to
		 * visit(Engine) etc.) can be made during compile time since 'this' is
		 * known at compile time to be a Wheel. Moreover, each implementation of
		 * CarElementVisitor implements the visit(Wheel), which is another
		 * decision that is made at run time. This can be considered the second
		 * dispatch.
		 */
		visitor.visit(this);
	}
}

class Engine implements CarElement {
	@Override
	public void accept(CarElementVisitor visitor) {
		visitor.visit(this);
	}
}

class Body implements CarElement {
	@Override
	public void accept(CarElementVisitor visitor) {
		visitor.visit(this);
	}
}

class Car implements CarElement {
	CarElement[] elements;

	public Car() {
		// create new Array of elements
		this.elements = new CarElement[]{new Wheel("front left"),
				new Wheel("front right"), new Wheel("back left"),
				new Wheel("back right"), new Body(), new Engine()};
	}

	@Override
	public void accept(CarElementVisitor visitor) {
		for (CarElement elem : elements) {
			elem.accept(visitor);
		}
		// visitor.visit(this);
	}
}

class CarElementPrintVisitor implements CarElementVisitor {
	@Override
	public void visit(Wheel wheel) {
		System.out.println("Visiting " + wheel.getName() + " wheel");
	}

	@Override
	public void visit(Engine engine) {
		System.out.println("Visiting engine");
	}

	@Override
	public void visit(Body body) {
		System.out.println("Visiting body");
	}

	@Override
	public void visit(Car car) {
		System.out.println("Visiting car");
	}
}

class CarElementDoVisitor implements CarElementVisitor {
	@Override
	public void visit(Wheel wheel) {
		System.out.println("Kicking my " + wheel.getName() + " wheel");
	}

	@Override
	public void visit(Engine engine) {
		System.out.println("Starting my engine");
	}

	@Override
	public void visit(Body body) {
		System.out.println("Moving my body");
	}

	@Override
	public void visit(Car car) {
		System.out.println("Starting my car");
	}
}

/*
 * Element - 실행 주체(실행될 동작 선택)
 * Visitor - Element에서 실행되는 실제 로직(선택된 동작에서 수행할 로직)
 * 
 * 서버에 비유
 * Element - 서버동작(accept,bind,read,write......)
 * Visitor - 동작에 따른 실제 로직(socket생성, buffer생성, read/write 수행......)
 */
public class VisitorExample {
	static public void main(String[] args) {
		Wheel car = new Wheel("wheel-");
		car.accept(new CarElementPrintVisitor());
		car.accept(new CarElementDoVisitor());
	}
}
