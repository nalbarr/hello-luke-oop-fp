package pokemon

import kotlin.random.Random

class Main {
    val greeting: String
        get() {
            return "Hello world."
        }
}

fun main(args: Array<String>) {
    println(Main().greeting)

    // possible moves
    val move1 = Move(1, "run", 2)
    val move2 = Move(2, "punch", 1)
    val move3 = Move(3, "crawl", 1)
    val move4 = Move(4, "bite", 2)

    // Simple engines just use data classes a Pokemon 
    // compute total values and will always be stuck in a (3, 3) tie.
    val p1 = Pokemon(1, "Man", listOf(move1, move2))
    val p2 = Pokemon(2, "Bear", listOf(move3, move4))

    // Simple engines just compute total values and will always be stuck in a (3, 3) tie.

    println("*** SimpleOOPEngine using values.")
    SimpleOOPEngine(p1, p2).run()

    println("*** SimpleFPEngine using values.")
    SimpleFPEngine(p1, p2).run()

    // Complex engines set a policy to break the tie.
    // 1. Man will always have totoal value of 3 with (run, punch)
    // 2. Bear will always have total value of 3 with (crawl, bite)
    // Compute random Int between [0, 9]
    // [0, 1, 2, 4]    -> Man awarded tie breaker point
    // [5, 6, 7, 8, 9] -> Bear awarded tie breaker point

    val p1Obj = Man(1, "Man", listOf(move1, move2))
    val p2Obj = Bear(2, "Bear", listOf(move3, move4))

    println("*** ComplexOOPEngine using objects.")
    ComplexOOPEngine(p1Obj, p2Obj).run()

    println("*** ComplexFPEngine using objects.")
    ComplexFPEngine(p1Obj, p2Obj).run()
}

// NA.
// https://stackoverflow.com/questions/26444145/extend-data-class-in-kotlin

// NOTE:  NLA.  Kotlin data class stores state is like Scala case class
data class Pokemon(val id: Int, val name: String, val moves: List<Move>)
data class Move(val id: Int, val name: String, val value: Int)

// NOTE: NLA.  An alternate representation using Kotlin interfaces and extended data
// classes overriding properties

interface IPokemon {
    val id: Int
    val name: String
    val moves: List<Move>
}

data class Man(override val id: Int, 
    override val name: String, 
    override val moves: List<Move>) : IPokemon

data class Bear(override val id: Int, 
    override val name: String, 
    override val moves: List<Move>) : IPokemon

data class Unassigned(override val id: Int, 
    override val name: String, 
    override val moves: List<Move>) : IPokemon

data class SimpleOOPEngine(val p1: Pokemon, val p2: Pokemon) {
    fun run():Unit {
        if (getTotal(p1) > getTotal(p2)) {
            println("p1 wins.  Total: " + getTotal(p1))
        } else if (getTotal(p1) > getTotal(p2)) {
            println("p2 wins.  Total: ")
        } else {
            println("Tie.")
        }
    }
    // NOTE: NLA.  This an imperative approach using mutal var as sum
    fun getTotal(p: Pokemon):Int {
        var sum:Int = 0
        for (m in p.moves) {
            sum += m.value
        }
        return sum
    }
}

data class SimpleFPEngine(val p1: Pokemon, val p2: Pokemon) {
    fun run():Unit {
        // NOTE: NLA.  This is more FP approach and in place without mutable var
        fun getTotal(p:Pokemon):Int { return p.moves.map {it.value}.sum()}
        when {
           (getTotal(p1) > getTotal(p2)) -> println("p1 wins.")
           (getTotal(p1) < getTotal(p2)) -> println("p2 wins.")
           else -> println("Tie.")
        }
    }    
}

data class ComplexOOPEngine(val p1: IPokemon, val p2: IPokemon) {

    fun run():Unit {
        val p = getWinner(p1, p2)
        if (p is Man) {
            println("Man: " + p.name + " wins tiebreaker with: " + getTotal(p) )
        } else if (p is Bear) {
            println("Bear: " + p.name + " wins tiebreaker with: " + getTotal(p) ) 
        } else {
            throw Exception("Uknown Pokemon type.")
        }
    }

    fun getWinner(p1: IPokemon, p2: IPokemon): IPokemon {
        val r = Random.nextInt(0, 10)
        println("r: " + r)

	var p:IPokemon = Unassigned(0, "unnasigned", listOf() )

        if (r <= 5) {
            p = p1
        } else if (r > 5) {
            p = p2
        }

        return p
    } 

    // NOTE: NLA.  This an imperative approach using mutable var as sum
    fun getTotal(p: IPokemon):Int {
        var sum:Int = 0
        for (m in p.moves) {
            sum += m.value
        }
        return sum
    }
}

data class ComplexFPEngine(val p1: IPokemon, val p2: IPokemon) {
    fun run():Unit {
        // NOTE: NLA.  This is more FP approach and in place without mutable var
        fun getTotal(p:IPokemon):Int { return p.moves.map {it.value}.sum() }
        fun getWinner(p1: IPokemon, p2: IPokemon): IPokemon {
            val r = Random.nextInt(0, 10)
            println("r: " + r)
	    var p:IPokemon = Unassigned(0, "unnasigned", listOf() )

            if (r <= 5) {
                p = p1
            } else if (r > 5) {
                p = p2
            }

	    return p
        }           
	val p:IPokemon = getWinner(p1, p2)
        when(p) {
           is Man  -> println(p.name + " wins.")
           is Bear -> println(p.name + " wins.")
           else ->  throw Exception("Uknown Pokemon type.")
        }
    }    
}
