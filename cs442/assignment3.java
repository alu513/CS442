

import java.sql.*;

//Alexander Lu, alu1, I pledge my Honor that I have abided by the Stevens Honor System

public class assignment3 {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306?allowPublicKeyRetrieval=true&useSSL=false";

    //  Database credentials
    static final String USER = "root";
    //the user name; You can change it to your username (by default it is root).
    static final String PASS = "strikers";
    //the password; You can change it to your password (the one you used in MySQL server configuration).


    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 1: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 2: Open a connection to database
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            System.out.println("Creating database...");
            stmt = conn.createStatement();

            // Delete old DB
            String sql = "DROP DATABASE IF EXISTS BoatRental";
            stmt.executeUpdate(sql);

            //STEP 3: Use SQL to Create Database;
            sql = "CREATE DATABASE BoatRental";
            stmt.executeUpdate(sql);
            System.out.println("Database created successfully...");

            //STEP 4: Use SQL to select the database;
            sql = "use BoatRental";
            stmt.executeUpdate(sql);

            //STEP 5: Use SQL to create Tables;
            //STEP 5.1: Create Table Sailor;
            sql = "create table sailor( sid integer not null PRIMARY KEY, " + "sname varchar(20) not null,"
                    + "rating real not null," + "age integer)";
            stmt.executeUpdate(sql);

            //STEP 5.2: Create Table Boats;
            sql = "create table boats(bid integer not null PRIMARY KEY," + "bname varchar(40) not null,"
                    + "color varchar(40))";
            stmt.executeUpdate(sql);

            //STEP 5.3: Create Table Reserves;
            sql = "CREATE TABLE reserves(sid integer not null, bid integer not null, day date, FOREIGN KEY (sid) REFERENCES sailor(sid), FOREIGN KEY (bid) REFERENCES boats(bid), PRIMARY KEY (sid, bid, day));";
            stmt.executeUpdate(sql);

            //STEP 6: Use SQL to insert tuples into tables;
            //STEP 6.1: insert tuples into Table Sailor;
            sql = "insert into sailor values(22, 'Dustin', 7, 45)";
            stmt.executeUpdate(sql);

            sql = "insert into sailor values(29, 'Brutus', 1, 33)";
            stmt.executeUpdate(sql);

            sql = "insert into sailor values(31, 'Lubber', 8, 55)";
            stmt.executeUpdate(sql);

            sql = "insert into sailor values(32, 'Andy', 8, 26)";
            stmt.executeUpdate(sql);

            sql = "insert into sailor values(58, 'Rusty', 10, 35)";
            stmt.executeUpdate(sql);

            sql = "insert into sailor values(64, 'Horatio', 7, 35)";
            stmt.executeUpdate(sql);

            sql = "insert into sailor values(71, 'Zorba', 20, 18)";
            stmt.executeUpdate(sql);

            sql = "insert into sailor values(74, 'Horatio', 9, 35)";
            stmt.executeUpdate(sql);

            //STEP 6.2: insert tuples into Table Boats;
            sql = "insert into boats values(101, 'Interlake', 'Blue')";
            stmt.executeUpdate(sql);

            sql = "insert into boats values(102, 'Interlake', 'Red')";
            stmt.executeUpdate(sql);

            sql = "insert into boats values(103, 'Clipper', 'Green')";
            stmt.executeUpdate(sql);

            sql = "insert into boats values(104, 'Marine', 'Red')";
            stmt.executeUpdate(sql);


            //STEP 6.3: insert tuples into Table Reserves;
            sql = "insert into reserves values(22, 101, '2018-10-10')";
            stmt.executeUpdate(sql);

            sql = "insert into reserves values(22, 102, '2018-10-10')";
            stmt.executeUpdate(sql);

            sql = "insert into reserves values(22, 103, '2017-10-8')";
            stmt.executeUpdate(sql);

            sql = "insert into reserves values(22, 104, '2017-10-9')";
            stmt.executeUpdate(sql);

            sql = "insert into reserves values(31, 102, '2018-11-10')";
            stmt.executeUpdate(sql);

