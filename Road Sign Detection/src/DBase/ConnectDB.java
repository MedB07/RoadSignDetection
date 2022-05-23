package DBase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.opencv.features2d.KeyPoint;
import org.opencv.core.Mat;
import java.sql.Statement;

public class ConnectDB {
	static Connection connection= null;
	static String dbName = "";
	static String url="jdbc:mysql://localhost:3306/twizy?serverTimezone=UTC";
	static String userName = "root";
	static String password;
	 static String portNumber = "3306";
	 static String serverName = "localhost";
	 
	/**
	 * instance du patron singleton
	 */
	 static ConnectDB instance;

	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//createTable();
		//Connection con = getConnection();
		//remplirKeypoints("ref50.jpg",con);
		createallkeypoints();
		//resultFindkeypoints("ref30.jpg",con);
		
	}
	 public static Connection getConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
	
		 Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			Scanner scanner1 = new Scanner(System.in);
			Scanner scanner2 = new Scanner(System.in);
			System.out.println("Entrez votre userName MySQL :");
			userName=scanner2.next();
			System.out.println("Entrez votre mot de passe MySQL :");
			password = scanner1.next();
			connection=DriverManager.getConnection(url,userName,password);
			//PreparedStatement ps=connection.prepareStatement("INSERT INTO `studentdb`.student(`name`) VALUES ('mERYOUMA');");
			/*int status=ps.executeUpdate();
			if (status!=0) {
				System.out.println("db connect and record inserted ");
			}*/
			return connection;
}
	 public static Connection getConnectionING(String user,String pass) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
			
		 Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		 System.out.println(user);
		 System.out.println(pass);
			connection=DriverManager.getConnection(url,user,pass);
			return connection;
}
	 public static void createTable() throws Exception{
		 Connection con = getConnection();
		 PreparedStatement ps=con.prepareStatement("CREATE TABLE IF NOT EXISTS Keypoints(id int NOT NULL AUTO_INCREMENT,panneauref varchar(255),x double NOT NULL,y double NOT NULL,size double NOT NULL,angle double NOT NULL,response double NOT NULL,octave double NOT NULL,PRIMARY KEY(id))");
		 int status=ps.executeUpdate();
			if (status!=0) {
				System.out.println("db connect and record inserted ");
			}
			System.out.println("db created and record inserted ");
		
}
	 public static void remplirKeypoints(String ref,Connection con) throws Exception{
		 List<Keypoints>  kypgenereted = new ArrayList<Keypoints>();
		 kypgenereted=Keypoints.createkeypoints(ref);
		 double response,x,y,size,octave,angle;
		 String name;
		 for(Keypoints key:kypgenereted) {
			 name=key.ref;
			 x=key.x;
			 y=key.y;
			 size=key.size;
			 angle=key.angle;
			 response=key.response;
			 octave=key.octave;
			 Keypoints keyDB = new Keypoints(name,x,y,size,angle,response,octave);
			 key.Savekeypoints(con);
		 }
		 
}
	 public static void deletetable(Connection con) throws SQLException {
		 Statement stmt = con.createStatement();
		 String sql = "DROP TABLE Keypoints";
         stmt.executeUpdate(sql);
         System.out.println("Table deleted in given database..."); 
		 }
	 public static void createallkeypoints() throws Exception {
	 List<String>  allpannels = new ArrayList<String>();
	 allpannels.add("ref30.jpg");
	 allpannels.add("ref50.jpg");
	 allpannels.add("ref70.jpg");
	 allpannels.add("ref90.jpg");
	 allpannels.add("ref110.jpg");
	 allpannels.add("refdouble.jpg");
	 allpannels.add("train1.jpg");
	 System.out.println(allpannels);
	 	Connection con = ConnectDB.getConnection();
	 	ConnectDB.deletetable(con);
	 	createTable();
	 	for(String pannel:allpannels) {
	 		ConnectDB.remplirKeypoints(pannel,con);
	 	}
	 
	 }
	 public static KeyPoint[]  resultFindkeypoints(String pannel,Connection con) throws Exception {
		 	//Connection con = ConnectDB.getConnection();
		 	String SQLPrep = "select *from Keypoints where panneauref='"+pannel+"';";
		 	//Creating a Statement object
		      Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                      ResultSet.CONCUR_UPDATABLE);
		      ResultSet rs = stmt.executeQuery(SQLPrep);
		      int sizers=sizers(rs);
			KeyPoint f;
			KeyPoint[]  result = new KeyPoint[sizers];
			int i=0;
			while (rs.next()) {
				int id = rs.getInt("id");
				float x = rs.getFloat("x");
				float y = rs.getFloat("y");
				float size = rs.getFloat("size");
				float angle = rs.getFloat("angle");
				float response = rs.getFloat("response");
				float octave = rs.getFloat("octave");
				f = new KeyPoint((float)x, (float)y, (float)size, (float)angle, (float)response, (int) octave,-1);
				result[i]=f;
				i=i+1;
				System.out.println("  -> (" + id + ") " +"Coordonnées: "+ x+";"+y +" size: "+size+" angle: "+angle+" response: "+response+" octave: "+octave);
			}
			return result;
		 }
	 public static int sizers(ResultSet rs) throws SQLException {
	 int count = 0;

	 while (rs.next()) {
	     ++count;
	     // Get data from the current row and use it
	 }

	 if (count == 0) {
	     System.out.println("No records found");
	 }
	 rs.beforeFirst();
	return count;
	 }	 
	 
}
