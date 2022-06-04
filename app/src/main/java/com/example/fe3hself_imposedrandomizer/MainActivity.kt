package com.example.fe3hself_imposedrandomizer

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {
    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //def route from spinner
        val routeSpinner: Spinner = findViewById(R.id.route_spinner)
        //def Yes/No for DLC from spinner
        val dlcSpinner: Spinner = findViewById(R.id.dlc_spinner)
        //def other student number from spinner
        val otherStudentSpinner: Spinner = findViewById(R.id.other_student_spinner)
        //def Non students from spinner
        val nonStudentSpinner: Spinner = findViewById(R.id.non_student_spinner)
        //def random button
        val randomButton = findViewById<Button>(R.id.random_button)

        //Button function
        randomButton.setOnClickListener{
            val routeChoice = routeSpinner.selectedItem.toString()
            val dlc = dlcSpinner.selectedItem.toString()
            val otherStudents = otherStudentSpinner.selectedItem.toString().toInt()
            val nonStudents = nonStudentSpinner.selectedItem.toString().toInt()
            var dlcBoolean = false
            if (dlc == "Yes"){
                dlcBoolean = true
            }
            var route = 0
            if (routeChoice == "Black Eagles"){
                route = 0
            }else if (routeChoice == "Blue Lions"){
                route = 1
            }else if (routeChoice == "Golden Deer"){
                route = 2
            }else{
                route = 3
            }
            val chosenUnits = ChooseUnits()
            chosenUnits.addRouteStudents(route)
            chosenUnits.addOtherStudents(route, otherStudents, dlcBoolean)
            chosenUnits.addNonStudents(route, dlcBoolean, nonStudents)

            //generate proficiencies
            val unitAmount = chosenUnits.units.size
            val proficiencies = ChooseProficiencies()
            proficiencies.getProficiencies(unitAmount)

            val resultsList = mutableListOf<String>()
            var headerString = "DLC: $dlc"
            headerString += "\n${chosenUnits.otherStudentMessage}"
            val otherStudentAmount = chosenUnits.addedOtherStudents.size - 1
            var stringToAdd = ""
            for (i in 0..otherStudentAmount){
                stringToAdd = chosenUnits.addedOtherStudents[i]
                headerString += "\n        $stringToAdd added."
            }
            headerString += "\n${chosenUnits.nonStudentMessage}"
            val nonStudentAmount = chosenUnits.addedNonStudents.size - 1
            for (i in 0..nonStudentAmount){
                stringToAdd = chosenUnits.addedNonStudents[i]
                headerString += "\n        $stringToAdd added."
            }
            headerString += "\n$unitAmount total units."
            resultsList.add(headerString)
            val printSize = unitAmount - 1
            for (i in 0..printSize) {
                val j = i + 1
                val name = chosenUnits.units[i]
                val prof1 = proficiencies.weaponProf1[i]
                val prof2 = proficiencies.weaponProf2[i]
                val prof3 = proficiencies.classProf[i]
                val data = "$j. $name: \n      $prof1 $prof2 $prof3"
                resultsList.add(data)
            }
            setContentView(R.layout.activity_results)
            val resultView: ListView = findViewById<ListView>(R.id.result_display)
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, resultsList)
            resultView.adapter = adapter
        }
    }
    override fun onBackPressed() {
        if (backPressedTime + 3000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(this, "Press again to end the app.", Toast.LENGTH_LONG).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}
class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        return parent.getItemAtPosition(pos) as Unit
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}

class ChooseUnits{
    val units: MutableList<String> = mutableListOf("Byleth")
    var nonStudents: MutableList<String> = mutableListOf()
    var addedNonStudents: MutableList<String> = mutableListOf()
    var nonStudentMessage = ""

    var otherStudents: MutableList<String> = mutableListOf()
    var addedOtherStudents: MutableList<String> = mutableListOf()
    var otherStudentMessage = ""