            sql = "insert into reserves values(31, 103, '2018-11-6')";
            stmt.executeUpdate(sql);

            sql = "insert into reserves values(31, 104, '2018-11-12')";
            stmt.executeUpdate(sql);

            sql = "insert into reserves values(64, 101, '2018-4-5')";
            stmt.executeUpdate(sql);

            sql = "insert into reserves values(64, 102, '2018-9-8')";
            stmt.executeUpdate(sql);

            sql = "insert into reserves values(74, 103, '2018-9-8')";
            stmt.executeUpdate(sql);

            //STEP 7: Use SQL to ask queries and retrieve data from the tables;
            Statement s;
            ResultSet rs;

            //Q1:  Find the name of all sailors who have reserved red boats but not green boats before June 1, 2018.
            System.out.println("Q1");
            s = conn.createStatement();
            rs = s.executeQuery("SELECT s.sname FROM sailor s, boats b, reserves r WHERE s.sid = r.sid AND b.bid = r.bid AND b.color='Red' AND r.day <= '2018-6-1' AND s.sid NOT IN (SELECT r.sid FROM sailor s, boats b, reserves r WHERE s.sid = r.sid AND b.bid = r.bid AND b.color='Green');");
            while (rs.next()) {
                String nameVal = rs.getString("sname");
                System.out.println(nameVal);
            }
            rs.close();
            s.close();

            //Q2:  Find the names of sailors who never reserved a red boat.
            System.out.println("Q2");
            s = conn.createStatement();
            rs = s.executeQuery("SELECT DISTINCT sname FROM sailor s WHERE s.sid NOT IN (SELECT r.sid FROM sailor s, boats b, reserves r WHERE s.sid = r.sid AND b.bid = r.bid AND b.color='Red');");
            while (rs.next()) {
                String nameVal = rs.getString("sname");
                System.out.println(nameVal);
            }
            rs.close();
            s.close();

            //Q3:  Find sailors whose rating is better than all the sailors named Horatio.
            System.out.println("Q3");
            s = conn.createStatement();
            rs = s.executeQuery("SELECT * FROM sailor s WHERE s.rating > ALL (SELECT s2.rating FROM sailor s2 WHERE s2.sname = 'Horatio');");
            while (rs.next()) {
                int idVal = rs.getInt("sid");
                String nameVal = rs.getString("sname");
                float rateVal = rs.getFloat("rating");
                int ageVal = rs.getInt("age");
                System.out.println("sid = " + idVal + ", name = " + nameVal + ", rating = " + rateVal + ", age = " + ageVal);
            }
            rs.close();
            s.close();

            //Q4:  Find the names of sailors who have reserved all the boats.
            System.out.println("Q4");
            s = conn.createStatement();
            rs = s.executeQuery("SELECT s.sname FROM sailor s WHERE NOT EXISTS (SELECT b.bid FROM boats b WHERE NOT EXISTS(SELECT r.bid FROM reserves r WHERE r.bid = b.bid AND r.sid = s.sid))" );
            while (rs.next()) {
                String nameVal = rs.getString("sname");
                System.out.println(nameVal);
            }
            rs.close();
            s.close();

            //Q5:  Find the name of the sailors who have made the maximum number of reservations among all sailors who have reserved red boats.
            System.out.println("Q5");
            s = conn.createStatement();
            rs = s.executeQuery("SELECT DISTINCT sname FROM sailor s, reserves r WHERE s.sid = r.sid AND r.sid = (SELECT r.sid FROM reserves r GROUP BY r.sid HAVING COUNT(*) = (SELECT MAX(reserveCount) FROM (SELECT COUNT(r.sid) AS reserveCount FROM reserves r WHERE r.sid IN (SELECT r.sid FROM sailor s, boats b, reserves r WHERE s.sid = r.sid AND b.bid = r.bid AND b.color='Red') GROUP BY r.sid) t1))" );
            while (rs.next()) {
                String nameVal = rs.getString("sname");
                System.out.println(nameVal);
            }
            rs.close();
            s.close();



        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            } // nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } //end finally try
        } //end try
    }//end main
}//end JDBCExample