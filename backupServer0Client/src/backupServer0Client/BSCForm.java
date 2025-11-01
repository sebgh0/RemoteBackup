/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package backupServer0Client;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author sebas
 */
public class BSCForm extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(BSCForm.class.getName());

    /**
     * Creates new form BSCForm
     */
    public BSCForm() {
        initComponents();
        this.setTitle("Cliente de Respaldo");
        mostrarIpLocal();
        
        new Thread(this::iniciarServidorCliente).start();
    }

    private void mostrarIpLocal() {
        try {
            String ipLocal = null;
            var interfaces = java.net.NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                var iface = interfaces.nextElement();

                // Ignorar interfaces que no están activas o son de loopback (ej. lo, docker0)
                if (!iface.isUp() || iface.isLoopback() || iface.isVirtual()) {
                    continue;
                }

                var direcciones = iface.getInetAddresses();
                while (direcciones.hasMoreElements()) {
                    var direccion = direcciones.nextElement();

                    // Filtrar solo IPv4 reales (no localhost ni IPv6)
                    if (!direccion.isLoopbackAddress() && direccion instanceof java.net.Inet4Address) {
                        ipLocal = direccion.getHostAddress();
                        break;
                    }
                }
                if (ipLocal != null) {
                    break;
                }
            }

            if (ipLocal != null) {
                lbl_ipLocal.setText("IP Local: " + ipLocal);
            } else {
                lbl_ipLocal.setText("No se encontró una IP válida de red");
            }
        } catch (Exception e) {
            lbl_ipLocal.setText("Error al obtener la IP: " + e.getMessage());
        }
    }

    private void iniciarServidorCliente() {
        try (ServerSocket server = new ServerSocket(4000)) {
            agregarMensaje("Esperando conexión en puerto 4000...");

            while (true) {
                Socket socket = server.accept();
                new Thread(() -> manejarSolicitud(socket)).start();
            }

        } catch (IOException e) {
            agregarMensaje("Error en servidor cliente: " + e.getMessage());
        }
    }

    private void manejarSolicitud(Socket socket) {
        try (DataInputStream in = new DataInputStream(socket.getInputStream());
             OutputStream out = socket.getOutputStream()) {

            String comando = in.readUTF();
            if (comando.startsWith("SOLICITAR_BACKUP|")) {
                String ruta = comando.split("\\|")[1];
                agregarMensaje("Solicitud recibida para respaldar: " + ruta);

                File carpeta = new File(ruta);
                if (!carpeta.exists()) {
                    agregarMensaje("Error: carpeta no encontrada.");
                    return;
                }

                ZipOutputStream zos = new ZipOutputStream(out);
                comprimirCarpeta(carpeta, carpeta.getName(), zos);
                zos.finish();
                agregarMensaje("Respaldo enviado correctamente.");
            }

        } catch (Exception e) {
            agregarMensaje("Error manejando solicitud: " + e.getMessage());
        }
    }

    private void comprimirCarpeta(File carpeta, String base, ZipOutputStream zos) throws IOException {
        File[] archivos = carpeta.listFiles();
        if (archivos == null) return;

        for (File archivo : archivos) {
            if (archivo.isDirectory()) {
                comprimirCarpeta(archivo, base + "/" + archivo.getName(), zos);
            } else {
                try (FileInputStream fis = new FileInputStream(archivo)) {
                    ZipEntry entry = new ZipEntry(base + "/" + archivo.getName());
                    zos.putNextEntry(entry);
                    byte[] buffer = new byte[4096];
                    int bytesLeidos;
                    while ((bytesLeidos = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, bytesLeidos);
                    }
                    zos.closeEntry();
                }
            }
        }
    }

    private void agregarMensaje(String msg) {
        SwingUtilities.invokeLater(() -> txt_area.append(msg + "\n"));
    }

    private boolean verificarConectividad(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.isReachable(5000);
        } catch (Exception e) {
            return false;
        }
    }

    private void mostrarMensajeExito() {
        SwingUtilities.invokeLater(() -> {
            lbl_estado.setText("Estado: Respaldo solicitado con éxito");
            JOptionPane.showMessageDialog(this, "Solicitud de respaldo enviada con éxito a VM B", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void mostrarError(String error) {
        SwingUtilities.invokeLater(() -> {
            lbl_estado.setText("Estado: Error en la solicitud");
            JOptionPane.showMessageDialog(this, "Error al solicitar respaldo: " + error, "Error", JOptionPane.ERROR_MESSAGE);
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_estado = new javax.swing.JLabel();
        lbl_ipLocal = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_area = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lbl_estado.setText("Esperando solicitudes...");

        lbl_ipLocal.setText("IP Local: ...");

        txt_area.setColumns(20);
        txt_area.setRows(5);
        jScrollPane1.setViewportView(txt_area);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lbl_ipLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 431, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lbl_estado)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(lbl_ipLocal)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new BSCForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_estado;
    private javax.swing.JLabel lbl_ipLocal;
    private javax.swing.JTextArea txt_area;
    // End of variables declaration//GEN-END:variables
}
