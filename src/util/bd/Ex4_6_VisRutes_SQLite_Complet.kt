package util.bd



import java.awt.EventQueue
import java.awt.GridLayout
import java.awt.FlowLayout
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JTextField
import javax.swing.JTable
import javax.swing.JScrollPane
import exercicis.Ruta
import exercicis.PuntGeo
import kotlin.system.exitProcess

class FinestraComplet : JFrame() {
    val gRutes = GestionarRutesBD("jdbc:sqlite:Rutes.sqlite")
    var llista = arrayListOf<Ruta>()
    var numActual = 0

    val qNom = JTextField(15)
    val qDesn = JTextField(5)
    val qDesnAcum = JTextField(5)
    val punts = JTable(1,3)
    val primer = JButton(" << ")
    val anterior = JButton(" < ")
    val seguent = JButton(" > ")
    val ultim = JButton(" >> ")
    val tancar = JButton("Tancar")
    init {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setTitle("JDBC: Visualitzar Rutes Complet")
        setLayout(GridLayout(0,1))

        val p_prin = JPanel()
        p_prin.setLayout(BoxLayout(p_prin, BoxLayout.Y_AXIS))
        val panell1 = JPanel(GridLayout(0,2))
        panell1.add(JLabel("Ruta:"))
        qNom.setEditable(false)
        panell1.add(qNom)
        panell1.add(JLabel("Desnivell:"))
        qDesn.setEditable(false)
        panell1.add(qDesn)
        panell1.add(JLabel("Desnivell acumulat:"))
        qDesnAcum.setEditable(false)
        panell1.add(qDesnAcum)
        panell1.add(JLabel("Punts:"))

        val panell2 = JPanel(GridLayout(0,1))
        punts.setEnabled(false)
        val scroll = JScrollPane(punts)
        panell2.add(scroll, null)

        val panell5 = JPanel(FlowLayout())
        panell5.add(primer)
        panell5.add(anterior)
        panell5.add(seguent)
        panell5.add(ultim)

        val panell6 = JPanel(FlowLayout())
        panell6.add(tancar)

        add(p_prin)
        p_prin.add(panell1)
        p_prin.add(panell2)
        p_prin.add(panell5)
        p_prin.add(panell6)
        pack()

        primer.addActionListener{
            // instruccions per a situar-se en la primera ruta, i visualitzar-la
            numActual = 0
            VisRuta()
        }
        anterior.addActionListener{
            // instruccions per a situar-se en la ruta anterior, i visualitzar-la
            numActual -= 1
            VisRuta()
        }
        seguent.addActionListener{
            // instruccions per a situar-se en la ruta següent, i visualitzar-la
            numActual += 1
            VisRuta()

        }
        ultim.addActionListener{
            // instruccions per a situar-se en la ultima ruta, i visualitzar-la
            numActual = llista.size - 1
            VisRuta()
        }
        tancar.addActionListener{
            gRutes.close()
            exitProcess(0)
        }

        inicialitzar()
        VisRuta()
    }

    fun plenarTaula(ll_punts: MutableList<PuntGeo>){
        var ll = Array(ll_punts.size) { arrayOfNulls<String>(3) }
        for (i in 0 until ll_punts.size){
            ll[i][0]=ll_punts.get(i).nom
            ll[i][1]=ll_punts.get(i).coord.latitud.toString()
            ll[i][2]=ll_punts.get(i).coord.longitud.toString()
        }
        val caps = arrayOf("Nom punt","Latitud","Longitud")
        punts.setModel(javax.swing.table.DefaultTableModel(ll,caps))
    }

    fun inicialitzar() {
        val st = gRutes.conexion.createStatement()
        val sentenciaSQL = "SELECT MAX(num_r) FROM RUTES"
        val numRutas = st.executeQuery(sentenciaSQL).getInt(1)
        for (i in 0..numRutas) {
            llista.add(gRutes.buscar(i))
        }
    }

    fun VisRuta(){
        // instruccions per a visualitzar la ruta actual (l'índex el tenim en numActual)
        qNom.text = llista.get(numActual).nom
        qDesn.text = llista.get(numActual).desnivell.toString()
        qDesnAcum.text = llista.get(numActual).desnivellAcumulat.toString()
        plenarTaula(llista.get(numActual).llistaDePunts)
        ActivarBotons()
    }

    fun ActivarBotons(){
        // instruccions per a activar o desactivar els botons de moviment ( isEnabled )
        primer.isEnabled = true
        ultim.isEnabled = true
        seguent.isEnabled = numActual != llista.size - 1
        anterior.isEnabled = numActual != 0
    }

}

fun main(args: Array<String>) {
    EventQueue.invokeLater {
        FinestraComplet().isVisible = true
    }
}

