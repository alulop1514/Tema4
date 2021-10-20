package exemples

import java.sql.DriverManager

fun main() {
    val url = "jdbc:sqlite:Empleats.sqlite"
    val con = DriverManager.getConnection(url)
    val st = con.createStatement()

    val sentenciaSQL = "SELECT * FROM EMPLEAT WHERE sou > 1100"
    val rs = st.executeQuery(sentenciaSQL)

    println("Núm. \tNom \tDep \tEdat \tSou")
    println("-----------------------------------------")

    while (rs.next()) {
        print("" + rs.getInt(1) + "\t")
        print(rs.getString(2) + "\t")
        print("" + rs.getInt(3) + "\t")
        print("" + rs.getInt(4) + "\t")
        println(rs.getDouble(5))
    }

    rs.close()
    st.close()
    con.close()
}