# RecyclerViewBasic

## Set up a RecyclerView
<img width="300" alt="スクリーンショット 2022-09-21 8 22 58" src="https://user-images.githubusercontent.com/47273077/191381827-c73a756c-c763-4953-b60e-d2e9fe0ad2e9.png">

activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/creatureRecyclerView"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

list_item_creature.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="@dimen/list_item_creature_height"
    xmlns:tools="http://schemas.android.com/tools"
    >

    <ImageView
        android:id="@+id/creatureImage"
        android:layout_width="@dimen/list_item_creature_height"
        android:layout_height="@dimen/list_item_creature_height"
        android:layout_marginStart="@dimen/padding_standard"
        android:contentDescription="@string/content_description_creature_image"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:srcCompat="@drawable/thumbnail_creature_cat_derp" />

   <TextView
        android:id="@+id/fullNameText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="@dimen/padding_half"
        android:layout_marginEnd="@dimen/padding_standard"
        android:layout_marginStart="@dimen/padding_standard"
        android:layout_marginTop="@dimen/padding_half"
        android:textColor="@android:color/black"
        android:textSize="@dimen/creature_name_text"
        android:textStyle="bold"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toEndOf="@+id/creatureImage"
        app:layout_constraintTop_toTopOf="parent"
        android:text="Full Name" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

ViewGroupExtentions.kt
```kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}
```

CreatureAdapter.kt
```kt
class CreatureAdapter(private val creatures: List<Creature>): RecyclerView.Adapter<CreatureAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_creature))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount() = creatures.count()

}
```

AllFragment.kt
```kt
class AllFragment : Fragment() {

  private val adapter = CreatureAdapter(CreatureStore.getCreatures())

  companion object {
    fun newInstance(): AllFragment {
      return AllFragment()
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_all, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    creatureRecyclerView.layoutManager = LinearLayoutManager(activity)
    creatureRecyclerView.adapter = adapter
  }
}
```

CreatureStore.kt
```kt
object CreatureStore {

  private const val TAG = "CreatureStore"

  private lateinit var creatures: List<Creature>
  private lateinit var foods: List<Food>

  fun loadCreatures(context: Context) {
    val gson = Gson()
    val json = loadJSONFromAsset("creatures.json", context)
    val listType = object : TypeToken<List<Creature>>() {}.type
    creatures = gson.fromJson(json, listType)
    creatures
      .filter { Favorites.isFavorite(it, context) }
      .forEach { it.isFavorite = true }
    Log.v(TAG, "Found ${creatures.size} creatures")
  }

  fun loadFoods(context: Context) {
    val gson = Gson()
    val json = loadJSONFromAsset("foods.json", context)
    val listType = object : TypeToken<List<Food>>() {}.type
    foods = gson.fromJson(json, listType)
    Log.v(TAG, "Found ${foods.size} food items")
  }

  fun getCreatures() = creatures

  fun getCreatureById(id: Int) = creatures.firstOrNull { it.id == id }

  fun getFoodById(id: Int) = foods.firstOrNull { it.id == id }

  private fun loadJSONFromAsset(filename: String, context: Context): String? {
    var json: String? = null
    try {
      val inputStream = context.assets.open(filename)
      val size = inputStream.available()
      val buffer = ByteArray(size)
      inputStream.read(buffer)
      inputStream.close()
      json = String(buffer)
    } catch (ex: IOException) {
      Log.e(TAG, "Error reading from $filename", ex)
    }
    return json
  }
}
```

## Bind the Views
<img width="300" alt="スクリーンショット 2022-09-21 8 57 17" src="https://user-images.githubusercontent.com/47273077/191385049-ceff9589-edc1-4e76-9ea1-24a5af298594.png">

CreatureAdapter.kt
```kt
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private lateinit var creature: Creature

        fun bind(creature: Creature) {
            this.creature = creature
            val context = itemView.context
            itemView.creatureImage.setImageResource(context.resources.getIdentifier(creature.uri, null, context.packageName))
            itemView.fullNameText.text = creature.fullName
        }
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(creatures[position])
    }
```

