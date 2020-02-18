package target;

public class ComponentApp {
	int comX = 10, comY = 10;
	String comID = "Component";
	
	void move(int dx, int dy,String str) {
		
	}


	public static void main(String[] args)
	{
		System.out.println("[Component App] Run...");
		ComponentApp a = new ComponentApp();
		a.move(0,0,null);
		System.out.println("[Component App] Done.");
	}

}