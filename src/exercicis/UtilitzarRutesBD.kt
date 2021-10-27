package exercicis

import util.bd.GestionarRutesBD

fun main(args: Array<String>) {
    // Creació del gestionador
    val gRutes = GestionarRutesBD("jdbc:sqlite:Rutes.sqlite")

    // Inserció d'una nova Ruta
    val noms = arrayOf( "Les Useres", "Les Torrocelles", "Lloma Bernat", "Xodos (Molí)", "El Marinet", "Sant Joan")
    val latituds = arrayOf(40.158126, 40.196046, 40.219210, 40.248003, 40.250977, 40.251221)
    val longituds = arrayOf(-0.166962, -0.227611, -0.263560, -0.296690, -0.316947, -0.354052)

    val punts = arrayListOf<PuntGeo>()
    for (i in 0 until 6){
        punts.add(PuntGeo(noms[i], Coordenades(latituds[i], longituds[i])))
    }

    gRutes.inserir(Ruta("Pelegrins de Les Useres",896,1738,punts))

    // Llistat de totes les rutes
    for (r in gRutes.llistat())
        r?.mostrarRuta()
    // Buscar una ruta determinada
    val r2 = gRutes.buscar(2)
    if (r2 != null)
        r2.mostrarRuta()

    gRutes.close()
}