## Challenge: Add More Data
<img width="300" alt="スクリーンショット 2022-09-21 9 09 57" src="https://user-images.githubusercontent.com/47273077/191386264-47dbb67a-fcce-42f6-83b6-6b3e23b2c2ef.png">

list_item_creature.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="@dimen/list_item_creature_height">

    <ImageView
        android:id="@+id/creatureImage"
        android:layout_width="@dimen/list_item_creature_height"
        android:layout_height="@dimen/list_item_creature_height"
        android:layout_marginStart="@dimen/padding_standard"
        android:contentDescription="@string/content_description_creature_image"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:srcCompat="@drawable/thumbnail_creature_cat_derp" />

    <TextView
        android:id="@+id/fullNameText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="@dimen/padding_standard"
        android:layout_marginTop="@dimen/padding_half"
        android:layout_marginEnd="@dimen/padding_standard"
        android:layout_marginBottom="@dimen/padding_half"
        android:text="Full Name"
        android:textColor="@android:color/black"
        android:textSize="@dimen/creature_name_text"
        android:textStyle="bold"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintBottom_toTopOf="@id/nickname"
        app:layout_constraintStart_toEndOf="@+id/creatureImage"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_chainStyle="packed" />

    <TextView
        android:id="@+id/nickname"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="@dimen/padding_standard"
        android:layout_marginBottom="@dimen/padding_half"
        android:text="Nick Name"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toEndOf="@id/creatureImage"
        app:layout_constraintTop_toBottomOf="@id/fullNameText" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

CreatureAdapter.kt
```kt
        fun bind(creature: Creature) {
            this.creature = creature
            val context = itemView.context
            itemView.creatureImage.setImageResource(context.resources.getIdentifier(creature.uri, null, context.packageName))
            itemView.fullNameText.text = creature.fullName
            itemView.nickname.text = creature.nickname

        }
```


## Respond to Clicks

<img width="300" alt="スクリーンショット 2022-09-21 9 09 57" src="https://user-images.githubusercontent.com/47273077/191387040-c2cec93e-8bc1-4fff-ab97-35ae2f2dec90.gif">

CreatureActivity.kt
```kt 
class CreatureActivity : AppCompatActivity() {

  private lateinit var creature: Creature

  companion object {
    private const val EXTRA_CREATURE_ID = "EXTRA_CREATURE_ID"

    fun newIntent(context: Context, creatureId: Int): Intent {
      val intent = Intent(context, CreatureActivity::class.java)
      intent.putExtra(EXTRA_CREATURE_ID, creatureId)
      return intent
    }
  }

```

CreatureAdapter.kt
```kt
    class ViewHolder(v: View) : View.OnClickListener, RecyclerView.ViewHolder(v) {
        private lateinit var creature: Creature

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(creature: Creature) {
            this.creature = creature
            val context = itemView.context
            itemView.creatureImage.setImageResource(context.resources.getIdentifier(creature.uri, null, context.packageName))
            itemView.fullNameText.text = creature.fullName
            itemView.nickname.text = creature.nickname

        }

        override fun onClick(view: View?) {
            view?.let {
                val context = it.context
                val intent = CreatureActivity.newIntent(context, creature.id)
                context.startActivity(intent)
            }
        }
    }
```


-------
## その他変更
build.gradle
```gradle
android {
    compileSdkVersion 31
    defaultConfig {
        applicationId "com.raywenderlich.android.creatures"
        minSdkVersion 21
        targetSdkVersion 31
```

gradle.properties(app)
```gradle
distributionUrl=https\://services.gradle.org/distributions/gradle-6.1.1-all.zip

android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
```

build.gradle(Project)
```gradle
 kotlin_version = '1.6.0'
```

```xml
    <activity
      android:name=".ui.SplashActivity"
      android:theme="@style/SplashTheme"
      android:exported="true">
      <intent-filter>
        <action android:name="android.intent.action.MAIN" />

        <category android:name="android.intent.category.LAUNCHER" />
      </intent-filter>
    </activity>
```


