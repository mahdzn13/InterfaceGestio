import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.*;
import java.util.*;

class Preu{
    int idPreu;
    int idProducte;
    String dataAlta;
    String dataBaixa;
    Double preu;
}
class Producte{
    int idProducte;
    String descripcio;

    @Override
    public String toString() {
        return descripcio;
    }
}
public class Database{
    private static Connection connection;
    private static final String dbClassName = "com.mysql.jdbc.Driver";
    private static String CONNECTION = "";

    public Database() throws Exception{
        SimpleXML xml = new SimpleXML(new FileInputStream("/home/mahernandezd/Escriptori/xmlTest/database.xml"));
        Document doc = xml.getDoc();
        Element root = doc.getDocumentElement();

        //Extraccion datos XMLfile
        Element servidor = xml.getElement(root,"server");
        String server = servidor.getTextContent();
        Element usuario= xml.getElement(root, "username");
        String username = usuario.getTextContent();
        Element contrasena = xml.getElement(root, "password");
        String password = contrasena.getTextContent();

        //System.out.println(password + " " + username + " " + server);

        CONNECTION += "jdbc:mysql://"+server+"/gestio_d";
        Class.forName(dbClassName);
        Properties p = new Properties();
        p.put("user", username);
        p.put("password", password);
        connection = DriverManager.getConnection(CONNECTION, p);

    }

    public List<Preu> allPrices() throws Exception {
        String sql = "SELECT * from PREU";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        List<Preu> lp = new ArrayList<>();
        while (rs.next()){
            Preu p = new Preu();
            p.idPreu = rs.getInt("id_preu");
            p.idProducte = rs.getInt("id_producte");
            p.dataAlta = rs.getString("data_alta");
            p.dataBaixa = rs.getString("data_baixa");
            p.preu = rs.getDouble("preu");
            lp.add(p);
        }
        return lp;
    }

    public void nouPreu(int idProducto,String fechaA,String fechaB,double precio)
            throws ClassNotFoundException,SQLException {
        String sql;
        int idPrecio= maxCodiPreu()+1;

            sql="INSERT INTO PREU SET id_preu=?,id_producte=?,data_alta=?,data_baixa=?,preu=?";
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setInt(1,idPrecio);
            ps.setInt(2, idProducto);
            ps.setString(3,fechaA);
            ps.setString(4, fechaB);
            ps.setDouble(5, precio);
            ps.execute();
    }

    public void deleteAll() throws Exception{
        List<Preu> lp = allPrices();
        for (int i = 0; i < lp.size(); i++) {
            String sql = "DELETE FROM PREU WHERE id_preu=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,lp.get(i).idPreu);
            ps.execute();
        }
    }
    public void actualitzarPreu(int id,int idP,String fA,String fB,double preu) throws ClassNotFoundException,SQLException {
        String sql;
        sql = "UPDATE PREU SET id_producte=?,data_alta=?,data_baixa=?,preu=? WHERE id_preu=?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1,idP);
        ps.setString(2,fA);
        ps.setString(3, fB);
        ps.setDouble(4,preu);
        ps.setInt(5,id);

        ps.execute();
    }
    public List<Preu> visualitzaPreu(int id_preu)
            throws ClassNotFoundException,SQLException {
        List<Preu> lp = new ArrayList<>();

        //Seleccionar precio
        String sql = "select id_preu,id_producte,data_alta,data_baixa,preu FROM PREU " +
                "WHERE PREU.id_preu=?";

        //Preparacion del SQL
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id_preu);
        //Ejecucion
        ResultSet rs = preparedStatement.executeQuery();


        while (rs.next()){
            Preu p = new Preu();
            p.idPreu = rs.getInt("id_preu");
            p.idProducte = rs.getInt("id_producte");
            p.dataAlta = rs.getString("data_alta");
            p.dataBaixa = rs.getString("data_baixa");
            p.preu = rs.getDouble("preu");
            lp.add(p);
        }
        return lp;
    }
    //Maximo codigoPrecio
    public int maxCodiPreu() throws ClassNotFoundException,SQLException {
        String sql="select max(id_preu) as max from PREU";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        return rs.getInt("max");
    }
    //Borrar un precio
    public void borrarPreu(int id) throws ClassNotFoundException,SQLException {
        String sql;
        sql = "DELETE FROM PREU WHERE id_preu=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1,id);
        ps.execute();
    }

    public int trobarIDproducte(String nom) throws Exception{
        String sql = "SELECT id_producte from PRODUCTE WHERE descripcio = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, nom);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()){
            int id = rs.getInt("id_producte");
            System.out.println(id);
            return id;
        }
        return 0;
    }
    public double devolverPrecio(int id) throws Exception{
        String sql = "SELECT preu from PREU WHERE id_preu=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,id);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()){
            double preu = rs.getInt("preu");
            System.out.println(preu);
            return preu;
        }
        return 0;
    }
    public List<Producte> extraerProductos()
            throws ClassNotFoundException,SQLException {
        String sql = "select id_producte,descripcio FROM PRODUCTE";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();

        List<Producte> lp = new ArrayList<>();
        while (rs.next()){
            Producte p = new Producte();
            p.idProducte = rs.getInt("id_producte");
            p.descripcio = rs.getString("descripcio");
            lp.add(p);
        }
        return lp;
    }

}
