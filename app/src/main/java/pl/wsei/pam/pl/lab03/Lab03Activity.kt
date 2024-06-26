package pl.wsei.pam.pl.lab03

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.Toast
import pl.wsei.pam.lab01.R
import java.util.Random
import java.util.Stack
import java.util.Timer
import kotlin.concurrent.schedule

class Lab03Activity : AppCompatActivity() {
    private lateinit var mBoardModel: MemoryBoardView
    lateinit var completionPlayer: MediaPlayer
    lateinit var negativePLayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab03)
        val columns = intent.getIntExtra("columns", 3)
        val rows = intent.getIntExtra("rows", 4)
        val mBoard: GridLayout = findViewById(R.id.grid1)
        var isSound = true
        mBoard.columnCount = columns
        mBoard.rowCount = rows

        if (savedInstanceState != null) {
            val gameStateString = savedInstanceState.getString("gameState")
            val gameState = gameStateString?.split(",")?.map { it.toInt() } ?: listOf()
            mBoardModel = MemoryBoardView(mBoard, columns, rows, gameState)
            mBoardModel.setState(gameState)
        } else {
            mBoardModel = MemoryBoardView(mBoard, columns, rows)
        }


        mBoardModel.setOnGameChangeListener { e ->
            run {
                when (e.state) {
                    GameStates.Matching -> {
                        e.tiles.forEach { tile -> tile.revealed = true }
                    }

                    GameStates.Match -> {
                        if (isSound) {
                            completionPlayer.start();
                        }

                        e.tiles.forEach { tile -> tile.revealed = true
                            animatePairedButton(tile.button) {}
                        }

                    }

                    GameStates.NoMatch -> {
                        if (isSound) {
                            negativePLayer.start()
                        }
                        e.tiles.forEach { tile ->
                            tile.revealed = true

                            animateNotPairedButton(tile.button) { tile.revealed = false }


                        }
                    }

                    GameStates.Finished -> {
                        Toast.makeText(this, "Game finished", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean  {
        val inflater: MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.board_activity_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.getItemId()){
            R.id.board_activity_sound ->
                if (item.getIcon()?.getConstantState()?.equals(getResources().getDrawable(R.drawable.baseline_volume_up_24, getTheme()).getConstantState())== true) {
                    Toast.makeText(this, "Sound turn off", Toast.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.baseline_volume_off_24)
                    isSound = false;
                } else {
                    Toast.makeText(this, "Sound turn on", Toast.LENGTH_SHORT).show()
                    item.setIcon(R.drawable.baseline_volume_up_24)
                    isSound = true
                }
        }
        return false
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val gameState = mBoardModel.getState().joinToString(",")
        outState.putString("gameState", gameState)
    }
    override protected fun onResume() {
        super.onResume()
        completionPlayer = MediaPlayer.create(applicationContext, R.raw.completion)
        negativePLayer = MediaPlayer.create(applicationContext, R.raw.negative_guitar)
    }
    override protected fun onPause() {
        super.onPause();
        completionPlayer.release()
        negativePLayer.release()
    }
}

    class MemoryBoardView(
        private val gridLayout: GridLayout,
        private val cols: Int,
        private val rows: Int,
        private val deleteIcons: List<Int> = listOf()


    ) {
        private val tiles: MutableMap<String, Tile> = mutableMapOf()
        private val icons: List<Int> = listOf(
            R.drawable.baseline_moon,
            R.drawable.baseline_bolt_24,
            R.drawable.baseline_cake_24,
            R.drawable.baseline_directions_railway_24,
            R.drawable.baseline_water_drop_24,
            R.drawable.baseline_wb_cloudy_24,
            R.drawable.baseline_wifi_24,
            R.drawable.baseline_yard_24,
            R.drawable.baseline_moon,
            R.drawable.baseline_bolt_24,
            R.drawable.baseline_cake_24,
            R.drawable.baseline_view_agenda_24,
            R.drawable.baseline_visibility_24,
            R.drawable.baseline_wb_cloudy_24,
            R.drawable.baseline_wifi_24,
            R.drawable.baseline_yard_24,
            R.drawable.baseline_moon,
            R.drawable.baseline_bolt_24,
            R.drawable.baseline_cake_24,
        )
        init {
            val resultIcons: MutableList<Int> = icons.toMutableList()
            for(icon in icons)
            {
                for(deleteIcon in deleteIcons) {
                    if(icon == deleteIcon)
                    {
                        println("Delete")
                        resultIcons.remove(icon)
                    }
                }
            }
            val shuffledIcons: MutableList<Int> = mutableListOf<Int>().also {
                it.addAll(resultIcons.subList(0, cols * rows / 2))
                it.addAll(resultIcons.subList(0, cols * rows / 2))
                it.shuffle()
            }
            for(row in 0 until rows)
            {
                for(col in 0 until cols)
                {
                    val tag = "${row}x${col}"
                    val btn = ImageButton(gridLayout.context).also {
                        it.tag = tag
                        val layoutParams = GridLayout.LayoutParams()
                        it.setImageResource(R.drawable.baseline_audiotrack_24)
                        layoutParams.width = 0
                        layoutParams.height = 0
                        layoutParams.setGravity(Gravity.CENTER)
                        layoutParams.columnSpec = GridLayout.spec(col, 1, 1f)
                        layoutParams.rowSpec = GridLayout.spec(row, 1, 1f)
                        it.layoutParams = layoutParams
                    }
                    gridLayout.addView(btn);
                    addTile(btn, shuffledIcons[0])
                    shuffledIcons.removeAt(0)
                }
            }

        }


        private val deckResource: Int = R.drawable.baseline_rocket_launch_24
        private var onGameChangeStateListener: (MemoryGameEvent) -> Unit = { (e) -> }
        private val matchedPair: Stack<Tile> = Stack()
        private val logic: MemoryGameLogic = MemoryGameLogic(cols * rows / 2)

        private fun onClickTile(v: View) {
            val tile = tiles[v.tag]
            tile?.revealed = true
            matchedPair.push(tile)
            val matchResult = logic.process {
                tile?.tileResource?:-1
            }
            onGameChangeStateListener(MemoryGameEvent(matchedPair.toList(), matchResult))
            if (matchResult != GameStates.Matching) {
                matchedPair.clear()
            }
        }

        fun getState(): List<Int> {
            return tiles.values.map { tile ->
                if (tile.revealed) tile.tileResource else -1
            }

        }

        fun setState(state: List<Int>) {
            tiles.values.forEachIndexed { index, tile ->
                val tileState = state[index]
                tile.revealed = tileState != -1

                if (tile.revealed) {
                    tile.tileResource = state[index]
                    tile.button.setImageResource(tileState)
                } else {
                    tile.button.setImageResource(tile.deckResource)
                }
            }
        }

        fun setOnGameChangeListener(listener: (event: MemoryGameEvent) -> Unit) {
            onGameChangeStateListener = listener

        }

        private fun addTile(button: ImageButton, resourceImage: Int) {
            button.setOnClickListener(::onClickTile)
            val tile = Tile(button, resourceImage, deckResource)
            tiles[button.tag.toString()] = tile
        }


    }

    data class MemoryGameEvent(
        val tiles: List<Tile>,
        val state: GameStates) {
    }
    class MemoryGameLogic(private val maxMatches: Int) {

        private var valueFunctions: MutableList<() -> Int> = mutableListOf()

        private var matches: Int = 0

        fun process(value: () -> Int):  GameStates{
            if (valueFunctions.size < 1) {
                valueFunctions.add(value)
                return GameStates.Matching
            }
            valueFunctions.add(value)
            val result = valueFunctions[0]() == valueFunctions[1]()
            matches += if (result) 1 else 0
            valueFunctions.clear()
            return when (result) {
                true -> if (matches == maxMatches) GameStates.Finished else GameStates.Match
                false -> GameStates.NoMatch
            }
        }
    }
    private fun animatePairedButton(button: ImageButton, action: Runnable ) {
        val set = AnimatorSet()
        val random = Random()
        button.pivotX = random.nextFloat() * 200f
        button.pivotY = random.nextFloat() * 200f

        val rotation = ObjectAnimator.ofFloat(button, "rotation", 1080f)
        val scallingX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 2f)
        val scallingY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 2f)
        val fade = ObjectAnimator.ofFloat(button, "alpha", 1f, 0f)
        set.startDelay = 200
        set.duration = 2000
        set.interpolator = DecelerateInterpolator()
        set.playTogether(rotation, scallingX, scallingY, fade)
        set.addListener(object: Animator.AnimatorListener {

            override fun onAnimationStart(animator: Animator) {

            }

            override fun onAnimationEnd(animator: Animator) {
                button.scaleX = 1f
                button.scaleY = 1f
                button.alpha = 0.0f
                action.run();
            }

            override fun onAnimationCancel(animator: Animator) {
            }

            override fun onAnimationRepeat(animator: Animator) {
            }
        })
        set.start()
    }
    private fun animateNotPairedButton(button: ImageButton, action: Runnable ) {
        val set = AnimatorSet()
        val random = Random()
        button.pivotX = random.nextFloat() * 200f
        button.pivotY = random.nextFloat() * 200f
        val moveAmount: Int = 20
        val firstPhase = ObjectAnimator.ofFloat(button, "rotation", button.rotationX, button.rotationX + moveAmount)
        val secondPhase = ObjectAnimator.ofFloat(button, "rotation", button.rotationX + moveAmount, button.rotationX - moveAmount)
        val thirdPhase = ObjectAnimator.ofFloat(button, "rotation", button.rotationX - moveAmount, button.rotationX)

        set.startDelay = 100
        set.duration = 300
        set.playSequentially(firstPhase,secondPhase,thirdPhase);
        set.addListener(object: Animator.AnimatorListener {

            override fun onAnimationStart(animator: Animator) {
            }

            override fun onAnimationEnd(animator: Animator) {

                action.run();
            }

            override fun onAnimationCancel(animator: Animator) {
            }

            override fun onAnimationRepeat(animator: Animator) {
            }
        })
        set.start()
    }
    enum class GameStates {
        Matching, Match, NoMatch, Finished
    }
    data class Tile(val button: ImageButton, var tileResource: Int, val deckResource: Int) {
        init {
            button.setImageResource(deckResource)
        }
        private var _revealed: Boolean = false
        var revealed: Boolean
            get() {
                return _revealed
            }
            set(value){
                _revealed = value
                if(_revealed)
                {
                    button.setImageResource(tileResource)
                } else {
                    button.setImageResource(deckResource)
                }
            }
        fun removeOnClickListener(){
            button.setOnClickListener(null)
        }
    }
