/**
 * =====================================================================
 * Programming Project for NCEA Level 3, Standard 91906
 * ---------------------------------------------------------------------
 * Project Name:   SUBPOD
 * Project Author: NED WAITE
 * GitHub Repo:    https://github.com/waimea-nawaite/kotlin-level3-game
 * ---------------------------------------------------------------------
 * Notes:
 * =====================================================================
 */

import com.formdev.flatlaf.themes.FlatMacDarkLaf
import java.awt.Font
import javax.swing.*

/**
 * Application entry point
 */
fun main() {
    FlatMacDarkLaf.setup()          // Initialise the LAF
    // Colors for the UI
    UIManager.put("Panel.background", java.awt.Color(5, 15, 25))
    UIManager.put("Label.foreground", java.awt.Color(0, 180, 255))
    UIManager.put("Button.background", java.awt.Color(0, 60, 100))
    UIManager.put("Button.foreground", java.awt.Color.WHITE)

    val game = Game()    // Get an app state object
    val window = MainWindow(game)    // Spawn the UI, passing in the app state

    SwingUtilities.invokeLater { window.show() }
}


class Location(
    val podName: String,
    val description: String,
    val distanceToStartPod: Int,
) {
    var hasPDA: Boolean = false
}


/**
 * Manage app state
 *
 * @property name the user's name
 * @property score the points earned
 */
class Game() {
    var name = "User"
    var score = 0
    var oxygen = 100

    // Score PDAs
    fun scorePdas(pdas: Int) {
        score += pdas
    }

    val lifepods = mutableListOf<Location>()
    val blocked = Location("BLOCKED", "", 0)            //Blocked area
    val openOcean = Location("OpenOcean", "Nothing but water here", 0)  //openOcean nothing is around

    var currentPodIndex: Int    // Index into the list of the player's location

    init {
        val lifepod5 = Location("Lifepod 5", "Safe shallows the only floating pod that survived", 0)
        val lifepod17 = Location("Lifepod 17", "Open Grassy Plateaus, good visibility", 100)
        val lifepod6 = Location("Lifepod 6", "Grassy Plateaus, slightly deeper and more open", 200)
        val lifepod13 = Location("Lifepod 13", "Mushroom Forest, unique and visually distinct", 250)
        val lifepod7 = Location("Lifepod 7", "Crag Field, rugged terrain with more tension", 400)
        val lifepod12 = Location("Lifepod 12", "Bulb Zone, alien environment", 400)
        val lifepod19 = Location("Lifepod 19", "Sparse Reef, darker and more isolated", 500)
        val lifepod3 = Location("Lifepod 3", "Far but seems safe and calm...", 500)
        val lifepod2 = Location("Lifepod 2", "Blood Kelp Zone, furthest and very dangerous", 600)

        //This is the map layout
        lifepods.add(lifepod5)
        lifepods.add(openOcean)
        lifepods.add(blocked)
        lifepods.add(blocked)

        lifepods.add(blocked)
        lifepods.add(lifepod17)
        lifepods.add(openOcean)
        lifepods.add(lifepod6)

        lifepods.add(openOcean)
        lifepods.add(openOcean)
        lifepods.add(blocked)
        lifepods.add(blocked)

        lifepods.add(lifepod13)
        lifepods.add(blocked)
        lifepods.add(openOcean)
        lifepods.add(lifepod19)

        lifepods.add(openOcean)
        lifepods.add(openOcean)
        lifepods.add(lifepod7)
        lifepods.add(blocked)

        lifepods.add(blocked)
        lifepods.add(lifepod12)
        lifepods.add(blocked)
        lifepods.add(lifepod2)

        lifepods.add(openOcean)
        lifepods.add(openOcean)
        lifepods.add(lifepod3)
        lifepods.add(openOcean)

        resetPDAs()

        //Sets the default spawn location
        currentPodIndex = 0
    }

    //This function places the pdas on the map, shuffles them and blocks them from going into blocked areas
    fun resetPDAs() {
        for (pod in lifepods) {
            pod.hasPDA = false
        }
        lifepods
            .filter {
                it != blocked && it != openOcean && it.podName != "Lifepod 5"
            }
            .shuffled()
            .take(3)
            .forEach {
                it.hasPDA = true
            }
    }

