package exercicis

import javax.swing.JFrame
import java.awt.EventQueue
import java.awt.BorderLayout
import javax.swing.JPanel
import java.awt.FlowLayout
import java.sql.DriverManager
import javax.swing.JComboBox
import javax.swing.JButton
import javax.swing.JTextArea
import javax.swing.JLabel
import kotlin.system.exitProcess

class Finestra : JFrame() {

    init {
        // Sentències per a fer la connexió
        val url = "jdbc:sqlite:Rutes.sqlite"
        val con = DriverManager.getConnection(url)
        val st = con.createStatement()

        defaultCloseOperation = EXIT_ON_CLOSE
        title = "JDBC: Visualitzar Rutes"
        setSize(450, 450)
        layout = BorderLayout()

        val panell1 = JPanel(FlowLayout())
        val panell2 = JPanel(BorderLayout())
        add(panell1, BorderLayout.NORTH)
        add(panell2, BorderLayout.CENTER)

        val llistaRutes = arrayListOf<String>()
        // Sentències per a omplir l'ArrayList amb el nom de les rutes
        val sentenciaSQLNoms = "SELECT nom_r FROM RUTES"
        val rs = st.executeQuery(sentenciaSQLNoms)
        while (rs.next())
            llistaRutes.add(rs.getString(1))

        val combo = JComboBox(llistaRutes.toTypedArray())
        panell1.add(combo)
        val eixir = JButton("Eixir")
        panell1.add(eixir)
        val area = JTextArea()
        panell2.add(JLabel("Llista de punts de la ruta:"),BorderLayout.NORTH)
        panell2.add(area,BorderLayout.CENTER)

        combo.addActionListener {
            // Sentèncis quan s'ha seleccionat un element del JComboBox
            // Han de consistir en omplir el JTextArea
            area.text = ""
            val sentenciaSQLPunts = "SELECT nom_p, latitud, longitud FROM PUNTS WHERE num_r = ${combo.selectedIndex}"
            val resultado = st.executeQuery(sentenciaSQLPunts)
            while (resultado.next())
                area.text += "${resultado.getString(1)} (${resultado.getDouble(2)}, ${resultado.getDouble(3)})\n"

        }
        eixir.addActionListener {
            // Sentències per a tancar la connexió i eixir
            st.close()
            con.close()
            exitProcess(0)
        }
        combo.selectedIndex = 0
    }
}

fun main() {
    EventQueue.invokeLater {
        Finestra().isVisible = true
    }
}

