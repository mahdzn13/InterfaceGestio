import javax.swing.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class NuevoPrecioDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> productos;
    private JTextField fechaA;
    private JTextField fechaB;
    private JTextField precio;

    public NuevoPrecioDialog(JFrame parent) {
        super(parent);
        setLocationRelativeTo(parent);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        //Menu dsplegable
        try {
            DefaultComboBoxModel<String> dcm = new DefaultComboBoxModel<String>();
            List<Producte> lp = Main.db.extraerProductos();
            dcm.addElement("Selecciona...");

            for (Producte aLp : lp) {
                dcm.addElement(aLp.idProducte + "-" +  aLp.descripcio);
            }
            productos.setModel(dcm);
        } catch (Exception a) {
            a.printStackTrace();
        }
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() throws Exception{
        Calendar calendario = Calendar.getInstance();
        String mes,dia;
        String any =String.valueOf(calendario.get(Calendar.YEAR));
        if (calendario.get(Calendar.DAY_OF_MONTH ) < 10){
            dia = "0" + String.valueOf(calendario.get(Calendar.DAY_OF_MONTH));
        } else {
           dia = String.valueOf(calendario.get(Calendar.DAY_OF_MONTH ));
        }
        if (calendario.get(Calendar.MONTH) + 1 < 10){
            mes = "0" + String.valueOf(calendario.get(Calendar.MONTH) + 1);
        } else {
            mes = String.valueOf(calendario.get(Calendar.MONTH) + 1);
        }
        String fecha = any + "-" + mes + "-" + dia;

        String s =(String) productos.getSelectedItem();
        int i = 0;
        char c;
        String ids = "";
        while ((c=s.charAt(i)) != '-'){
            ids = ids + c;
            i++;
        }
        int id = Integer.parseInt(ids);
        String fA = fechaA.getText();
        Double preu = Double.parseDouble(precio.getText());


        if (Objects.equals(fechaB.getText(), "") || Objects.equals(fechaB.getText(), "0000-00-00")){
            Main.db.nouPreu(id,fA,fecha,preu);
        } else{
            String fB = fechaB.getText();
            Main.db.nouPreu(id,fA,fB,preu);
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
