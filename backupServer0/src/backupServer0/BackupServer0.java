/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package backupServer0;

import javax.swing.SwingUtilities;

/**
 *
 * @author sebas
 */
public class BackupServer0 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Iniciar la interfaz gráfica
        SwingUtilities.invokeLater(() -> {
            // Crear y mostrar la interfaz gráfica
            BSForm gui = new BSForm();
            gui.setVisible(true);
        });
    }
    
}