    val blackEagles: List<String> = listOf( "Edelgard", "Hubert", "Ferdinand", "Linhardt",
        "Caspar", "Bernadetta", "Dorothea", "Petra")
    val blueLions: List<String> = listOf(   "Dimitri", "Dedue", "Felix", "Ashe",
        "Sylvain", "Mercedes", "Annette", "Ingrid")
    val goldenDeer: List<String> = listOf(  "Claude", "Lorenz", "Raphael", "Ignatz",
        "Lysithea", "Marianne", "Hilda", "Leonie")
    val churchStudents: List<String> = listOf(  "Ferdinand", "Linhardt", "Caspar",
        "Bernadetta", "Dorothea", "Petra")
    val ashenWolves: MutableList<String> = mutableListOf( "Yuri", "Balthus", "Constance", "Hapi")


    val blackEaglesOther: MutableList<String> = mutableListOf(  "Hanneman", "Manuela", "Alois", "Shamir")
    val nonBlackEaglesOther: MutableList<String> = mutableListOf("Hanneman", "Manuela", "Alois", "Shamir",
        "Cyril", "Catherine", "Flayn", "Seteth")

    val gilbert = "Gilbert"
    val jeritza = "Jeritza"
    val anna = "Anna"

    fun addRouteStudents(route: Int){
        if (route == 0){ //black eagles route
            blackEagles.forEach{i -> units.add(i)}

        }else if (route == 1){ //blue lions route
            blueLions.forEach{i -> units.add(i)}

        }else if (route == 2){ //golden deer route
            goldenDeer.forEach{i -> units.add(i)}

        }else if (route == 3){ //church route
            churchStudents.forEach{i -> units.add(i)}

        }}

    fun addOtherStudents(route: Int, extraStudents: Int, DLC: Boolean){
        //otherStudents is a list that this randomizer will choose from.
        //It is generated from the available students from the chosen route.
        if (DLC){
            otherStudents = ashenWolves}

        if (route == 0){ //black eagles route
            otherStudents.add(blueLions[2]) // add Felix
            otherStudents.add(blueLions[3]) // add Ashe
            otherStudents.add(blueLions[4]) // add Sylvain
            otherStudents.add(blueLions[5]) // add Mercedes
            otherStudents.add(blueLions[6]) // add Annette
            otherStudents.add(blueLions[7]) // add Ingrid

            otherStudents.add(goldenDeer[1]) // add Lorenz
            otherStudents.add(goldenDeer[2]) // add Raphael
            otherStudents.add(goldenDeer[3]) // add Ignatz
            otherStudents.add(goldenDeer[4]) // add Lysithea
            otherStudents.add(goldenDeer[5]) // add Marianne (#6, Hilda, is not available in this route)
            otherStudents.add(goldenDeer[7]) // add Leonie

        }else if (route == 1){ //blue lions route
            otherStudents.add(blackEagles[2]) // add Ferdinand
            otherStudents.add(blackEagles[3]) // add Linhardt
            otherStudents.add(blackEagles[4]) // add Caspar
            otherStudents.add(blackEagles[5]) // add Bernadetta
            otherStudents.add(blackEagles[6]) // add Dorothea
            otherStudents.add(blackEagles[7]) // add Petra

            otherStudents.add(goldenDeer[1]) // add Lorenz
            otherStudents.add(goldenDeer[2]) // add Raphael
            otherStudents.add(goldenDeer[3]) // add Ignatz
            otherStudents.add(goldenDeer[4]) // add Lysithea
            otherStudents.add(goldenDeer[5]) // add Marianne
            otherStudents.add(goldenDeer[6]) // add Hilda
            otherStudents.add(goldenDeer[7]) // add Leonie

        }else if (route == 2){ //golden deer route
            otherStudents.add(blueLions[2]) // add Felix
            otherStudents.add(blueLions[3]) // add Ashe
            otherStudents.add(blueLions[4]) // add Sylvain
            otherStudents.add(blueLions[5]) // add Mercedes
            otherStudents.add(blueLions[6]) // add Annette
            otherStudents.add(blueLions[7]) // add Ingrid

            otherStudents.add(blackEagles[2]) // add Ferdinand
            otherStudents.add(blackEagles[3]) // add Linhardt
            otherStudents.add(blackEagles[4]) // add Caspar
            otherStudents.add(blackEagles[5]) // add Bernadetta
            otherStudents.add(blackEagles[6]) // add Dorothea
            otherStudents.add(blackEagles[7]) // add Petra

        }else if (route == 3){ //church route
            otherStudents.add(blueLions[2]) // add Felix
            otherStudents.add(blueLions[3]) // add Ashe
            otherStudents.add(blueLions[4]) // add Sylvain
            otherStudents.add(blueLions[5]) // add Mercedes
            otherStudents.add(blueLions[6]) // add Annette
            otherStudents.add(blueLions[7]) // add Ingrid

            otherStudents.add(goldenDeer[1]) // add Lorenz
            otherStudents.add(goldenDeer[2]) // add Raphael
            otherStudents.add(goldenDeer[3]) // add Ignatz
            otherStudents.add(goldenDeer[4]) // add Lysithea
            otherStudents.add(goldenDeer[5]) // add Marianne
            otherStudents.add(goldenDeer[6]) // add Hilda
            otherStudents.add(goldenDeer[7]) // add Leonie

        }

        val numberOfStudents: Int
        if (extraStudents > otherStudents.size){
            numberOfStudents = otherStudents.size
            otherStudentMessage = "Chosen amount greater than pool of other students, picked all $numberOfStudents"
        }else{
            numberOfStudents = extraStudents
            otherStudentMessage = "Picked $numberOfStudents other students as specified."
        }
        for (i in 1..numberOfStudents){
            val randomMax = otherStudents.size - 1
            val randomNumber = (0..randomMax).random()
            val studentToAdd = otherStudents[randomNumber]
            units.add(studentToAdd)
            otherStudents.removeAt(randomNumber)
            addedOtherStudents.add(studentToAdd)
        }
    }