    //Everytime the player moves it takes away 5 oxygen
    fun useOxygen(distance: Int) {
        val oxygenLoss = (distance / 50) * 5
        oxygen -= oxygenLoss
        if (oxygen < 0) {
            oxygen = 0
        }
    }

    //This function lets the player go north and the same with e.g. fun goEast but to the east
    fun goNorth() {
        if (canGoNorth()) {
            currentPodIndex -= 4
            useOxygen(50)
            checkForPda()
        }
    }

    //This function checks if going north is allowed because if there is a blocked path then it shouldnt let
    //the player go there and the same with e.g. fun canGoEast
    fun canGoNorth(): Boolean {
        // Are we at top edge of map?
        if (currentPodIndex - 4 < 0) return false

        // See what is to the north
        val northPod = lifepods[currentPodIndex - 4]
        if (northPod == blocked) return false

        // Not of edge, and not blocked
        return true
    }

    fun goEast() {
        if (canGoEast()) {
            currentPodIndex++
            useOxygen(50)
            checkForPda()
        }
    }

    fun canGoEast(): Boolean {
        //Are we at top edge of the map?
        if (currentPodIndex % 4 == 3) return false

        //See what is to the East
        val eastPod = lifepods[currentPodIndex + 1]
        if (eastPod == blocked) return false

        //Not the edge, and not blocked
        return true
    }

    fun goSouth() {
        if (canGoSouth()) {
            currentPodIndex += 4
            useOxygen(50)
            checkForPda()
        }
    }

    fun canGoSouth(): Boolean {

        if (currentPodIndex + 4 > 27) return false

        val southPod = lifepods[currentPodIndex + 4]
        if (southPod == blocked) return false

        return true
    }

    fun goWest() {
        if (canGoWest()) {
            currentPodIndex--
            useOxygen(50)
            checkForPda()
        }
    }

    fun canGoWest(): Boolean {

        if (currentPodIndex % 4 == 0) return false

        val westPod = lifepods[currentPodIndex - 1]
        if (westPod == blocked) return false

        return true
    }

    //This function is pretty self explanatory it checks for a PDA at the location the player is at and if there is one it will plus 1 to the score
    //And it also adds oxygen if a PDA is found
    fun checkForPda() {
        val location = lifepods[currentPodIndex]
        //if location has a PDA then +1 to score
        if (location.hasPDA) {
            location.hasPDA = false
            scorePdas(1)
            //If PDA collected
            oxygen += 15
            //Dont let the oxygen go over 100
            if (oxygen > 100) {
                oxygen = 100
            }
        }
    }

