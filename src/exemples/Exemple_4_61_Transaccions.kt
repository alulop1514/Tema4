package exemples

import java.sql.DriverManager
import java.sql.Connection

fun main(args: Array<String>) {
    val con = DriverManager.getConnection("jdbc:sqlite:Empleats.sqlite")

    resumEmpleats(con)			// estat inicial

    val autocommit = con.getAutoCommit()
    con.setAutoCommit(false)
    val st = con.createStatement()
    st.executeUpdate("UPDATE EMPLEAT SET sou = sou * 1.05")
    st.executeUpdate("DELETE FROM EMPLEAT WHERE depart=10")

    resumEmpleats(con)			// s'han modificat els sous i s'han esborrat dos empleats

    con.rollback()				// desfem els canvis

    resumEmpleats(con)			// hem tornat a l'estat inicial:
    // no s'ha fet ni l'actualització de sous ni l'esborrat d'empleats

    con.setAutoCommit(autocommit)

    con.close()
}

fun resumEmpleats(c: Connection) {  // En el resum traurem el número d'empleats i el total de sous
    val st = c.createStatement()
    val rs = st.executeQuery("SELECT COUNT(*), SUM(sou) FROM EMPLEAT")
    rs.next()
    println("Actualment hi ha " + rs.getInt(1) + " empleats que cobren un total de " + rs.getDouble(2) + " €")
    rs.close()
    st.close()
}