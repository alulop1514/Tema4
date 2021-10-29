package util.bd

import exercicis.Coordenades
import exercicis.PuntGeo
import exercicis.Ruta
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class GestionarRutesBD(var url: String) {
    var conexion : Connection
    var st : Statement

    init {
        conexion = DriverManager.getConnection(url)
        st = conexion.createStatement()
        val sentenciaSQLRUTES = "CREATE TABLE IF NOT EXISTS RUTES(" +
                "num_r INTEGER PRIMARY KEY, " +
                "nom_r TEXT, " +
                "desn INTEGER, " +
                "desn_ac INTEGER " +
                ")"
        val sentenciaSQLPUNTS = "CREATE TABLE IF NOT EXISTS PUNTS(" +
                "num_r INTEGER, " +
                "num_p INTEGER, " +
                "nom_p TEXT, " +
                "latitud REAL, " +
                "longitud REAL, " +
                "FOREIGN KEY(num_r) REFERENCES RUTES(num_r), " +
                "PRIMARY KEY (num_r,num_p) " +
                ")"
        st.executeUpdate(sentenciaSQLRUTES)
        st.executeUpdate(sentenciaSQLPUNTS)
        st.close()
    }

    fun inserir(ruta : Ruta) {
        val sentenciaSQL = "SELECT MAX(num_r) FROM RUTES"
        val numRutas = st.executeQuery(sentenciaSQL).getInt(1) + 1
        st.executeUpdate("INSERT INTO RUTES VALUES (${numRutas},'${ruta.nom}',${ruta.desnivell},${ruta.desnivellAcumulat})")
        for (punt in ruta.llistaDePunts)
            st.executeUpdate("INSERT INTO PUNTS VALUES (${numRutas},${ruta.llistaDePunts.indexOf(punt)},'${punt.nom}', ${punt.coord.latitud}," +
                    "${punt.coord.longitud})")
        st.close()
    }
    fun close() {
        conexion.close()
    }
    fun buscar(posicion : Int): Ruta {
        st = conexion.createStatement()
        val sentenciaSQLRutas = "SELECT * FROM RUTES WHERE num_r = $posicion"
        val sentenciaSQLPunts = "SELECT nom_p, latitud, longitud FROM PUNTS WHERE num_r = $posicion"
        val ruta = st.executeQuery(sentenciaSQLRutas)
        st = conexion.createStatement()
        val punts = st.executeQuery(sentenciaSQLPunts)
        val arrayPunts = arrayListOf<PuntGeo>()
        while (punts.next()) {
            arrayPunts.add(PuntGeo(punts.getString(1), Coordenades(punts.getDouble(2), punts.getDouble(3))))
        }
        st.close()
        return Ruta(ruta.getString(2), ruta.getInt(3),ruta.getInt(4), arrayPunts)
    }
    fun llistat() : ArrayList<Ruta> {
        val arrayRutas = arrayListOf<Ruta>()
        st = conexion.createStatement()
        val sentenciaSQLRutas = "SELECT num_r, nom_r, desn, desn_ac FROM RUTES"
        val ruta = st.executeQuery(sentenciaSQLRutas)
        while (ruta.next()) {
            val sentenciaSQLPunts = "SELECT nom_p, latitud, longitud FROM PUNTS WHERE num_r = ${ruta.getInt(1)}"
            st = conexion.createStatement()
            val punts = st.executeQuery(sentenciaSQLPunts)
            val arrayPunts = arrayListOf<PuntGeo>()
            while (punts.next()) {
                arrayPunts.add(PuntGeo(punts.getString(1), Coordenades(punts.getDouble(2), punts.getDouble(3))))
            }
            arrayRutas.add(Ruta(ruta.getString(2), ruta.getInt(3),ruta.getInt(4), arrayPunts))
        }
        return arrayRutas
    }
    fun esborrar(posicion: Int) {
        st = conexion.createStatement()
        val sentenciaSQLRutas = "DELETE FROM RUTES WHERE num_r = $posicion"
        val sentenciaSQLPunts = "DELETE FROM PUNTS WHERE num_r = $posicion"
        val sentenciaActualitzaRutes = "UPDATE RUTES set num_r=num_r-1 WHERE num_r > $posicion"
        val sentenciaActualitzaPunts = "UPDATE PUNTS set num_r=num_r-1 WHERE num_r > $posicion"
        st.executeUpdate(sentenciaSQLPunts)
        st.executeUpdate(sentenciaSQLRutas)
        st.executeUpdate(sentenciaActualitzaRutes)
        st.executeUpdate(sentenciaActualitzaPunts)
        st.close()
    }
    fun guardar(r : Ruta) {
        val sentenciaSQL = "SELECT MAX(num_r) FROM RUTES"
        val numRutas = st.executeQuery(sentenciaSQL).getInt(1)
        var posicion = 0
        var esta = false
        for (i in 0..numRutas) {
            if (buscar(i).nom == r.nom) {
                posicion = i
                esta = true
            }
        }
        if (esta) {
            st = conexion.createStatement()
            val sentenciaSQLRutas =
                "UPDATE RUTES SET nom_r='${r.nom}', desn=${r.desnivell}, desn_ac=${r.desnivellAcumulat} WHERE num_r = $posicion"
            st.executeUpdate(sentenciaSQLRutas)
            st = conexion.createStatement()
            val sentenciaSQLBorrarPunts = "DELETE FROM PUNTS WHERE num_r = $posicion"
            st.executeUpdate(sentenciaSQLBorrarPunts)
            for (punt in r.llistaDePunts)
                st.executeUpdate(
                    "INSERT INTO PUNTS VALUES (${posicion},${r.llistaDePunts.indexOf(punt)},'${punt.nom}', ${punt.coord.latitud}," +
                            "${punt.coord.longitud})"
                )
            st.close()
        } else {
            inserir(r)
        }
    }
    fun editar(r: Ruta, posicion: Int) {
        st = conexion.createStatement()
        val sentenciaSQLRutas =
            "UPDATE RUTES SET nom_r='${r.nom}', desn=${r.desnivell}, desn_ac=${r.desnivellAcumulat} WHERE num_r = $posicion"
        st.executeUpdate(sentenciaSQLRutas)
        st = conexion.createStatement()
        val sentenciaSQLBorrarPunts = "DELETE FROM PUNTS WHERE num_r = $posicion"
        st.executeUpdate(sentenciaSQLBorrarPunts)
        for (punt in r.llistaDePunts)
            st.executeUpdate(
                "INSERT INTO PUNTS VALUES (${posicion},${r.llistaDePunts.indexOf(punt)},'${punt.nom}', ${punt.coord.latitud}," +
                        "${punt.coord.longitud})"
            )
        st.close()
    }
}