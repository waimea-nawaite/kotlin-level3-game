import com.formdev.flatlaf.themes.FlatMacDarkLaf
import java.awt.Color
import java.awt.Font
import javax.swing.*

/**
 * Application entry point
 */
fun main() {
    FlatMacDarkLaf.setup()          // Initialise the LAF

    val startLocation = Location(
        "Lifepod 5",
        "Safe shallows the only floating pod that survived",
        0
    )

    val game = Game(startLocation)                 // Get an app state object
    val window = MainWindow(game)    // Spawn the UI, passing in the app state

    SwingUtilities.invokeLater { window.show() }
}


class Location(
    val name: String,
    val description: String,
    val distance: Int
) {
    var visited: Boolean = false

    fun info(): String {
        var infoText = "$name is at $description ${distance}M"
        return infoText
    }
}


/**
 * Manage app state
 *
 * @property name the user's name
 * @property score the points earned
 */
class Game(
    var currentLocation: Location

) {
    var name = "Ned"
    var score = 0

    val lifepods = mutableListOf<Location>()

    init {
        val lifepod5 = Location("Lifepod 5", "Safe shallows the only floating pod that survived", 0)
        val lifepod3 = Location("Lifepod 3", "Kelp Forest which has some predators but is mostly safe", 250)
        val lifepod17 = Location("Lifepod 17", "Open Grassy Plateaus, good visibility", 350)
        val lifepod6 = Location("Lifepod 6", "Grassy Plateaus, slightly deeper and more open", 450)
        val lifepod13 = Location("Lifepod 13", "Mushroom Forest, unique and visually distinct", 650)
        val lifepod7 = Location("Lifepod 7", "Crag Field, rugged terrain with more tension", 750)
        val lifepod19 = Location("Lifepod 19", "Sparse Reef, darker and more isolated", 850)
        val lifepod12 = Location("Lifepod 12", "Bulb Zone, alien environment", 950)
        val lifepod2 = Location("Lifepod 2", "Blood Kelp Zone, furthest and very dangerous", 1200)

        lifepods.add(lifepod5)
        lifepods.add(lifepod3)
        lifepods.add(lifepod17)
        lifepods.add(lifepod6)
        lifepods.add(lifepod13)
        lifepods.add(lifepod7)
        lifepods.add(lifepod19)
        lifepods.add(lifepod12)
        lifepods.add(lifepod2)
    }

    fun addLifepod(lifepod: Location) {
        lifepods.add(lifepod)
    }

    fun info(): String {
        var infoText = "`"
        for (lifepod in lifepods) {
            infoText += "\n" + lifepod.info()
        }
        return infoText
    }

    fun getAction(): Char {
        while (true) {
            print("Action: ")
            val action = readlnOrNull()?.firstOrNull()?.uppercaseChar()
            if (action != null && action in "") return action
        }
    }

    fun getDestination(): Location {

        for (lifepod in lifepods) {
            println(lifepod.name)
        }

        print("Where do you want to travel? ")

        val input = readlnOrNull()

        for (lifepod in lifepods) {
            if (lifepod.name.equals(input, ignoreCase = true)) {
                return lifepod
            }
        }
        println("That isn't a lifepod ")
        return currentLocation
    }

    fun travelTo(destination: Location) {
        currentLocation = destination
    }

    fun scorePoints(points: Int) {
        score += points
    }

    fun resetScore() {
        score = 0
    }

    fun maxScoreReached(): Boolean {
        return score >= 10000
    }
}


/**
 * Main UI window, handles user clicks, etc.
 *
 * @param game the app state object
 */
class MainWindow(val game: Game) {
    val frame = JFrame("SUBPOD")
    private val panel = JPanel().apply { layout = null }

    private val titleLabel = JLabel("SUBPOD")

    private val infoLabel = JLabel()
    private val moveButton = JButton("Move")
    private val infoButton = JButton("Info")

    private val infoWindow = InfoWindow(this, game)      // Pass app state to dialog too

    init {
        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
        updateUI()
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(800, 600)

        titleLabel.setBounds(330, 30, 340, 30)
        infoLabel.setBounds(30, 90, 340, 30)
        moveButton.setBounds(275, 500, 240, 40)
        infoButton.setBounds(360, 550, 70, 40)

        panel.add(titleLabel)
        panel.add(infoLabel)
        panel.add(moveButton)
        panel.add(infoButton)
    }

    private fun setupStyles() {
        titleLabel.font = Font(Font.SANS_SERIF, Font.BOLD, 32)
        infoLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)

        moveButton.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)
        moveButton.background = Color(0xcc0055)

        infoButton.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)
    }

    private fun setupWindow() {
        frame.isResizable = false                           // Can't resize
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE  // Exit upon window close
        frame.contentPane = panel                           // Define the main content
        frame.pack()
        frame.setLocationRelativeTo(null)                   // Centre on the screen
    }

    private fun setupActions() {
        moveButton.addActionListener { handleMoveClick() }
        infoButton.addActionListener { handleInfoClick() }
    }

    private fun handleMoveClick() {
        game.scorePoints(1000)       // Update the app state
        updateUI()                  // Update this window UI to reflect this
    }

    private fun handleInfoClick() {
        infoWindow.show()
    }

    fun updateUI() {
        infoLabel.text = "Current location: ${game.currentLocation.name}"

//        if (game.maxScoreReached()) {
//            moveButton.text = "No More!"
//            moveButton.isEnabled = false
//        } else {
//            moveButton.text = "Click Me!"
//            moveButton.isEnabled = true
//        }

        infoWindow.updateUI()       // Keep child dialog window UI up-to-date too
    }

    fun show() {
        frame.isVisible = true
    }
}


/**
 * Info UI window is a child dialog and shows how the
 * app state can be shown / updated from multiple places
 *
 * @param owner the parent frame, used to position and layer the dialog correctly
 * @param app the app state object
 */
class InfoWindow(val owner: MainWindow, val game: Game) {
    private val dialog = JDialog(owner.frame, "DIALOG TITLE", false)
    private val panel = JPanel().apply { layout = null }

    private val infoLabel = JLabel()
    private val resetButton = JButton("Reset")

    init {
        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
        updateUI()
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(240, 180)

        infoLabel.setBounds(30, 30, 180, 60)
        resetButton.setBounds(30, 120, 180, 30)

        panel.add(infoLabel)
        panel.add(resetButton)
    }

    private fun setupStyles() {
        infoLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 16)
        resetButton.font = Font(Font.SANS_SERIF, Font.PLAIN, 16)
    }

    private fun setupWindow() {
        dialog.isResizable = false                              // Can't resize
        dialog.defaultCloseOperation = JDialog.HIDE_ON_CLOSE    // Hide upon window close
        dialog.contentPane = panel                              // Main content panel
        dialog.pack()
    }

    private fun setupActions() {
        resetButton.addActionListener { handleResetClick() }
    }

    private fun handleResetClick() {
        game.resetScore()    // Update the app state
        owner.updateUI()    // Update the UI to reflect this, via the main window
    }

    fun updateUI() {
        // Use app properties to display state
        infoLabel.text = "<html>User: ${game.name}<br>Score: ${game.score} points"

        resetButton.isEnabled = game.score > 0
    }

    fun show() {
        val ownerBounds = owner.frame.bounds          // get location of the main window
        dialog.setLocation(                           // Position next to main window
            ownerBounds.x + ownerBounds.width + 10,
            ownerBounds.y
        )

        dialog.isVisible = true
    }
}