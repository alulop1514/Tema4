package exemples

import java.sql.DriverManager
import java.util.Scanner

fun main(args: Array<String>) {
    val con = DriverManager.getConnection("jdbc:postgresql://89.36.214.106:5432/geo_ad", "geo_ad", "geo_ad")

    println("Introdueix una comarca:")
    val com = Scanner(System.`in`).nextLine()
    println("Introdueix una altura:")
    val alt = Scanner(System.`in`).nextInt()

    val st = con.prepareStatement("SELECT nom,altura FROM POBLACIO WHERE nom_c=? AND altura>?")
    st.setString(1,com)				// Abans d'executar-la s'han d'iniciar els paràmetres
    st.setInt(2,alt)
    val rs = st.executeQuery()		// La sentència no va en el moment de l'execució sinó en el de creació
    while (rs.next()) {
        println(rs.getString(1) + " (" +rs.getInt(2) + " m.)")
    }
    st.close()
    con.close()
}