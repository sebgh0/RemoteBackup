/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package backupServer0Client;

import javax.swing.SwingUtilities;

/**
 *
 * @author sebas
 */
public class BackupServer0Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(() -> {
            // Crear y mostrar la interfaz gráfica
            BSCForm gui = new BSCForm();
            gui.setVisible(true);
        });
    }
    
}
