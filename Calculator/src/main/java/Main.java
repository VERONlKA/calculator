import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        int i = 0;
        int id = 1;
        String idexp = "";
        String exp = "";
        String res = "";
        ResultSet rs = null;

        Scanner scan = new Scanner(System.in);
        do {
            System.out.println("1. Input expression\n2. Show history\n3. Edit expression\n4. Find expression by result");
            i = scan.nextInt();
            scan.nextLine();
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", "root", "toor#81");

                Statement statement = connection.createStatement();
            switch (i) {
                case 1:
                    System.out.println("Input expression");
                    exp = scan.nextLine();
                    res = String.valueOf(Conventer.evaluate(exp));
                    String query = ("SELECT * FROM expression ORDER BY idexpression DESC LIMIT 1;");
                    rs = statement.executeQuery(query);
                    if (rs.next()) {
                        id = rs.getInt("idexpression");
                        id++;}
                    statement.executeUpdate("insert into expression" + "(idexpression, expression, result)" + "values('" + id + "','" + exp + "', '" + res + "')");
                    break;
                case 2:
                    System.out.println("History of calculation");
                    rs = statement.executeQuery("select * from expression");
                    while (rs.next()) {
                        System.out.println(rs.getString("idexpression")+" "+rs.getString("expression") + " = " + rs.getString("result"));
                    }
                    break;
                case 3:
                    System.out.println("Input id of expression");
                    idexp = scan.nextLine();
                    query = ("SELECT * FROM expression HAVING idexpression = '" + idexp + "' ;");
                    rs = statement.executeQuery(query);
                    if (rs.next()) {
                        String output = rs.getString("expression");
                        System.out.println(output);
                    }
                    System.out.println("Input edited expression");
                    exp = scan.nextLine();
                    res = String.valueOf(Conventer.evaluate(exp));
                    query = ("update expression set expression = '"+exp+"', result = '"+res+"' where idexpression = '"+idexp+"';");
                    statement.executeUpdate(query);
                    System.out.println("Record Updated!");
                    break;
                case 4:
                    int j = 0;
                           do {
                               System.out.println("Input number for search");
                               String str = scan.nextLine();
                               double d = Double.parseDouble(str);
                               res = Double.toString(d);
                               System.out.println("Choose operation:\n5. Search equal\n6. Search bigger\n7. Search less\n8. Back to menu");
                               j = scan.nextInt();
                               switch (j){
                               case 5:
                                   query = ("SELECT * FROM expression HAVING result = '" + res + "';");
                                   rs = statement.executeQuery(query);
                                   while (rs.next()) {
                                       System.out.println(rs.getString("expression"));
                                   }
                                   break;
                                   case 6:
                               query = ("SELECT * FROM expression GROUP BY expression HAVING result > '" + res + "';");
                               rs = statement.executeQuery(query);
                               while (rs.next()) {
                                   System.out.println(rs.getString("expression"));
                               }
                               break;
                                   case 7:
                                       query = ("SELECT * FROM expression GROUP BY expression HAVING result < '" + res + "';");
                                       rs = statement.executeQuery(query);
                                       while (rs.next()) {
                                           System.out.println(rs.getString("expression"));
                                       }
                               break;
                                   default:
                               break;}

                           }while(j<8);
                default:
                    break;
            } } catch (Exception e) {
                        e.printStackTrace();
            }
        } while (i<5);
    }
}
