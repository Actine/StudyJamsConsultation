package info.teib.newtest;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /**
     * Список визначних місць, який буде створений при першому запуску актівіті, а також збережений і відтворений при
     * перегортанні екрану
     */
    private Landmark[] landmarks;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Увімкнути стрілочку у екшн-барі (зліва від заголовку актівіті)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Замінити стрілочку на довільний бажаний drawable (у цьому випадку - хрестик)
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        // Дістати «повноекранний переглядач» картинок...
        final View container = findViewById(R.id.preview_container);
        // ...і зробити так, щоб при натисканні на картинку всередині нього переглядач ховався
        findViewById(R.id.preview_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.setVisibility(View.INVISIBLE);
            }
        });

        if (savedInstanceState == null) {
            // Якщо програма запускається вперше (нема збереженого стану) - згенерувати список (див. метод)
            landmarks = loadLandmarks();
        } else {
            // Інакше зчитати список зі збереженого стану.
            // Ключ повинен бути ідентичний тому, з яким зберігали стан цього списку в onSaveInstanceState
            landmarks = (Landmark[]) savedInstanceState.getSerializable("info.teib.newtest.landmarks");
        }

        // Дістати RecyclerView - компонент, який відповідає за відображення колекцій даних
        // Для того, щоб він був у вашому проекті, потрібно додати бібліотеку для RecyclerView
        // (див. Gradle Scripts / build.gradle (Module: app))
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // RecyclerView сам не рисує дані - йому треба передати LayoutManager, який відповідає за стиль відображення.
        // Зокрема, LinearLayoutManager виводить дані у вигляді списку (в одну колонку)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // І передаємо йому створений нами адаптер, де ми кажемо йому, що саме виводити у списку
        recyclerView.setAdapter(new LandmarksAdapter(this, container, landmarks));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // ID елементів меню краще називати з префіксом action, щоб відрізняти їх від ID в’ювів
        // Див. /res/menu/menu_main.xml
        // Також не забувайте return true, щоб сказати системі, що ви обробили натискання

        if (id == R.id.action_help) {
            Toast.makeText(MainActivity.this, "Натиснули допомогу", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, "Натиснули налаштування,\nвитерли інфо про переглянуті замки\n(перезапустіть програму)", Toast.LENGTH_LONG).show();

            // Витремо інформацію про переглянуті замки (буде видно тільки після перезапуску програми)
            PreferenceManager.getDefaultSharedPreferences(this).edit().remove("viewed").apply();

            return true;
        } else if (id == android.R.id.home) {
            // При натисканні на стрілку в екшн-барі
            // Зверніть увагу: android.R.id..., а не просто R.id...
            Toast.makeText(MainActivity.this, "Натиснули хрестик, закрили актівіті", Toast.LENGTH_SHORT).show();

            // Закриємо актівіті
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Оголосіть (перекрийте) цей метод, щоби зберегти необхідні вам дані при перезапуску актівіті (повороті екрану тощо)
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Важливо зберегти цей виклик - без нього система не збереже те, що їй самій потрібно
        super.onSaveInstanceState(outState);

        // Збережемо наш список визначних місць за деяким ключем. За ним потім і витягнемо в onCreate.
        // Ми зробили Landmark implements Serializable, тому можемо зберегти Landmark або їх масив через putSerializable
        outState.putSerializable("info.teib.newtest.landmarks", landmarks);
    }

    /**
     * Метод, який зчитує назви, описи і ID картинок, і будує із них масив об’єктів для списку
     *
     * @return масив об’єктів для списку
     */
    private Landmark[] loadLandmarks() {
        // Вичитуємо масиви назв і описів із strings.xml
        // Допускаємо, що вони однакової довжини
        String[] titles = getResources().getStringArray(R.array.landmark_titles);
        String[] descriptions = getResources().getStringArray(R.array.landmark_descriptions);

        // Створюємо масив для наших об’єктів для списку
        Landmark[] landmarks = new Landmark[titles.length];

        // і заповнюємо його об’єктами із відповідними назвою, описом і картинкою
        for (int i = 0; i < landmarks.length; i++) {
            landmarks[i] = new Landmark();
            landmarks[i].title = titles[i];
            landmarks[i].description = descriptions[i];
            landmarks[i].index = i;

            // Нехай у парних рядків буде одна картинка, а у непарних інша
            if (i % 2 == 0) {
                landmarks[i].pictureId = R.drawable.grape2;
            } else {
                landmarks[i].pictureId = R.drawable.grapes;
            }
        }

        // Дістаємо інформацію про переглянуті визначні місця із «налаштувань».
        // Рядок треба витягти і розбити на частини
        final String viewed = PreferenceManager.getDefaultSharedPreferences(this).getString("viewed", null);
        if (viewed == null) {
            // Нічого не збережено. Значить, і робити більше нічого
            return landmarks;
        }

        // Інакше розбити на частини по комі (див. LandmarkHolder, рядок 68)
        String[] viewedIdsAsStrings = viewed.split(",");

        // і для кожного числа позначити відповідний лендмарк як переглянутий
        for (String s : viewedIdsAsStrings) {
            // перетворити рядок на число
            int viewedId = Integer.parseInt(s);

            // і якщо воно в допустимих межах - позначити відповідний лендмарк
            if (viewedId >= 0 && viewedId < landmarks.length) {
                landmarks[viewedId].isViewed = true;
            }
        }

        return landmarks;
    }

}
