package fitChoice;

public class TestConnect {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            Connect db = Connect.getInstance();

            String query = "SELECT 1";
            if (db.execQuery(query)!=null) {
                System.out.println("success!");
            }else {
                System.out.println("failed!");
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}
