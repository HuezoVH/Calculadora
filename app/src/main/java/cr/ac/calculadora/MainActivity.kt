package cr.ac.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.mariuszgromada.math.mxparser.Expression

class MainActivity : AppCompatActivity() {
    // TEXTO DE LA CALCULADORA
    lateinit var texto : TextView

    // Si el ultimo digito fue un valor numerico
    var ultimoNum: Boolean = false

    // Si hay un error de calculo
    var estadoError: Boolean = false

    // Si es true, no permite agregar otro punto
    var ultimoPunto: Boolean = false

    // Si un valor de porcentaje fue agregado, no permite agregar otro
    var ultimoPorcentaje : Boolean = false

    // Si ya se presiono el boton igual
    var igualado : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        texto = findViewById(R.id.Texto)
    }

    fun botonNumero(view: View) {
        if (estadoError || igualado || texto.text.toString() == "0") {
            // Si el estado inicial es error o la ultima accion fue igualar, reemplaza el mensaje
            texto.text = (view as Button).text
            estadoError = false
            ultimoPunto = false
            ultimoPorcentaje = false
            igualado = false
        } else {
            // Sino, ya existe una expresion valida para añadir
            texto.append((view as Button).text)
        }
        // True ya que lo ultimo que se ingreso fue un numero
        ultimoNum = true
    }

    fun botonPuntoDecimal(view: View) {
        if (ultimoNum && !estadoError && !ultimoPunto) {
            texto.append(".")
            ultimoNum = false
            ultimoPunto = true
        }
    }

    fun botonOperador(view: View) {
        if (ultimoNum && !estadoError && !igualado) {
            texto.append((view as Button).text)
            ultimoNum = false
            ultimoPunto = false    // false ya que el numero se termino de digitar
        }
    }

    fun botonClear(view: View) {
        this.texto.text = "0"
        ultimoNum = false
        estadoError = false
        ultimoPunto = false
        ultimoPorcentaje = false
    }

    fun botonIgual(view: View) {
        // Si el estadoError es true, no se realiza la accion.
        // Solamente si el ultimo digito ingresado fue un numero, se puede encontrar una solucion
        if (ultimoNum && !estadoError) {
            // Se lee la expresion
            val txt = texto.text.toString()
            // Se crea la expresion con la funcion Expression() de la libreria MathParser
            val expresion = Expression(txt)
            try {
                // Se calcula el resultado y se muestra en el TextView texto
                val resultado = expresion.calculate()
                texto.text = resultado.toString()
                ultimoPunto = true // Esto ya que el resultado contiene un punto
                ultimoPorcentaje = true
                igualado = true
            } catch(ex: ArithmeticException) {
                // Devuelve un error
                texto.text = "NaN"
                estadoError = true
                ultimoNum = false
            }
        }else{
            // Devuelve un error
            texto.text = "NaN"
            estadoError = true
            ultimoNum = false
        }
    }

    fun botonPorcentajeClick(view : View) {
        if (ultimoNum && !estadoError && !ultimoPunto && !ultimoPorcentaje) {
            texto.append("%")
            ultimoNum = true
            ultimoPunto = true
            ultimoPorcentaje = true
        }
    }
}