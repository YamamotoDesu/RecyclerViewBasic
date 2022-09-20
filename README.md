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
        android:id="@+id/textView"
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


