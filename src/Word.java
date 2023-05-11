import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

public class Word extends JFrame implements ActionListener {
    private JTextArea workSpace; //Supports multiple lines, TextField only supports one line
    private JSpinner sizeChooser; //Up and Down arrows, uses ChangeListener
    private JScrollPane scroll;
    private JButton colorButton;
    private JComboBox fontChooser;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem saveFile;
    private JMenuItem loadFile;
    private JLabel fontText;

    Word(){
        //fontText
        fontText = new JLabel("Font: ");

        //sizeChooser
        sizeChooser = new JSpinner();
        sizeChooser.setPreferredSize(new Dimension(50,25));
        sizeChooser.setValue(25);
        sizeChooser.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                workSpace.setFont(new Font(workSpace.getFont().getFamily(), Font.PLAIN, (int) sizeChooser.getValue()));
            }
        });

        //workSpace
        workSpace = new JTextArea();
        workSpace.setFont(new Font("Arial",Font.PLAIN,25));
        workSpace.setLineWrap(true); //Switch to the next line if the letters reach the border
        workSpace.setWrapStyleWord(true); //Switch to the next line if the letters reach the border

        scroll = new JScrollPane(workSpace);
        scroll.setPreferredSize(new Dimension(450,450));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        //colorChooser
        colorButton = new JButton("Colour");
        colorButton.setFocusable(false);
        colorButton.addActionListener(this);

        //fontChooser
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(); //Get all available fonts
        fontChooser = new JComboBox(fonts);
        fontChooser.setSelectedItem("Arial");
        fontChooser.addActionListener(this);

        //menuBar & fileMenu
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        saveFile = new JMenuItem("Save");
        loadFile = new JMenuItem("Load");

        saveFile.addActionListener(this);
        loadFile.addActionListener(this);

        fileMenu.add(saveFile);
        fileMenu.add(loadFile);
        menuBar.add(fileMenu);

        //Add everything to the frame
        this.setJMenuBar(menuBar);
        this.add(fontText);
        this.add(sizeChooser);
        this.add(colorButton);
        this.add(fontChooser);
        this.add(scroll);

        //Frame settings
        this.setTitle("Word 2.0");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,500);
        this.setLayout(new FlowLayout());
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == colorButton){
            Color color = JColorChooser.showDialog(null, "Pick a color", Color.black);
            workSpace.setForeground(color);
        }
        else if(e.getSource() == fontChooser) {
            workSpace.setFont(new Font((String) fontChooser.getSelectedItem(), Font.PLAIN, workSpace.getFont().getSize()));
        }
        else if (e.getSource() == saveFile){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File ("C:\\Users\\grife\\IdeaProjects\\NotWord"));
            int response = fileChooser.showSaveDialog(null);

            if (response == JFileChooser.APPROVE_OPTION){
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                PrintWriter printWriter = null;

                try {
                    printWriter = new PrintWriter(file);
                    printWriter.println(workSpace.getText());
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } finally {
                    printWriter.close();
                }
            }
        }
        else if (e.getSource() == loadFile){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("C:\\Users\\grife\\IdeaProjects\\NotWord"));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
            fileChooser.setFileFilter(filter); //Add search filters (Description provided to the user, The extension)
            int response = fileChooser.showOpenDialog(null);

            if (response == JFileChooser.APPROVE_OPTION){
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner fileIn = null;

                try {
                    fileIn = new Scanner(file);
                    workSpace.setText("");

                    if (file.isFile()){
                        while(fileIn.hasNextLine()){
                            String line = fileIn.nextLine() + "\n";
                            workSpace.append(line);
                        }
                    }
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } finally {
                    fileIn.close();
                }
            }
        }
    }
}