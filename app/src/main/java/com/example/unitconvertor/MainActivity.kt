package com.example.unitconvertor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.example.unitconvertor.Unit

data class Unit(val name:String , val conversionFactor:Double){
    override fun toString(): String {
        return name
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var categorySpinner: Spinner
    private lateinit var unitSpinnerFrom : Spinner
    private lateinit var unitSpinnerTo: Spinner
    private lateinit var inputValue:EditText
    private lateinit var convertButton :Button
    private lateinit var outputValue:TextView

    private val lenghtUnit = listOf(
        Unit("Meter",1.0),
        Unit("Kilometer",0.001),
        Unit("Centimeter",100.0),
        Unit("Millimeter",1000.0),
        Unit("Miles",0.000621),
        Unit("Yard",1.094),
        Unit("Feet",3.2808),
        Unit("Inch",39.37)
    )

    private val massUnits = listOf(
        Unit("Kilograms",0.001),
        Unit("Grams",1.0),
        Unit("Milligrams",1000.0),
        Unit("Pound",0.0022)
    )

    private val temperatureUnit = listOf(
        Unit("Celcius",1.0),
        Unit("Fahrenheit",1.0),
        Unit("Kelvin",1.0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        categorySpinner = findViewById(R.id.categorySpinner)
        unitSpinnerFrom = findViewById(R.id.unitSpinnerFrom)
        unitSpinnerTo = findViewById(R.id.unitSpinnerTo)
        inputValue = findViewById(R.id.inputValue)
        convertButton = findViewById(R.id.convertButton)
        outputValue = findViewById(R.id.outputValue)

        val categories = listOf("Length","Mass","Temperature")
        val categoryAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter
        categorySpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                val selectedCategory = categories[position]
                updateUnitSpinner(selectedCategory)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        convertButton.setOnClickListener {
            val fromUnit = unitSpinnerFrom.selectedItem as Unit
            val toUnit = unitSpinnerTo.selectedItem as Unit
            val value = inputValue.text.toString().toDoubleOrNull()
            if (value != null){
                val result = convert(fromUnit,toUnit,value)
                outputValue.text = "Result: $result ${toUnit.name}"
            }
            else
            {
                outputValue.text = "Please enter a valid value"
            }
        }

    }



    private fun updateUnitSpinner(selectedCategory: String) {
        val units = when(selectedCategory){
            "Length"->lenghtUnit
            "Mass"->massUnits
            "Temperature"->temperatureUnit
            else-> emptyList()
        }

        val unitAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,units)
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        unitSpinnerFrom.adapter = unitAdapter
        unitSpinnerTo.adapter = unitAdapter

    }

    private fun convert(fromUnit: Unit, toUnit: Unit, value: Double): Double{

        return if (fromUnit.name=="Celcius"&&toUnit.name=="Fahrenheit"){
            value*9/5+32
        }
        else if (fromUnit.name=="Fahrenheit"&&toUnit.name=="Celcius"){
            (value-32)*5/9
        }
        else if (fromUnit.name=="Celcius"&&toUnit.name=="Kelvin"){
            value + 273.15       }
        else if (fromUnit.name=="Kelvin"&&toUnit.name=="Celcius"){
            value - 273.15
        }
        else if (fromUnit.name=="Fahrenheit"&&toUnit.name=="Kelvin"){
            (value-32) * 5/9 + 273.15
        }
        else if (fromUnit.name=="Kelvin"&&toUnit.name=="Fahrenheit"){
            (value-32) * 9/5 + 32
        }
        else{
            value * toUnit.conversionFactor / fromUnit.conversionFactor
        }
    }
}

