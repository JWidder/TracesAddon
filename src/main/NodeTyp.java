package main;

public class NodeTyp {
	public NodeTyp(String name) {
		this.name = name;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String inName) {
		name = inName;
	}
	
	@Override
	public String toString(){return this.name;}
}
