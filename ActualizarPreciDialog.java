import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class ActualizarPreciDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fechaA;
    private JTextField fechaB;
    private JTextField precio;
    private JComboBox<String> productos;
    private JTextField id_precio;
    private JButton consultarButton;
    private int rowCount;

    public ActualizarPreciDialog(JFrame parent) {
        super(parent);
        setLocationRelativeTo(parent);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        //Menu dsplegable
        try {
            DefaultComboBoxModel dcm = new DefaultComboBoxModel<>();
            List<Producte> lp = Main.db.extraerProductos();
            dcm.addElement("Selecciona...");
            productos.setModel(dcm);
        } catch (Exception a) {
            a.printStackTrace();
        }

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
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
        consultarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<Preu> lp = Main.db.visualitzaPreu(Integer.parseInt(id_precio.getText()));
                    int id = lp.get(0).idProducte;
                    fechaA.setText(lp.get(0).dataAlta);
                    fechaB.setText(lp.get(0).dataBaixa);
                    precio.setText(String.valueOf(lp.get(0).preu));


                    DefaultComboBoxModel dcm = new DefaultComboBoxModel<>();
                    List<Producte> llp = Main.db.extraerProductos();
                    dcm.addElement("Selecciona...");

                    int selecc = -1;
                    int inx = 1;
                    for (Producte aLp : llp) {
                        dcm.addElement(aLp);
                        if (id == aLp.idProducte) selecc = inx;
                        inx++;
                    }
                    productos.setModel(dcm);
                    productos.setSelectedIndex(selecc);

                    productos.setEnabled(true);
                    fechaA.setEnabled(true);
                    fechaB.setEnabled(true);
                    precio.setEnabled(true);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void onOK() {
        int id = Integer.parseInt(id_precio.getText());
        String fechaAlta = fechaA.getText();
        String fechaBaja = fechaB.getText();
        Double preu = Double.parseDouble(precio.getText());

        Producte prod = (Producte) productos.getSelectedItem();

        try {
            Main.db.actualitzarPreu(id,prod.idProducte,fechaAlta,fechaBaja,preu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
