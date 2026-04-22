import com.formdev.flatlaf.themes.FlatMacDarkLaf
import java.awt.Font
import javax.swing.*

/**
 * Application entry point
 */
fun main() {
    FlatMacDarkLaf.setup()          // Initialise the LAF

    val game = Game()    // Get an app state object
    val window = MainWindow(game)    // Spawn the UI, passing in the app state

    SwingUtilities.invokeLater { window.show() }
}


class Location(
    val podName: String,
    val description: String,
    val distanceToStartPod: Int,
) {
    var hasPda: Boolean = false

    fun info(): String {
        val infoText = "$podName is at $description ${distanceToStartPod}M"
        return infoText
    }
}


/**
 * Manage app state
 *
 * @property name the user's name
 * @property score the points earned
 */
class Game() {
    var name = "Ned"
    var score = 0

    fun scorePdas(pdas: Int) {
        score += pdas
    }

    fun maxScoreReached(): Boolean {
        return score >= 4
    }

    val lifepods = mutableListOf<Location>()
    val blocked = Location("BLOCKED", "", 0)
    val openOcean = Location("OpenOcean", "Nothing but water here", 0)

    var currentPodIndex: Int    // Index into the list of the player's location

    init {
        val lifepod5 = Location("Lifepod 5", "Safe shallows the only floating pod that survived", 0)
        val lifepod17 = Location("Lifepod 17", "Open Grassy Plateaus, good visibility", 350)
        val lifepod6 = Location("Lifepod 6", "Grassy Plateaus, slightly deeper and more open", 450)
        val lifepod13 = Location("Lifepod 13", "Mushroom Forest, unique and visually distinct", 650)
        val lifepod7 = Location("Lifepod 7", "Crag Field, rugged terrain with more tension", 750)
        val lifepod19 = Location("Lifepod 19", "Sparse Reef, darker and more isolated", 850)
        val lifepod12 = Location("Lifepod 12", "Bulb Zone, alien environment", 950)
        val lifepod3 = Location("Lifepod 3", "Far but seems safe and calm...", 1100)
        val lifepod2 = Location("Lifepod 2", "Blood Kelp Zone, furthest and very dangerous", 1200)

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
        lifepods.add(lifepod7)

        lifepods.add(openOcean)
        lifepods.add(openOcean)
        lifepods.add(lifepod19)
        lifepods.add(blocked)

        lifepods.add(blocked)
        lifepods.add(lifepod12)
        lifepods.add(blocked)
        lifepods.add(lifepod2)

        lifepods.add(openOcean)
        lifepods.add(openOcean)
        lifepods.add(lifepod3)
        lifepods.add(openOcean)

        lifepods
            .filter { it != blocked && it != lifepod5 }
            .shuffled()
            .take(4)
            .forEach {
                it.hasPda = true
            }

        currentPodIndex = 0

    }

