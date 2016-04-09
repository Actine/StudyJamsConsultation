package info.teib.newtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * «Тримач» для шаблону окремого рядка списку визначних місць. Об’єкти цього класу «тримають» у собі леяути необхідної
 * кількості рядків і відповідають за вписування у них даних. Якщо леяут виходить за межі екрану, то його холдер не
 * знищується, а перевикористовується для наступних рядків (у леяут вписуються нові дані).
 *
 * Обов’язково мусить extends RecyclerView.ViewHolder
 *
 * @author Paul Danyliuk
 */
public class LandmarkHolder extends RecyclerView.ViewHolder {

    /**
     * Контекст треба нам, щоб витягти картинку по Drawable ID
     */
    private Context context;

    /**
     * Останнє визначне місце, яке було прив’язане до цього «тримача»
     */
    private Landmark landmark;

    private ImageView picture;
    private TextView title;
    private TextView description;
    private View seenLabel;

    public LandmarkHolder(final Context context, View itemView, final View container) {
        super(itemView);
        this.context = context;
        picture = (ImageView) itemView.findViewById(R.id.picture);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
        seenLabel = itemView.findViewById(R.id.seen);

        // Прив’язуємо дію, яку треба виконати при натисканні на весь рядок
        // Зверніть увагу у item_landmark.xml: android:background="?selectableItemBackground" додасть ефект при натисканні
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (landmark == null) {
                    // Перевірка про всяк випадок, що landmark прив’язаний. Якщо ні, не робити нічого
                    return;
                }

                // Якщо цей лендмарк ще не було переглянуто -
                if (!landmark.isViewed) {
                    // записати, що переглянуто
                    landmark.isViewed = true;

                    // відобразити лейблик
                    seenLabel.setVisibility(View.VISIBLE);

                    // дописати в «настройки», що цей лендмарк переглянуто:
                    // 1. витягти існуюче значення (якщо нема - пустий рядок)
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    String viewedPlaces = preferences.getString("viewed", "");

                    // 2. і дописати туди індекс місця і кому, щоб можна було по комі потім розділити індекси
                    preferences.edit().putString("viewed", viewedPlaces + landmark.index + ',').apply();
                }

                // Ну і виконати дію, яку треба виконати при натисканні рядка.
                // Наприклад, відкрити актівіті з детальним описом. У моєму випадку - перейти у пошук Google по назві цього місця.
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://google.com/search?q=" + landmark.title));
                context.startActivity(intent);
            }
        });

        // Прив’язуємо дію, яку виконати при натисканні на картинку: показати «повноекранний перегляд» фотографії
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // зробити переглядач видимим
                container.setVisibility(View.VISIBLE);
                // і призначити йому це саме зображення, що в картинці в рядку
                ((ImageView) container.findViewById(R.id.preview_image)).setImageDrawable(picture.getDrawable());
            }
        });
    }

    /**
     * Метод, який впише у леяут дані із переданого лендмарка
     *
     * @param landmark об’єкт, який описує одне визначне місце
     */
    public void setNewData(Landmark landmark) {
        this.landmark = landmark;

        // Витягуємо зображення по id та вписуємо у піктограму
        this.picture.setImageDrawable(
                context.getResources().getDrawable(landmark.pictureId)
        );

        this.title.setText(landmark.title);
        this.description.setText(landmark.description);

       /* Показати або сховати повідомлення, що визначне місце переглянуто.

          Такий вираз із ?: рівноцінний наступному:

          if (landmark.isViewed) {
              this.seenLabel.setVisibility(View.VISIBLE);
          } else {
              this.seenLabel.setVisibility(View.GONE);
          }
        */
        this.seenLabel.setVisibility(landmark.isViewed ? View.VISIBLE : View.GONE);
    }

}
