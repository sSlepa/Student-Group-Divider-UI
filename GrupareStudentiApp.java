import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;


public class GrupareStudentiApp extends JFrame {
    private DefaultListModel<String> listaStudentiModel = new DefaultListModel<>();
    private JList<String> listaStudenti = new JList<>(listaStudentiModel);
    private JComboBox<String> grupuriComboBox = new JComboBox<>();
    private DefaultListModel<String> studentiGrupaModel = new DefaultListModel<>();
    private JList<String> listaStudentiGrupa = new JList<>(studentiGrupaModel);
    private Map<String, List<String>> grupuriMap = new HashMap<>();

    public GrupareStudentiApp() {
        setTitle("Aplicatie Grupare Studenti");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        JPanel panouPrincipal = new JPanel(new GridLayout(1, 2));

        
        JPanel panouStudenti = new JPanel(new BorderLayout());
        panouStudenti.setBorder(BorderFactory.createTitledBorder("Lista Studentilor Neatribuiti"));
        JScrollPane scrollListaStudenti = new JScrollPane(listaStudenti);
        panouStudenti.add(scrollListaStudenti, BorderLayout.CENTER);
        JButton incarcaButton = new JButton("Incarca Studenti");
        panouStudenti.add(incarcaButton, BorderLayout.SOUTH);
        panouPrincipal.add(panouStudenti);

     
        JPanel panouGrupuri = new JPanel(new BorderLayout());
        panouGrupuri.setBorder(BorderFactory.createTitledBorder("Grupuri"));
        JPanel selectareGrupa = new JPanel();
        grupuriComboBox.addItem("Selecteaza Grupa");
        selectareGrupa.add(new JLabel("Selecteaza Grupa:"));
        selectareGrupa.add(grupuriComboBox);
        panouGrupuri.add(selectareGrupa, BorderLayout.NORTH);

        JScrollPane scrollListaGrupa = new JScrollPane(listaStudentiGrupa);
        panouGrupuri.add(scrollListaGrupa, BorderLayout.CENTER);

        JPanel mutareStudenti = new JPanel();
        JButton mutaInGrupaButton = new JButton("Adauga în Grup");
        JButton scoateDinGrupaButton = new JButton("Scoate din Grup");
        mutareStudenti.add(mutaInGrupaButton);
        mutareStudenti.add(scoateDinGrupaButton);
        panouGrupuri.add(mutareStudenti, BorderLayout.SOUTH);
        panouPrincipal.add(panouGrupuri);

        add(panouPrincipal, BorderLayout.CENTER);

        
        JPanel panouOptiuni = new JPanel();
        JButton adaugaGrupaButton = new JButton("Adauga Grup");
        JButton stergeGrupaCurentaButton = new JButton("Sterge Grupa Curenta");
        JButton stergeToateGrupeleButton = new JButton("Sterge Toate Grupele");
        JButton imparteAutomatButton = new JButton("Imparte Automat");
        JButton salveazaButton = new JButton("Salveaza");
        panouOptiuni.add(adaugaGrupaButton);
        panouOptiuni.add(stergeGrupaCurentaButton);
        panouOptiuni.add(stergeToateGrupeleButton);
        panouOptiuni.add(imparteAutomatButton);
        panouOptiuni.add(salveazaButton);
        add(panouOptiuni, BorderLayout.SOUTH);

        // Funcționalități
        incarcaButton.addActionListener(e -> incarcaStudenti());
        adaugaGrupaButton.addActionListener(e -> adaugaGrupa());
        grupuriComboBox.addActionListener(e -> selecteazaGrupa());
        mutaInGrupaButton.addActionListener(e -> mutaStudentInGrupa());
        scoateDinGrupaButton.addActionListener(e -> scoateStudentDinGrupa());
        imparteAutomatButton.addActionListener(e -> imparteAutomat());
        salveazaButton.addActionListener(e -> salveazaGrupuri());
        stergeGrupaCurentaButton.addActionListener(e -> stergeGrupaCurenta());
        stergeToateGrupeleButton.addActionListener(e -> stergeToateGrupele());
    }