    fun addNonStudents(route:Int, DLC:Boolean, userNonStudents: Int){
        if (DLC){
            nonStudents.add(anna)
            if (route == 0){nonStudents.add(jeritza)}
        }
        if (route == 0){ //black eagles route
            blackEaglesOther.forEach{i -> nonStudents.add(i)}

        }else if (route == 1){ //blue lions route
            nonBlackEaglesOther.forEach{i -> nonStudents.add(i)}
            nonStudents.add(gilbert)

        }else if (route == 2){ //golden deer route
            nonBlackEaglesOther.forEach{i -> nonStudents.add(i)}

        }else if (route == 3){ //church route
            nonBlackEaglesOther.forEach{i -> nonStudents.add(i)}}

        val numberOfNonStudents: Int
        if (userNonStudents > nonStudents.size){
            numberOfNonStudents = nonStudents.size
            nonStudentMessage = "Chosen amount great than pool of non-students, picked all $numberOfNonStudents."
        }else{
            numberOfNonStudents = userNonStudents
            nonStudentMessage = "Picked $numberOfNonStudents non-students as specified."
        }
        for (i in 1..numberOfNonStudents){
            val randomMax = nonStudents.size - 1
            val randomNumber = (0..randomMax).random()
            val nonStudentToAdd = nonStudents[randomNumber]
            units.add(nonStudentToAdd)
            nonStudents.removeAt(randomNumber)
            addedNonStudents.add(nonStudentToAdd)
        }
    }
}

class ChooseProficiencies{
    val weaponProficiencies: MutableList<String> = mutableListOf("Swords", "Lances", "Axes",
        "Bows", "Fists", "Reason", "Faith")
    val classProficiencies: List<String> = listOf("Riding", "Flying", "Armor")
    val weaponProf1: MutableList<String> = mutableListOf()
    val weaponProf2: MutableList<String> = mutableListOf()
    val classProf: MutableList<String> = mutableListOf()

    fun getProficiencies(unitAmount: Int){
        println("unitAmount is $unitAmount.")
        for (i in 0..unitAmount){
            val tempList: MutableList<String> = mutableListOf("Swords", "Lances", "Axes",
                "Bows", "Fists", "Reason", "Faith")
            var randomNumber = (0..6).random()
            val profToAdd1 = tempList[randomNumber]
            tempList.removeAt(randomNumber)
            println(tempList)
            randomNumber = (0..5).random()
            val profToAdd2 = tempList[randomNumber]
            randomNumber = (0..2).random()
            val profToAdd3 = classProficiencies[randomNumber]
            weaponProf1.add(profToAdd1)
            weaponProf2.add(profToAdd2)
            classProf.add(profToAdd3)
            println("$i. $profToAdd1 $profToAdd2 $profToAdd3 added")
        }
    }
}

