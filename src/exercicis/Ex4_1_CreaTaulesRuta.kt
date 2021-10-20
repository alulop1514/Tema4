package exercicis

import java.sql.DriverManager

fun main() {
    val url = "jdbc:sqlite:Rutes.sqlite"
    val con = DriverManager.getConnection(url)
    val st = con.createStatement()

    val sentenciaSQLRUTES = "CREATE TABLE RUTES(" +
            "num_r INTEGER PRIMARY KEY, " +
            "nom_r TEXT, " +
            "desn INTEGER, " +
            "desn_ac INTEGER " +
            ")"

    val sentenciaSQLPUNTS = "CREATE TABLE PUNTS(" +
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
    con.close()
}