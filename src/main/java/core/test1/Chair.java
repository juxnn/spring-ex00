package core.test1;


public class Chair {
	private Leg leg;

	@Override
	public String toString() {
		return "Chair [leg=" + leg + "]";
	}

	public Chair(Leg leg) {
		this.leg = leg;
	}

	public Chair() {

	}
	
	
}
