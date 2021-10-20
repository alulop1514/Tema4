package exercicis

import java.io.EOFException
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.sql.DriverManager

fun main() {
    val fIn = ObjectInputStream(FileInputStream("Rutes.obj"))
    val rutes = arrayListOf<Ruta>()
    val url = "jdbc:sqlite:Rutes.sqlite"
    val con = DriverManager.getConnection(url)
    val st = con.createStatement()

    try {
        while (true) {
            val ruta = fIn.readObject() as Ruta
            rutes.add(ruta)
        }
    } catch (ex : EOFException) {
        fIn.close()
    }
    for (ruta in rutes) {
        st.executeUpdate("INSERT INTO RUTES VALUES (${rutes.indexOf(ruta)},'${ruta.nom}',${ruta.desnivell},${ruta.desnivellAcumulat})")

        for (punt in ruta.llistaDePunts)
        st.executeUpdate("INSERT INTO PUNTS VALUES (${rutes.indexOf(ruta)},${ruta.llistaDePunts.indexOf(punt)},'${punt.nom}', ${punt.coord.latitud}," +
                "${punt.coord.longitud})")
    }
    st.close()
    con.close()
}