    fun goNorth() {
        if (canGoNorth()) {
            currentPodIndex -= 4
            checkForPda()
        }
    }

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
            checkForPda()
        }
    }

    fun canGoEast(): Boolean {
        if (currentPodIndex % 4 == 3) return false

        val eastPod = lifepods[currentPodIndex + 1]
        if (eastPod == blocked) return false

        return true
    }

    fun goSouth() {
        if (canGoSouth()) {
            currentPodIndex += 4
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
            checkForPda()
        }
    }

    fun canGoWest(): Boolean {

        if (currentPodIndex % 4 == 0) return false

        val westPod = lifepods[currentPodIndex - 1]
        if (westPod == blocked) return false

        return true
    }

    fun checkForPda() {
        val location = lifepods[currentPodIndex]

        if (location.hasPda) {
            location.hasPda = false
            scorePdas(1)
        }
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
    private val descriptionLabel = JLabel()
    private val distanceLabel = JLabel()
    private val allPdasCollectedLabel = JLabel("All PDAS collected return to lifepod 5")
    private val northButton = JButton("North")
    private val eastButton = JButton("East")
    private val southButton = JButton("South")
    private val westButton = JButton("West")
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

        titleLabel.setBounds(330, 20, 340, 30)
        lifepodLabel.setBounds(30, 90, 600, 30)
        descriptionLabel.setBounds(30, 120, 600, 30)
        distanceLabel.setBounds(30, 150, 600, 30)
        allPdasCollectedLabel.setBounds(30, 300, 600, 30)
        infoButton.setBounds(360, 550, 70, 40)
        northButton.setBounds(110, 460, 90, 40)
        eastButton.setBounds(200, 500, 90, 40)
        southButton.setBounds(110, 540, 90, 40)
        westButton.setBounds(20, 500, 90, 40)



        panel.add(titleLabel)
        panel.add(lifepodLabel)
        panel.add(descriptionLabel)
        panel.add(distanceLabel)
        panel.add(allPdasCollectedLabel)
        panel.add(infoButton)
        panel.add(northButton)
        panel.add(eastButton)
        panel.add(southButton)
        panel.add(westButton)

    }

    private fun setupStyles() {
        titleLabel.font = Font(Font.SANS_SERIF, Font.BOLD, 32)
        lifepodLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)
        descriptionLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)
        distanceLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)
        allPdasCollectedLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)

        infoButton.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)
    }

    private fun setupWindow() {
        frame.isResizable = false                           // Can't resize
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE  // Exit upon window close
        frame.contentPane = panel                           // Define the main content
        frame.pack()
        frame.setLocationRelativeTo(null)                   // Centre on the screen
    }

    private fun handleNorthClick() {
        game.goNorth()

        updateUI()                  // Update this window UI to reflect this
    }

    private fun handleEastClick() {
        game.goEast()

        updateUI()                  // Update this window UI to reflect this
    }

    private fun handleSouthClick() {
        game.goSouth()

        updateUI()                  // Update this window UI to reflect this
    }

    private fun handleWestClick() {
        game.goWest()

        updateUI()                  // Update this window UI to reflect this
    }

    private fun setupActions() {
        northButton.addActionListener { handleNorthClick() }
        eastButton.addActionListener { handleEastClick() }
        southButton.addActionListener { handleSouthClick() }
        westButton.addActionListener { handleWestClick() }
        infoButton.addActionListener { handleInfoClick() }
    }

    private fun handleInfoClick() {
        infoWindow.show()
    }

    fun updateUI() {
        val location = game.lifepods[game.currentPodIndex]
        lifepodLabel.text = "Current location: ${location.podName}"
        descriptionLabel.text = "Description: ${location.description}"
        distanceLabel.text = "Distance: ${location.distanceToStartPod}m"

        northButton.isEnabled = game.canGoNorth()
        eastButton.isEnabled = game.canGoEast()
        southButton.isEnabled = game.canGoSouth()
        westButton.isEnabled = game.canGoWest()

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
    private val dialog = JDialog(owner.frame, "DATA", false)
    private val panel = JPanel().apply { layout = null }

    private val infoLabel = JLabel()
//    private val resetButton = JButton("Reset")

    init {
        setupLayout()
        setupStyles()
//        setupActions()
        setupWindow()
        updateUI()
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(240, 180)

        infoLabel.setBounds(30, 30, 180, 60)
//        resetButton.setBounds(30, 120, 180, 30)

        panel.add(infoLabel)
//        panel.add(resetButton)
    }

    private fun setupStyles() {
        infoLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 16)
//        resetButton.font = Font(Font.SANS_SERIF, Font.PLAIN, 16)
    }

    private fun setupWindow() {
        dialog.isResizable = false                              // Can't resize
        dialog.defaultCloseOperation = JDialog.HIDE_ON_CLOSE    // Hide upon window close
        dialog.contentPane = panel                              // Main content panel
        dialog.pack()
    }

//    private fun setupActions() {
//        resetButton.addActionListener { handleResetClick() }
//    }

//    private fun handleResetClick() {
//       game.resetScore()    // Update the app state
//        owner.updateUI()    // Update the UI to reflect this, via the main window
//    }

    fun updateUI() {
        // Use app properties to display state
        infoLabel.text = "<html>User: ${game.name}<br>Score: ${game.score} points"

//        resetButton.isEnabled = game.score > 0
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