    //This restarts the game if you die or win
    fun restartGame() {
        currentPodIndex = 0
        oxygen = 100
        score = 0

        resetPDAs()
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

    private val lifepodLabel = JLabel()
    private val instructionsLabel = JLabel()
    private val descriptionLabel = JLabel()
    private val distanceLabel = JLabel()
    private val pdaNotificationLabel = JLabel()
    private val northButton = JButton("North")
    private val eastButton = JButton("East")
    private val southButton = JButton("South")
    private val westButton = JButton("West")
    private val oxygenLevel = JLabel()
    private val anyOxygen = JLabel()
    private val pdaCounterLabel = JLabel()

    init {
        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
        updateUI()
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(800, 600)

        titleLabel.setBounds(330, 20, 340, 30)
        instructionsLabel.setBounds(30, 60, 500, 140)
        lifepodLabel.setBounds(30, 240, 600, 30)
        descriptionLabel.setBounds(30, 300, 600, 30)
        distanceLabel.setBounds(30, 270, 600, 30)
        pdaNotificationLabel.setBounds(30, 300, 600, 30)
        northButton.setBounds(110, 460, 90, 40)
        eastButton.setBounds(200, 500, 90, 40)
        southButton.setBounds(110, 540, 90, 40)
        westButton.setBounds(20, 500, 90, 40)
        oxygenLevel.setBounds(210, 540, 120, 40)
        anyOxygen.setBounds(300, 330, 180, 40)
        pdaCounterLabel.setBounds(50, 340, 180, 40)

        panel.add(titleLabel)
        panel.add(instructionsLabel)
        panel.add(lifepodLabel)
        panel.add(descriptionLabel)
        panel.add(distanceLabel)
        panel.add(pdaNotificationLabel)
        panel.add(northButton)
        panel.add(eastButton)
        panel.add(southButton)
        panel.add(westButton)
        panel.add(oxygenLevel)
        panel.add(anyOxygen)
        panel.add(pdaCounterLabel)

    }

    private fun setupStyles() {
        titleLabel.font = Font(Font.SANS_SERIF, Font.BOLD, 32)
        titleLabel.foreground = java.awt.Color(133, 103, 8)
        instructionsLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 15)
        lifepodLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)
        descriptionLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)
        distanceLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)
        pdaNotificationLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)
        oxygenLevel.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)
        anyOxygen.font = Font(Font.SANS_SERIF, Font.PLAIN, 30)
        pdaCounterLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 30)
        pdaCounterLabel.foreground = java.awt.Color(55, 89, 91)
    }

    private fun setupWindow() {
        frame.isResizable = false                           // Can't resize
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE  // Exit upon window close
        frame.contentPane = panel                           // Define the main content
        frame.pack()
        frame.setLocationRelativeTo(null)                   // Centre on the screen
    }

    //these functions handle the clicks for movement
    private fun handleNorthClick() {
        game.goNorth()              //makes the player go north
        checkDeath()                //Checks if no oxygen left
        checkWin()                  //Check if player has all PDAs
        updateUI()                  // Update this window UI to reflect this
    }

    private fun handleEastClick() {
        game.goEast()
        checkDeath()
        checkWin()
        updateUI()                  // Update this window UI to reflect this
    }

    private fun handleSouthClick() {
        game.goSouth()
        checkDeath()
        checkWin()
        updateUI()                  // Update this window UI to reflect this
    }

    private fun handleWestClick() {
        game.goWest()
        checkDeath()
        checkWin()
        updateUI()                  // Update this window UI to reflect this
    }

    private fun setupActions() {
        northButton.addActionListener { handleNorthClick() }
        eastButton.addActionListener { handleEastClick() }
        southButton.addActionListener { handleSouthClick() }
        westButton.addActionListener { handleWestClick() }
    }

    private fun handleRestartClick() {
        game.restartGame()
        updateUI()
    }

    //This function checks if the player runs out of oxygen and lets the user decide if they want to play again or quit
    private fun checkDeath() {

        //If the player runs out of oxygen ask
        if (game.oxygen <= 0) {
            val restart = JOptionPane.showConfirmDialog(
                frame, "Your Oxygen Supply Ran Out. Restart?",
                "You Lost",
                JOptionPane.YES_NO_OPTION
            )

            //if user says yes
            if (restart == JOptionPane.YES_OPTION) {
                game.restartGame()
                updateUI()
            }

            //Otherwise
            else
                frame.dispose()
        }
    }

    //Same thing here if the player wins then the game asks if they want to play again or quit
    private fun checkWin() {
        //If the player gets all the PDAS on map ask
        if (game.score == 3) {
            val restart = JOptionPane.showConfirmDialog(
                frame, "You collected all the PDAS you win! Do you want to replay?",
                "Victory",
                JOptionPane.YES_NO_OPTION
            )

            //If user says yes
            if (restart == JOptionPane.YES_OPTION) {
                game.restartGame()
                updateUI()
            }

            //Otherwise
            else
                frame.dispose()
        }
    }

    fun updateUI() {
        val location = game.lifepods[game.currentPodIndex]
        instructionsLabel.text =
            "<html><center>Welcome to SUBPOD! In this game the objective is to collect all the PDAS scattered around the map!(3 total)." +
                    "To move around press the North, East, South, and West buttons in the bottom left corner it is sort of like a maze." +
                    "But be careful your oxygen runs out with each movement, each time you collect a PDA it refills +30% back. Not every time is a win, Goodluck!"
        lifepodLabel.text = "Current location: ${location.podName}"
        descriptionLabel.text = "Description: ${location.description}"
        distanceLabel.text = "Distance: ${location.distanceToStartPod}m"
        oxygenLevel.text = "Oxygen: ${game.oxygen}"
        pdaCounterLabel.text = "PDAS: ${game.score}"

        //enables the buttons if the game allows it to move that way
        northButton.isEnabled = game.canGoNorth()
        eastButton.isEnabled = game.canGoEast()
        southButton.isEnabled = game.canGoSouth()
        westButton.isEnabled = game.canGoWest()
    }

    fun show() {
        frame.isVisible = true
    }
}