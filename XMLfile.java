import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class XMLfile {
    public void toXML(){
        try {
            JFileChooser jfc = new JFileChooser();
            jfc.showOpenDialog(null);
            //String s = (String) table1.getValueAt(table1.getSelectedRow(),table1.getSelectedColumn());
            SimpleXML xml = new SimpleXML();
            Document doc = xml.getDoc();
            Element root = doc.createElement("taula");
            doc.appendChild(root);


            List<Preu> lp = Main.db.allPrices();

            for (int i = 0; i < lp.size(); i++) {
                //Padre row
                Element row = doc.createElement("row");
                root.appendChild(row);
                //Creacion de tags
                Element id_preu = doc.createElement("id_preu");
                id_preu.setTextContent("" + lp.get(i).idPreu);
                Element id_producte = doc.createElement("id_producte");
                id_producte.setTextContent("" + lp.get(i).idProducte);
                Element data_alta = doc.createElement("data_alta");
                data_alta.setTextContent("" + lp.get(i).dataAlta);
                Element data_baja = doc.createElement("data_baixa");
                data_baja.setTextContent("" + lp.get(i).dataBaixa);
                Element preu = doc.createElement("preu");
                preu.setTextContent("" + lp.get(i).preu);

                //Appends a los atributos
                row.appendChild(id_preu);
                row.appendChild(id_producte);
                row.appendChild(data_alta);
                row.appendChild(data_baja);
                row.appendChild(preu);
            }

            FileOutputStream fo = new FileOutputStream(jfc.getSelectedFile());
            xml.write(fo);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void fromXML(){
        try {
            JFileChooser jfc = new JFileChooser();
            jfc.showOpenDialog(null);
            SimpleXML xml = new SimpleXML(new FileInputStream(jfc.getSelectedFile()));
            Document doc = xml.getDoc();
            Element root = doc.getDocumentElement();
            List<Element> list = xml.getChildElements(root);

            for (Element e: list) {
                //No alterar los ids
                //Element id_preu = xml.getElement(e,"id_preu");
                //String id = id_preu.getTextContent();

                Element id_prod = xml.getElement(e, "id_producte");
                int idprod = Integer.parseInt(id_prod.getTextContent());

                Element fA = xml.getElement(e, "data_alta");
                String fechA = fA.getTextContent();

                Element fB = xml.getElement(e, "data_baixa");
                String fechaB = fB.getTextContent();

                Element price = xml.getElement(e, "preu");
                Double preu = Double.parseDouble(price.getTextContent());
                if (Objects.equals(fechA, "null")){
                    fechA = dateTruncation();
                }
                if (Objects.equals(fechaB, "null")){
                    fechaB = dateTruncation();
                }
                Main.db.nouPreu(idprod,fechA,fechaB,preu);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String dateTruncation(){
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
        return any + "-" + mes + "-" + dia;
    }
}