    private void incarcaStudenti() {
        File defaultDirectory = new File("C:\\Users\\adria\\Desktop");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(defaultDirectory);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fisiere Text", "txt"));
        int rezultat = fileChooser.showOpenDialog(this);
        if (rezultat == JFileChooser.APPROVE_OPTION) {
            File fisier = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(fisier))) {
                String linie;
                listaStudentiModel.clear();
                while ((linie = reader.readLine()) != null) {
                    listaStudentiModel.addElement(linie);
                }
                JOptionPane.showMessageDialog(this, "Lista de studenti a fost incarcata!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Eroare la incarcarea fisierului!");
            }
        }
    }

    private void adaugaGrupa() {
        String numeGrupa = JOptionPane.showInputDialog(this, "Introduceti numele grupei:");
        if (numeGrupa != null && !numeGrupa.trim().isEmpty()) {
            grupuriComboBox.addItem(numeGrupa);
            grupuriMap.put(numeGrupa, new ArrayList<>());
            JOptionPane.showMessageDialog(this, "Grupa a fost adaugata!");
        }
    }

    private void selecteazaGrupa() {
        String grupaSelectata = (String) grupuriComboBox.getSelectedItem();
        studentiGrupaModel.clear();
        if (grupaSelectata != null && grupuriMap.containsKey(grupaSelectata)) {
            for (String student : grupuriMap.get(grupaSelectata)) {
                studentiGrupaModel.addElement(student);
            }
        }
    }

    private void mutaStudentInGrupa() {
        String student = listaStudenti.getSelectedValue();
        String grupaSelectata = (String) grupuriComboBox.getSelectedItem();
        if (student != null && grupaSelectata != null && grupuriMap.containsKey(grupaSelectata)) {
            grupuriMap.get(grupaSelectata).add(student);
            listaStudentiModel.removeElement(student);
            studentiGrupaModel.addElement(student);
        }
    }

    private void scoateStudentDinGrupa() {
        String student = listaStudentiGrupa.getSelectedValue();
        String grupaSelectata = (String) grupuriComboBox.getSelectedItem();
        if (student != null && grupaSelectata != null && grupuriMap.containsKey(grupaSelectata)) {
            grupuriMap.get(grupaSelectata).remove(student);
            studentiGrupaModel.removeElement(student);
            listaStudentiModel.addElement(student);
        }
    }

    private void stergeGrupaCurenta() {
        String grupaSelectata = (String) grupuriComboBox.getSelectedItem();
        if (grupaSelectata != null && grupuriMap.containsKey(grupaSelectata)) {
            // Mută studenții din grupa curentă în lista de studenți neatribuiți
            for (String student : grupuriMap.get(grupaSelectata)) {
                listaStudentiModel.addElement(student);
            }
            // Șterge grupa din mapă și din ComboBox
            grupuriMap.remove(grupaSelectata);
            grupuriComboBox.removeItem(grupaSelectata);

            // Curăță lista de studenți din grupă
            studentiGrupaModel.clear();
            JOptionPane.showMessageDialog(this, "Grupa curenta a fost stearsa");
        }
    }


    private void stergeToateGrupele() {
    // Mută toți studenții din toate grupurile în lista de studenți neatribuiți
        for (String grupa : grupuriMap.keySet()) {
            for (String student : grupuriMap.get(grupa)) {
                listaStudentiModel.addElement(student);
            }
        }
        // Șterge toate grupele
        grupuriMap.clear();
        grupuriComboBox.removeAllItems();
        grupuriComboBox.addItem("Selecteaza Grupa");

        // Curăță lista de studenți din grupă
        studentiGrupaModel.clear();
        JOptionPane.showMessageDialog(this, "Toate grupele au fost șterse");
    }


    private void imparteAutomat() {
        if (listaStudentiModel.isEmpty() && grupuriMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nu există studenți de împărțit!");
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Introduceți numărul de grupuri:");
        if (input != null) {
            try {
                int numarGrupe = Integer.parseInt(input);
                if (numarGrupe <= 0) {
                    JOptionPane.showMessageDialog(this, "Numărul de grupuri trebuie să fie pozitiv!");
                    return;
                }

                // Mută toți studenții din grupuri în lista de studenți neatribuiți
                for (List<String> studentiGrupa : grupuriMap.values()) {
                    for (String student : studentiGrupa) {
                        listaStudentiModel.addElement(student);
                    }
                }

                // Șterge grupele existente
                grupuriMap.clear();
                grupuriComboBox.removeAllItems();
                grupuriComboBox.addItem("Selectează Grupa");

                // Creează noile grupe
                List<String> studentiNeatribuiti = Collections.list(listaStudentiModel.elements());
                Collections.shuffle(studentiNeatribuiti); // Amestecă lista pentru a împărți aleatoriu
                int index = 0;

                for (int i = 1; i <= numarGrupe; i++) {
                    String numeGrupa = "Grupa " + i;
                    grupuriComboBox.addItem(numeGrupa);
                    grupuriMap.put(numeGrupa, new ArrayList<>());
                }

                // Atribuie studenții grupelor
                for (String student : studentiNeatribuiti) {
                    String numeGrupa = "Grupa " + ((index % numarGrupe) + 1);
                    grupuriMap.get(numeGrupa).add(student);
                    index++;
                }

                // Golește lista de studenți neatribuiți
                listaStudentiModel.clear();

                JOptionPane.showMessageDialog(this, "Studenții au fost împărțiți automat în grupuri!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Introduceți un număr valid pentru grupuri!");
            }
        }
    }




    private void salveazaGrupuri() {
        File defaultDirectory = new File("C:\\Users\\adria\\Desktop");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(defaultDirectory);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fisiere CSV", "csv"));
        int rezultat = fileChooser.showSaveDialog(this);
        if (rezultat == JFileChooser.APPROVE_OPTION) {
            File fisier = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fisier))) {
                for (String grupa : grupuriMap.keySet()) {
                    for (String student : grupuriMap.get(grupa)) {
                        writer.write(grupa + "," + student);
                        writer.newLine();
                    }
                }
                JOptionPane.showMessageDialog(this, "Grupele au fost salvate cu succes!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Eroare la salvarea fișierului!");
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GrupareStudentiApp app = new GrupareStudentiApp();
            app.setVisible(true);
            app.setResizable(false);
            app.setLocationRelativeTo(null);
        });
    }
}
