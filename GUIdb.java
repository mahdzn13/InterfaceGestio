import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GUIdb {
    private JFrame frame = new JFrame();
    private JButton precioID;
    private JTable table1;
    private JPanel panel;
    private JButton maxCodi;
    private JButton borrarPrecioButton;
    private JButton mostrarTodasButton;
    private JButton encontrarIDProductoButton;
    private JButton encontrarPrecioButton;
    private JButton insertarNuevoPrecioButton;
    private JButton actualizarPrecioButton;
    private JButton exportarXMLButton;
    private JButton importarXMLfileButton;

    public static void main() throws Exception{
        GUIdb form = new GUIdb();
        form.frame.setTitle("Gestio Preu Interface");

        form.frame.setContentPane(form.panel);
        form.frame.pack();
        ImageIcon img = new ImageIcon("src/cute_bear.png");

        form.frame.setIconImage(img.getImage());

        form.frame.setSize(new Dimension(800,600));
        form.frame.setVisible(true);
    }

    public GUIdb() throws Exception{
        showAll();

        //Actualizar tabla a tupla demandada
        precioID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                try {
                    String s = JOptionPane.showInputDialog(panel, "introduce ID del precio:");
                    int id = Integer.parseInt(s);
                    //Añadir nueva tabla con contenido
                    DefaultTableModel dtm = new DefaultTableModel();
                    dtm.addColumn("ID preu");
                    dtm.addColumn("ID producte");
                    dtm.addColumn("Data Alta");
                    dtm.addColumn("Data baixa");
                    dtm.addColumn("Preu");

                    //LLamar a la funcion que recoge todas las tuplas de la base de datos.
                    //Mediante una lista saca todos los valores de Precio
                    List<Preu> lp = Main.db.visualitzaPreu(id);
                    for (Preu aLp : lp) {
                        dtm.addRow(new Object[]{aLp.idPreu, aLp.idProducte, aLp.dataAlta, aLp.dataBaixa, aLp.preu});
                    }
                    //Añadir rows...
                    table1.setModel(dtm);
                    //Not editable
                    table1.setDefaultEditor(Object.class, null);

                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //Maximo codigo precio
        maxCodi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    //Sacar maximo codigo precio
                    int maxCodi = Main.db.maxCodiPreu();
                    JOptionPane.showMessageDialog(panel, "Maximo codigo: " + maxCodi);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //borrar tupla
        borrarPrecioButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                List<Preu> lp;
                String s = JOptionPane.showInputDialog(panel, "ID del precio");
                int id = Integer.parseInt(s);
               try{
                   lp = Main.db.visualitzaPreu(id);
                    int i = JOptionPane.showConfirmDialog(panel, "Seguro que quieres eliminar la tupla con: id_producto: " + lp.get(0).idProducte + " y precio: " + lp.get(0).preu);
                    if (i == JOptionPane.YES_OPTION){
                    Main.db.borrarPreu(id);
                }
               }catch(Exception e){
                e.printStackTrace();
            }
                showAll();
            }
        });
        //Mostrar registros de nuevo
        mostrarTodasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
               showAll();
            }
        });
        //Buscar ID producto mediante nombre
        encontrarIDProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String s = JOptionPane.showInputDialog(panel, "Introduce nombre del producto: ");

                try{
                    int entero = Main.db.trobarIDproducte(s);
                    JOptionPane.showMessageDialog(panel, "La ID de la palabra " + s + " es: " + entero);

                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        //Devuelve precio en base a la ID
        encontrarPrecioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String s = JOptionPane.showInputDialog(panel, "Introduce la ID del precio: ");
                int id = Integer.parseInt(s);
                try {
                    double preu = Main.db.devolverPrecio(id);
                    JOptionPane.showMessageDialog(panel, "El precio para la ID " + id + " es: " + preu);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //Insertar nuevo precio dialog (listener)
        insertarNuevoPrecioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                NuevoPrecioDialog npd = new NuevoPrecioDialog(frame);
                npd.pack();
                npd.setVisible(true);
                showAll();
            }
        });
        actualizarPrecioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActualizarPreciDialog npd = new ActualizarPreciDialog(frame);
                npd.pack();
                npd.setVisible(true);
                showAll();
            }
        });
        exportarXMLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                XMLfile XMLfile = new XMLfile();
                XMLfile.toXML();
                showAll();
            }
        });
        importarXMLfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String s = JOptionPane.showInputDialog(panel, "Deseas borar la base de datos actual?(y/n)");
                if (Objects.equals(s, "y")){
                    try {
                        Main.db.deleteAll();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                XMLfile XMLfile = new XMLfile();
                XMLfile.fromXML();
                showAll();
            }
        });
    }
    //Funcion showAll muestra todos los registros de la db.
    public void showAll(){
        List<Preu> lp = new ArrayList<>();
        //Añadir nueva tabla con contenido
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.addColumn("ID preu");
        dtm.addColumn("ID producte");
        dtm.addColumn("Data Alta");
        dtm.addColumn("Data baixa");
        dtm.addColumn("Preu");

        //LLamar a la funcion que recoge todas las tuplas de la base de datos.
        //Mediante una lista saca todos los valores de Precio
        try{
            lp = Main.db.allPrices();
        } catch (Exception e){
            e.printStackTrace();
        }
        for (Preu aLp : lp) {
            dtm.addRow(new Object[]{aLp.idPreu, aLp.idProducte, aLp.dataAlta, aLp.dataBaixa, aLp.preu});
        }

        //Añadir rows...
        table1.setModel(dtm);

        //Not editable
        table1.setDefaultEditor(Object.class, null);
    }

}
