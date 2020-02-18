package target;

public class ServiceApp {
	int srcX = 1000, secY = 1000;
	String srcID = "Service";
void move(int dx, int dy,String str) {
		
	}


	public static void main(String[] args)
	{
		System.out.println("[Service App] Run...");
		ServiceApp a = new ServiceApp();
		a.move(0,0,null);
		System.out.println("[Service App] Done.");
	}

}