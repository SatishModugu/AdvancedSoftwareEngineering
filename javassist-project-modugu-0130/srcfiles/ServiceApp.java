import java.lang.reflect.Field;

public class ServiceApp {
   
   public static void main(String[] args) throws Exception {
      System.out.println("Run...");
      ServiceApp localMyApp = new ServiceApp();
      localMyApp.runComponent();
      System.out.println( Class.forName(args[0]).getField(args[1]).getName());
      System.out.println( Class.forName(args[0]).getField(args[1]).get( Class.forName(args[0]).newInstance()));
      System.out.println("Done.");
   }

   public void runComponent() {
      System.out.println("Called runService.");
   }
}