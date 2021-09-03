package dao;

public class ConnectionUtility {
    public static String url = "jdbc:postgresql://" + System.getenv("PostgresDb_Url") + "/ers_project1_db";
    public static String username = System.getenv("PostgresDb_Username");
    public static String password = System.getenv("PostgresDb_Password");
}
